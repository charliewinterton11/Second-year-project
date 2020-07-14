package view;


import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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
import model.Delivery;


public class SelectModulesPane extends BorderPane implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2051649230387060158L;
	private ListView<Module> lVUnModuleT1;
	private ListView<Module> lVUnModuleT2;
	private ListView<Module> lVSeModuleT1;
	private ListView<Module> lVSeModuleT2;
	private ListView<Module> lVSeModuleYearLong;
	private Button t1Add;
	private Button t2Add;
	private Button t1Remove;
	private Button t2Remove;
	private Button submit;
	private Button reset;
	private TextField t1CreditsField;
	private TextField t2CreditsField;
	
	
	public SelectModulesPane() {
		this.setPadding(new Insets(15,15,15,15));
		
		//labels
		Label unT1 = new Label("Unselected Term One Modules ");
		Label T1 = new Label("Term 1");
		Label unT2 = new Label("Unselected Term Two Modules ");
		Label T2 = new Label("Term 2");
		Label yearLong = new Label("Selected Year Long Modules");
		Label seT1 = new Label("Selected Term One Modules");
		Label seT2 = new Label("Selected Term Two Modules");
		Label t1Credits = new Label("Current Term One Credits: ");
		Label t2Credits = new Label("Current Term Two Credits: ");

		
		//listview boxes
		lVUnModuleT1 = new ListView<Module>();
		lVUnModuleT2 = new ListView<Module>();
		lVSeModuleT1 = new ListView<Module>();
		lVSeModuleT2 = new ListView<Module>();
		lVSeModuleYearLong = new ListView<Module>();
		
		VBox left = new VBox();
		VBox right = new VBox();
		HBox bottom = new HBox();
		
		
		//buttons
		t1Add = new Button("Add");
		t1Remove = new Button("Remove");
		t2Add = new Button("Add");
		t2Remove = new Button("Remove");
		submit = new Button("Submit"); 
		reset = new Button("Reset");
		
		t1CreditsField = new TextField(""+0);
		t1CreditsField.setPrefSize(40, 10);
		t2CreditsField = new TextField(""+0);
		t2CreditsField.setPrefSize(40, 10);
		t1CreditsField.setEditable(false);
		t2CreditsField.setEditable(false);
		
		
		//T2 and T1 Button rows
		HBox buttonRowT1 = new HBox();
		buttonRowT1.setSpacing(10);
		buttonRowT1.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.SOLID, null, new BorderWidths(15))));
		buttonRowT1.setAlignment(Pos.CENTER);
		
		buttonRowT1.getChildren().addAll(T1,t1Add,t1Remove);
		
		HBox buttonRowT2 = new HBox();
		buttonRowT2.setSpacing(10);
		buttonRowT2.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.SOLID, null, new BorderWidths(15))));
		buttonRowT2.setAlignment(Pos.CENTER);
		buttonRowT2.getChildren().addAll(T2,t2Add,t2Remove);
		
		
		
		//Alignment 
		left.setAlignment(Pos.CENTER_LEFT);
		right.setAlignment(Pos.CENTER_LEFT);
		right.setSpacing(5);
		
		//credit labels and values
		HBox t1CreditsBox = new HBox(t1Credits,t1CreditsField);
		t1CreditsBox.setAlignment(Pos.BASELINE_CENTER);
		
		HBox t2CreditsBox = new HBox(t2Credits,t2CreditsField);
		t2CreditsBox.setAlignment(Pos.BASELINE_CENTER);
		
		HBox creditsBox = new HBox(t1CreditsBox,t2CreditsBox);
		creditsBox.setSpacing(100);
		HBox subResBox = new HBox(submit,reset);
		subResBox.setAlignment(Pos.CENTER);
		subResBox.setSpacing(20);
		
		VBox bottomBox = new VBox(creditsBox,subResBox);
		bottomBox.setSpacing(15);
		bottomBox.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.SOLID, null, new BorderWidths(15))));
		
		
		left.getChildren().addAll(unT1, lVUnModuleT1,buttonRowT1,unT2,lVUnModuleT2,buttonRowT2);
		right.getChildren().addAll(yearLong, lVSeModuleYearLong,seT1, lVSeModuleT1,seT2,lVSeModuleT2);
		bottom.getChildren().add(bottomBox);
		
	
		HBox center = new HBox();
		center.setAlignment(Pos.CENTER);
		center.getChildren().addAll(left,right);
		center.setSpacing(30);
		HBox.setHgrow(left,Priority.ALWAYS);
		HBox.setHgrow(right,Priority.ALWAYS);
		VBox.setVgrow(right,Priority.ALWAYS);
		VBox.setVgrow(left,Priority.ALWAYS);
		
		
		bottom.setAlignment(Pos.CENTER);
		bottom.setSpacing(15);
		
		
		this.setCenter(center);
		this.setBottom(bottom);
	}
		
		//populates listview's with appropriate modules
		public void populateListViews(Course[] courses, String courseName) {
			lVSeModuleT1.getItems().clear();
			lVUnModuleT1.getItems().clear();
			lVSeModuleT2.getItems().clear();
			lVUnModuleT2.getItems().clear();
			lVSeModuleYearLong.getItems().clear();
			
			int courseIndex = 0;
			
			for (int i = 0; i < courses.length;i++) {
				if (courses[i].toString().equals(courseName)) {
					 courseIndex = i;
				}
			}
			Course termModules = courses[courseIndex];
			Collection<Module> ModulesCollection = termModules.getAllModulesOnCourse();
			
			List<Module> termOneModulesUnselected;
			List<Module> termTwoModulesUnselected;
			List<Module> yearLongModules;
			
			List<Module> termOneModulesSelected;
			List<Module> termTwoModulesSelected;
			
			termOneModulesUnselected = ModulesCollection.stream()
					.filter(Module -> Module.isMandatory() == false)
					.filter(Module -> Module.getRunPlan() == Delivery.TERM_1)
					.collect(Collectors.toList());
			
			termTwoModulesUnselected = ModulesCollection.stream()
					.filter(Module -> Module.isMandatory() == false)
					.filter(Module -> Module.getRunPlan() == Delivery.TERM_2)
					.collect(Collectors.toList());
			
			yearLongModules = ModulesCollection.stream()
					.filter(Module -> Module.isMandatory() == true)
					.filter(Module -> Module.getRunPlan() == Delivery.YEAR_LONG)
					.collect(Collectors.toList());
			
			termOneModulesSelected = ModulesCollection.stream()
					.filter(Module -> Module.isMandatory() == true)
					.filter(Module -> Module.getRunPlan() == Delivery.TERM_1)
					.collect(Collectors.toList());
			
			termTwoModulesSelected = ModulesCollection.stream()
					.filter(Module -> Module.isMandatory() == true)
					.filter(Module -> Module.getRunPlan() == Delivery.TERM_2)
					.collect(Collectors.toList());
			
			lVUnModuleT1.getItems().addAll(termOneModulesUnselected);
			lVUnModuleT2.getItems().addAll(termTwoModulesUnselected);
			lVSeModuleT1.getItems().addAll(termOneModulesSelected);
			lVSeModuleT2.getItems().addAll(termTwoModulesSelected);
			lVSeModuleYearLong.getItems().addAll(yearLongModules);
			
		}
		
		
		
			

			
		
		
		public ListView<Module> getYearLongSelected() {
			return lVSeModuleYearLong;
		}
		
		public ListView<Module> getSelectedTerm1() {
			return lVSeModuleT1;
		}
		
		public ListView<Module> getSelectedTerm2() {
			return lVSeModuleT2;
		}
		
		public ListView<Module> getUnselectedTerm1() {
			return lVUnModuleT1;
		}
		
		public ListView<Module> getUnselectedTerm2() {
			return lVUnModuleT2;
		}		
		
		
		public void updateCreditVaules() {
			int term1 = 0;
			int term2 = 0;
			int yearLong = 0;
			for (int i = 0; i<lVSeModuleT1.getItems().size();i++){
				 term1 += lVSeModuleT1.getItems().get(i).getCredits();	
			}
			for (int i = 0; i<lVSeModuleT2.getItems().size();i++){
				 term2 += lVSeModuleT2.getItems().get(i).getCredits();
			}
			for (int i = 0; i<lVSeModuleYearLong.getItems().size();i++){
				yearLong += lVSeModuleYearLong.getItems().get(i).getCredits();
			}
			term1 += (yearLong/2);
			term2 += (yearLong/2);
			
			t1CreditsField.setText(""+term1);
			t2CreditsField.setText(""+term2);
			
		}
		
		public TextField getTermOneTextField() {
			return t1CreditsField;
		}
		
		public TextField getTermTwoTextField() {
			return t2CreditsField;
		}
		
		
		
		public int getIntForTextField(TextField text) {
			String number = text.getText();
			return Integer.parseInt(number);
		}
		
		
		
		//event handlers
		
		public void addTerm1AddBtnHandler(EventHandler<ActionEvent> handler) {
			t1Add.setOnAction(handler);
		}
		
		public void addTerm2AddBtnHandler(EventHandler<ActionEvent> handler) {
			t2Add.setOnAction(handler);
		}
		
		public void addTerm1RemoveBtnHandler(EventHandler<ActionEvent> handler) {
			t1Remove.setOnAction(handler);
		}
		
		public void addTerm2RemoveBtnHandler(EventHandler<ActionEvent> handler) {
			t2Remove.setOnAction(handler);
		}
		
		
		public void addSubmitSelectedModulesHandler(EventHandler<ActionEvent> handler) {
			submit.setOnAction(handler);
		}
		
		
		public void addResetBtnHandler(EventHandler<ActionEvent> handler) {
			reset.setOnAction(handler);
		}

		


	
		
		
	
}
