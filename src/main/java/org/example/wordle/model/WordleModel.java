package org.example.wordle.model;

import org.example.grid.CellPosition;
import org.example.grid.GridCell;
import org.example.grid.GridDimension;
import org.example.wordle.controller.ControllableWordleModel;
import org.example.wordle.model.word.AnswerType;
import org.example.wordle.model.word.WordleAnswer;
import org.example.wordle.model.word.WordleCharacter;
import org.example.wordle.model.word.WordleWord;
import org.example.wordle.view.gameView.ViewableWordleModel;

import java.util.ArrayList;
import java.util.List;

public class WordleModel implements ViewableWordleModel, ControllableWordleModel {

    /**
     * Wordle board. Whenever a guess is locked in the board is given the acompanying letters and answer types (CORRECT, MISPLACED, WRONG)
     */
    private WordleBoard board;
    /**
     * All legal guess and answer words
     */
    private WordleDictionary wordleDictionary;
    /**
     * The word that the user needs to guess.
     */
    private WordleAnswer answer;
    /**
     * Current  Wordle guess. 
     */
    private String currentGuess;

    private GameState gameState;

    public WordleModel(WordleBoard board) {
        this(board, new WordleDictionary());
    }
    
    public WordleModel(WordleBoard board, WordleDictionary wordleDictionary) {
        this.board = board;
        this.wordleDictionary = wordleDictionary;
        this.answer = new WordleAnswer(wordleDictionary);
        this.currentGuess = "";

        this.gameState = GameState.ACTIVE_GAME;
    }

    @Override
    public boolean removeCharacter() {
    	if(currentGuess.isEmpty())
    		return false;

    	currentGuess = currentGuess.substring(0, currentGuess.length()-1);
        return true;
    }

    @Override
    public boolean addCharacter(char c) {
    	if(currentGuess.length()>= wordleDictionary.WORD_LENGTH)
    		return false;
    	currentGuess = currentGuess+c;
        return true;
    }

    @Override
    public WordleWord makeGuess() throws IllegalArgumentException {
        if (!wordleDictionary.isLegalGuess(currentGuess))
            throw new IllegalArgumentException("Word is not legal");
        // Check what letters were CORRECT/WRONG POSITION/WRONG
        WordleWord guessFeedback = answer.makeGuess(currentGuess);
        board.setRow(guessFeedback);
        if (guessFeedback.allMatch())
            gameState = GameState.VICTORY;
        else if (board.getCurrentRow()+1 > board.rows())
            gameState = GameState.GAME_OVER;
        currentGuess = "";
        return guessFeedback;
    }

    @Override
    public GridDimension getDimension() {
        return board;
    }

    @Override
    public Iterable<GridCell<WordleCharacter>> getTilesOnBoard() {
        return board;
    }

    @Override
    public Iterable<GridCell<WordleCharacter>> getCurrentGuess() {
        List<GridCell<WordleCharacter>> cellList = new ArrayList<>();
        int col = 0;
        for (Character c : currentGuess.toCharArray()) {
        	WordleCharacter cg = new WordleCharacter(c, AnswerType.BLANK);
            CellPosition pos = new CellPosition(board.getCurrentRow(), col++);
            cellList.add(new GridCell<WordleCharacter>(pos, cg));
        }
        return cellList;
    }

    @Override
    public WordleDictionary getDictionary() {
        return wordleDictionary;
    }

    @Override
    public GameState getGameState() {
        return gameState;
    }

    @Override
    public int getTimerDelay() {
        return 1000;
    }

    @Override
    public void clockTick() {
        if (gameState == GameState.GAME_OVER)
            return;
    }

    @Override
    public void reset() {
        this.answer = new WordleAnswer(wordleDictionary);
        this.currentGuess = "";
        this.board = new WordleBoard(this.board.rows(), this.board.cols());

        this.gameState = GameState.ACTIVE_GAME;
    }

}
