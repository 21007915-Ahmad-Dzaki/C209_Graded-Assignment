/**
 * I declare that this code was written by me.
 * I will not copy or allow others to copy my code.
 * I understand that copying code is considered as plagiarism.
 *
 * Ahmad Dzaki Bin Osman, 21007915, 6 Jul 2022 5:59:29 pm
 */

package project_3;

import java.time.LocalTime;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * @author ahmad
 *
 */
public class JSApp_Add extends Application {

	private VBox vbPane = new VBox();
	private Label lbWelcome = new Label("           Jogging Spot\nAdding new Jogging Spot");
	private Label lbName = new Label("Name");
	private Label lbCat = new Label("Category");
	private Label lbSeaview = new Label("Is there a Seaview?");
	private Label lbDistance = new Label("What is the total distance?");
	private Label lbClosingTime = new Label("What is the closing time?");
	private Label lbStatus = new Label("");
	
	private TextField tfName = new TextField();
	private TextField tfCat = new TextField();
	private TextField tfSeaview = new TextField();
	private TextField tfDistance = new TextField();
	private TextField tfClosingTime = new TextField();
	
	private Label status = new Label();
	
	private Button btAdd = new Button("Add Jogging Spot");
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	public void start(Stage primaryStage) {
		lbWelcome.setFont(Font.font("Arial",15));
		vbPane.getChildren().addAll(lbWelcome,lbName,tfName,lbCat,tfCat);
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
		
		
	}
	private void addJS(){
		
		String sql = "";
		//get the last used ID to generate a new ID for new INSERT
//		String lastID = jsList.get(jsList.size()-1).getId();
		int hasSeaview = 0;
		int rowsAffected = 0;
//		
//		//used to increment the ID then add back together with the letter J
//		String letterID = lastID.substring(0,1);
//		int numID = Integer.parseInt(lastID.substring(1));
//		numID++;
		String newID = getNewID();

		
		System.out.println("ADDING JOGGING SPOT");
		Helper.line(40, "-");
		String name = tfName.getText();
		String cat = Helper.readStringRegEx("Enter Category > ", "([p|P]ark)|([p|P]ark\\s[c|C]onnector)|([s|S]tadium)");
		// making the First letter to be capital
		String firstLetter = cat.substring(0,1).toUpperCase();
		String rmgLetter = cat.substring(1).toLowerCase();
		cat = firstLetter + rmgLetter;
		
		if (cat.toLowerCase().equals("park")) {
			vbPane.getChildren().addAll(lbSeaview,tfSeaview);
			String seaview = tfSeaview.getText();
			if (seaview) {
				hasSeaview = 1;
			}
			else {
				hasSeaview = 0;
			}
			sql = "INSERT INTO jogging_spot(ID, Name, Category, HasSeaview, DistanceKm, ClosingTime) " 
					+ "VALUES ('" + newID + "' ,'"+ name + "', '" + cat + "', '"+ hasSeaview +"'," + null+ "," + null + ")";
			rowsAffected = DBUtil.execSQL(sql);
		}
		else if (cat.toLowerCase().equals("park connector")) {
			double distance = Helper.readDouble("Enter total distance for park connector > ");
			sql = "INSERT INTO jogging_spot(ID, Name, Category, HasSeaview, DistanceKm, ClosingTime) " 
					+ "VALUES ('" + newID + "' ,'"+ name + "', '" + cat + "', "+ null +", '" + distance + "',"+ null + ")";
			rowsAffected = DBUtil.execSQL(sql);
		}
		else if (cat.toLowerCase().equals("stadium")) {
			String ct = Helper.readString("Enter closing time for stadium (hh:mm:ss) > ");
			LocalTime closeTime = LocalTime.parse(ct);
			sql = "INSERT INTO jogging_spot(ID, Name, Category, HasSeaview, DistanceKm, ClosingTime) " 
					+ "VALUES ('" + newID + "' ,'" + name + "', '" + cat + "'," + null + "," + null + ",'" + closeTime + "'" + ")";
			rowsAffected = DBUtil.execSQL(sql);
			
		}
		if (rowsAffected == 1) {
			System.out.println("Jogging Spot added!");
		} else {
			System.out.println("Insert failed!");
		}
	}
}
