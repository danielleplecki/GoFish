package cardGame;

/**
 * Class of all players in a game
 * @author Danielle Plecki
 */
public class Player{
    int playerNumber;
    int totalNumberOfPlayers;
    public int score;
    PlayerStrategy strategy;

    Player(int newPlayerNumber, int totalNumberOfPlayers, PlayerStrategy strategy){
        this.playerNumber = newPlayerNumber;
        this.totalNumberOfPlayers = totalNumberOfPlayers;
        this.strategy = strategy;
    }
    Player() {
    }
    public PlayerStrategy getStrategy(){
        return strategy;
    }

    public int getTotalNumberOfPlayers(){
        return totalNumberOfPlayers;
    }

    public int getPlayerNumber(){
        return playerNumber;
    }


}
