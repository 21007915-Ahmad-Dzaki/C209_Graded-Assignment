 /**
* I declare that this code was written by me.
 * I will not copy or allow others to copy my code.
 * I understand that copying code is considered as plagiarism.
 *
 * Ahmad Dzaki Bin Osman, 21007915, 28 Jul 2022 10:13:09 pm
 */

package project_3;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * @author ahmad
 *
 */
public class JSApp_Edit extends Application {
	
	private ObservableList<JoggingSpot> jsList = FXCollections.observableArrayList();
	private int index;
	
	private ToggleGroup RBgroup = new ToggleGroup();
	private ToggleGroup YNgroup = new ToggleGroup();

	private VBox vbPane = new VBox();
	private HBox RBPane = new HBox();
	private HBox timePane = new HBox();
	
	private Label lbWelcome = new Label("       Jogging Spot\n  Edit Jogging Spot");
	private Label lbName = new Label("Name");
	private Label lbCat = new Label("Category");
	private Label lbSeaview = new Label("Is there a Seaview? (Y/N)");
	private Label lbDistance = new Label("Total Distance");
	private Label lbClosingTime = new Label();
	
	private RadioButton Yseaview = new RadioButton("Yes");
	private RadioButton Nseaview = new RadioButton("No");
	
	private ComboBox<Integer> hour = new ComboBox<Integer>();
	private ComboBox<Integer> min = new ComboBox<Integer>();
	
	private TextField tfCat = new TextField();
	private TextField tfName = new TextField();
	private TextField tfDistance = new TextField();
	private int hourValue;
	private int minValue;
	
	private Label status = new Label();
	
	private Button btUpdate = new Button("Update Jogging Spot");
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	public void start(Stage primaryStage) {
		//declare connectionString,user,pass for SQL connection
		String connectionString = "jdbc:mysql://localhost:3306/assignment_pr_3";
		String userid = "root";
		String password = "";
	
		DBUtil.init(connectionString, userid, password);
		
		lbWelcome.setFont(Font.font("Arial",15));
		
		for (int i = 0; i<24;i++) {
			hour.getItems().add(i);
		}
		for (int i = 0; i<61;i++) {
			min.getItems().add(i);
		}
		tfCat.setEditable(false);
		tfName.setEditable(false);
		
		timePane.getChildren().addAll(hour,min);
		timePane.setAlignment(Pos.CENTER);
		timePane.setSpacing(10);

		vbPane.getChildren().addAll(lbWelcome,lbName,tfName,lbCat,tfCat);
		vbPane.setAlignment(Pos.TOP_CENTER);
		vbPane.setSpacing(10);
		vbPane.setPadding(new Insets(10,10,10,10));
		
		if (jsList.get(index) instanceof Park) {
			parkTf();
			Park p = (Park)jsList.get(index);
			tfName.setText(p.getName());
			tfCat.setText(p.getCategory());
			if (p.isSeaview()==true) {
				Yseaview.setSelected(true);
			}else {
				Nseaview.setSelected(true);
			}
		}
		if (jsList.get(index) instanceof ParkConnector) {
			pcTf();
			ParkConnector pc = (ParkConnector)jsList.get(index);
			tfName.setText(pc.getName());
			tfCat.setText(pc.getCategory());
			tfDistance.setText(String.valueOf(pc.getDistance()));
		}
		if (jsList.get(index) instanceof Stadium) {
			stadiumTf();
			Stadium s = (Stadium)jsList.get(index);
			tfName.setText(s.getName());
			tfCat.setText(s.getCategory());
			lbClosingTime.setText("Current Closing Time: " + String.valueOf(s.getCloseTime()));
		}

		Scene mainScene = new Scene(vbPane);

		primaryStage.setTitle("Edit Jogging Spot");
		primaryStage.setWidth(400);
		primaryStage.setHeight(500);
		primaryStage.setScene(mainScene);
		primaryStage.show();

		EventHandler<ActionEvent> handleUpdate = (ActionEvent e) -> updateJS();
		btUpdate.setOnAction(handleUpdate);

		hour.setOnAction((event) -> {
			hourValue = hour.getValue(); //get value and convert to string
		});

		min.setOnAction((event) -> {
			minValue = min.getValue(); //get value and convert to string
		});
	}
	private void updateJS(){
		String sql = "";
		int rowsAffected = 0;
		String id = "";
		if (jsList.get(index) instanceof Park) {
			id =jsList.get(index).getId();
			int hasSeaview = 0;
			if (Yseaview.isSelected()) {
				hasSeaview = 1;
			} 
			else if (Nseaview.isSelected()) {
				hasSeaview = 0;
			}
			sql = "UPDATE jogging_spot SET HasSeaview=" + hasSeaview + " WHERE ID='" + id + "'";
			rowsAffected = DBUtil.execSQL(sql);
		}
		if (jsList.get(index) instanceof ParkConnector) {
			id = jsList.get(index).getId();
			double distance = Double.parseDouble(tfDistance.getText());
			sql = "UPDATE jogging_spot SET DistanceKm=" + distance + " WHERE ID='" + id + "'";
			rowsAffected = DBUtil.execSQL(sql);
		}
		if (jsList.get(index) instanceof Stadium) {
			id =jsList.get(index).getId();
			LocalTime closeTime = LocalTime.of(hourValue, minValue);
			sql = "UPDATE jogging_spot SET ClosingTime= '" + closeTime + "' WHERE ID='" + id + "'";
			rowsAffected = DBUtil.execSQL(sql);
		}
		if (rowsAffected == 1) {
			status.setText("Jogging Spot has been updated!");
		}else {
			status.setText("Update Failed!");
		}

	}

	private void parkTf() {
		vbPane.getChildren().addAll(lbSeaview,Yseaview,Nseaview,btUpdate,status);
		Yseaview.setToggleGroup(YNgroup);
	    Nseaview.setToggleGroup(YNgroup);
	}
	private void pcTf() {
		vbPane.getChildren().addAll(lbDistance,tfDistance,btUpdate,status);
	}
	private void stadiumTf() {
		vbPane.getChildren().addAll(lbClosingTime,timePane,btUpdate,status);
	}

	public void store(int index,ObservableList<JoggingSpot> jsList) {
		this.index = index;
		this.jsList = jsList;
	}

}