import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Debug
    {
     public static Image HandleFilePath(String ending){
         //Gets their OS user name to be used in file path
        String userName = System.getProperty("user.name");
        //First attempt to find file path
        String userPath = "/Desktop";
        //Tries to find file
        String finalPath = "file:///C:/Users/" + userName + userPath + "/Black-Jack-TrueFinal/" + ending;
        //Assigning to class
        Image test = new Image(finalPath);
        //Image classes do not throw exceptions but do set an internal boolean to true, this finds if it true.
        if(!test.isError()){
            //If this is the file path it will return it
            return test;
        } else{
            System.out.println("First attempt failed! Trying next file path path...");
            //Checks different folder
            userPath = "/Downloads";
            finalPath = "file:///C:/Users/" + userName + userPath + "/Black-Jack-TrueFinal/" + ending;
            test = new Image(finalPath);
            if(!test.isError()){
               return test;       
            } else{
                    System.out.println("WARNING: Second attempt failed! Trying next file path...");
                    userPath = "/Documents";
                    finalPath = "file:///C:/Users/" + userName + userPath + "/Black-Jack-TrueFinal/" + ending;
                    test = new Image(finalPath);
                if(!test.isError()){
                    return test;
                    } else{
                        System.out.print("WARNING SERIOUS ERROR: TRYING FINAL ATTEMPT!");
                        finalPath = "file:///C:/Users/" + userName +"/Black-Jack-TrueFinal/" + ending; 
                        test = new Image(finalPath);
                        if(!test.isError()){
                            return test;
                    } else{
                        System.out.println("GAME ASSETS NOT FOUND: PLEASE CENTRALISE INCLUDED FILES LIKE:" + ending);
                        return null;
                    }
                }
            }
        }
    }
    
    public static Pane HandleNullPointer(Pane mainPane){
        try{
            //Creates new pane
            Pane newPane = new Pane();
            //Attempts to set root to Main Pane
            mainPane.getScene().setRoot(new Pane());
            return newPane;
        }catch(NullPointerException e){
            //Stops if mainPane already has root
            System.out.println("Root already taken");
            return mainPane;
        }
    }
}
    


