/**
 * I declare that this code was written by me.
 * I will not copy or allow others to copy my code.
 * I understand that copying code is considered as plagiarism.
 *
 * Ahmad Dzaki Bin Osman, 21007915, 30 Jul 2022 6:49:06 pm
 */

package project_3;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author ahmad
 *
 */
public class JSApp_EventDate extends Application{

	private VBox topVPane = new VBox();
	private VBox midVPane = new VBox();
	private HBox botHPane = new HBox();
	private BorderPane pane = new BorderPane();
	
	private DatePicker datePick = new DatePicker();
	
	private Button btAdd = new Button("Add");
	private Button btReset = new Button("Reset");
	
	private Label name = new Label("Event Name");
	private Label date = new Label("Choose Date");
	private Label stadium = new Label();
	private Label id = new Label();
	private Label status = new Label();
	
	private TextField tfEventName = new TextField();
	private ObservableList<JoggingSpot> jsList = FXCollections.observableArrayList();
	private int index;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	public void start(Stage stage) {
		
		String connectionString = "jdbc:mysql://localhost:3306/assignment_pr_3";
		String userid = "root";
		String password = "";
		
		datePick.setEditable(false);
		DBUtil.init(connectionString, userid, password);
		id.setText("ID:"+jsList.get(index).getId());
		id.setAlignment(Pos.CENTER);
		stadium.setText(jsList.get(index).getName());
		stadium.setAlignment(Pos.CENTER);
		topVPane.getChildren().addAll(id,stadium,name,tfEventName);
		topVPane.setAlignment(Pos.CENTER);
		topVPane.setSpacing(10);
		midVPane.getChildren().addAll(date,datePick,status);
		midVPane.setAlignment(Pos.CENTER);
		midVPane.setSpacing(10);
		botHPane.getChildren().addAll(btAdd,btReset);
		botHPane.setAlignment(Pos.CENTER);
		botHPane.setSpacing(10);
		tfEventName.setMaxWidth(200);

		pane.setTop(topVPane);
		pane.setCenter(midVPane);
		pane.setBottom(botHPane);
		pane.setPadding(new Insets(20));
		
		Scene mainScene = new Scene(pane);
		
		stage.setTitle("Adding Stadium Event Date");
		stage.setScene(mainScene);
		stage.setWidth(250);
		stage.setHeight(300);
		stage.show();
		
		EventHandler<ActionEvent> handleAddEvent = (ActionEvent e) -> addEvent();
		btAdd.setOnAction(handleAddEvent);
	}
	public void store(int index,ObservableList<JoggingSpot> jsList) {
		this.index = index;
		this.jsList = jsList;
	}
	private void addEvent() {
		String event = tfEventName.getText();
		String sql = "INSERT INTO unavailability_date(ID, DateUnavailable, Event) " 
				+ "VALUES ('" + jsList.get(index).getId() + "','" + datePick.getValue()+ "','" + event + "')";
		if (!tfEventName.getText().isBlank()) {
			int rowsAffected = DBUtil.execSQL(sql);
		
		if (rowsAffected == 1) {
			status.setText("Event Added!");
		} else {
			status.setText("Insert failed! Ensure all fields are filled!");
		}
		}else {
			status.setText("Please fill in all fields.");
		}
	}

}
