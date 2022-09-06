package program;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;



public class program extends Application{

    
    @Override
    public void start(Stage stage){

        Parent root = new Parent(){};


        try{
            root = FXMLLoader.load(this.getClass().getResource("/test.fxml"));
            System.out.print("UI file found!");
        
        }
        catch (IOException e){
            System.out.print("Error using the UI file "+e.getCause());
        }
        catch (NullPointerException e2){
            System.out.print("UI file not found!");
        }

        Scene mainScene = new Scene(root, 500, 500, Color.PURPLE);
        stage.setScene(mainScene);
        stage.setFullScreen(true);

        stage.show();

    }
    
    public static void main(String args[]) {
        launch();
    }
}