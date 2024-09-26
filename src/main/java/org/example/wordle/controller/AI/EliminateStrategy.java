package org.example.wordle.controller.AI;

import org.example.wordle.model.WordleDictionary;
import org.example.wordle.model.word.WordleWord;
import org.example.wordle.model.word.WordleWordList;

import java.util.Random;

/**
 * This strategy eliminates guesses that are impossible with the feedback given
 * throughout the game.
 * <br>
 * </br>
 * For example:
 * If the answer is "break" and you answer "chest", you will get
 * feedback showing that the middle "e" is in the right position. Therefore you
 * eliminate all words that do not have an e in the middle position.
 */
public class EliminateStrategy implements IStrategy {

    private WordleDictionary wordleDictionary;
    private WordleWordList guesses;
    private Random random = new Random();

    /**
     * Constructs an EliminateStrategy with the given dictionary.
     *
     * @param wordleDictionary The dictionary to use for word guesses.
     */
    public EliminateStrategy(WordleDictionary wordleDictionary) {
        this.wordleDictionary = wordleDictionary;
        reset();
    }

    /**
     * Makes a guess by eliminating impossible words based on feedback, then choosing
     * a random word from the remaining possible guesses.
     *
     * @param feedback The feedback from the previous guess.
     * @return The next guess word.
     */
    @Override
    public String makeGuess(WordleWord feedback) {
        // Eliminate words based on feedback if available
        if (feedback != null) {
            guesses.eliminateWords(feedback);
        }

        // Choose a random word from the remaining possible guesses
        int randIndex = random.nextInt(guesses.size());
        return guesses.possibleAnswers().get(randIndex);
    }

    /**
     * Resets the strategy, reinitializing the guesses from the dictionary.
     */
    @Override
    public void reset() {
        guesses = new WordleWordList(wordleDictionary);
    }
}