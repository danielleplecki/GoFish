package cardGame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *This class creates a new game and holds all game attributes and functions to be used
 * @author Danielle Plecki
 */
class GameSetup {
    Hand[] playersHands = new Hand[10]; //Max players is 10
    private static final int TOTAL_BOOKS = Card.NUM_RANKS; //Each rank creates a book of 4 cards
    String allPlaysInGame = "";
    private Iterator handIterator;
    ArrayList<Player> allPlayers;
    Deck gameDeck;
    boolean stillPlaying;
    int totalNumberOfPlayers;
    int dealer;
    Player currentPlayer;
    int currentPlayerPos;
    Play currentPlay;
    boolean goesNextTurn;
    Card newCard;
    int requestedRank;
    int targetPlayerPos;
    List<Card> cardsGivenInPlay = new ArrayList<>();
    RecordedPlay completedPlay;
    int booksMade;

    /**
     * Creates Player objects from the users string inputs
     * @param players String array input by user specifying each player's strategy
     * @return an ArrayList of all the player sin the game
     */
    ArrayList<Player> initializePlayers(String[] players) {
        ArrayList<Player> allPlayers = new ArrayList<>();
        int numberOfPlayers = players.length;
        for (int i=0; i<players.length; i++) {
            if (players[i].equals("naive")) {
                allPlayers.add(new Player(i, numberOfPlayers, new NaivePlayer(i, numberOfPlayers)));
            } else {
                allPlayers.add(new Player(i, numberOfPlayers, new SmartPlayer(i, numberOfPlayers)));
            }
        }
        return allPlayers;
    }

    /**
     * Gives one card to each player, whichever player has the lowest ranking card become the dealer
     * @param allPlayers collection of all players in the game
     * @return position of the player who is the dealer
     */
    int getDealer(ArrayList<Player> allPlayers){
        Deck initialDeck = new Deck();
        Hand[] initialHands = new Hand[allPlayers.size()];
        int lowestRank = Card.NUM_RANKS+1; // first rank will be compared to number of ranks plus one, which is one more than the max rank
        int playerWithLowestRank = 0;
        for (int handPos = 0; handPos<allPlayers.size(); handPos++) {
            initialHands[handPos] = new Hand(initialDeck.draw(1));
        }
        for (int j=0; j<allPlayers.size(); j++) {
            Hand currentHand = initialHands[j];
            Card currentCard = currentHand.getCard(0); //All players only have 1 card
            if(currentCard.getRank() < lowestRank){
                lowestRank = currentCard.getRank();
                playerWithLowestRank = j;
            }
        }
        return playerWithLowestRank;
    }

    /**
     * Gives every player random cards from the deck. If there are 2 or 3 players, each player receives 7 cards, if there
     * are more than 3, each player receives 5.
     * @param dealer integer position of the deal, will be player to receive final card
     * @param allPlayers collection of all players in the game
     * @param gameDeck shuffled deck of cards for use in game
     */
    void dealCards(int dealer, ArrayList<Player> allPlayers, Deck gameDeck){
        if(allPlayers.size()<4){
            deal(dealer, allPlayers, gameDeck, 7);
        }
        else{
            deal(dealer, allPlayers, gameDeck, 5);
        }
    }

    /**
     * Gives specified number of cards to each player, used in dealCards method
     * @param dealer integer position of the dealer, will receive the final card
     * @param allPlayers collection of all players in game
     * @param gameDeck shuffled deck of cards
     * @param requiredCards number of cards each player will receive
     */
    private void deal(int dealer, ArrayList<Player> allPlayers, Deck gameDeck, int requiredCards){
        int currentPlayerPosition = dealer;
        boolean doneDealing = false;
        while(!doneDealing){
            currentPlayerPosition++; //Player to left of dealer
            if(currentPlayerPosition == allPlayers.size()){
                currentPlayerPosition = 0;
            }
            playersHands[currentPlayerPosition] = addCard(playersHands[currentPlayerPosition], gameDeck.draw());
            //Dealer will be last to have all cards
            if(currentPlayerPosition == dealer && playersHands[dealer].size() == requiredCards){
                doneDealing = true;
            }
        }
    }

    /**
     * Creates a new Hand of cards consisting of all cards in old and and specified new card
     * @param currentHand the current hand of the player
     * @param card card to be added to hand
     * @return a new hand with the added card and cards in old hand
     */
    Hand addCard(Hand currentHand, Card card){
        List<Card> newHand = new ArrayList<>();
        if(currentHand == null){
            newHand.add(card);
        }
        else{
            for (handIterator = currentHand.iterator(); handIterator.hasNext();){
                Card currentCard = (Card) handIterator.next();
                newHand.add(currentCard);
            }
            newHand.add(card);
        }
        return new Hand(newHand);
    }

