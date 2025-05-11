package Weaver.gui;

import Weaver.model.Model;

import javax.swing.*;


import Weaver.model.Model;

import javax.swing.*;

/**
 * Main entry`` point for launching the GUI version of the Weaver game.
 * Connects Model, View, and Controller components.
 */
public class MainGUI {
    public static void main(String[] args) {
        // Run GUI code on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Step 1: Create the Model
            Model model = new Model();

            // Step 2: Create the View and pass the Model into it
            GameView view = new GameView(model); // View registers itself as an observer

            // Step 3: Create the Controller and pass both Model and View into it
            GameController controller = new GameController(model, view);

            // Step 4: Let the View know which controller it is working with
            view.setController(controller);
        });
    }
}
