package org.example.wordle.model.word;

import org.example.wordle.model.WordleDictionary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * This class represents an answer to a Wordle puzzle.
 * 
 * The answer must be one of the words in the LEGAL_WORDLE_LIST.
 */
public class WordleAnswer {

    private final String WORD;

    private WordleDictionary wordleDictionary;

    private static Random random = new Random();

    /**
     * Creates a WordleAnswer object with a random word from the answer word list
     */
    public WordleAnswer(WordleDictionary wordleDictionary) {
        this(random, wordleDictionary);
    }

    /**
     * Creates a WordleAnswer object with a random word from the answer word list
     * using a specified random object.
     * This gives us the opportunity to set a seed so that tests are repeatable.
     */
    public WordleAnswer(Random random, WordleDictionary wordleDictionary) {
        this(getRandomWordleAnswer(random, wordleDictionary), wordleDictionary);
    }

    /**
     * Creates a WordleAnswer object with a given word.
     * 
     * @param answer
     */
    public WordleAnswer(String answer, WordleDictionary wordleDictionary) {
        this.WORD = answer.toLowerCase();
        this.wordleDictionary = wordleDictionary;
    }

    /**
     * Gets a random wordle answer
     * 
     * @param random
     * @return
     */
    private static String getRandomWordleAnswer(Random random, WordleDictionary wordleDictionary) {
        List<String> possibleAnswerWords = wordleDictionary.getAnswerWordsList();
        int randomIndex = random.nextInt(possibleAnswerWords.size());
        String newWord = possibleAnswerWords.get(randomIndex);
        return newWord;
    }

    /**
     * Guess the Wordle answer. Checks each character of the word guess and gives
     * feedback on which that is in correct position, wrong position and which is
     * not in the answer word.
     * This is done by updating the AnswerType of each WordleCharacter of the
     * WordleWord.
     * 
     * @param wordGuess
     * @return wordleWord with updated answertype for each character.
     */
    public WordleWord makeGuess(String wordGuess) {
        if (!wordleDictionary.isLegalGuess(wordGuess))
            throw new IllegalArgumentException("The word '" + wordGuess + "' is not a legal guess");

        WordleWord guessFeedback = matchWord(wordGuess, WORD);
        return guessFeedback;
    }

    /**
     * Generates a WordleWord showing the match between <code>guess</code> and
     * <code>answer</code>
     * 
     * @param guess
     * @param answer
     * @return
     */
    public static WordleWord matchWord(String guess, String answer) {
        int wordLength = answer.length();
        if (guess.length() != wordLength) {
            throw new IllegalArgumentException("Guess and answer must have same number of letters but guess = "
                    + guess + " and answer = " + answer);
        }

        AnswerType[] feedback = new AnswerType[wordLength];
        Map<Character, Integer> map = new HashMap<>();

        // First pass: Identify correct (green) letters and build the frequency map
        for (int i = 0; i < wordLength; i++) {
            char guessChar = guess.charAt(i);
            char answerChar = answer.charAt(i);
            map.put(answerChar, map.getOrDefault(answerChar, 0) + 1);
            if (guessChar == answerChar) {
                feedback[i] = AnswerType.CORRECT;
                map.put(guessChar, map.get(guessChar) - 1);
            }
        }

        // Second pass: Identify misplaced (yellow) and wrong (grey) letters
        for (int i = 0; i < wordLength; i++) {
            char guessChar = guess.charAt(i);
            if (feedback[i] == AnswerType.CORRECT) {
                continue;
            }
            if (map.getOrDefault(guessChar, 0) > 0) {
                feedback[i] = AnswerType.MISPLACED;
                map.put(guessChar, map.get(guessChar) - 1);
            } else {
                feedback[i] = AnswerType.WRONG;
            }
        }

        // Construct and return the WordleWord object with the guess and its feedback
        return new WordleWord(guess, feedback);
    }
}
