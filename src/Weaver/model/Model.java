package Weaver.model;

import java.util.*;

public class Model extends Observable {
    private String startWord;
    private String targetWord;
    private List<String> attemptedPath;
    private Set<String> dictionary;
    private boolean showErrorIfInvalid;
    private boolean showSolution;
    private boolean useRandomStartAndTarget = false;

    public Model(Set<String> dictionary) {
        this.dictionary = dictionary;
        this.attemptedPath = new ArrayList<>();
        this.showErrorIfInvalid = true;
        this.showSolution = false;
        this.startWord = "cold";
        this.targetWord = "warm";
        resetGame();
    }

    public Model() {
        this(new HashSet<>(List.of("cold", "cord", "card", "ward", "warm")));
    }

    public void resetGame() {
        if (useRandomStartAndTarget) {
            List<String> words = new ArrayList<>(dictionary);
            Random rand = new Random();
            do {
                startWord = words.get(rand.nextInt(words.size()));
                targetWord = words.get(rand.nextInt(words.size()));
            } while (startWord.equals(targetWord));
        }
        attemptedPath.clear();
        attemptedPath.add(startWord);
        showSolution = false;
        setChanged();
        notifyObservers();
    }

    public boolean makeMove(String word) {
        if (!dictionary.contains(word)) return false;
        String lastWord = attemptedPath.get(attemptedPath.size() - 1);
        if (!isOneLetterDiff(lastWord, word)) return false;
        attemptedPath.add(word);
        setChanged();
        notifyObservers();
        return true;
    }

    public boolean isValidWord(String word) {
        return dictionary.contains(word);
    }

    public boolean isWinningWord(String word) {
        return targetWord.equals(word);
    }

    public boolean isOneLetterDiff(String a, String b) {
        if (a.length() != b.length()) return false;
        int diff = 0;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) diff++;
            if (diff > 1) return false;
        }
        return diff == 1;
    }

    public void setStartAndTarget(String start, String target) {
        this.startWord = start;
        this.targetWord = target;
    }

    public String getStartWord() {
        return startWord;
    }

    public String getTargetWord() {
        return targetWord;
    }

    public List<String> getAttemptedPath() {
        return attemptedPath;
    }

    public boolean isShowErrorIfInvalid() {
        return showErrorIfInvalid;
    }

    public void setShowErrorIfInvalid(boolean value) {
        this.showErrorIfInvalid = value;
    }

    public void setShowPath(boolean value) {
        this.showSolution = value;
        setChanged();
        notifyObservers();
    }

    public boolean isShowPath() {
        return showSolution;
    }

    public boolean getShowSolution() {
        return showSolution;
    }

    public Set<String> getDictionary() {
        return dictionary;
    }

    public void setDictionary(Set<String> dictionary) {
        this.dictionary = dictionary;
    }

    public void setRandomStartAndTarget(boolean enabled) {
        this.useRandomStartAndTarget = enabled;
    }

    public boolean isRandomStartAndTarget() {
        return useRandomStartAndTarget;
    }

    public List<String> findSolutionPath() {
        String start = getStartWord();
        String target = getTargetWord();
        Set<String> dict = getDictionary();

        if (!dict.contains(start) || !dict.contains(target)) return null;

        Queue<List<String>> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.add(List.of(start));
        visited.add(start);

        while (!queue.isEmpty()) {
            List<String> path = queue.poll();
            String last = path.get(path.size() - 1);
            if (last.equals(target)) return path;

            for (String word : dict) {
                if (!visited.contains(word) && isOneLetterDiff(last, word)) {
                    visited.add(word);
                    List<String> newPath = new ArrayList<>(path);
                    newPath.add(word);
                    queue.add(newPath);
                }
            }
        }
        return null;
    }
}