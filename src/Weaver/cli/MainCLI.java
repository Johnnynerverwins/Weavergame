package Weaver.cli;

import Weaver.model.Model;

import java.util.List;
import java.util.Scanner;

/**
 * Command-line version of the Weaver game
 * - Uses Model only (no Controller or View)
 */
public class MainCLI {
    public static void main(String[] args) {
        Model model = new Model();

        // Set fixed words or random based on flag
        model.setRandomStartAndTarget(false); // for demo: set to false
        model.setShowErrorIfInvalid(true);
        model.setShowPath(true);

        // Fixed for testing - you can change or randomize
        model.setStartAndTarget("cold", "warm");

        Scanner scanner = new Scanner(System.in);
        System.out.println("🎮 Welcome to Weaver (CLI Version)!");
        System.out.println("Start Word: " + model.getStartWord());
        System.out.println("Target Word: " + model.getTargetWord());

        while (true) {
            System.out.print("Enter next word: ");
            String input = scanner.nextLine().trim().toLowerCase();

            // Attempt move
            if (!model.makeMove(input)) {
                if (model.isShowErrorIfInvalid()) {
                    System.out.println("❌ Invalid move. Must be a valid 4-letter word and differ by one letter.");
                }
                continue;
            }

            // Check win condition
            if (model.isWinningWord(input)) {
                System.out.println("🎉 Congratulations! You've reached the target word: " + model.getTargetWord());
                if (model.isShowPath()) {
                    List<String> path = model.getAttemptedPath();
                    System.out.println("Your path: " + path);
                }
                break;
            }
        }

        scanner.close();
    }
}
