package view;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.Course;
import model.Module;
import model.Name;


public class OverviewSelectionPane extends BorderPane implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1619295122235698011L;
	private TextArea studentInfoTA;
	private TextArea selectedTA;
	private TextArea reservedTA;
	private Button save;
	
	public OverviewSelectionPane() {
		
		studentInfoTA = new TextArea();
		selectedTA = new TextArea();
		reservedTA = new TextArea();
		save = new Button("Save Overview");
		
		studentInfoTA.setEditable(false);
		selectedTA.setEditable(false);
		reservedTA.setEditable(false);
		
		HBox selRes = new HBox(selectedTA,reservedTA);
		selRes.setSpacing(20);
		selRes.setAlignment(Pos.CENTER);
		
		VBox centre = new VBox(studentInfoTA,selRes);
		centre.setAlignment(Pos.CENTER);
		centre.setSpacing(15);
		
		HBox.setHgrow(selectedTA, Priority.ALWAYS);
		HBox.setHgrow(reservedTA, Priority.ALWAYS);
		VBox.setVgrow(selectedTA, Priority.ALWAYS);
		VBox.setVgrow(reservedTA, Priority.ALWAYS);
		HBox.setHgrow(centre, Priority.ALWAYS);
		VBox.setVgrow(centre, Priority.ALWAYS);
		HBox.setHgrow(selRes, Priority.ALWAYS);
		VBox.setVgrow(selRes, Priority.ALWAYS);
		
		VBox bottomBox = new VBox(save);
		bottomBox.setSpacing(15);
		bottomBox.setAlignment(Pos.CENTER);
		bottomBox.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.SOLID, null, new BorderWidths(15))));
		this.setPadding(new Insets (20,20,20,20));
		this.setCenter(centre);
		this.setBottom(bottomBox);
		
		
		
	}
	
	public void populateOverviewPane(Name name, String pNum, String email, LocalDate date, Course course, Set<Module> selected, Set<Module> reserved) {
		studentInfoTA.setText("Student Name: "+name.getFullName()+"\n"+"P Number: "+pNum+"\n"+"Email: "+ email+"\n"+"Submission Date: "+date.toString()+"\n"+"Course: "+course.toString());
		String selectedOutput = "Selected Modules: \n\n ======================\n\n";
		String selectedLine = "";
			for (Module module: selected) {
				selectedLine = "Module Code: "+ module.getModuleCode()+", Module Name: "+ module.getModuleName()+"\n"+
						"Credits: "+ module.getCredits()+", Mandatory?: "+ module.isMandatory()+", Delivery: "+ module.getRunPlan()+"\n\n";
				selectedOutput = selectedOutput + selectedLine;
	
			}
		
		selectedOutput = selectedOutput.replace("TERM_1", "Term One").replace("TERM_2", "Term Two").replace("YEAR_LONG", "Year Long").replace("false", "No").replace("true", "Yes");	
		selectedTA.setText(selectedOutput);
		
		
		String reservedOutput = "Reserved Modules: \n\n ======================\n\n";
		String reservedLine = "";
			for (Module module: reserved) {
				reservedLine = "Module Code: "+ module.getModuleCode()+", Module Name: "+ module.getModuleName()+"\n"+
						"Credits: "+ module.getCredits()+", Mandatory?: "+ module.isMandatory()+", Delivery: "+ module.getRunPlan()+"\n\n";
				reservedOutput = reservedOutput + reservedLine;
				
	
			}
		
		reservedOutput = reservedOutput.replace("TERM_1", "Term One").replace("TERM_2", "Term Two").replace("YEAR_LONG", "Year Long").replace("false", "No").replace("true", "Yes");	
		reservedTA.setText(reservedOutput);
		//I apologise for this mess, may the programming gods have mercy on my soul.
	}
	public TextArea getSelectedModulesText() {
		return selectedTA;
	}
	
	public TextArea getReservedModulesText() {
		return reservedTA;
	}
	
	public void addSaveOverviewBtnHandler (EventHandler<ActionEvent> handler) {
		save.setOnAction(handler);
	}
}



