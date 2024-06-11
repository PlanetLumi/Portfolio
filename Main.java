import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.ArrayList;

//This holds critical variables and starts the game
public class Main extends Application
    {
    //Declares global Game Objects to be called throughout the program
    public static ArrayList<String> mainDeck = Model.BuildDeck(); 
    public static GameObj gameObj = new GameObj();
    public static GameObj.CPU[] comp = Model.getCPU();
    public static GameObj.Player Player = gameObj.new Player();
    public static GameObj.Dealer Dealer = gameObj.new Dealer();
    
    //Creates the pain for the main menu and also runs the Controller start game process.
    public void start(Stage primaryStage){
      View view = new View();
      view.start(primaryStage);
      Controller.startGame();
    }  

    public static void main(String[] args) {

        //launches the javafx
        launch(args); 
    }
}