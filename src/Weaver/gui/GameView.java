package Weaver.gui;

import Weaver.model.Model;

import javax.swing.*;
import java.awt.*;
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
    private JTextArea pathArea;

    private JButton resetButton;
    private JButton newGameButton;

    private JCheckBox errorBox;
    private JCheckBox pathBox;
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
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Panel: Start and Target Words
        JPanel topPanel = new JPanel();
        startLabel = new JLabel("Start: ----");
        targetLabel = new JLabel("Target: ----");
        topPanel.add(startLabel);
        topPanel.add(targetLabel);
        add(topPanel, BorderLayout.NORTH);

        // Center: Input + Path Display
        JPanel centerPanel = new JPanel(new BorderLayout());

        inputField = new JTextField();
        inputField.addActionListener(e -> {
            if (controller != null) controller.handleInput(inputField.getText());
        });
        centerPanel.add(inputField, BorderLayout.NORTH);

        pathArea = new JTextArea();
        pathArea.setEditable(false);
        centerPanel.add(new JScrollPane(pathArea), BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom: Buttons and Checkboxes
        JPanel bottomPanel = new JPanel();

        resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> {
            if (controller != null) controller.handleReset();
        });

        newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> {
            if (controller != null) controller.handleNewGame();
        });

        errorBox = new JCheckBox("Show Error");
        errorBox.setSelected(true);
        errorBox.addActionListener(e -> model.setShowErrorIfInvalid(errorBox.isSelected()));

        pathBox = new JCheckBox("Show Path");
        pathBox.addActionListener(e -> model.setShowPath(pathBox.isSelected()));

        randomBox = new JCheckBox("Random Start/Target");
        randomBox.addActionListener(e -> model.setRandomStartAndTarget(randomBox.isSelected()));

        bottomPanel.add(resetButton);
        bottomPanel.add(newGameButton);
        bottomPanel.add(errorBox);
        bottomPanel.add(pathBox);
        bottomPanel.add(randomBox);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        startLabel.setText("Start: " + model.getStartWord());
        targetLabel.setText("Target: " + model.getTargetWord());

        if (model.isShowPath()) {
            StringBuilder sb = new StringBuilder();
            for (String word : model.getAttemptedPath()) {
                sb.append(word).append("\n");
            }
            pathArea.setText(sb.toString());
        } else {
            pathArea.setText("");
        }

        inputField.setText("");
    }

    public void showWinDialog() {
        JOptionPane.showMessageDialog(this, "🎉 You reached the target word!", "Victory", JOptionPane.INFORMATION_MESSAGE);
    }

    public String getInputText() {
        return inputField.getText();
    }
}