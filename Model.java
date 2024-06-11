import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
public class Model{

//this is used if I need to flip a boolean value like the panes 
public static boolean flip(boolean value){
    return !value;
}
      
//This automates the deck building process. 
public static ArrayList<String> BuildDeck(){
    ArrayList<String> deck = new ArrayList<String>(); 
    String[] suit = {"Spades", "Hearts", "Clubs", "Diamonds"};
    String[] card = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight","Nine","Ten","Jack", "Queen", "King"};
    //This is a nested for loop that will create a deck of 52 cards and gives them their name and deck.
    for(int y = 0; y < 4; y++){
      for(int x = 0; x < 13; x++){
        String new_card = card[x] + " of " + suit[y];
        deck.add(new_card);
        }
    }
    return deck;
}   
  
//This is the shuffle system. It creates an array by calling the BuildDeck() method, then shuffles it 
//using the shuffle(array list) method.
public static ArrayList<String> ShuffleDeck(){
    ArrayList<String> deck = BuildDeck();
    Collections.shuffle(deck);
    return deck;
}

//This is the dealing system, it will simply pop the top card from the deck and return it as a value
//it also makes sure the deck is not empty.
public static String DealerPick(ArrayList<String> deck){
    if(deck.isEmpty()){
       deck = ShuffleDeck();
    }
    String card = deck.remove(0);
    return card;
}

//this takes the name of the current card and then finds the value of the
//the cards string name. ("Two of Hearts" -> int 2)
public static int findValue(String card){
    //Finds the first word
    String num = card.split(" ")[0];
    switch (num) {
        case "Two":
            return 2;
        case "Three":
            return 3;
        case "Four":
            return 4;
        case "Five":
            return 5;
        case "Six":
            return 6;
        case "Seven":
            return 7;
        case "Eight":
            return 8;
        case "Nine":
            return 9;
        case "Ten":
            return 10;
        case "Jack":
            return 10;
        case "Queen":
            return 10;
        case "King":
            return 10;
        case "Ace":
            return 11;  
        default:
            return 0;  
        }
}
  
//this takes all the game objects that play and finds their value by plugging
//their object into the getDeckValue function and then adding that to their 
//object deck Value code
public static void findAllValue(GameObj[] comp, GameObj player){
    for(int x = 0; x < 3; x++){
        comp[x].updateValue(getDeckValue(comp[x]));
    }
    player.updateValue(getDeckValue(player));
}

//this checks the game objects hand and returns the total of the hand
public static int getDeckValue(GameObj comp){
    int total = 0;
    int placeholder;
    for(int deckIndex = 0; deckIndex < comp.myDeck.size(); deckIndex++){
        placeholder = findValue(comp.myDeck.get(deckIndex));
        //Ace -> 11
        //If Ace makes player go bust (Ace -> 1)
        if(placeholder == 11){
            if((total + 11) > 21){
                placeholder = 1;
            }
        }
        total += placeholder;
        if(total > 21){
            //SetBust stops players from trying to play more
            comp.setBust(true);
            Controller.goneBust(comp);
        }
    }
    return total;
}

//This checks to see if the player has gotten a Black jack (card value of 21)
public static void checkBlackJack(GameObj comp){
    comp.updateValue(Model.getDeckValue(comp));
    if(comp.myHandValue == 21){
        comp.fold(true); //Stops their turn automatically, ensures you don't throw
        comp.updateBlackJack(true);
        Controller.goneBlackJack(comp);
    }
}

//This uses a FOR loop to distribute a hand to each user
//This deals cars to a newHand depending on how many need to be dealt 
public static void dealCards(ArrayList<String>deck, GameObj comp, int numCards){
    //This stops from getting new cards if turn ended
    if(!comp.hasBust && !comp.hasFold){ 
        for(int y = 0; y < numCards; y++){
            comp.updateMyDeck(DealerPick(deck));
            comp.updateValue(getDeckValue(comp));
        }
     }
}

//Deals all the decks to the players and dealer
public static void dealAll(ArrayList<String>deck, GameObj CPU[], GameObj Player, GameObj Dealer){
    for(int y = 0; y < 5; y++) {
        //Finds if it is adding to a computer so that it adds to the right computer
        if(y < 3){
        dealCards(deck, CPU[y], 2);
        }
        //Deals starting deck to player
        if(y == 3){
        dealCards(deck, Player, 2);
        } 
        //This is nessecary to have the second hidden card when the game starts.
        //it will be removed and replaced once the turns have been made
        if(y == 4){
          dealCards(deck, Dealer,1); 
          ArrayList<String> backDeck = new ArrayList<String>(); 
          backDeck.add("CardBack");
          dealCards(backDeck,Dealer,1);
        }
    }
}

//This uses the constructor GameObj function to get all the computers
public static GameObj.CPU[] getCPU(){
    GameObj gameObj = new GameObj();
    GameObj.CPU[] comp = new GameObj.CPU[3];
    comp[0] = gameObj.new CPU(0.47,0.25,"Steve");
    comp[1] = gameObj.new CPU(0.18, 0.45, "Mla");
    comp[2] = gameObj.new CPU(0.75,0.45, "Devin");
    return comp;
}

//This find where the userData should be on the table
public static double findPlaceX(GameObj comp, int sizeOfScreen){
    double xPos;
    //If computer is on the left most, we want their stuff to be more to 
    //the left also
    if(comp.myPlace[0] < 0.46){
       xPos = (comp.myPlace[0] * sizeOfScreen) - 190;
    //If computer is on the right most, we want their stuff to be
    //more on the right.
    } else if(comp.myPlace[0] > 0.5){
        xPos = (comp.myPlace[0] * sizeOfScreen) + 150;
    //If computer is on the top or bottom of the screen then their stuff should
    //only start a little bit more left then their other data (like pictures)
    } else{
        xPos = (comp.myPlace[0] * sizeOfScreen) - 20;
    }
    return xPos;
}

//Finds how high their bet should show up on the screen
public static double findPlaceYBet(GameObj comp, int sizeOfScreen){
    double yPos;
    if(comp.myPlace[1] == 0.63){
        yPos = (comp.myPlace[1] * sizeOfScreen) + 170;
    } else if(comp.myPlace[1] == 0.25){
        yPos = (comp.myPlace[1] * sizeOfScreen) - 80;
    }else{
        yPos = (comp.myPlace[1] * sizeOfScreen);
    }
    return yPos;
}

  
//Same concept, needs to change between bet and score to not let them overlap
public static double findPlaceYScore(GameObj comp, int sizeOfScreen){
    double yPos;
    if(comp.myPlace[1] == 0.63){
        yPos = (comp.myPlace[1] * sizeOfScreen) + 210;
    } else if(comp.myPlace[1] == 0.25){
        yPos = (comp.myPlace[1] * sizeOfScreen) - 140;
    }else{
        yPos = (comp.myPlace[1] * sizeOfScreen) + 60;
    }
    return yPos;
}

  
//Iterates to each to show the score by calling 
///showThisScore' for each object in the array
//This calls each object to show in the view
public static void showAllScore(GameObj.CPU[] comp, GameObj player){
    for(int x = 0; x < 3; x++){
        Controller.showThisScore(comp[x]);
    }
        Controller.showThisScore(player);
} 

//Takes all the players score and adds it to bet
public static void betFull(GameObj player){
//If score is less than one they cannot bet All in
  if((player.getScore() > 0)){
      player.addToBet((int) (player.getScore()));
      player.subtractScore(player.getBet());
      Controller.updateBet();
  }
}

//Takes half the players score and adds it to bet object variable
public static void betHalf(GameObj player) {
//If they don't have enough to half their score then they cannot bet half
    if (player.getScore() > 2) {
        player.addToBet((int) (player.getScore() * 0.5));
        player.subtractScore((int) (player.getScore() * 0.5));
        Controller.updateBet();
    }
}   

//Takes quarter of the players score and adds it to bet object variable
public static void betQuarter(GameObj player) {
    if (player.getScore() > 4) {
        player.addToBet((int) (player.getScore() * 0.25));
        player.subtractScore((int) (player.getScore() * 0.25));
        Controller.updateBet();
    } 
  }
//Takes 10 of the players score and adds it to the bet object variable
public static void betTen(GameObj player) {
   if (player.getScore() >= 10) {
      player.addToBet(10);
      player.subtractScore(10);   
      Controller.updateBet();
   } 
}

//Takes 5 of the players score and adds it to the bet variable
public static void betFive(GameObj player) {
   if (player.getScore() >= 5) {
      player.addToBet(5);
      player.subtractScore(5);
      Controller.updateBet();
   } 
}

//Takes 1 of the players score and adds it to the bet
public static void betOne(GameObj player) {
    if (player.getScore() > 0) {
        player.addToBet(1);
        player.subtractScore(1);
    }
}

//Sets the bet to 0 and then adds the bet back to the score
public static void resetBet(GameObj player){
    player.addToScore(player.getBet());
    player.resetBet();
    Controller.updateBet();
}

//This takes the inputs from the betting buttons as numbers to classify
//what is being bet (6 -> Bets all of score)
public static void playerBet(GameObj player, int betNumber){
      switch (betNumber) {
            case 6:
                Model.betFull(player);
                break;
            case 5:
                Model.betHalf(player);
                break;
            case 4:
                Model.betQuarter(player);
                break;
            case 3:
                Model.betTen(player);
                break;
            case 2:
                Model.betFive(player);
                break;
            case 1:
                Model.betOne(player);
                break;
            case 0:
                Model.resetBet(player);
                break;
        }
    }
    
//This iterates through the array to see if the computers have bust or folded yet
public static boolean compAllDone(GameObj.CPU[] comp){
    boolean allDone = true;
    for(int x = 0; x < 3; x++){     
        if(!comp[x].hasBust && !comp[x].hasFold){
           allDone = false;
        }
    }
    return allDone;
}

//This gives a card to the player
public static void roundPlayer(GameObj.Player player, ArrayList<String>deck){
    //Makes sure the player cant play after finishing their turn
    if(!player.checkBust() && !player.hasFold){ 
        Model.dealCards(deck,player,1);
        //Checks to see if the player has lost from the new card
        player.updateHasBust();
        View.display(player);
    }
}

//This runs through all the turns of each computer until they have folded or 
//Gone bust
public static void round(GameObj.CPU[] comp, ArrayList<String>deck){
    //This is a counter variable
    int x = 0; 
    //Will loop until the computer ends their turn
    while(!compAllDone(comp)){
        if(x == 3){
            x = 0;
        }
        //Runs an objects code that makes a decision depending on the 
        //Size of their bet
        comp[x].AI();
        //Checks to see if the player is bust before dealing another card
        comp[x].updateHasBust();
        Model.dealCards(deck,comp[x],1);
        //Shows the new card
        View.display(comp[x]);
        //Increments the counter
        x++; 
    }
    
    //After they finish it will get all their scores
    for(int y = 0; y < 3; y++){
        comp[y].updateValue(getDeckValue(comp[y]));  
    }

}

//Plays a round for the dealer when everyone else has finished
public static void dealerRound(GameObj.Dealer dealer, ArrayList<String> deck){
    //Gets rid of the back facing card
    dealer.myDeck.remove(1);
    //Replaces it with a real card
    dealCards(deck,dealer,1);
    //Finds the new value
    dealer.updateValue(Model.getDeckValue(dealer));
    //Shows the new card
    View.display(dealer);
    //The dealer will keep dealing cards until his deck value is over 17,
    //He has gone bust or has hit a black jack
    while(!dealer.AI() && !dealer.checkBust() && !dealer.blackJack){
         dealCards(deck,dealer,1);    
         View.display(dealer);
         dealer.updateValue(Model.getDeckValue(dealer));
    }
}



//Dump bet will add the negative value of myBet to myBet (Makes into 0)
public static void dumpBet(GameObj comp){
      comp.updateBetPost(-1);
}

//This will double their score if they won (myBet)+(myBet * 1) 
public static void winBet(GameObj comp){
      comp.updateBetPost(1);
}

//This returns the bet to the players if they drew with the dealer
//(myBet) + (myBet*0)
public static void returnBet(GameObj comp){
    //This will times their bet by 1 (returning it) and then setting it as their bet
      comp.updateBetPost(0);
}


//This will check with the dealers deck to see if other folded players
//have got higher, equal or lower deck values then him
//This only runs if the dealer has folded
public static void compareFold(GameObj comp, int dealerValue){
        //Is a check flag to see if they user has gone bust
        if(!comp.checkBust()){
            if(comp.myHandValue > dealerValue){
                winBet(comp);
                Controller.displayOutcome(comp,1);
            }
            if(comp.myHandValue == dealerValue){
                returnBet(comp);
                Controller.displayOutcome(comp,2);
            }
            if(comp.myHandValue < dealerValue){
                Model.dumpBet(comp);
                Controller.displayOutcome(comp,0);
            }
        }
  }
 
//This will run an AI that sets the bets of the computers
public static void getCompBet(GameObj.CPU comp){
          comp.Bet();
}

//Iterates through the CPU array of all players to get their bets at the start 
//of the game
public static void getAllCompBet(GameObj.CPU[] comp){
    for(int x = 0; x < 3; x++){
        getCompBet(comp[x]);
        Controller.showBet(comp[x]);
    }
}

//Iterates through the CPU array to see if the players beat the folded dealer
public static void compareFoldAll(GameObj[] comp, GameObj player, int dealerValue){
      for(int x = 0; x < 3; x++){
          compareFold(comp[x], dealerValue);
      }
     //Also compares the players score  
      compareFold(player, dealerValue);
      
}
  
//Iterates through the CPU array to pay out if they have not gone bust
public static void compareDealerBustAll(GameObj[] comp, GameObj player){
    for(int x = 0; x < 3; x++){
            winBet(comp[x]);
            Controller.displayOutcome(comp[x],1);
    }
    winBet(player);
    Controller.displayOutcome(player,1);
}

//Checks all the players to whether they have gone bust
public static void compareBustAll(GameObj[] comp, GameObj player){
        for(int x = 0; x < 3; x++){
            if(comp[x].checkBust()){
                //Sets their bets to 0 
                dumpBet(comp[x]);
                Controller.displayOutcome(comp[x],0);
            }
        }
        
        if(player.checkBust()){
            dumpBet(player);
            Controller.displayOutcome(player,0);
        }
}

//Checks all the possible ways the players could win or lose
public static void compareScores(GameObj[] comp, GameObj player,GameObj dealer){
    Model.findAllValue(comp,player);
    dealer.updateValue(Model.getDeckValue(dealer));
    //If dealer is bust they will see if they have not gone bust and will win
    if(dealer.checkBust()){
        compareBustAll(comp,player);
        compareDealerBustAll(comp,player);
    }
    //If the dealer folded they will check to see if the players beat the dealer
    //This must do both because when a dealer goes bust it will set their bust 
    //value to true
    if(dealer.checkFold() && !dealer.checkBust()){
        compareBustAll(comp,player);
        compareFoldAll(comp, player, dealer.myHandValue);
    }
    setNewScoresAll(comp,player);
}

//Updates all the scores by iterating through CPU array
public static void setNewScoresAll(GameObj[] comp, GameObj player){
    for(int x = 0; x < 3; x++){
        setNewScore(comp[x]);
    }
    setNewScore(player);
    giveEndScore(comp,player);
}

//This puts all the objects into order using the newly made ranking array
//It compares their total score using a bubble sort
public static ArrayList<GameObj> rankScore(GameObj[] comp, GameObj player){
    ArrayList<GameObj> rank = rankAdd(comp,player);
    //N is how many elements the list has (4)
    int n = rank.size();
    //Iterates and compares through each element for each element in the array
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - i - 1; j++) {
            if (rank.get(j).getTotalScore() < rank.get(j + 1).getTotalScore()) {
                //This sets a placeholder so they can swap positions
                GameObj temp = rank.get(j);
                rank.set(j, rank.get(j + 1));
                rank.set(j + 1, temp);
            }
        }
    }
    return rank;
}

