package Weaver.gui;

import Weaver.model.Model;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * View component of the Weaver game GUI.
 * Displays the game interface and observes the Model for updates.
 */
public class GameView extends JFrame implements Observer {
    private final Model model;
    private GameController controller;

    private JLabel startLabel;
    private JLabel targetLabel;
    private JTextField inputField;
    private JPanel pathPanel;

    private JButton resetButton;
    private JButton newGameButton;
    private JButton showSolutionButton;

    private JCheckBox errorBox;
    private JCheckBox randomBox;

    public GameView(Model model) {
        this.model = model;
        model.addObserver(this);
        initUI();
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }

    private void initUI() {
        setTitle("Weaver Game GUI");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Panel: Start and Target Words
        JPanel topPanel = new JPanel();
        startLabel = new JLabel("Start: ----");
        targetLabel = new JLabel("Target: ----");
        topPanel.add(startLabel);
        topPanel.add(targetLabel);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel: Tile Path Display
        pathPanel = new JPanel();
        pathPanel.setLayout(new BoxLayout(pathPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(pathPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Right Panel: Virtual Keyboard
        JPanel keyboardPanel = new JPanel(new GridLayout(4, 7));
        String keys = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (char c : keys.toCharArray()) {
            JButton keyButton = new JButton(String.valueOf(c));
            keyButton.addActionListener(e -> inputField.setText(inputField.getText() + c));
            keyboardPanel.add(keyButton);
        }
        JButton backspace = new JButton("←");
        backspace.addActionListener(e -> {
            String text = inputField.getText();
            if (!text.isEmpty()) {
                inputField.setText(text.substring(0, text.length() - 1));
            }
        });
        JButton enter = new JButton("⏎");
        enter.addActionListener(e -> {
            if (controller != null) controller.handleInput(inputField.getText());
        });
        keyboardPanel.add(backspace);
        keyboardPanel.add(enter);
        add(keyboardPanel, BorderLayout.EAST);

        // Bottom Panel: Controls
        JPanel bottomPanel = new JPanel();
        inputField = new JTextField(10);
        inputField.addActionListener(e -> {
            if (controller != null) controller.handleInput(inputField.getText());
        });

        resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> {
            if (controller != null) controller.handleReset();
        });

        newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> {
            if (controller != null) controller.handleNewGame();
        });

        showSolutionButton = new JButton("Show Path");
        showSolutionButton.addActionListener(e -> {
            if (controller != null) controller.showSolutionPath();
        });

        errorBox = new JCheckBox("Show Error");
        errorBox.setSelected(true);
        errorBox.addActionListener(e -> model.setShowErrorIfInvalid(errorBox.isSelected()));

        randomBox = new JCheckBox("Random Start/Target");
        randomBox.addActionListener(e -> model.setRandomStartAndTarget(randomBox.isSelected()));

        bottomPanel.add(inputField);
        bottomPanel.add(resetButton);
        bottomPanel.add(newGameButton);
        bottomPanel.add(showSolutionButton);
        bottomPanel.add(errorBox);
        bottomPanel.add(randomBox);

        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        startLabel.setText("Start: " + model.getStartWord());
        targetLabel.setText("Target: " + model.getTargetWord());

        pathPanel.removeAll();
        List<String> wordsToDisplay = model.getShowSolution() ? model.findSolutionPath() : model.getAttemptedPath();

        for (String word : wordsToDisplay) {
            JPanel row = new JPanel(new GridLayout(1, word.length()));
            for (int i = 0; i < word.length(); i++) {
                JLabel cell = new JLabel(String.valueOf(word.charAt(i)), SwingConstants.CENTER);
                cell.setPreferredSize(new Dimension(40, 40));
                cell.setOpaque(true);
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cell.setFont(new Font("Arial", Font.BOLD, 18));
                if (model.getTargetWord().length() == word.length()
                        && word.charAt(i) == model.getTargetWord().charAt(i)) {
                    cell.setBackground(Color.GREEN);
                } else {
                    cell.setBackground(Color.WHITE);
                }
                row.add(cell);
            }
            pathPanel.add(row);
        }
        pathPanel.revalidate();
        pathPanel.repaint();
        inputField.setText("");
    }

    public void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Invalid Move", JOptionPane.ERROR_MESSAGE);
    }

    public void showWinDialog() {
        JOptionPane.showMessageDialog(this, "🎉 You reached the target word!", "Victory", JOptionPane.INFORMATION_MESSAGE);
    }

    public String getInputText() {
        return inputField.getText();
    }
}