    /**
     * Passes the turn to the next player
     * @param currentPlayer player whose turn has just ended
     * @param allPlayers collection of all players in game
     * @return the position of the new current player
     */
    int getNewCurrentPlayer(Player currentPlayer, ArrayList<Player> allPlayers){
        int currentPlayerNumber = currentPlayer.getPlayerNumber();
        int newCurrentPlayerNumber = currentPlayerNumber+1;
        if(newCurrentPlayerNumber == allPlayers.size()){
            newCurrentPlayerNumber = 0;
        }
        return newCurrentPlayerNumber;
    }

    /**
     * Takes cards of requested rank from the target player and gives them to the current player
     * @param currentPlayerPos position of the current player
     * @param requestedPlayer position of the target player
     * @param requestedRank rank of card that player requested
     * @return list of cards that were given by the target player to the current player
     */
    List<Card> giveRequestedCards(int currentPlayerPos, int requestedPlayer, int requestedRank){
        List<Card> cardsGiven = new ArrayList<>();
        List<Card> newTargetPlayerHand= new ArrayList<>();
        Card cardEvaluating;
        Hand requestedPlayerHand = playersHands[requestedPlayer];
        Iterator targetHandIterator = requestedPlayerHand.iterator();
        while (targetHandIterator.hasNext()){
            cardEvaluating = (Card) targetHandIterator.next();
            if(cardEvaluating.getRank() == requestedRank){
                playersHands[currentPlayerPos] = addCard(playersHands[currentPlayerPos], cardEvaluating);
                cardsGiven.add(cardEvaluating);
            }
            else{
                newTargetPlayerHand.add(cardEvaluating);
            }
        }
        playersHands[requestedPlayer] = new Hand(newTargetPlayerHand);
        return cardsGiven;
    }

    /**
     * Records play after each play is made in each player's "memory"
     * @param completedPlay play that just occurred
     * @param allPlayers collection of all players in game
     */
    void recordPlay(RecordedPlay completedPlay, ArrayList<Player> allPlayers){
        for (Player currentPlayer: allPlayers){
            currentPlayer.getStrategy().playOccurred(completedPlay);
        }
    }

    /**
     * Converts the completed play to a String to be added to the game output
     * @param completedPlay play that just occurred
     * @return String represented the completed play
     */
    String toString(RecordedPlay completedPlay){
        int sourcePlayer = completedPlay.getSourcePlayer();
        int targetPlayer = completedPlay.getTargetPlayer();
        int requestedRank = completedPlay.getRank();
        String requestedCards = Card.CARD_NAMES[requestedRank];
        int cardsReturned = completedPlay.getCardsReturned().size();
        return "Player"+sourcePlayer+" asks Player"+targetPlayer+" for "+requestedCards+"s and " +
                "got "+cardsReturned+" card(s).\n";
    }

    /**
     * Checks the current players hand for a completed book
     * @param currentPlayer position of the current player
     * @param newCardRank rank of cards they just received
     * @return whether or not a book was made
     */
    boolean checkForBook(int currentPlayer, int newCardRank){
        int numberOfCardsOfRank = 0;
        Hand currentHand = playersHands[currentPlayer];
        handIterator = currentHand.iterator();
        while(handIterator.hasNext()){
            Card currentCard = (Card) handIterator.next();
            if(currentCard.getRank() == newCardRank){
                numberOfCardsOfRank++;
            }
        }
        return(numberOfCardsOfRank == 4);
    }

    /**
     * If book was made, this method removes the cards of that book from the player's hand
     * @param currentPlayer position of the current player
     * @param bookRank rank of cards of the book that was made
     */
    void createBook(int currentPlayer, int bookRank){
        List<Card> newHand = new ArrayList<>();
        handIterator = playersHands[currentPlayer].iterator();
        while(handIterator.hasNext()){
            Card currentCard = (Card) handIterator.next();
            if(currentCard.getRank() != bookRank){
                newHand.add(currentCard);
            }
        }
        playersHands[currentPlayer] = new Hand(newHand);
    }

    /**
     * Checks if game has ended
     * @param booksMade number of books made in the game
     * @return whether or not all books have been made
     */
    boolean gameOver(int booksMade){
        return (booksMade == TOTAL_BOOKS);
    }

    /**
     * Evaluates scores of all players and determines the winner
     * @param allPlayers collection of all players in game
     * @return String that consists of all players' scores and the winner of the game
     */
    String determineWinner(ArrayList<Player> allPlayers){
        String scoreOutput= "";
        int highestScore = 0;
        Player winner = allPlayers.get(0);
        for (Player currentPlayer: allPlayers){
            scoreOutput += "Player"+allPlayers.indexOf(currentPlayer)+" had "+currentPlayer.score+" points.\n";
            if(currentPlayer.score > highestScore){
                highestScore = currentPlayer.score;
                winner = currentPlayer;
            }
        }
        for (Player tempPlayer: allPlayers){
            if(tempPlayer.score == highestScore){
                scoreOutput += "Player"+allPlayers.indexOf(tempPlayer)+" won!\n";
            }
        }
        return scoreOutput;
    }
}
