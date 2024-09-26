package wordle.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DictionaryTest {
    
    private static Dictionary dictionary;

    @BeforeAll
    public static void setup() {
        dictionary = new Dictionary();
    }

    @Test
    public void guessWordLengthTest() {
        int wordLength = dictionary.WORD_LENGTH;
        for (String word : dictionary.getGuessWordsList()) {
            if (word.length() != wordLength)
                assertEquals(wordLength, word.length());
        }
    }

    @Test
    public void answerWordLengthTest() {
        int wordLength = dictionary.WORD_LENGTH;
        for (String word : dictionary.getAnswerWordsList()) {
            if (word.length() != wordLength)
                assertEquals(word.length(), wordLength);
        }
    }

    @Test
    public void dictionaryIsSafeFromDeletions() {
    	List<String> answers = dictionary.getAnswerWordsList();
    	assertThrows(UnsupportedOperationException.class, () -> answers.remove(answers.get(0)),"Should not be possible to remove from Dictionary object");
    	List<String> guesses = dictionary.getGuessWordsList();
    	assertThrows(UnsupportedOperationException.class, () -> guesses.remove(guesses.get(0)),"Should not be possible to remove from Dictionary object");
    }
    
    @Test
    public void canFindAllWords() {
    	for(String word : dictionary.getGuessWordsList()) {
    		assertTrue(dictionary.isLegalGuess(word),"The word "+word+" should be a leagal guess");
    	}
    	for(String word : dictionary.getAnswerWordsList()) {
    		assertTrue(dictionary.isLegalGuess(word),"The word "+word+" should be a leagal guess");
    		assertTrue(dictionary.isLegalAnswer(word),"The word "+word+" should be a leagal answer");
    	}
    }
    
    @Test
    public void canNotFindGibberishWords() {
    	List<String> gibberish = new ArrayList<>();
    	gibberish.add("hfepy");
    	gibberish.add("glegg");
    	gibberish.add("blkni");
    	gibberish.add("leoem");
    	for(String word : gibberish) {
    		assertFalse(dictionary.isLegalGuess(word),"The word "+word+" should not be a leagal guess");
    	}
    }   

}
