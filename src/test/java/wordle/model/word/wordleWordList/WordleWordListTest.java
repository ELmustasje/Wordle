package wordle.model.word.wordleWordList;

import no.uib.inf102.util.ReadFile;
import no.uib.inf102.wordle.model.word.WordleAnswer;
import no.uib.inf102.wordle.model.word.WordleWord;
import no.uib.inf102.wordle.model.word.WordleWordList;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

public class WordleWordListTest {

    private static final String FOLDER_PATH = "src/test/java/no/uib/inf102/wordle/model/word/wordleWordList/";

    private Dictionary dictionary = new Dictionary();

    //Note that these tests may fail if there are any changes to the dictionary
    @Test
    public void eliminateWords1() {
        WordleAnswer answer = new WordleAnswer("rocks", dictionary);
        WordleWordList list = new WordleWordList(dictionary);
        WordleWord feedback = answer.makeGuess("carry");

        List<String> actualList = getRemainingWords(list, feedback);
        List<String> expectedList = ReadFile.readLines(FOLDER_PATH + "eliminateWords1.txt");

        compareLists(actualList, expectedList);
    }

	protected void compareLists(List<String> actualList, List<String> expectedList) {
        assertEquals(expectedList.size(), actualList.size(), "The number of words eliminated was not correct.");

        Collections.sort(actualList);
        Collections.sort(expectedList);

        for (int i = 0; i < actualList.size(); i++) {
            String actual = actualList.get(i);
            String expected = expectedList.get(i);
            assertEquals(expected, actual, "The remaining words after elimination were not the same.");
        }
	}

	protected List<String> getRemainingWords(WordleWordList list, WordleWord feedback) {
		list.eliminateWords(feedback);
        List<String> actualList = new ArrayList<>(list.possibleAnswers());
		return actualList;
	}

    @Test
    public void eliminateWords2() {
        WordleAnswer answer = new WordleAnswer("rocks", dictionary);
        WordleWordList list = new WordleWordList(dictionary);
        WordleWord feedback = answer.makeGuess("apoop");

        List<String> actualList = getRemainingWords(list, feedback);
        List<String> expectedList = ReadFile.readLines(FOLDER_PATH + "eliminateWords2.txt");

        compareLists(actualList, expectedList);
    }

    @Test
    public void eliminateWords3() {
        WordleAnswer answer = new WordleAnswer("rocks", dictionary);
        WordleWordList list = new WordleWordList(dictionary);
        WordleWord feedback = answer.makeGuess("arise");

        List<String> actualList = getRemainingWords(list, feedback);
        List<String> expectedList = ReadFile.readLines(FOLDER_PATH + "eliminateWords3.txt");

        compareLists(actualList, expectedList);
    }

    @Test
    public void guessCorrectWordEliminateEmptyList() {
        WordleAnswer answer = new WordleAnswer("rocks", dictionary);
        WordleWordList list = new WordleWordList(dictionary);
        WordleWord feedback = answer.makeGuess("rocks");

        list.eliminateWords(feedback);
        List<String> possibleAnswers = list.possibleAnswers();
        assertTrue(possibleAnswers.isEmpty(), "Guessing the correct word didn't eliminate all answers.");
    }

    @Test
    public void guessCloseWordEliminateEmptyList() {
        WordleAnswer answer = new WordleAnswer("rocks", dictionary);
        WordleWordList list = new WordleWordList(dictionary);
        WordleWord feedback = answer.makeGuess("docks");

        list.eliminateWords(feedback);
        List<String> possibleAnswers = list.possibleAnswers();
        assertTrue(possibleAnswers.isEmpty());
    }

    @Test
    public void testCalculateLetterFrequencies() {
        // Retrieve possible answers from the word list
        List<String> words = new ArrayList<>();
        words.add("apple");
        words.add("angle");
        words.add("ample");
        words.add("apply");

        // Calculate letter frequencies
        WordleWordList wordList = new WordleWordList(dictionary);
        Map<Character, Integer>[] letterFrequencies = wordList.calculateLetterFrequencies(
                words
        );

        // Validate the size of the resulting array of maps
        int wordLength = words.get(0).length();
        assertEquals(wordLength, letterFrequencies.length);
        // Check each position's frequency map
        assertEquals(4, (int) letterFrequencies[0].get('a'));  // 'a' appears 4 times at index 0
        assertEquals(1, (int) letterFrequencies[1].get('m'));  // 'm' appears 1 time at index 1
        assertEquals(4, (int) letterFrequencies[3].get('l'));  // 'l' appears 4 times at index 3
        // Check a position with no expected occurrences
        assertNull(letterFrequencies[0].get('p'));  // 'p' is not at index 0
    }
    @Test
    public void testCalculateGuessBasedOnFrequency() {
        // Retrieve possible answers from the word list
        List<String> words = new ArrayList<>();
        words.add("apple");
        words.add("angle");
        words.add("ample");
        words.add("apply");
        // Prepare the list of possible answers

        // Calculate letter frequencies for the given possible answers
        WordleWordList wordList = new WordleWordList(dictionary);
        Map<Character, Integer>[] letterFrequencies = wordList.calculateLetterFrequencies(words);

        // Conduct the test for calculating the best guess based on frequency
        String bestGuess = wordList.calculateGuessBasedOnFrequency(words, letterFrequencies);
        assertEquals("apple", bestGuess);
    }

