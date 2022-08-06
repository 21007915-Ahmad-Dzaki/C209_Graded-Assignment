/**
 * I declare that this code was written by me.
 * I will not copy or allow others to copy my code.
 * I understand that copying code is considered as plagiarism.
 *
 * Ahmad Dzaki Bin Osman, 21007915, 6 Jul 2022 5:59:29 pm
 */

package project_3;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javafx.application.Application;
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
public class JSApp_Add extends Application {
	
	private ArrayList<JoggingSpot> jsList = new ArrayList<JoggingSpot>();
	
	private ToggleGroup RBgroup = new ToggleGroup();
	private ToggleGroup YNgroup = new ToggleGroup();

	private VBox vbPane = new VBox();
	private HBox RBPane = new HBox();
	private HBox timePane = new HBox();
	
	private Label lbWelcome = new Label("           Jogging Spot\nAdding new Jogging Spot");
	private Label lbName = new Label("Name");
	private Label lbCat = new Label("Category");
	private Label lbSeaview = new Label("Is there a Seaview? (Y/N)");
	private Label lbDistance = new Label("Enter total distance");
	private Label lbClosingTime = new Label("Enter Closing Time? (hh:mm)");
	
	private RadioButton RBPark = new RadioButton("Park");
	private RadioButton RBPC= new RadioButton("Park Connector");
	private RadioButton RBStadium= new RadioButton("Stadium");
	private RadioButton Yseaview = new RadioButton("Yes");
	private RadioButton Nseaview = new RadioButton("No");
	
	private ComboBox<Integer> hour = new ComboBox<Integer>();
	private ComboBox<Integer> min = new ComboBox<Integer>();
	
	private TextField tfName = new TextField();
	private TextField tfDistance = new TextField();
	private int hourValue;
	private int minValue;
	
	private Label status = new Label();
	
	private Button btAdd = new Button("Add Jogging Spot");
	
