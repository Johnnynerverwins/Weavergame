package Weaver.gui;

import Weaver.model.Model;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Set<String> dictionary = loadDictionary("dictionary.txt");
            if (dictionary.isEmpty()) {
                JOptionPane.showMessageDialog(null, "⚠️ Could not load dictionary. Using fallback.", "Warning", JOptionPane.WARNING_MESSAGE);
                dictionary.add("cold");
                dictionary.add("cord");
                dictionary.add("card");
                dictionary.add("ward");
                dictionary.add("warm");
            }

            Model model = new Model(dictionary);
            GameView view = new GameView(model);
            GameController controller = new GameController(model, view);
        });
    }

    private static Set<String> loadDictionary(String filename) {
        Set<String> words = new HashSet<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename));
            for (String line : lines) {
                String cleaned = line.trim().toLowerCase();
                if (cleaned.matches("[a-zA-Z]{4}")) {
                    words.add(cleaned);
                }
            }
        } catch (IOException e) {
            System.err.println("Could not read dictionary file: " + e.getMessage());
        }
        return words;
    }
}
