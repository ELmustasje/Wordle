package org.example.wordle.model.word;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.example.wordle.model.WordleDictionary;

/**
 * This class describes a structure of two lists for a game of Wordle: The list
 * of words that can be used as guesses and the list of words that can be
 * possible answers.
 */
public class WordleWordList {

	/**
	 * All words in the game. These words can be used as guesses.
	 */
	private WordleDictionary allWords;


	/**
	 * A subset of <code>allWords</code>. <br>
	 * </br>
	 * These words can be the answer to a wordle game.
	 */
	private List<String> possibleAnswers;

	/**
	 * Create a WordleWordList that uses the full words and limited answers of the
	 * GetWords class.
	 */
	public WordleWordList(WordleDictionary wordleDictionary) {
		this.allWords = wordleDictionary;
		this.possibleAnswers = new ArrayList<>(wordleDictionary.getAnswerWordsList());
	}
	public void setPossibleAnswers(List<String> newPossibleAnswers) {
		this.possibleAnswers = newPossibleAnswers;
	}

	/**
	 * Get the list of all guessing words.
	 * 
	 * @return all words
	 */
	public WordleDictionary getAllWords() {
		return allWords;
	}

	/**
	 * Returns the list of possible answers.
	 * 
	 * @return
	 */
	public List<String> possibleAnswers() {
		return Collections.unmodifiableList(possibleAnswers);
	}



	/**
	 * Eliminates words from the possible answers list using the given
	 * <code>feedback</code>
	 * 
	 * @param feedback
	 */
	public void eliminateWords(WordleWord feedback) {
		// TODO: Implement me :)
		for(int i = possibleAnswers.size() - 1; i >= 0; i--) {
			if(!WordleWord.isPossibleWord(possibleAnswers.get(i), feedback)){
				possibleAnswers.remove(i);
			}
		}
	}
	/**
	 * Calculates the frequency of each letter at each position in a list of words.
	 *
	 * @param words List of words with the same length.
	 * @return Array of maps, each holding frequency of letters at a specific position.
	 */
	public Map<Character, Integer>[] calculateLetterFrequencies(List<String> words) {
		int size = words.get(0).length();  // Length of each word
		Map<Character, Integer>[] letterFrequencies = new HashMap[size];  // Array of maps

		// Initialize each map in the array
		for (int i = 0; i < size; i++) {
			letterFrequencies[i] = new HashMap<>();
		}

		// Populate frequencies for each position
		for (String word : words) {
			for (int i = 0; i < word.length(); i++) {
				char c = word.charAt(i);
				letterFrequencies[i].put(c, letterFrequencies[i].getOrDefault(c, 0) + 1);
			}
		}

		// Return the array of frequency maps
		return letterFrequencies;
	}

	/**
	 * Finds the best guess from a list of words based on letter frequency scores.
	 *
	 * @param words List of possible guess words.
	 * @param letterFrequencies Array of maps holding letter frequencies at each position.
	 * @return The word with the highest frequency-based score.
	 */
	public String calculateGuessBasedOnFrequency(List<String> words, Map<Character, Integer>[] letterFrequencies) {
		String bestWord = null;
		double bestScore = -1;

		// Iterate through each word to find the one with the highest score
		for (String word : words) {
			double score = calculateWordScore(word, letterFrequencies);
			if (score > bestScore) {
				bestScore = score;
				bestWord = word;
			}
		}

		// Return the best scoring word
		return bestWord;
	}

	/**
	 * Calculates the score for a given word based on letter frequencies at each position.
	 *
	 * @param word The word to calculate the score for.
	 * @param letterFrequencies Array of maps holding letter frequencies at each position.
	 * @return The total score for the word.
	 */
	public double calculateWordScore(String word, Map<Character, Integer>[] letterFrequencies) {
		double score = 0;

		// Iterate through each character in the word
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			// Add the frequency of the character at the current position to the score
			score += letterFrequencies[i].getOrDefault(c, 0);
		}

