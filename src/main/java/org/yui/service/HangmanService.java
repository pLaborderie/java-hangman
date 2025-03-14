package org.yui.service;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.yui.util.HangmanASCIIDrawer;
import org.yui.util.WordSelector;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class HangmanService {
    private final int MAX_TRIES = 6;
    private final String LETTER_PLACEHOLDER = "_";
    String word = "";
    List<String> guesses = new ArrayList<>();

    /**
     * Sets a random word for the current game
     */
    void pickWord() {
        word = WordSelector.pickRandomWord();
    }

    String wordGuessProgress() {
        StringBuilder guessProgress = new StringBuilder();
        for (Character c : word.toCharArray()) {
            if (guesses.contains(c.toString())) {
                guessProgress.append(c);
            } else {
                guessProgress.append(LETTER_PLACEHOLDER);
            }
            guessProgress.append(" ");
        }
        return guessProgress.toString();
    }

    List<String> wrongGuesses() {
        return guesses.stream().filter(c -> !word.contains(c)).collect(Collectors.toList());
    }

    int livesLeft() {
        return MAX_TRIES - wrongGuesses().size();
    }

    String hangmanArt() {
        return HangmanASCIIDrawer.drawHangman(livesLeft());
    }

    boolean isGameWon() {
        for (Character c : word.toCharArray()) {
            if (!guesses.contains(c.toString())) {
                return false;
            }
        }
        return true;
    }

    boolean isGameLost() {
        return livesLeft() <= 0;
    }

    public boolean isGameOver() {
        return isGameWon() || isGameLost();
    }

    public boolean isGuessed(String guess) {
        return guesses.contains(guess);
    }

    public void addGuess(String guess) {
        guesses.add(guess);
    }

    public void startGame() {
        Log.info("Starting game...");
        pickWord();
        guesses = new ArrayList<>();
        Log.info("The word is " + word);
    }

    public String printGameState() {
        if (isGameWon()) {
            return "Game won! The word was: " + word;
        }
        if (isGameLost()) {
            return "Game lost! The word was: " + word;
        }

        return hangmanArt() +
                "\n" +
                "You have " + livesLeft() + " lives left." +
                "\n" +
                wordGuessProgress() +
                "\n" +
                wrongGuesses();
    }
}
