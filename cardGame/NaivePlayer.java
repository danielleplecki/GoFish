package cardGame;

/**
 * A class that specifies the player's strategy as naive
 *
 * @author Danielle Plecki
 */
public class NaivePlayer extends Player implements PlayerStrategy{

    NaivePlayer(int playerNumber, int totalNumberOfPlayers){
        this.playerNumber = playerNumber;
        this.totalNumberOfPlayers = totalNumberOfPlayers;
    }

    /**
     * The first function called on the object to provide information that is true for the whole execution.
     * @param yourPlayerNumber a number between 0 and (totalNumberOfPlayers - 1) specifying the current player
     * @param totalNumberOfPlayers a positive integer indicating the total number of players in the game
     */
    public void initialize(int yourPlayerNumber, int totalNumberOfPlayers){
    }

    /**
     * This method uses the player's current hand and generates a random play
     * @param hand The current state of the player's hand when they are to act
     * @return a random play requesting a random player for a random rank in their hand
     */
    public Play doTurn(Hand hand){
        int targetPlayer = (int) (Math.random()*totalNumberOfPlayers);
        while (targetPlayer == playerNumber){
            targetPlayer = (int) (Math.random()*totalNumberOfPlayers);
        }
        int randomCard = (int) (Math.random()*hand.size());
        int requestedRank = hand.getCard(randomCard).getRank();
        return new Play(targetPlayer, requestedRank);
    }

    /**
     * Every time a game action takes place, the game engine invokes the following function on each player.
     * @param recordedPlay an object representing the information of the play that just occurred and its results.
     */
    public void playOccurred(RecordedPlay recordedPlay){
    }
}
