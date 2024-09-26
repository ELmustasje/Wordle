package org.example.wordle.model;

import org.example.grid.CellPosition;
import org.example.grid.Grid;
import org.example.wordle.model.word.AnswerType;
import org.example.wordle.model.word.WordleCharacter;
import org.example.wordle.model.word.WordleWord;

/**
 * This class represents the Wordle board and is a Grid of WordleCharacter.
 */
public class WordleBoard extends Grid<WordleCharacter> {

    private int currentRow = 0;

    public WordleBoard(int rows, int cols) {
        super(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                CellPosition pos = new CellPosition(i, j);
                WordleCharacter cg = new WordleCharacter(' ', AnswerType.BLANK);
                this.set(pos, cg);
            }
        }
    }

    /**
     * Set the given WordleWord in the current selected row of the board. When the
     * word is set in this row, the board moves to the next row.
     * 
     * @param wordGuess
     */
    public void setRow(WordleWord wordGuess) {
        int col = 0;
        for (WordleCharacter cg : wordGuess) {
            CellPosition pos = new CellPosition(currentRow, col++);
            set(pos, cg);
        }
        currentRow++;
    }

    /**
     * Gets the current row of the board.
     * @return current row
     */
    public int getCurrentRow() {
        return currentRow;
    }

}
