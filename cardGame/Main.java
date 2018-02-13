package cardGame;

/**
 * This class creates a new game to user's specifications and prints the output of the game
 * @author Danielle Plecki
 */

public class Main {

    public static void main(String[] args) {
        if (args.length > 10 || args.length < 2) {
            System.out.println("ERROR: Invalid number of Players!");
        } else {
            Game currentGame = new Game();
            System.out.println(currentGame.playGame(args));
        }
    }
}
