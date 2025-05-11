package Weaver.test;

import Weaver.model.Model;
import org.junit.jupiter.api.Test;

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
     * Requires that 'cold' is in dictionary.txt.
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
     * Requires 'cold', 'cord', 'card', 'ward', 'warm' to be in dictionary.txt.
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
        assertEquals(4, model.getAttemptedPath().size());
    }
}
