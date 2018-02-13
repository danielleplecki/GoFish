package cardGame;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Class to run test suite of the other three test classes
 * @author Danielle Plecki
 */

@RunWith(Suite.class)
@SuiteClasses({CardTest.class, DeckTest.class, HandTest.class, GameTest.class})

public class RunTestSuite {
}