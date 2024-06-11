import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.ArrayList;

//This takes request and uses the other programs to complete logic
public class Controller {

    // Variables to track the state of different panes and game stages
    public static boolean mainPaneOpen = false;
    public static boolean betPaneOpen = false;
    public static Stage keepMainStage;
    public static Stage keepBetStage;
    public static boolean gameEnded = false;

    // Handles the player hitting for an additional card
    public static void hit() {
        if (!Main.Player.checkBust() && !Main.Player.whatFold()) {
            if (!Main.Player.getBetMade()) {
                bet();
            } else {
                Main.Player.updateHasBust();
                Model.checkBlackJack(Main.Player);
                if (!Main.Player.whatFold() && !Main.Player.checkBlackJack()) {
                    Model.roundPlayer(Main.Player, Main.mainDeck);
                }
                if (Main.Player.hasBust || Main.Player.checkBlackJack()) {
                    playerFinished();
                }
            }
        }
    }

    // Runs the round for all players after the main player's turn
    public static void playerFinished() {
        Model.round(Main.comp, Main.mainDeck);
        Main.Dealer.updateHasBust();
        if (!Main.Dealer.checkBust()) {
            Model.dealerRound(Main.Dealer, Main.mainDeck);
        }
        Model.compareScores(Main.comp, Main.Player, Main.Dealer);
        if (Main.Player.getScore() > 0) {
            displayRoundButton();
        } else {
            Failed();
        }
    }

    // Requests the computer players to make their bets
    public static void requestBet() {
        Model.getAllCompBet(Main.comp);
    }

    // Displays the bets made by the computer players
    public static void showBet(GameObj comp) {
        View.displayCompBet(comp);
    }

    // Opens the betting pane for the main player
    public static void bet() {
        if (!betPaneOpen) {
            if (!Main.Player.getBetMade()) {
                keepBetStage = View.loadBetView(Main.Player);
            }
        }
    }

    // Handles the main player folding
    public static void fold() {
        if (!Main.Player.getBetMade()) {
            bet();
        } else {
            Model.checkBlackJack(Main.Player);
            if (!Main.Player.checkBust() && !Main.Player.checkBlackJack()) {
                Main.Player.fold(true);
                goneFold(Main.Player);
                playerFinished();
            } else {
                playerFinished();
            }
        }
    }

    // Displays the scores for all players
    public static void showAllScore() {
        Model.showAllScore(Main.comp, Main.Player);
    }

    // Displays the score for a specific player or computer
    public static void showThisScore(GameObj comp) {
        View.displayScore(comp);
    }

    // Adds the player's bet to their game object
    public static void addBet(int betNumber) {
        if (!Main.Player.getBetMade()) {
            Model.playerBet(Main.Player, betNumber);
        }
    }

    // Updates the displayed bet amount and player's current score
    public static void updateBet() {
        View.betAmount.setText("Current Bet " + Main.Player.getBet());
        View.betViewScore.setText("Current Amount " + Main.Player.getScore());
    }

    // Finalizes the player's bet and closes the betting pane
    public static void setBet() {
        if (Main.Player.getBet() > 0) {
            Main.Player.betPlaced(true);
            stageClose(keepBetStage);
            betPaneOpen = Model.flip(betPaneOpen);
            showBet(Main.Player);
        }
    }

    // Displays the outcome (win, loss, draw) for a player or computer
    public static void displayOutcome(GameObj comp, int outcome) {
        if (outcome == 0) {
            View.displayLoss(comp);
        }
        if (!comp.checkBust()) {
            if (outcome == 1) {
                View.displayWinner(comp);
            }
            if (outcome == 2) {
                View.displayDraw(comp);
            }
        }
    }

    // Closes a given stage
    public static void stageClose(Stage stage) {
        View.viewClose(stage);
    }

    // Displays that a player has gotten a blackjack
    public static void goneBlackJack(GameObj comp) {
        View.displayBlackJack(comp);
    }

    // Displays that a player has gone bust
    public static void goneBust(GameObj comp) {
        View.displayBust(comp);
    }

    // Displays that a player has folded
    public static void goneFold(GameObj comp) {
        View.displayFold(comp);
    }

    // Displays the button to start the next round if the player has points to bet
    public static void displayRoundButton() {
        View.loadNextRoundButton();
    }

    // Ends the game, displays leaderboard, and closes all panes
    public static void EndOfGame() {
        View.loadLeaderBoard();
        Model.setNewScoresAll(Main.comp, Main.Player);
        Model.displayAllLeaderBoard(Main.comp, Main.Player);
        closeMainGame();
        if (betPaneOpen) {
            stageClose(keepBetStage);
            betPaneOpen = Model.flip(betPaneOpen);
        }
        gameEnded = Model.flip(gameEnded);
    }

    // Starts the main game
    public static void mainGameButton() {
        if (!mainPaneOpen) {
            View.loadMainGame();
            mainPaneOpen = Model.flip(mainPaneOpen);
            Controller.requestBet();
            Controller.showAllScore();
        }
    }

    // Closes the main game
    public static void closeMainGame() {
        View.viewClose(View.mainGame);
        mainPaneOpen = Model.flip(mainPaneOpen);
    }

    // Handles the player failing and ending the game
    public static void Failed() {
        EndOfGame();
        retryGame();
    }

    // Allows the player to restart the game and play the next round
    public static void restart() {
        betPaneOpen = Model.flip(betPaneOpen);
        Model.restartAllObjects(Main.comp, Main.Player, Main.Dealer);
        Model.startGame();
        closeMainGame();
        mainGameButton();
    }

    // Attempts to retry the game after exiting or losing
    public static void retryGame() {
        Main.mainDeck = Model.BuildDeck();
        Model.restartAllObjects(Main.comp, Main.Player, Main.Dealer);
        Model.restartAllScore(Main.comp,Main.Player);
        gameEnded = Model.flip(gameEnded);
        Model.startGame();  
    }

    // Starts the game or retries if the game has ended
    public static void startGame() {
        if(!gameEnded){
            Model.startGame();
        } else{
            retryGame();
        }
    }
}