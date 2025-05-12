package Weaver.test;

import Weaver.model.Model;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Model class of the Weaver game.
 */
public class ModelTest {

    /**
     * Test Case 1:
     * Verifies that isOneLetterDiff returns true for words with only one letter different.
     */
    @Test
    public void testIsOneLetterDiff() {
        Model model = new Model();
        assertTrue(model.isOneLetterDiff("cold", "cord"));
        assertFalse(model.isOneLetterDiff("cold", "warm")); // multiple letters differ
    }

    /**
     * Test Case 2:
     * Verifies that a valid word from the dictionary is accepted.
     */
    @Test
    public void testIsValidWord() {
        Model model = new Model();
        assertTrue(model.isValidWord("cold"));   // valid word
        assertFalse(model.isValidWord("xxxx"));  // invalid word
    }

    /**
     * Test Case 3:
     * Tests a successful move sequence that ends in a win.
     */
    @Test
    public void testMakeMoveToWin() {
        Model model = new Model();
        model.setRandomStartAndTarget(false);
        model.setStartAndTarget("cold", "warm");

        assertTrue(model.makeMove("cord"));
        assertTrue(model.makeMove("card"));
        assertTrue(model.makeMove("ward"));
        assertTrue(model.makeMove("warm"));

        assertTrue(model.isWinningWord("warm"));
        assertEquals(5, model.getAttemptedPath().size()); // includes starting word
    }

    /**
     * Test Case 4:
     * Tests invalid moves are not accepted (word not in dictionary).
     */
    @Test
    public void testInvalidMoveNotInDictionary() {
        Model model = new Model();
        model.setStartAndTarget("cold", "warm");
        assertFalse(model.makeMove("xxxx"));
    }

    /**
     * Test Case 5:
     * Tests move with more than one letter difference is rejected.
     */
    @Test
    public void testInvalidMoveMultiLetterDiff() {
        Model model = new Model();
        model.setStartAndTarget("cold", "warm");
        assertFalse(model.makeMove("warm")); // cold to warm differs by more than 1 letter
    }

    /**
     * Test Case 6:
     * Tests random start/target logic can be activated.
     */
    @Test
    public void testRandomStartAndTarget() {
        Model model = new Model();
        model.setRandomStartAndTarget(true);
        model.resetGame();
        assertNotEquals(model.getStartWord(), model.getTargetWord());
    }

    /**
     * Test Case 7:
     * Tests that findSolutionPath returns a non-empty path for a known solvable pair.
     */
    @Test
    public void testFindSolutionPath() {
        Model model = new Model();
        model.setStartAndTarget("cold", "warm");
        List<String> path = model.findSolutionPath();
        assertNotNull(path);
        assertFalse(path.isEmpty());
        assertEquals("cold", path.get(0));
        assertEquals("warm", path.get(path.size() - 1));
    }
}
