package controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.event.ActionEvent;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Course;
import model.Delivery;
import model.Module;
import model.Name;
import model.StudentProfile;
import view.ModuleSelectionRootPane;
import view.OverviewSelectionPane;
import view.ReserveModulesPane;
import view.SelectModulesPane;
import view.CreateProfilePane;
import view.ModuleSelectionMenuBar;

public class ModuleSelectionController  {

	
	//fields to be used throughout class
	private StudentProfile model;
	private ModuleSelectionRootPane view;
	
	private CreateProfilePane cpp;
	private ModuleSelectionMenuBar msmb;
	private SelectModulesPane smp;
	private ReserveModulesPane rmp;
	private OverviewSelectionPane osp;

	public ModuleSelectionController(StudentProfile model, ModuleSelectionRootPane view) {
		//initialise model and view fields
	    this.model = model;
		this.view = view;
		
		//initialise view subcontainer fields
		cpp = view.getCreateProfilePane();
		msmb = view.getModuleSelectionMenuBar();
		smp = view.getSelectModulesPane();
		rmp = view.getReserveModulesPane();
		osp = view.getOverviewSelectionPane();
		

		//populate combobox in create profile pane with courses using the setupAndGetCourses method below
		cpp.populateCourseComboBox(setupAndGetCourses());
		
	
		//attach event handlers to view using private helper method
		this.attachEventHandlers();	
	}

	
	//a helper method used to attach event handlers
	private void attachEventHandlers() {
		//attach an event handler to the create profile pane
		cpp.addCreateProfileHandler(new CreateProfileHandler());
		smp.addSubmitSelectedModulesHandler(new SubmitSelectionHandler());
		smp.addTerm1AddBtnHandler(new Term1AddBtnHandler());
		smp.addTerm2AddBtnHandler(new Term2AddBtnHandler());
		smp.addTerm1RemoveBtnHandler(new Term1RemoveBtnHandler());
		smp.addTerm2RemoveBtnHandler(new Term2RemoveBtnHandler());
		smp.addResetBtnHandler(new ResetBtnHandler());
		rmp.addTerm1AddBtnReserveHandler(new Term1AddBtnReserveHandler());
		rmp.addTerm1RemoveBtnReserveHandler(new Term1RemoveBtnReserveHandler());
		rmp.addTerm2AddBtnReserveHandler(new Term2AddBtnReserveHandler());
		rmp.addTerm2RemoveBtnReserveHandler(new Term2RemoveBtnReserveHandler());
		rmp.addTerm1ConfirmBtnReserveHandler(new Term1ConfirmBtnReserveHandler());
		rmp.addTerm2ConfirmBtnReserveHandler(new Term2ConfirmBtnReserveHandler());
		osp.addSaveOverviewBtnHandler(new SaveOverviewBtnHandler());
		msmb.addSaveHandler(new SaveMenuHandler());
		msmb.addLoadHandler(new LoadMenuHandler());
		
		
		//attach an event handler to the menu bar that closes the application
		msmb.addExitHandler(e -> System.exit(0));
	}
	
	//create profile pane
	private class CreateProfileHandler implements EventHandler<ActionEvent> {
		
