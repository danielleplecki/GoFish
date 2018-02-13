package cardGame;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Class to test methods in the GameSetup class
 * @author Danielle Plecki
 */
public class GameTest extends GameSetup{
    private Deck testDeck;
    private List<Card> testList;
    private Hand testHand;
    private ArrayList<Player> testPlayers;
    private String[] testArgs = {"naive", "naive", "smart"};

    @Before
    //Creates a test hand of 7 cards
    public void setUp(){
        testDeck = new Deck();
        testList = testDeck.draw(7);
        testHand = new Hand(testList);
        testPlayers = initializePlayers(testArgs);
    }

    @Test
    public void tesAddCard(){
        Card newCard = testDeck.draw();
        Hand newHand = addCard(testHand,newCard);
        assertTrue(newHand.size() == 8);
    }

    @Test
    public void testGetNewCurrentPlayer(){
        Player currentPlayer = testPlayers.get(2); // 3rd player
        int newPlayerPos = getNewCurrentPlayer(currentPlayer, testPlayers);
        assertFalse(newPlayerPos == 3);
    }



}