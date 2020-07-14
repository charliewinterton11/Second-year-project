package view;

import java.io.Serializable;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.Module;

public class ReserveModulesPane extends Accordion implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5275092243555592496L;
	private TitledPane term1TP;
	private TitledPane term2TP;
	private ListView<Module> termOneUn,termOneSe,termTwoUn,termTwoSe;
	private Button addT1,removeT1,confirmT1,addT2,removeT2,confirmT2;
	
	public ReserveModulesPane() {
		this.setPrefSize(1000, 1000);
		this.setPadding(new Insets(15,15,15,15));
		
		
		Label UnT1 = new Label("Unselected Term One Modules");
		Label SeT1 = new Label("Selected Term One Modules");
		Label UnT2 = new Label("Unselected Term Two Modules");
		Label SeT2 = new Label("Selected Term Two Modules");
		Label resT1 = new Label("Reserve 30 credits worth of term one modules ");
		Label resT2 = new Label("Reserve 30 credits worth of term two modules ");
		
		addT1 = new Button("Add");
		removeT1 = new Button("Remove");
		confirmT1 = new Button("Confirm");
		
		addT2 = new Button("Add");
		removeT2 = new Button("Remove");
		confirmT2 = new Button("Confirm");
		
		
		BorderPane t1BP = new BorderPane();
		BorderPane t2BP = new BorderPane();
		
		term1TP = new TitledPane("Term One Modules", t1BP);
		term2TP = new TitledPane("Term Two Modules", t2BP);

		termOneUn = new ListView<Module>();
		termOneSe = new ListView<Module>();
		termTwoUn = new ListView<Module>();
		termTwoSe = new ListView<Module>();
		
		VBox term1UnVBox = new VBox(UnT1,termOneUn);
		term1UnVBox.setAlignment(Pos.CENTER_LEFT);
		
		VBox term1SeVBox = new VBox(SeT1,termOneSe);
		term1SeVBox.setAlignment(Pos.CENTER_LEFT);
		HBox term1HBox = new HBox(term1UnVBox,term1SeVBox);
		
		
		HBox.setHgrow(term1UnVBox,Priority.ALWAYS);
		HBox.setHgrow(term1SeVBox,Priority.ALWAYS);
		VBox.setVgrow(termOneUn,Priority.ALWAYS);
		VBox.setVgrow(termOneSe,Priority.ALWAYS);
		
		
		
		term1HBox.setSpacing(15);
		term1HBox.setAlignment(Pos.CENTER);
		t1BP.setCenter(term1HBox);
		
		HBox bottomHBox = new HBox(resT1,addT1,removeT1,confirmT1);
		bottomHBox.setSpacing(10);
		bottomHBox.setAlignment(Pos.CENTER);
		bottomHBox.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.SOLID, null, new BorderWidths(15))));
		
		t1BP.setBottom(bottomHBox);

		//term 2
		VBox term2UnVBox = new VBox(UnT2,termTwoUn);
		term1UnVBox.setAlignment(Pos.CENTER_LEFT);
		
		VBox term2SeVBox = new VBox(SeT2,termTwoSe);
		term2SeVBox.setAlignment(Pos.CENTER_LEFT);
		HBox term2HBox = new HBox(term2UnVBox,term2SeVBox);
		
		
		HBox.setHgrow(term2UnVBox,Priority.ALWAYS);
		HBox.setHgrow(term2SeVBox,Priority.ALWAYS);
		VBox.setVgrow(termTwoUn,Priority.ALWAYS);
		VBox.setVgrow(termTwoSe,Priority.ALWAYS);
		
		
		
		term2HBox.setSpacing(15);
		term2HBox.setAlignment(Pos.CENTER);
		t2BP.setCenter(term2HBox);
		
		
		HBox bottomT2HBox = new HBox(resT2,addT2,removeT2,confirmT2);
		bottomT2HBox.setSpacing(10);
		bottomT2HBox.setAlignment(Pos.CENTER);
		bottomT2HBox.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.SOLID, null, new BorderWidths(15))));
		
		t2BP.setBottom(bottomT2HBox);
		
		this.getPanes().addAll(term1TP,term2TP);
		this.setExpandedPane(term1TP);
		

		}
	
	
	public void populateReserveModulesPane(List<Module> termOne, List<Module> termTwo) {
		termOneUn.getItems().addAll(termOne);
		termTwoUn.getItems().addAll(termTwo);
		
	}
	
	public boolean isReserveFull(ObservableList<Module> selected) {
		int total = 0;
		for (int i = 0; i<selected.size();i++) {
		    total += selected.get(0).getCredits();
		}
		if (total < 30) {
			return false;
		}
		else {
			return true;
			}
		}
	
	public void changeTab() {
		this.setExpandedPane(term2TP);
		
	}
		
	public ListView<Module> getUnselectedTermOne() {
		return termOneUn;
	}
	public ListView<Module> getUnselectedTermTwo() {
		return termTwoUn;
	}
	public ListView<Module> getSelectedTermOne() {
		return termOneSe;
	}
	public ListView<Module> getSelectedTermTwo() {
		return termTwoSe;
	}
	
	public void addTerm1AddBtnReserveHandler(EventHandler<ActionEvent> handler) {
		addT1.setOnAction(handler);
	}
	public void addTerm2AddBtnReserveHandler(EventHandler<ActionEvent> handler) {
		addT2.setOnAction(handler);
	}
	public void addTerm1RemoveBtnReserveHandler(EventHandler<ActionEvent> handler) {
		removeT1.setOnAction(handler);
	}
	public void addTerm2RemoveBtnReserveHandler(EventHandler<ActionEvent> handler) {
		removeT2.setOnAction(handler);
	}
	public void addTerm1ConfirmBtnReserveHandler(EventHandler<ActionEvent> handler) {
		confirmT1.setOnAction(handler);
	}
	public void addTerm2ConfirmBtnReserveHandler(EventHandler<ActionEvent> handler) {
		confirmT2.setOnAction(handler);
	}
		
		
		
		
		
		


	}