		// Return the total score for the word
		return score;
	}

	/**
	 * Calculates the best first guess for a Wordle game based on letter frequencies.
	 * Words without duplicate letters are given higher weight.
	 *
	 * @param words List of possible first guess words.
	 * @param letterFrequencies Array of maps holding letter frequencies at each position.
	 * @return The best first guess word based on frequency scores.
	 */
	public String calculateFirstGuess(List<String> words, Map<Character, Integer>[] letterFrequencies) {
		String bestWord = null;
		double bestScore = -1;

		// Iterate through each word to find the best first guess
		for (String word : words) {
			double score = -1;
			// Check if the word contains duplicate letters
			if (!containsDuplicateLetters(word)) {
				// Assign higher weight to words without duplicate letters
				score = calculateWordScore(word, letterFrequencies) * 10;
			} else {
				// Regular score for words with duplicate letters
				score = calculateWordScore(word, letterFrequencies);
			}

			// Update the bestWord if the current word's score is higher than bestScore
			if (score > bestScore) {
				bestScore = score;
				bestWord = word;
			}
		}

		// Return the best word as the first guess
		return bestWord;
	}



	/**
	 * Calculates the entropy (uncertainty reduction) for a given guess in the context of a Wordle game.
	 *
	 * @param guess The guess word to calculate entropy for.
	 * @return The entropy value for the guess.
	 */
    public double calculateEntropy(String guess) {
		// Map to store feedback counts for each type of feedback
		Map<String, Integer> feedbackCounts = new ConcurrentHashMap<>();

		// Compute feedback for each possible answer in parallel and update counts
		possibleAnswers.parallelStream().forEach(word -> {
			String feedback = WordleAnswer.matchWord(word, guess).getAnswerTypeString();
			feedbackCounts.merge(feedback, 1, Integer::sum);
		});

		double entropy = 0.0;
		int totalWords = possibleAnswers.size();

		// Calculate entropy based on feedback counts
		for (int count : feedbackCounts.values()) {
			double prob = (double) count / totalWords;
			entropy -= prob * Math.log(prob) / Math.log(2);
		}

		// Return the calculated entropy value
		return entropy;
	}

	/**
	 * Finds the best guess word based on maximum entropy, which provides the most information gain.
	 *
	 * @return The word with the highest entropy.
	 */
	public String bestGuessBasedOnEntropy() {
		String bestWord = null;
		double bestEntropy = -1;

		// Iterate through each possible answer to determine the one with the highest entropy
		for (String word : possibleAnswers()) {
			double entropy = calculateEntropy(word);
			if (entropy > bestEntropy) {
				bestEntropy = entropy;
				bestWord = word;
			}
		}

		// Return the word that has the highest entropy
		return bestWord;
	}

	/**
	 * Checks if a word contains duplicate letters.
	 *
	 * @param word The word to check for duplicate letters.
	 * @return True if the word contains duplicate letters, false otherwise.
	 */
    public Boolean containsDuplicateLetters(String word) {
		Set<Character> wordSet = new HashSet<>();

		// Add each character to a set and check the size
		for (Character c : word.toCharArray()) {
			wordSet.add(c);
		}

		// Return true if there are duplicate letters (set size differs from word length)
		return wordSet.size() != word.length();
	}

	/**
	 * Returns the amount of possible answers in this WordleWordList
	 *
	 * @return size of
	 */
	public int size() {
		return possibleAnswers.size();
	}

	/**
	 * Removes the given <code>answer</code> from the list of possible answers.
	 *
	 * @param answer
	 */
	public void remove(String answer) {
		possibleAnswers.remove(answer);
	}

	/**
	 * Returns the word length in the list of valid guesses.
	 *
	 * @return
	 */
	public int wordLength() {
		return allWords.WORD_LENGTH;
	}

}