		public void handle(ActionEvent e) {
			model.clearReservedModules();
			model.clearSelectedModules();
			boolean valid = true;
			Name n = cpp.getName();
			String p = cpp.getPnumberInput();
			Course c = cpp.getSelectedCourse();
			String em = cpp.getEmail();
			LocalDate d = cpp.getDate();
			String missing = "";
			
			if(n.getFirstName().equals("") || n.getFamilyName().equals("")) {
				valid = false;
				missing = missing.concat("-First/last name \n");
			} else {
				model.setStudentName(cpp.getName());
			}
			if(p.equals("")||!(p.toUpperCase().charAt(0) == 'P') || p.length() < 8 ) {
				valid = false;
				missing = missing.concat("-P Number \n");
			}else {
				model.setpNumber(cpp.getPnumberInput());
			}
			if(c.getCourseName().equals(null)|| c.getCourseName().equals("")) {		
				valid = false;
				missing = missing.concat("-Course selection \n");
			}else {
				model.setCourse(cpp.getSelectedCourse());
			}
			if(!em.contains("@")|| !(em.length() > 3)) {
				valid = false;
				missing = missing.concat("-Email address \n");
			}else {
				model.setEmail(cpp.getEmail());
			}
			if(d == null) {
				valid = false;
				missing = missing.concat("-Date \n");
			}else {
				model.setSubmissionDate(cpp.getDate());
			}
			if (valid == true) {
				smp.populateListViews(setupAndGetCourses(), model.getCourse().toString());
				smp.updateCreditVaules();
				view.changeTab(1);
			}else {
				alertDialogBuilder(AlertType.ERROR, "Error Dialog", null, "Please ensure you have correctly filled in the following:\n "+missing);
			}
		}	
	}
	//Select modules pane buttons
	private class Term1AddBtnHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e) {
			int value = smp.getIntForTextField(smp.getTermOneTextField());
			Module module = smp.getUnselectedTerm1().getSelectionModel().getSelectedItem();
			if(value < 60 && module != null) {
				smp.getUnselectedTerm1().getItems().remove(smp.getUnselectedTerm1().getSelectionModel().getSelectedItem());
				smp.getSelectedTerm1().getItems().add(module);
				smp.updateCreditVaules();
			}
		}
	}
	private class Term2AddBtnHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e) {
			int value = smp.getIntForTextField(smp.getTermTwoTextField());
			Module module = smp.getUnselectedTerm2().getSelectionModel().getSelectedItem();
			if(value < 60 && module != null) {
				smp.getUnselectedTerm2().getItems().remove(smp.getUnselectedTerm2().getSelectionModel().getSelectedItem());
				smp.getSelectedTerm2().getItems().add(module);
				smp.updateCreditVaules();
			}
		}
	}
	private class Term1RemoveBtnHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e) {
			Module module = smp.getSelectedTerm1().getSelectionModel().getSelectedItem();
			if (module != null && (module.isMandatory())!=true) {
				smp.getSelectedTerm1().getItems().remove(smp.getSelectedTerm1().getSelectionModel().getSelectedItem());
				smp.getUnselectedTerm1().getItems().add(module);
				smp.updateCreditVaules();
				
			}
		}
	}
	private class Term2RemoveBtnHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e) {
			Module module = smp.getSelectedTerm2().getSelectionModel().getSelectedItem();
			if (module != null && (module.isMandatory())!=true) {
				smp.getSelectedTerm2().getItems().remove(smp.getSelectedTerm2().getSelectionModel().getSelectedItem());
				smp.getUnselectedTerm2().getItems().add(module);
				smp.updateCreditVaules();
			}
		}
	}
		
	private class SubmitSelectionHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			int credits = 0;
			credits += smp.getIntForTextField(smp.getTermOneTextField());
			credits += smp.getIntForTextField(smp.getTermTwoTextField());
			if(credits == 120) {
				model.clearReservedModules();
				model.clearSelectedModules();
				
				smp.getSelectedTerm1().getItems().forEach(x->{model.addSelectedModule(x);});
				smp.getSelectedTerm2().getItems().forEach(x->{model.addSelectedModule(x);});
				smp.getYearLongSelected().getItems().forEach(x->{model.addSelectedModule(x);});
	
				List<Module> termOne = smp.getUnselectedTerm1().getItems();
				List<Module> termTwo = smp.getUnselectedTerm2().getItems();
				rmp.populateReserveModulesPane(termOne,termTwo);
				view.changeTab(2);
			}else{
				alertDialogBuilder(AlertType.ERROR, "Error Dialog", null, "Please enter in 120 credits worth of modules");
			}
			
		}
	}
	
	private class ResetBtnHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			smp.getUnselectedTerm1().getItems().clear();
			smp.getUnselectedTerm2().getItems().clear();
			smp.getSelectedTerm1().getItems().clear();
			smp.getSelectedTerm2().getItems().clear();
			smp.getYearLongSelected().getItems().clear();
			smp.populateListViews(setupAndGetCourses(), model.getCourse().toString());
			smp.updateCreditVaules();
		}
	}
	
	//reserve modules pane buttons
	private class Term1AddBtnReserveHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			Module module = rmp.getUnselectedTermOne().getSelectionModel().getSelectedItem();
			if (module != null && rmp.isReserveFull(rmp.getSelectedTermOne().getItems()) == false) {
				rmp.getUnselectedTermOne().getItems().remove(rmp.getUnselectedTermOne().getSelectionModel().getSelectedItem());
				rmp.getSelectedTermOne().getItems().add(module);
			}
		}
	}
	
	private class Term2AddBtnReserveHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			Module module = rmp.getUnselectedTermTwo().getSelectionModel().getSelectedItem();
			if (module != null && rmp.isReserveFull(rmp.getSelectedTermTwo().getItems()) == false) {
				rmp.getUnselectedTermTwo().getItems().remove(rmp.getUnselectedTermTwo().getSelectionModel().getSelectedItem());
				rmp.getSelectedTermTwo().getItems().add(module);
			}
		}
	}
	
	private class Term1RemoveBtnReserveHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e) {
			Module module = rmp.getSelectedTermOne().getSelectionModel().getSelectedItem();
			if (module != null) {
				rmp.getSelectedTermOne().getItems().remove(rmp.getSelectedTermOne().getSelectionModel().getSelectedItem());
				rmp.getUnselectedTermOne().getItems().add(module);
			}
		}
	}
	
	private class Term2RemoveBtnReserveHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e) {
			Module module = rmp.getSelectedTermTwo().getSelectionModel().getSelectedItem();
			if (module != null) {
				rmp.getSelectedTermTwo().getItems().remove(rmp.getSelectedTermTwo().getSelectionModel().getSelectedItem());
				rmp.getUnselectedTermTwo().getItems().add(module);
			}
		}
	}
	
	private class Term1ConfirmBtnReserveHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e) {
			if(rmp.isReserveFull(rmp.getSelectedTermOne().getItems()) == true) {
			rmp.getSelectedTermOne().getItems().forEach(x->{model.addReservedModule(x);});
			rmp.changeTab();
			}
			else {
				alertDialogBuilder(AlertType.ERROR, "Error Dialog", null, "Please enter in 30 credits worth of modules");
			}
		}
	}
	
	private class Term2ConfirmBtnReserveHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e) {
			if(rmp.isReserveFull(rmp.getSelectedTermTwo().getItems()) == true) {
				rmp.getSelectedTermTwo().getItems().forEach(x->{model.addReservedModule(x);});
				Name name = model.getStudentName();
				String pNum = model.getpNumber();
				String email = model.getEmail();
				LocalDate date = model.getSubmissionDate();
				Course course = model.getCourse();
				Set<Module> selected = model.getAllSelectedModules();
				Set<Module> reserved =  model.getAllReservedModules();
				
				osp.populateOverviewPane(name,pNum,email,date,course,selected,reserved);
				
				view.changeTab(3);
				
			}
			else {
				alertDialogBuilder(AlertType.ERROR, "Error Dialog", null, "Please enter in 30 credits worth of modules");
			}
		}
	}
	//overview selection pane
	private class SaveOverviewBtnHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e) {
			String fileName = "studentProfile.txt";
			PrintWriter writer = null;
			try {
			writer = new PrintWriter(fileName);
			} catch(FileNotFoundException n){
				n.printStackTrace();
			}
			writer.print("Student Name:" +model.getStudentName().getFullName()+"\n"+""
					+ "Student P Number: " + model.getpNumber()+"\n"+
					"Student Email: "+model.getEmail()+"\n"+
					"Submission Date: "+ model.getSubmissionDate()+"\n"+
					"Course: "+ model.getCourse()+"\n\n\n");
			writer.print("Selected Modules:\n**********************************************\n");
			model.getAllSelectedModules().forEach(writer::println);
			writer.print("\nReserved Modules:\n**********************************************\n");
			model.getAllReservedModules().forEach(writer::println);
			writer.close();
			alertDialogBuilder(AlertType.INFORMATION, "Information Dialog", "Save success", "Student details and selection saved to studentProfile.txt");
			}
		}
	
	private class SaveMenuHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			Map<String,Object> list = new HashMap<String,Object>();
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("studentObj.dat"));) {
				int pagesCompleted = 0;

				if (model.getpNumber() != "") {
					pagesCompleted ++;
				}
				
				if (!model.getAllSelectedModules().isEmpty()) {
					pagesCompleted ++;
				}

				if (!model.getAllReservedModules().isEmpty()) {
					pagesCompleted ++;
				}
					
				list.put("model",model);
				list.put("page", pagesCompleted);
				oos.writeObject(list);
				oos.flush();

				alertDialogBuilder(AlertType.INFORMATION, "Information Dialog", "Save success", "details saved to studentObj.dat");
			}
			catch (IOException ioExcep){
				System.out.println("Error saving");
			}
		}

		
		
	}
	
	private class LoadMenuHandler implements EventHandler<ActionEvent> {

	
		@SuppressWarnings("unchecked")
		public void handle(ActionEvent e) {
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("studentObj.dat"));) {

				Map<String,Object> list = new HashMap<String,Object>();
				list = (Map<String, Object>)ois.readObject();
				model = (StudentProfile) list.get("model");
				
		
				
			            
				 
				alertDialogBuilder(AlertType.INFORMATION, "Information Dialog", "Load success", "Register loaded from studentObj.dat");
			}
			
			catch (IOException ioExcep){
				System.out.println("Error loading");
			}
			catch (ClassNotFoundException c) {
				System.out.println("Class Not found");
			}

			
		}
	}
	


	//generates all module and course data and returns courses within an array
	private Course[] setupAndGetCourses() {
		Module imat3423 = new Module("IMAT3423", "Systems Building: Methods", 15, true, Delivery.TERM_1);
		Module ctec3451 = new Module("CTEC3451", "Development Project", 30, true, Delivery.YEAR_LONG);
		Module ctec3902_SoftEng = new Module("CTEC3902", "Rigorous Systems", 15, true, Delivery.TERM_2);	
		Module ctec3902_CompSci = new Module("CTEC3902", "Rigorous Systems", 15, false, Delivery.TERM_2);
		Module ctec3110 = new Module("CTEC3110", "Secure Web Application Development", 15, false, Delivery.TERM_1);
		Module ctec3605 = new Module("CTEC3605", "Multi-service Networks 1", 15, false, Delivery.TERM_1);	
		Module ctec3606 = new Module("CTEC3606", "Multi-service Networks 2", 15, false, Delivery.TERM_2);	
		Module ctec3410 = new Module("CTEC3410", "Web Application Penetration Testing", 15, false, Delivery.TERM_2);
		Module ctec3904 = new Module("CTEC3904", "Functional Software Development", 15, false, Delivery.TERM_2);
		Module ctec3905 = new Module("CTEC3905", "Front-End Web Development", 15, false, Delivery.TERM_2);
		Module ctec3906 = new Module("CTEC3906", "Interaction Design", 15, false, Delivery.TERM_1);
		Module imat3410 = new Module("IMAT3104", "Database Management and Programming", 15, false, Delivery.TERM_2);
		Module imat3406 = new Module("IMAT3406", "Fuzzy Logic and Knowledge Based Systems", 15, false, Delivery.TERM_1);
		Module imat3611 = new Module("IMAT3611", "Computer Ethics and Privacy", 15, false, Delivery.TERM_1);
		Module imat3613 = new Module("IMAT3613", "Data Mining", 15, false, Delivery.TERM_1);
		Module imat3614 = new Module("IMAT3614", "Big Data and Business Models", 15, false, Delivery.TERM_2);
		Module imat3428_CompSci = new Module("IMAT3428", "Information Technology Services Practice", 15, false, Delivery.TERM_2);


		Course compSci = new Course("Computer Science");
		compSci.addModule(imat3423);
		compSci.addModule(ctec3451);
		compSci.addModule(ctec3902_CompSci);
		compSci.addModule(ctec3110);
		compSci.addModule(ctec3605);
		compSci.addModule(ctec3606);
		compSci.addModule(ctec3410);
		compSci.addModule(ctec3904);
		compSci.addModule(ctec3905);
		compSci.addModule(ctec3906);
		compSci.addModule(imat3410);
		compSci.addModule(imat3406);
		compSci.addModule(imat3611);
		compSci.addModule(imat3613);
		compSci.addModule(imat3614);
		compSci.addModule(imat3428_CompSci);

		Course softEng = new Course("Software Engineering");
		softEng.addModule(imat3423);
		softEng.addModule(ctec3451);
		softEng.addModule(ctec3902_SoftEng);
		softEng.addModule(ctec3110);
		softEng.addModule(ctec3605);
		softEng.addModule(ctec3606);
		softEng.addModule(ctec3410);
		softEng.addModule(ctec3904);
		softEng.addModule(ctec3905);
		softEng.addModule(ctec3906);
		softEng.addModule(imat3410);
		softEng.addModule(imat3406);
		softEng.addModule(imat3611);
		softEng.addModule(imat3613);
		softEng.addModule(imat3614);

		Course[] courses = new Course[2];
		courses[0] = compSci;
		courses[1] = softEng;

		return courses;
	}
	
	private void alertDialogBuilder(AlertType type, String title, String header, String content) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header); 
		alert.setContentText(content);
		alert.showAndWait();
	}

}