	private static final String JDBC_URL ="jdbc:mysql://localhost:3306/assignment_pr_3";
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD = "";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	public void start(Stage primaryStage) {
		//declare connectionString,user,pass for SQL connection
		DBUtil.init(JDBC_URL, DB_USERNAME, DB_PASSWORD);
		
		lbWelcome.setFont(Font.font("Arial",15));
		RBgroup.getToggles().addAll(RBPark,RBPC,RBStadium);
		
		for (int i = 0; i<24;i++) {
			hour.getItems().add(i);
		}
		for (int i = 0; i<61;i++) {
			min.getItems().add(i);
		}

		RBgroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(RBPark)) {
            	parkTf();
            } else if (newValue.equals(RBPC)) {
            	pcTf();
            } else if(newValue.equals(RBStadium)) {
            	stadiumTf();
            }
        });
		
		RBPark.setPrefWidth(70);
		RBPC.setPrefWidth(130);
		RBPane.getChildren().addAll(RBPark,RBPC,RBStadium);
		RBPane.setAlignment(Pos.CENTER);
		
		timePane.getChildren().addAll(hour,min);
		timePane.setAlignment(Pos.CENTER);
		timePane.setSpacing(10);
		
		vbPane.getChildren().addAll(lbWelcome,lbName,tfName,lbCat,RBPane);
		vbPane.setAlignment(Pos.TOP_CENTER);
		vbPane.setSpacing(10);
		vbPane.setPadding(new Insets(10,10,10,10));

		Scene mainScene = new Scene(vbPane);
		
		primaryStage.setTitle("Add Jogging Spot");
		primaryStage.setWidth(400);
		primaryStage.setHeight(500);
		primaryStage.setScene(mainScene);
		primaryStage.show();
		
		EventHandler<ActionEvent> handleAdd = (ActionEvent e) -> addJS();
		btAdd.setOnAction(handleAdd);
		
		hour.setOnAction((event) -> {
		    hourValue = hour.getValue(); //get value and convert to string
		});
		
		min.setOnAction((event) -> {
		    minValue = min.getValue(); //get value and convert to string
		});
	}
	private void addJS(){
		
		String sql = "";

		int rowsAffected = 0;

		String newID = getNewID();
		
		String name = tfName.getText();
		
		if (RBPark.isSelected()&& !tfName.getText().isBlank()&& (Yseaview.isSelected()|| Nseaview.isSelected())) {
			int hasSeaview = 0;
			if (Yseaview.isSelected()) {
				hasSeaview = 1;
			} 
			else if (Nseaview.isSelected()) {
				hasSeaview = 0;
			} 
			sql = "INSERT INTO jogging_spot(ID, Name, Category, HasSeaview, DistanceKm, ClosingTime) " 
					+ "VALUES ('" + newID + "' ,'"+ name + "', '" + "Park" + "', '"+ hasSeaview +"'," + null+ "," + null + ")";
			rowsAffected = DBUtil.execSQL(sql);
		}
		if (RBPC.isSelected()&& !tfName.getText().isBlank()&& !tfDistance.getText().isBlank()) {
			String distance = tfDistance.getText();
			if(distance.matches("\\d{1,3}(\\.(\\d){1,2})?")) {
				sql = "INSERT INTO jogging_spot(ID, Name, Category, HasSeaview, DistanceKm, ClosingTime) " 
						+ "VALUES ('" + newID + "' ,'"+ name + "', '" + "Park Connector" + "', "+ null +", '" + Double.parseDouble(distance) + "',"+ null + ")";
				rowsAffected = DBUtil.execSQL(sql);

			}
		}
		if (RBStadium.isSelected()&& !tfName.getText().isBlank()) {
			LocalTime closeTime = LocalTime.of(hourValue, minValue);
//			System.out.println(closeTime);
			sql = "INSERT INTO jogging_spot(ID, Name, Category, HasSeaview, DistanceKm, ClosingTime) " 
					+ "VALUES ('" + newID + "' ,'" + name + "', '" + "Stadium" + "'," + null + "," + null + ",'" + closeTime + "'" + ")";
			rowsAffected = DBUtil.execSQL(sql);


		}
		if (rowsAffected == 1) {
			status.setText("Jogging Spot Added!");
		} else {
			status.setText("Insert failed! Ensure all fields are filled!");
		}


	}
	
	private void parkTf() {
		vbPane.getChildren().removeAll(lbDistance,tfDistance,lbClosingTime,timePane,btAdd,status);
		vbPane.getChildren().addAll(lbSeaview,Yseaview,Nseaview,btAdd,status);
		Yseaview.setToggleGroup(YNgroup);
	    Yseaview.setSelected(true);
	    Nseaview.setToggleGroup(YNgroup);
	}
	private void pcTf() {
		vbPane.getChildren().removeAll(lbSeaview,Yseaview,Nseaview,lbClosingTime,timePane,btAdd,status);
		vbPane.getChildren().addAll(lbDistance,tfDistance,btAdd,status);
	}
	private void stadiumTf() {
		vbPane.getChildren().removeAll(lbSeaview,Yseaview,Nseaview,lbDistance,tfDistance,btAdd,status);
		vbPane.getChildren().addAll(lbClosingTime,timePane,btAdd,status);
	}
	private void load() {
		try {
			String sql = "SELECT * FROM jogging_spot";
			ResultSet rs = DBUtil.getTable(sql);
			
			while (rs.next()) {
				String id = rs.getString("ID");
				String name = rs.getString("Name");
				String cat = rs.getString("Category");
				boolean seaview = rs.getBoolean("HasSeaview");
				double distance = rs.getDouble("DistanceKm");
				Time ct = rs.getTime("ClosingTime");
				
				if (distance == 0 && ct == null) {
					jsList.add(new Park(id, name, cat, seaview));
				}
				else if (seaview == false && ct == null) {
					jsList.add(new ParkConnector(id, name, cat, distance));
				}
				else if (seaview == false && distance == 0) {
					LocalTime closeTime = ct.toLocalTime();
					jsList.add(new Stadium(id, name, cat, closeTime));
				}
			}
			
		}catch (SQLException se) {
			se.printStackTrace();
		}
	}
	private String getNewID() {
		jsList.clear();
		load();
		String lastID = jsList.get(jsList.size()-1).getId();
		
		//used to increment the ID then add back together with the letter J
		String letterID = lastID.substring(0,1);
		int numID = Integer.parseInt(lastID.substring(1));
		numID++;
		return letterID + numID;
	}

}
