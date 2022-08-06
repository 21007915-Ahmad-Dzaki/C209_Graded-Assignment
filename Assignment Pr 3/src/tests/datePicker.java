/**
 * I declare that this code was written by me.
 * I will not copy or allow others to copy my code.
 * I understand that copying code is considered as plagiarism.
 *
 * Ahmad Dzaki Bin Osman, 21007915, 26 Jul 2022 7:27:05 pm
 */

package tests;

import java.time.LocalDate;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import project_3.JSApp_FindJS;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.*;


/**
 * @author ahmad
 *
 */
public class datePicker extends Application{

	private HBox hPane = new HBox();
	private Button bt = new Button("show date");
	private ComboBox hour = new ComboBox();
	private ComboBox min = new ComboBox();
	private Label time = new Label(":");
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	public void start(Stage s) {
		
		JSApp_FindJS js = new JSApp_FindJS();
		hour.getItems().add("5");
		hPane.getChildren().addAll(hour,time,min);
		hPane.setSpacing(10);
		
		hour.setOnAction((event) -> {
		    int selectedIndex = hour.getSelectionModel().getSelectedIndex();
		    Object selectedItem = hour.getSelectionModel().getSelectedItem();

		    System.out.println("Selection made: [" + selectedIndex + "] " + selectedItem);
		    System.out.println("   ComboBox.getValue(): " + hour.getValue());
		    String value = (String) hour.getValue(); //get value and convert to string
		    System.out.println(value + ":30");
//		    LocalTime timee = LocalTime.parse(value);
//		    System.out.println(timee);
		});
		 // set title for the stage
       s.setTitle("creating date picker");
 
       // create a tile pane
       TilePane r = new TilePane();
 
       // create a date picker
       DatePicker d = new DatePicker();
 
       // add button and label
       r.getChildren().addAll(d,bt,hPane);
 
       // create a scene
       Scene sc = new Scene(r, 200, 200);
 
       // set the scene
       s.setScene(sc);
 
       s.show();
       
       EventHandler<ActionEvent> show = (ActionEvent e) -> {
    	  System.out.println(d.getValue());
    	   
       };
       bt.setOnAction(show);
	}
}
