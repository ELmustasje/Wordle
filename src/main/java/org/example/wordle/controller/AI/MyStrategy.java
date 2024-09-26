package org.example.wordle.controller.AI;

import org.example.wordle.model.WordleDictionary;
import org.example.wordle.model.word.WordleWord;
import org.example.wordle.model.word.WordleWordList;

import java.util.ArrayList;
import java.util.Map;

public class MyStrategy implements IStrategy {


    private WordleDictionary wordleDictionary;
    private WordleWordList guesses;
    private int guessCount = 0;
    private ArrayList<WordleWord> feedbacks = new ArrayList<>();
    //avg: 3.455, Seed:14212l, N_games: 200
    public MyStrategy(WordleDictionary wordleDictionary) {
        this.wordleDictionary = wordleDictionary;
        reset();
    }
    @Override
    public String makeGuess(WordleWord feedback) {
        if(feedback != null) {
            guesses.eliminateWords(feedback);
            feedbacks.add(feedback);
        }
        if(guessCount < 1){
            guessCount++;
            Map<Character,Integer>[] letterFrequencies = guesses.calculateLetterFrequencies(guesses.possibleAnswers());
            return guesses.calculateFirstGuess(guesses.possibleAnswers(),letterFrequencies);
        }

        return guesses.bestGuessBasedOnEntropy();
    }

    @Override
    public void reset() {
        // TODO: Implement me :)
        feedbacks.clear();
        guessCount = 0;
        guesses = new WordleWordList(wordleDictionary);
    }
    
}