    @Test
    public void testCalculateWordScore() {
        // Prepare the list of possible answers
        List<String> words = new ArrayList<>();
        words.add("apple");
        words.add("angle");
        words.add("ample");
        words.add("apply");
        // Prepare the list of possible answers

        // Calculate letter frequencies for the given possible answers
        WordleWordList wordList = new WordleWordList(dictionary);
        Map<Character, Integer>[] letterFrequencies = wordList.calculateLetterFrequencies(words);

        // Test the word "apple"
        double scoreApple = wordList.calculateWordScore("apple", letterFrequencies);
        // Verify the score based on expected frequencies
        double expectedScoreApple =
                letterFrequencies[0].get('a') +
                        letterFrequencies[1].get('p') +
                        letterFrequencies[2].get('p') +
                        letterFrequencies[3].get('l') +
                        letterFrequencies[4].get('e');

        assertEquals(expectedScoreApple, scoreApple, 0.001);

        // Test another word "angle"
        double scoreAngle = wordList.calculateWordScore("angle", letterFrequencies);
        // Verify the score based on expected frequencies
        double expectedScoreAngle =
                letterFrequencies[0].get('a') +
                        letterFrequencies[1].get('n') +
                        letterFrequencies[2].get('g') +
                        letterFrequencies[3].get('l') +
                        letterFrequencies[4].get('e');

        assertEquals(expectedScoreAngle, scoreAngle, 0.001);
    }
    @Test
    public void testCalculateFirstGuess() {
        List<String> words = new ArrayList<>();
        words.add("apple");
        words.add("angle");
        words.add("ample");
        words.add("apply");
        // Prepare the list of possible answers

        // Calculate letter frequencies for the given possible answers
        WordleWordList wordList = new WordleWordList(dictionary);
        Map<Character, Integer>[] letterFrequencies = wordList.calculateLetterFrequencies(words);
        // Prepare the list of possible answers

        // Calculate letter frequencies for the given possible answers

        // Conduct the test for calculating the best first guess based on frequency and duplicate letters
        String firstGuess = wordList.calculateFirstGuess(words, letterFrequencies);

        // Expected best first guess is "angle" or "ample" without duplicate letters and high scores
        assertEquals("ample", firstGuess);
    }
    @Test
    public void testCalculateEntropy() {
        List<String> words = new ArrayList<>();
        words.add("apple");
        words.add("angle");
        words.add("ample");
        words.add("apply");
        // Prepare the list of possible answers

        // Calculate letter frequencies for the given possible answers
        WordleWordList wordList = new WordleWordList(dictionary);
        // Mock population of possible answers (simplifying the setup)
        wordList.setPossibleAnswers(words);

        // Test a specific guess. For simplicity, let's assume the guess is "apple"
        String guess = "apple";

        // Expected entropy calculation
        double actualEntropy = wordList.calculateEntropy(guess);

        // Here, we approximate the expected entropy manually for the test case.
        // Say we have certain known feedbacks and counts:
        Map<String, Integer> feedbackCounts = new ConcurrentHashMap<>();
        feedbackCounts.put("22222", 1); // "apple" matches exactly one word
        feedbackCounts.put("21111", 1); // e.g., "ample" -> "2" for 'a', "1" for 'm', 'p', 'l', "1" for 'e'
        feedbackCounts.put("21011", 1); // e.g., "angle" -> "2" for 'a', "1" for 'n', "1" for 'g', "0" for second 'p', "1" for 'l'
        feedbackCounts.put("22000", 1); // e.g., "apply" -> "2" for 'a', "2" for 'p', "0" for 'l' and "0" for 'y'

        double expectedEntropy = 0.0;
        int totalWords = 4; // number of words in the possible answers list
        for (int count : feedbackCounts.values()) {
            double prob = (double) count / totalWords;
            expectedEntropy -= prob * Math.log(prob) / Math.log(2);
        }

        // Asserting that the computed entropy matches the expected value
        assertEquals(expectedEntropy, actualEntropy, 0.001);
    }

    @Test
    public void testBestGuessBasedOnEntropy() {
        List<String> words = new ArrayList<>();
        words.add("apple");
        words.add("angle");
        words.add("ample");
        words.add("apply");
        // Prepare the list of possible answers

        // Calculate letter frequencies for the given possible answers
        WordleWordList wordList = new WordleWordList(dictionary);
        // Mock population of possible answers (simplifying the setup)
        wordList.setPossibleAnswers(words);

        // Conduct the test for the best guess based on entropy
        String bestGuess = wordList.bestGuessBasedOnEntropy();

        // For this test, we assume the correct implementation of calculateEntropy
        // The word with the highest entropy will be selected. Let's simulate the calculateEntropy values:
        Map<String, Double> entropyMap = new HashMap<>();
        entropyMap.put("apple", 1.6);
        entropyMap.put("angle", 1.5);
        entropyMap.put("ample", 1.4);
        entropyMap.put("apply", 1.2);

        // Mocking the calculateEntropy method to use predefined entropy values for testing
        double highestEntropy = -1;
        String expectedBestGuess = null;
        for (String word : wordList.possibleAnswers()) {
            double entropy = entropyMap.get(word);
            if (entropy > highestEntropy) {
                highestEntropy = entropy;
                expectedBestGuess = word;
            }
        }

        // Asserting that the best guess based on entropy matches the expected value
        assertEquals(expectedBestGuess, bestGuess);
    }
    @Test
    public void testContainsDuplicateLetters() {
        WordleWordList wordList = new WordleWordList(dictionary);
        // Test cases with duplicate letters
        assertTrue(wordList.containsDuplicateLetters("apple"));
        assertTrue(wordList.containsDuplicateLetters("book"));
        assertTrue(wordList.containsDuplicateLetters("banana"));

        // Test cases without duplicate letters
        assertFalse(wordList.containsDuplicateLetters("angle"));
        assertFalse(wordList.containsDuplicateLetters("study"));
        assertFalse(wordList.containsDuplicateLetters("brush"));

        // Edge cases
        assertFalse(wordList.containsDuplicateLetters("")); // Empty string
        assertFalse(wordList.containsDuplicateLetters("a")); // Single character
    }
}
