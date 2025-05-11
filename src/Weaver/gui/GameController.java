package Weaver.gui;

import Weaver.model.Model;

import java.util.*;

/**
 * Controller for handling user input and game actions in the GUI.
 * Receives input from the GameView and updates the Model accordingly.
 */
public class GameController {
    private final Model model;
    private final GameView view;

    public GameController(Model model, GameView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Handles user word input from the input field.
     * @param word the word entered by the user
     */
    public void handleInput(String word) {
        word = word.trim().toLowerCase();

        if (!model.makeMove(word)) {
            if (model.isShowErrorIfInvalid()) {
                System.out.println("Invalid move: either not in dictionary or not one letter different.");
            }
            return;
        }

        if (model.isWinningWord(word)) {
            view.showWinDialog();
        }
    }

    /**
     * Resets the game by clearing all progress and retaining start/target.
     */
    public void handleReset() {
        model.resetGame();
    }

    /**
     * Starts a new game: either random or fixed based on flag.
     */
    public void handleNewGame() {
        if (model.isRandomStartAndTarget()) {
            // Try generating reachable word pairs
            for (int i = 0; i < 10; i++) { // Try up to 10 times
                model.setStartAndTarget("----", "----");
                if (isReachable(model.getStartWord(), model.getTargetWord())) {
                    return;
                }
            }
            System.out.println("⚠️ No reachable word pair found after multiple attempts. Please try again.");
        } else {
            model.setStartAndTarget("cold", "warm");
        }
    }
    /**
     * Checks whether a path exists from start to target via valid words.
     */
    private boolean isReachable(String start, String target) {
        if (start == null || target == null) return false;
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(target)) return true;

            for (String word : model.getDictionary()) {
                if (!visited.contains(word) && model.isOneLetterDiff(current, word)) {
                    visited.add(word);
                    queue.add(word);
                }
            }
        }
        return false;
    }
}


