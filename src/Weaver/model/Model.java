package Weaver.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * The Model class implements the core logic for the Weaver word game.
 * It is independent of any user interface and follows the Observer pattern.
 *
 * @invariant All words (start, target, and intermediate) must be 4-letter words.
 */
public class Model extends Observable {
    private Set<String> dictionary;
    private String startWord;
    private String targetWord;
    private List<String> attempts;

    // Flags (settable via GUI or code)
    private boolean showErrorIfInvalid = true;
    private boolean showPath = false;
    private boolean randomStartAndTarget = false;

    public Model() {
        dictionary = new HashSet<>();
        attempts = new ArrayList<>();
        loadDictionary("dictionary.txt");
    }

    /**
     * Loads the list of valid 4-letter words from a file into the dictionary set.
     *
     * @requires filePath != null
     * @ensures All valid 4-letter words are loaded into the dictionary
     */
    public void loadDictionary(String filePath) {
        assert filePath != null : "File path must not be null";
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String word : lines) {
                word = word.trim().toLowerCase();
                if (word.length() == 4) {
                    dictionary.add(word);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load dictionary: " + e.getMessage());
        }
    }

    /**
     * Sets the starting and target words for the game. Can be random if the flag is true.
     *
     * @requires start != null && target != null && start.length() == 4 && target.length() == 4
     * @ensures Start and target words are set and previous attempts are cleared
     */
    public void setStartAndTarget(String start, String target) {
        assert start != null && target != null : "Start and target words must not be null";
        assert start.length() == 4 && target.length() == 4 : "Words must be 4 letters long";

        if (randomStartAndTarget) {
            List<String> words = new ArrayList<>(dictionary);
            Random rand = new Random();
            this.startWord = words.get(rand.nextInt(words.size()));
            this.targetWord = words.get(rand.nextInt(words.size()));
        } else {
            this.startWord = start.toLowerCase();
            this.targetWord = target.toLowerCase();
        }

        attempts.clear();
        setChanged();
        notifyObservers();
    }

    /**
     * Checks whether the given word is a valid dictionary word.
     *
     * @requires word != null
     * @ensures Returns true if the word is in the dictionary
     */
    public boolean isValidWord(String word) {
        assert word != null : "Word must not be null";
        return dictionary.contains(word.toLowerCase());
    }

    /**
     * Checks whether the given word matches the target word (case-insensitive).
     *
     * @requires word != null
     * @ensures Returns true if the word equals the target word
     */
    public boolean isWinningWord(String word) {
        assert word != null : "Word must not be null";
        return word.equalsIgnoreCase(targetWord);
    }

    /**
     * Checks whether two words differ by exactly one letter.
     *
     * @requires from != null && to != null && from.length() == to.length()
     * @ensures Returns true if words differ by exactly one letter
     */
    public boolean isOneLetterDiff(String from, String to) {
        assert from != null && to != null : "Words must not be null";
        assert from.length() == to.length() : "Words must be the same length";

        int diff = 0;
        for (int i = 0; i < from.length(); i++) {
            if (from.charAt(i) != to.charAt(i)) {
                diff++;
            }
        }
        return diff == 1;
    }

    /**
     * Records the move if the word is valid and differs from the previous word by one letter.
     *
     * @requires word != null && word.length() == 4
     * @ensures If the move is valid, it is added to the attempt path and observers are notified
     */
    public boolean makeMove(String word) {
        assert word != null && word.length() == 4 : "Word must be 4 letters long";

        String current = attempts.isEmpty() ? startWord : attempts.get(attempts.size() - 1);

        if (!isValidWord(word) || !isOneLetterDiff(current, word)) {
            return false;
        }

        attempts.add(word.toLowerCase());
        setChanged();
        notifyObservers();
        return true;
    }

    /**
     * Returns a copy of the list of attempted intermediate words.
     *
     * @ensures Returns the current path from start to last valid word
     */
    public List<String> getAttemptedPath() {
        return new ArrayList<>(attempts);
    }

    /**
     * Clears all previous attempts and resets the game state.
     *
     * @ensures The game is reset and observers are notified
     */
    public void resetGame() {
        attempts.clear();
        setChanged();
        notifyObservers();
    }

    // === Flag setters/getters (used in GUI, optional in CLI) ===

    public void setShowErrorIfInvalid(boolean value) { this.showErrorIfInvalid = value; }
    public boolean isShowErrorIfInvalid() { return showErrorIfInvalid; }

    public void setShowPath(boolean value) { this.showPath = value; }
    public boolean isShowPath() { return showPath; }

    public void setRandomStartAndTarget(boolean value) { this.randomStartAndTarget = value; }
    public boolean isRandomStartAndTarget() { return randomStartAndTarget; }

    public String getStartWord() { return startWord; }

    public String getTargetWord() { return targetWord; }

    public Set<String> getDictionary() {
        return dictionary;
    }

}
