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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * @author ahmad
 *
 */
public class JSApp_FindJS extends Application{

	private TextArea taResults = new TextArea();
	ListView<String> list = new ListView<String>();
	
	private VBox vbPane = new VBox();
	private HBox hbPane = new HBox();
	private BorderPane pane = new BorderPane();
	
	private Button btDelete = new Button("Delete");
	private Button btEdit = new Button("Edit Details");
	private Button btEventDate = new Button ("Add new Event Date");
	
	private int index;
	
	//ArrayList to store the objects with the field queried from SQL
	private final ObservableList<JoggingSpot> jsList = FXCollections.observableArrayList();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	public void start(Stage mainStage) {
		
		String connectionString = "jdbc:mysql://localhost:3306/assignment_pr_3";
		String userid = "root";
		String password = "";
	
		DBUtil.init(connectionString, userid, password);
		
		loadIntoArray();
		
		list.setEditable(false);
		list.setMaxHeight(150);

		loadList();
		taResults.setPrefHeight(130);
		taResults.setFont(Font.font("Consolas",15));
		
		list.getSelectionModel().selectedItemProperty().addListener(e -> {
			btDelete.setVisible(true);
			btEdit.setVisible(true);
			index = list.getSelectionModel().getSelectedIndex();
			if (jsList.get(index) instanceof Park) {
				btEventDate.setVisible(false);
				Park p = (Park)jsList.get(index);
				String outputPark = String.format("%s: %s\n%s: %s\n%s: %s\n", "ID",p.getId(),"NAME",p.getName(),"CATEGORY",p.getCategory());
				taResults.setText(outputPark);
				System.out.println(list.getSelectionModel().getSelectedIndex());
			}
			if (jsList.get(index) instanceof ParkConnector) {
				btEventDate.setVisible(false);
				ParkConnector pc = (ParkConnector)jsList.get(index);
				String outputPC = String.format("%s: %s\n%s: %s\n%s: %s\n", "ID",pc.getId(),"NAME",pc.getName(),"CATEGORY",pc.getCategory());
				taResults.setText(outputPC);
				System.out.println(list.getSelectionModel().getSelectedIndex());
			}
			if (jsList.get(index) instanceof Stadium) {
				btEventDate.setVisible(true);
				Stadium s = (Stadium)jsList.get(index);
				String outputStadium = String.format("%s: %s\n%s: %s\n%s: %s\n", "ID",s.getId(),"NAME",s.getName(),"CATEGORY",s.getCategory());
				taResults.setText(outputStadium);
				System.out.println(list.getSelectionModel().getSelectedIndex());
			}

		});
		

		btDelete.setVisible(false);
		btEdit.setVisible(false);
		btEventDate.setVisible(false);

		hbPane.getChildren().addAll(btDelete,btEdit,btEventDate);
		hbPane.setAlignment(Pos.CENTER);
		hbPane.setSpacing(10);
		vbPane.setSpacing(15);
		pane.setPadding(new Insets(10,10,10,10));

		vbPane.getChildren().addAll(taResults,hbPane);
		vbPane.setAlignment(Pos.CENTER);
		pane.setTop(list);
		pane.setCenter(vbPane);
		Scene mainScene = new Scene(pane);
		mainStage.setScene(mainScene);
		mainStage.setTitle("Delete Jogging Spot");
		mainStage.setWidth(600);
		mainStage.setHeight(400);
		mainStage.show();
		
		EventHandler<ActionEvent> handleDelete = (ActionEvent e) -> deleteJS();
		
		btDelete.setOnAction(handleDelete);
	}
	private void loadIntoArray() {
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
	public void loadList() {
		// TODO Auto-generated method stub
		try {
			for(JoggingSpot p : jsList) {
				if(list.getItems().size() == 0) {
					list.getItems().add(0, p.getName());

				} else {
					list.getItems().add(list.getItems().size(), p.getName());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void deleteJS() {
		list.refresh();
		String id =  jsList.get(index).getId();

		String sql = "DELETE from jogging_spot WHERE ID = '" + id + "'";
		int rowsAffected = DBUtil.execSQL(sql);
		
		if (rowsAffected == 1) {
			list.getItems().remove(index);
			taResults.setText("Jogging Spot deleted!");
		}
		else {
			taResults.setText("Delete Failed!");
		}
	}
}