//This will create a new array of the player objects to be used to sort into order
public static ArrayList<GameObj> rankAdd(GameObj[] comp,GameObj player){
    ArrayList<GameObj> rank = new ArrayList<GameObj>(); 
    for(int x=0; x < 3; x++){
        rank.add(comp[x]);
    }
    rank.add(player);
    return rank;
}

//Displays the players in order of score descending
public static void displayAllLeaderBoard(GameObj[] comp, GameObj player){
   ArrayList<GameObj> rank = rankAdd(comp, player);
   rank = rankScore(comp,player);
   for(int x = 0; x < 4; x++){
     GameObj current = rank.get(x);
     //800 is the size of the pane
     //100 is where the player will be positioned on the x co-ordinte
     //X is where they will be set on the Y axis.
     //The further down they are the higher the X value which is further on the page
     View.displayTotalScore(current, current.giveName(), 800, 100, x);
   }
}

//Starts the game
public static void startGame(){
        //Iniatilises all the important variables
        Main.mainDeck = Model.ShuffleDeck();
        //Distributes the starting decks
        Model.dealAll(Main.mainDeck, Main.comp, Main.Player,Main.Dealer);
        Model.findAllValue(Main.comp, Main.Player);
}

//Iterates through an array and reset all the players and dealers
public static void restartAllObjects(GameObj[] comp, GameObj player, GameObj dealer){
      for(int x = 0; x <3; x++){
          restartObject(comp[x]);      
      }
      restartObject(player);
      restartObject(dealer);
}

//Resets all the important values of the objects like whether they folded etc
public static void restartObject(GameObj comp){
    comp.betPlaced(false);
    comp.updateBlackJack(false);
    comp.fold(false);
    comp.setBust(false);
    comp.setBet(0);
    comp.resetDeck();
}

public static void restartAllScore(GameObj.CPU[] comp, GameObj player){
    for(int x = 0; x < 3; x++){
        restartScore(comp[x]);
    }
    restartScore(player);
}

public static void restartScore(GameObj comp){
    comp.resetTotalScore();
    comp.resetScore();
}
//Adds the bet after it has been changed due to winning or losing 
public static void setNewScore(GameObj comp){
      comp.addToScore(comp.getBet());
}

//This finds all the players total scores for the end of the game
public static void giveEndScore(GameObj[] comp, GameObj player){
      for(int x = 0; x< 3; x++){
          updateEndScore(comp[x]);
      }
      updateEndScore(player);
}

//Finds the total score of the given object
public static void updateEndScore(GameObj comp){
      comp.updateTotalScore(comp.getScore());
  }
}
