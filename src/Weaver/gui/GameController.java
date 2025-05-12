package Weaver.gui;

import Weaver.model.Model;

import javax.swing.*;
import java.util.List;

public class GameController {
    private final Model model;
    private final GameView view;

    public GameController(Model model, GameView view) {
        this.model = model;
        this.view = view;
        this.view.setController(this); // 连接 Controller 和 View
    }

    public void handleInput(String word) {
        word = word.trim().toLowerCase();
        if (!model.makeMove(word)) {
            if (model.isShowErrorIfInvalid()) {
                view.showErrorDialog("Invalid word. Either not in dictionary or not one letter different.");
            }
            return;
        }
        if (model.isWinningWord(word)) {
            view.showWinDialog();
        }
    }

    public void handleReset() {
        model.resetGame();
    }

    public void handleNewGame() {
        model.setStartAndTarget(model.getStartWord(), model.getTargetWord());
        model.resetGame();
    }

    public void showSolutionPath() {
        List<String> path = model.findSolutionPath();
        if (path == null || path.isEmpty()) {
            view.showErrorDialog("No solution path found.");
        } else {
            StringBuilder sb = new StringBuilder("Solution Path:\n");
            for (String word : path) {
                sb.append(word).append("\n");
            }
            JOptionPane.showMessageDialog(null, sb.toString(), "Solution Path", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
