import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;


public class GameObj{
int totalScore;
int myScore = 500;
int myHandValue;
int myBet;
boolean betMade = false;
boolean blackJack = false;
boolean hasFold = false;
boolean hasBust = false;
String compName;
double[] myPlace = {0,0}; 
ArrayList<String> myDeck = new ArrayList<String>();
boolean spread = false; //This is a value for the dealer as their cards spreads horizontally rather than diagonally 
  
  
//Sets the value for the players 'bet' object variable to the given integer
public final void setBet(int value) {
        this.myBet = value;
}

//This returns the name of the player
public String giveName(){
      return this.compName;
}

//Sets the compName Object variable to the given string value
public void setName(String name){
    this.compName = name;
}

//Sets the 'myDeck' arraylist value to a empty list
public void resetDeck(){
    this.myDeck = new ArrayList<String>();
}

//Finds the current bet
public boolean getBetMade(){
    return this.betMade;
}

//Sets the myBet value to 0
public void resetBet(){
    this.myBet = 0;
}

//Sets the value to true or false depending on whether the object has bet or not
public void betPlaced(boolean placed){
    this.betMade = placed;
}

//Sets the fold boolean to true or false if they have folded
public void fold(boolean folded){
    this.hasFold = folded;
}

//Returns the value of the objects bet
public int getBet(){
    return this.myBet;
}

//Removes the int of bet from the objects current score 
public void subtractScore(int bet){
   this.myScore = this.myScore - bet;
}

//Adds the int of bet from the objects current score
public void addToScore(int bet){
    this.myScore += bet;
}

//Adds the int of bet to the 'myBet' object variable
public void addToBet(int bet){
    this.myBet += bet;
}

//Sets the total score to the current score
public void updateTotalScore(int myScore){
    this.totalScore += myScore;
}

//Sets the blackJack variable value to true
public void updateBlackJack(boolean update){
    this.blackJack = update;
}

//Returns the boolean for the black jack object variable
public boolean checkBlackJack(){
    return this.blackJack;
}

//Returns the integer value for the score of the object
public int getScore(){
    return this.myScore;
}

//Sets the comp to the intialised 500 integer value
public void resetScore(){
    this.myScore = 500;
}

//Resets totalScore to 0
public void resetTotalScore(){
    this.totalScore = 0;
}

//Returns the integer value for the total score of the object
public int getTotalScore(){
    return this.totalScore;
}

//Adds to the value of the bet, it says post because it is post win/loss
public void updateBetPost(int win){
    //Times it by the returned value of win, which could be 0,1,2 depending on whether the player has won lost or drew 
    int placeholder = this.getBet() * win;
    this.myBet += placeholder;
}

//If the value of the deck is over 21 it will set the player to 
public void updateHasBust(){
      if(!this.hasBust){  
        if(myHandValue > 21){
          this.hasBust = true;
          //Displays that they have bust
          Controller.goneBust(this);
          } 
      }   
}

//Returns the boolean value of hasBust
public boolean checkBust(){
      return this.hasBust;
}
  
//Returns the boolean value of has fold 
  public boolean checkFold(){
    if(this.hasFold){
        //Displays that they have folded
        Controller.goneFold(this);
    }
    return this.hasFold;
}

//Returns the boolean value of hasFold
public boolean whatFold(){
      return this.hasFold;
}

//Sets the hasBust to the boolean value of the given value
public void setBust(boolean busted){
    this.hasBust = busted;
}

//Sets the value of the Objects myHandValue to the given integer
public void updateValue(int value){
     this.myHandValue = value;
}

//Adds integer newScore to the total Score of ther Object
public void updateScore(int newScore){
    this.totalScore += newScore;
}

//Adds a new card to the deck of object which is given in the parameters
public void updateMyDeck(String Hand){
    this.myDeck.add(Hand);
}

//Is a class for the computers as they have specific constructos and AI
public class CPU extends GameObj{
//constructor for values that are dfifferent with each player
public CPU(double posX, double posY, String name){
    //Coordinates for where to display on the screen
    this.myPlace[0] = posX;
    this.myPlace[1] = posY;
    //Name for the player to be distinguishable when on the leaderboard
    this.compName = name;
}

//This is the AI for the CPU, the higher the risk and higher the HandValue
//The less chance of hitting
public void AI() {
   //Checks to see if it has a turn
   this.updateHasBust();
   //Finds current value of hack
   this.updateValue(Model.getDeckValue(this));
   //Checks to see if it has a turn
   Model.checkBlackJack(this);
   //Will not loop if already folded or busted
   if(!this.checkBust() && !this.hasFold) {
       //generates random value between 1 and 20
       int roll = (int) (Math.random() * 20);
        // Check for conditions related to betting more than 70% of their current score
        if (this.myBet >= (this.myScore * 0.7) && this.myHandValue > 17 && this.myHandValue < 21) {
            //The higher the roll needs to be the harder it is to roll to hit again 
            //Simulating a 'safer option' the closer they are to 21
            if (roll > 15) {
                this.fold(false);
            } else {
                this.fold(true);
                Controller.goneFold(this);
            }
        }
        // Check for conditions related to betting more than 50% of their current score and having a low hand value
        else if (this.myBet >= (this.myScore * 0.5) && this.myHandValue < 17) {
            if (roll > 10) {
                this.fold(false);
            } else {
                this.fold(true);
                Controller.goneFold(this);
            }
        } 
        // Default action if none of the specific conditions are met
        else {
            this.fold(roll >= myHandValue);
            if (this.hasFold) {
                Controller.goneFold(this);
            }
        }
    }
    this.updateHasBust();
}
    
//This gets the bet of the players by a random number generation 
//This stops them from betting a random amount and losing at the start of the game
public void Bet(){
    int roll1 = this.myScore;
    int roll2 = this.myScore; 
    //allows informed choice
    this.updateValue(Model.getDeckValue(this));
    //All in when black Jack
    if(this.myScore >= 500 && (this.myHandValue > 17 || this.myHandValue <11)) {
    //Can bet up to 90% of score if in an advantageous position{    
       roll1 = (int)(Math.random() * (this.myScore * 0.9));
       roll2 = (int)(Math.random() * (this.myScore * 0.9));
    } else if ((this.myScore < 500 && this.myScore > 20) || (this.myHandValue < 17 && this.myHandValue > 11)){   
        //Can only bet up to 75% of their score
        roll1 = (int)(Math.random() * (this.myScore * 0.75)); 
        roll2 = (int)(Math.random() * (this.myScore * 0.75));
    }else if(this.myBet < 20 && this.myBet > 1){
        //Can bet any random number if score is low enough
        //Lets them 'give up' like a human
        roll1 = (int)(Math.random() * (this.myScore));
        roll2 = (int)(Math.random() * (this.myScore));
    }

    
    //Bets all in if the CPU gets Black Jack
    if(!(this.myHandValue == 21)){
        //Finds the lowest roll to make them the safest option
        this.myBet = Math.min(roll1,roll2);
    } else{
        this.setBet(myScore);
    }
    //Adds the bet and removes the bet from their score
    if(this.myBet < 1) {
        this.setBet(1);
    }
    this.subtractScore(this.getBet());
    } 
}

//Makes a distinct player class - easier to manage
public class Player extends GameObj{
    public Player(){
      //set up position values
      this.myPlace[0] = 0.47;
      this.myPlace[1] = 0.63;
      //Sets up name for leaderboard
      this.setName("You");
    }
}
  

//Makes a distinct dealer class
public class Dealer extends GameObj{
    public Dealer(){
          //Sets up coords for left of middle
          this.myPlace[0] = 0.36; 
          this.myPlace[1] = 0.45;
          //Makes it so that the cards spread horizontally not diagonally
          this.spread = true;
    }
      public boolean AI(){
          this.updateHasBust();
          Model.checkBlackJack(this);  
          if(myHandValue > 16 && !this.whatFold() && !this.checkBust() && !this.checkBlackJack()){
             this.fold(true);
             this.checkFold();
          }
          return this.hasFold;
      }
    }
}
