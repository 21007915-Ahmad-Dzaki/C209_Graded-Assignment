/**
 * I declare that this code was written by me.
 * I will not copy or allow others to copy my code.
 * I understand that copying code is considered as plagiarism.
 *
 * Ahmad Dzaki Bin Osman, 21007915, 28 Jun 2022 9:47:04 pm
 */

package project_3;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * @author ahmad
 *
 */
public class JSApp_JFX extends Application{
	
	// to position buttons, title, text vertically/horizontally
	private VBox vbPane = new VBox();
	private HBox hbPane = new HBox();
	private HBox hbPaneCheck = new HBox();
	
	//Display the title
	private Label lbAppTitleWelcome = new Label("Welcome to the Jogging Spots App!");
	
	private CheckBox checkPark = new CheckBox("Park");
	private CheckBox checkPC = new CheckBox("Park Connector");
	private CheckBox checkStadium = new CheckBox("Stadium");
	
	//4 buttons for the app
	private Button btView = new Button("View All Jogging Spots");
	private Button btAdd = new Button("Add Jogging Spots");
	private Button btEdit = new Button("Edit Jogging Spots Details");
	private Button btDelete = new Button("Delete Jogging Spots");

	//textbox used to display things
	private TextArea taResults = new TextArea();
	
	//ArrayList to store the objects with the field queried from SQL
	private ArrayList<JoggingSpot> jsList = new ArrayList<JoggingSpot>();
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	public void start(Stage primaryStage) {
		String connectionString = "jdbc:mysql://localhost:3306/assignment_pr_3";
		String userid = "root";
		String password = "";
	
		DBUtil.init(connectionString, userid, password);
		
		load();
		
		
		//positioning the buttons with spacing and position
		hbPane.setSpacing(10);
		hbPane.setAlignment(Pos.CENTER);
		// include the buttons in the horizontal pane
		hbPane.getChildren().addAll(btView,btAdd,btEdit,btDelete);
		hbPaneCheck.getChildren().addAll(checkPark,checkPC,checkStadium);
		hbPaneCheck.setSpacing(15);
		hbPaneCheck.setAlignment(Pos.CENTER);
		
		
		//positioning all elements with spacing, padding and position
		vbPane.setSpacing(10);
		vbPane.setPadding(new Insets(10,10,10,10));
		vbPane.setAlignment(Pos.TOP_CENTER);
		taResults.setPrefHeight(400);
		lbAppTitleWelcome.setFont(Font.font("Consolas", 15));
		// include all elements in the vertical pane
		taResults.setText(viewAll());
		taResults.setFont(Font.font("Consolas",15));
		vbPane.getChildren().addAll(lbAppTitleWelcome,hbPane,hbPaneCheck,taResults);
		
		// create scene to display within the stage(skeleton)
		Scene mainScene = new Scene(vbPane);
		primaryStage.setScene(mainScene);
		
		// create the stage (skeleton for the app)
		primaryStage.setTitle("Jogging Spot");
		primaryStage.setWidth(900);
		primaryStage.setHeight(600);
		
		// display the app window, the GUI
		primaryStage.show();
		
		EventHandler<ActionEvent> handleView = (ActionEvent e) -> viewAll();
		btView.setOnAction(handleView);
		
		EventHandler<ActionEvent> handleShowSpot = (ActionEvent e) -> viewByCat();
		checkPark.setOnAction(handleShowSpot);
		checkPC.setOnAction(handleShowSpot);
		checkStadium.setOnAction(handleShowSpot);
		
		EventHandler<ActionEvent> handleAdd = (ActionEvent e) -> (new JSApp_Add()).start(new Stage());
		btAdd.setOnAction(handleAdd);
		
	}
	
	// create the load method to store sql query into ArrayList
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
	
	private String viewAll() {
		String outputPark = String.format("%-10s %-30s %-25s %-30s\n", "ID","NAME","CATEGORY", "SEAVIEW");
		outputPark += Helper.line(80, "=") + "\n";
		String outputPC = String.format("\n%-10s %-30s %-25s %-30s\n", "ID","NAME","CATEGORY", "DISTANCE");
		outputPC += Helper.line(80, "=") + "\n";
		String outputStadium = "";
		for (JoggingSpot js : jsList) {
			if(js instanceof Park) {
				Park p = (Park)js;
				outputPark += p.display();
			}
			if(js instanceof ParkConnector) {
				ParkConnector pc = (ParkConnector)js;
				outputPC += pc.display();
			}
			if(js instanceof Stadium) {
				Stadium s = (Stadium)js;
				outputStadium += s.display();
				outputStadium += "\n" + s.announceUnavailability(s.getId());
				outputStadium += "\n" ;
				
			}
		}
		return outputPark + outputPC + outputStadium;
		
	}
	
	private void viewByCat() {
		String outputPark = String.format("%-10s %-30s %-25s %-30s\n", "ID","NAME","CATEGORY", "SEAVIEW");
		outputPark += Helper.line(80, "=") + "\n";
		String outputPC = String.format("%-10s %-30s %-25s %-30s\n", "ID","NAME","CATEGORY", "DISTANCE");
		outputPC += Helper.line(80, "=") + "\n";
		String outputStadium = "";
		String output = "";
		
//		boolean pFound = false;
//		boolean pcFound = false;
//		boolean sFound = false;
		
		for (JoggingSpot js : jsList) {
			if (checkPark.isSelected()) {
				if(js instanceof Park) {
					Park p = (Park)js;
					outputPark += p.display();
//					pFound = true;
					taResults.setText(outputPark);
			}
			}
			else if (checkPC.isSelected()) {
			if(js instanceof ParkConnector) {
				ParkConnector pc = (ParkConnector)js;
				outputPC += pc.display();
//				pcFound = true;
				taResults.setText(outputPC);
			}
			}
			else if (checkStadium.isSelected()) {
			if(js instanceof Stadium) {
				Stadium s = (Stadium)js;
				outputStadium += s.display();
				outputStadium += "\n" + s.announceUnavailability(s.getId());
				outputStadium += "\n" ;
//				sFound =true;
				taResults.setText(outputStadium);
			}
			}
			else {
				taResults.setText(viewAll());
			}
		}
//		if (pFound == true) {
//			output+= outputPark;
////			taResults.setText(output);
//		}else if (pcFound == true) {
//			output+=outputPC;
////			taResults.setText(output);
//		}else if (sFound == true) {
//			output+=outputStadium;
////			taResults.setText(output);
//		}
//		else {
//		taResults.setText(viewAll());
//		}
	}
	

	
}
