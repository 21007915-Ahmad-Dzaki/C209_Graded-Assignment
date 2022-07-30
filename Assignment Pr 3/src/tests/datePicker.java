/**
 * I declare that this code was written by me.
 * I will not copy or allow others to copy my code.
 * I understand that copying code is considered as plagiarism.
 *
 * Ahmad Dzaki Bin Osman, 21007915, 26 Jul 2022 7:27:05 pm
 */

package tests;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.*;


/**
 * @author ahmad
 *
 */
public class datePicker extends Application{

	private Button bt = new Button("show date");
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	public void start(Stage s) {
		 // set title for the stage
       s.setTitle("creating date picker");
 
       // create a tile pane
       TilePane r = new TilePane();
 
       // create a date picker
       DatePicker d = new DatePicker();
 
       // add button and label
       r.getChildren().addAll(d,bt);
 
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
