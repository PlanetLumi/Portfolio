import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class View extends Application {
    public static int sizeOfScreen = 1200;
    public static Label betAmount;
    public static Label betViewScore;
    public static Stage mainGame = new Stage();
    public static Pane mainPane = new Pane();
    public static Stage leaderBoardGame = new Stage();
    public static Pane leaderBoardPane = new Pane();

    // The main entry point for all JavaFX applications
    @Override
    public void start(Stage menuStage) {
        Pane mainMenu = new Pane();
        mainMenu.setStyle("-fx-background-color: black;"); // Set background color to black
        loadAttribution(mainMenu);
        loadTitleMainMenu(mainMenu); // Load the title on the main menu
        loadButtonMainMenu(mainMenu, menuStage); // Load buttons on the main menu
        Scene scene = new Scene(mainMenu, 1200, 1200);
        menuStage.setTitle("Main Menu");
        menuStage.setScene(scene);
        menuStage.show(); // Show the main menu
    }
    public static void loadAttribution(Pane mainMenu){
        Text shoutOut = new Text(100,100, "Game Table design by: Vecteezy.com");
        shoutOut.setFont(Font.font("Verdana", 25));
        shoutOut.relocate(700,700);
        shoutOut.setFill(Color.WHITE);
        mainMenu.getChildren().add(shoutOut);
    }

    // Creates a new main game stage
    public static void createNewMainGame() {
        mainGame = new Stage();
        mainPane = new Pane();
    }
    public static void loadTitleMainMenu(Pane mainMenu) {
        Text title = new Text(350, 100, "Black Jack");
        title.setFont(Font.font("Verdana", 100));
        title.relocate(325, 300);
        title.setFill(Color.RED);
        Text subTitle = new Text(350, 100, "Main Menu");
        subTitle.setFont(Font.font("Verdana", 50));
        subTitle.relocate(425, 400);
        subTitle.setFill(Color.RED); 
        mainMenu.getChildren().addAll(title, subTitle);
    }

    public static void loadButtonMainMenu(Pane mainMenu, Stage menuStage) {
        Button button1 = new Button("Start Game");
        Button button2 = new Button("Quit Game");
        button1.setMaxSize(100, 200);
        button1.relocate(500, 500);
        button2.setMaxSize(100, 200);
        button2.relocate(500, 550);
        button1.setOnAction(e -> Controller.mainGameButton());
        button2.setOnAction(e -> viewClose(menuStage));
        mainMenu.getChildren().addAll(button1, button2);
    }

    public static void loadMainGame() {
        clearMainPane();
        setMainPaneStyle();
        loadTable();
        loadGameButtons();
        displayAll(Main.comp, Main.Player, Main.Dealer);
        setupMainGameScene();
    }

    private static void clearMainPane() {
        mainPane.getChildren().clear();
        mainPane = (Debug.HandleNullPointer(mainPane));
    }
    private static void setMainPaneStyle() {
        mainPane.setStyle("-fx-background-color: grey;");
    }

    private static void setupMainGameScene() {
        Scene mainScene = new Scene(mainPane, sizeOfScreen, sizeOfScreen);
        mainGame.setScene(mainScene);
        mainGame.setTitle("Game Table");
        mainGame.show();
    }

    public static void loadTable() {
        ImageView mainTable = new ImageView();
        Image table = ((Debug.HandleFilePath("cartoontable.png")));
        mainTable.setImage(table);
        mainTable.setFitWidth(900);
        mainTable.setFitHeight(700);
        mainTable.relocate(0.12 * sizeOfScreen, 0.2 * sizeOfScreen);
        mainPane.getChildren().add(mainTable);
    }

    public static void loadGameButtons() {
        createGameButton("HIT", 0.2, e -> Controller.hit());
        createGameButton("STAND", 0.4, e -> Controller.fold());
        createGameButton("BET", 0.6, e -> Controller.bet());
        createGameButton("EXIT", 0.8, e -> Controller.Failed());
    }

    private static void createGameButton(String text, double xMultiplier, EventHandler<ActionEvent> eventHandler) {
        Button button = new Button(text);
        button.setPrefSize(100, 100);
        button.relocate(xMultiplier * sizeOfScreen, 1000);
        button.setOnAction(eventHandler);
        mainPane.getChildren().add(button);
    }

    public static void loadNextRoundButton() {
        createRoundButton("START NEXT ROUND", 0.9, e -> Controller.restart());
    }

    public static void loadLeaderBoardButton() {
        createRoundButton("SHOW STATS", 0.9, e -> Controller.restart());
    }

    private static void createRoundButton(String text, double xMultiplier, EventHandler<ActionEvent> eventHandler) {
        Button button = new Button(text);
        button.setPrefSize(100, 100);
        button.relocate(xMultiplier * sizeOfScreen, 1000);
        button.setOnAction(eventHandler);
        mainPane.getChildren().add(button);
    }

    public static void displayCompBet(GameObj comp) {
        Text bets = createText("BET IS " + comp.getBet(), 25, Color.WHITE);
        relocateText(bets, Model.findPlaceX(comp, sizeOfScreen), Model.findPlaceYBet(comp, sizeOfScreen));
        mainPane.getChildren().add(bets);
    }

    public static void displayScore(GameObj comp) {
        Text score = createText("SCORE IS " + comp.getScore(), 15, Color.WHITE);
        relocateText(score, Model.findPlaceX(comp, sizeOfScreen), Model.findPlaceYScore(comp, sizeOfScreen));
        mainPane.getChildren().add(score);
    }

    public static void displayFold(GameObj comp) {
        Text fold = createText("STAND!", 50, Color.GREEN);
        relocateText(fold, comp.myPlace[0] * sizeOfScreen, comp.myPlace[1] * sizeOfScreen - 50);
        mainPane.getChildren().add(fold);
    }

    public static void displayBust(GameObj comp) {
        Text bust = createText("BUST!", 50, Color.RED);
        relocateText(bust, comp.myPlace[0] * sizeOfScreen, comp.myPlace[1] * sizeOfScreen - 50);
        mainPane.getChildren().add(bust);
    }

    public static void displayBlackJack(GameObj comp) {
        Text bJack = createText("BLACK JACK!", 50, Color.BLUE);
        relocateText(bJack, comp.myPlace[0] * sizeOfScreen - 20, comp.myPlace[1] * sizeOfScreen - 50);
        mainPane.getChildren().add(bJack);
    }

    public static void displayAll(GameObj[] comp, GameObj player, GameObj dealer) {
        displayComp(comp);
        display(player);
        display(dealer);
    }

    public static void displayWinner(GameObj comp) {
        Text winner = createText("PAY OUT!", 30, Color.GREEN);
        relocateText(winner, comp.myPlace[0] * sizeOfScreen + 10, comp.myPlace[1] * sizeOfScreen + 130);
        mainPane.getChildren().add(winner);
    }

    public static void displayLoss(GameObj comp) {
        Text loser = createText("LOSS!", 30, Color.RED);
        relocateText(loser, comp.myPlace[0] * sizeOfScreen + 10, comp.myPlace[1] * sizeOfScreen + 130);
        mainPane.getChildren().add(loser);
    }

    public static void displayDraw(GameObj comp) {
        Text draw = createText("DRAW!", 30, Color.BLUE);
        relocateText(draw, comp.myPlace[0] * sizeOfScreen + 10, comp.myPlace[1] * sizeOfScreen + 130);
        mainPane.getChildren().add(draw);
    }

    private static Text createText(String content, int fontSize, Color color) {
        Text text = new Text(100, 100, content);
        text.setFont(Font.font("Verdana", fontSize));
        text.setFill(color);
        return text;
    }

    private static void relocateText(Text text, double xPos, double yPos) {
        text.relocate(xPos, yPos);
    }

    public static Stage loadBetView(GameObj player) {
        int sizeOfScreenBet = 600;
        Stage betView = new Stage();
        Pane betViewPane = new Pane();
        betViewPane.setStyle("-fx-background-color: grey;");
        addBetViewTitle(betViewPane);
        loadBetButtons(betViewPane, sizeOfScreenBet);
        loadBetLabel(betViewPane, player, sizeOfScreenBet);
        loadBetScore(betViewPane, player, sizeOfScreenBet);
        setupBetViewScene(betView, betViewPane);
        return betView;
    }

    private static void addBetViewTitle(Pane betViewPane) {
        Text betViewTitle = new Text(50, 50, "Betting Box");
        betViewTitle.setFont(Font.font("Verdana", 50));
        betViewTitle.relocate(50, 100);
        betViewTitle.setFill(Color.ORANGE);
        betViewPane.getChildren().add(betViewTitle);
    }

    private static void setupBetViewScene(Stage betView, Pane betViewPane) {
        Scene betScene = new Scene(betViewPane, 500, 600);
        betView.setTitle("Game Table");
        betView.setScene(betScene);
        betView.show();
    }

    public static void viewClose(Stage stage) {
        stage.close();
    }

    public static void loadBetButtons(Pane betViewPane, int sizeOfScreenBet) {
        createBetButton("ALL IN!", 0, 0.4, e -> Controller.addBet(6), betViewPane, sizeOfScreenBet);
        createBetButton("ADD HALF", 0.2, 0.4, e -> Controller.addBet(5), betViewPane, sizeOfScreenBet);
        createBetButton("ADD QUARTER", 0.4, 0.4, e -> Controller.addBet(4), betViewPane, sizeOfScreenBet);
        createBetButton("ADD TEN", 0, 0.6, e -> Controller.addBet(3), betViewPane, sizeOfScreenBet);
        createBetButton("ADD FIVE", 0.2, 0.6, e -> Controller.addBet(2), betViewPane, sizeOfScreenBet);
        createBetButton("ADD ONE", 0.4, 0.6, e -> Controller.addBet(1), betViewPane, sizeOfScreenBet);
        createBetButton("RESET", 0, 0.8, e -> Controller.addBet(0), betViewPane, sizeOfScreenBet);
        createBetButton("MAKE BET", 0.2, 0.8, e -> Controller.setBet(), betViewPane, sizeOfScreenBet);
    }

    private static void createBetButton(String text, double xMultiplier, double yMultiplier, EventHandler<ActionEvent> eventHandler, Pane pane, int sizeOfScreenBet) {
        Button button = new Button(text);
        button.setPrefSize(100, 100);
        button.relocate(xMultiplier * sizeOfScreenBet, yMultiplier * sizeOfScreenBet);
        button.setOnAction(eventHandler);
        pane.getChildren().add(button);
    }

    public static void loadBetLabel(Pane betViewPane, GameObj player, int sizeOfScreenBet) {
        betAmount = new Label("Current Bet " + player.getBet());
        betAmount.setFont(Font.font("Verdana", 20));
        betAmount.relocate(0.4 * sizeOfScreenBet, 0.8 * sizeOfScreenBet);
        betAmount.setTextFill(Color.WHITE);
        betViewPane.getChildren().add(betAmount);
    }

    public static void loadBetScore(Pane betViewPane, GameObj player, int sizeOfScreenBet) {
        betViewScore = new Label("Current Balance " + player.getScore());
        betViewScore.setFont(Font.font("Verdana", 20));
        betViewScore.relocate(0.4 * sizeOfScreenBet, 0.9 * sizeOfScreenBet);
        betViewScore.setTextFill(Color.WHITE);
        betViewPane.getChildren().add(betViewScore);
    }

    public static void displayComp(GameObj[] comp) {
         for (int i = 0; i < comp.length; i++) { 
             display(comp[i]);
        }
    }

    public static void display(GameObj current) {
        int cardIndex = 0;
        System.out.println(current);
        for(cardIndex = 0; cardIndex < current.myDeck.size(); cardIndex++){ 
            displayDeck(current,current.myDeck.get(cardIndex), cardIndex); 
        }
    }
    
    public static void displayDeck(GameObj comp, String card, int cardIndex) {
        double xPos;
        double yPos;
        String currentCard = "PNG-cards-1.3/" + card + ".png";
        ImageView compCard = new ImageView();
        Image cardSet = ((Debug.HandleFilePath(currentCard)));
        compCard.setImage(cardSet);
        compCard.setFitWidth(70);
        compCard.setFitHeight(100);
        if(comp.spread){
            xPos = (comp.myPlace[0] * sizeOfScreen) + (cardIndex*90);
            yPos = (comp.myPlace[1] * sizeOfScreen);
        }else{
            xPos = (comp.myPlace[0] * sizeOfScreen) + (cardIndex*15);
            yPos = (comp.myPlace[1] * sizeOfScreen)+ (cardIndex*15);
        }
        compCard.relocate(xPos, yPos);
        mainPane.getChildren().add(compCard);
    }

    public static void loadLeaderBoard() {
        int sizeOfScreenBoard = 800;
        leaderBoardGame = new Stage();
        leaderBoardPane = new Pane();
        leaderBoardPane.setStyle("-fx-background-color: blue;");
        addLeaderBoardTitle(sizeOfScreenBoard);
        setupLeaderBoardScene(sizeOfScreenBoard);
    }

    private static void addLeaderBoardTitle(int sizeOfScreenBoard) {
        Text leaderBoardTitle = new Text(350, 100, "LeaderBoard");
        leaderBoardTitle.setFont(Font.font("Verdana", 100));
        leaderBoardTitle.relocate(0.1 * sizeOfScreenBoard, 0.1 * sizeOfScreenBoard);
        leaderBoardTitle.setFill(Color.GREEN);
        leaderBoardPane.getChildren().add(leaderBoardTitle);
    }

    private static void setupLeaderBoardScene(int sizeOfScreenBoard) {
        Scene boardScene = new Scene(leaderBoardPane, sizeOfScreenBoard, sizeOfScreenBoard);
        leaderBoardGame.setTitle("Leader Board");
        leaderBoardGame.setScene(boardScene);
        leaderBoardGame.show();
    }

    public static void displayTotalScore(GameObj comp, String playerName, int sizeOfScreenBoard, int placementPosX, int placementPosY) {
        final int baseHeight = 100;
        final int offsetX = 100;
        int newYPos = placementPosY * baseHeight + 200;
        Text thisPlayer = createLeaderBoardText("Player " + playerName, 40, offsetX, newYPos);
        Text thisScore = createLeaderBoardText("Score of: " + comp.getTotalScore(), 20, offsetX, newYPos + 50);
        leaderBoardPane.getChildren().addAll(thisPlayer, thisScore);
    }

    private static Text createLeaderBoardText(String content, int fontSize, int x, int y) {
        Text text = new Text(content);
        text.setFont(Font.font("Verdana", fontSize));
        text.setFill(Color.BLACK);
        text.relocate(x, y);
        return text;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
