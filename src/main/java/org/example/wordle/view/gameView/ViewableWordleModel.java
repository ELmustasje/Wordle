package org.example.wordle.view.gameView;

import org.example.grid.GridCell;
import org.example.grid.GridDimension;
import org.example.wordle.model.GameState;
import org.example.wordle.model.word.WordleCharacter;

public interface ViewableWordleModel  {
    
    /**
     * Get dimensions of Wordle board
     * @return dimensions of board
     */
    public GridDimension getDimension();

    /**
     * Get tiles of Wordle board.
     * @return iterable of board tiles
     */
    public Iterable<GridCell<WordleCharacter>> getTilesOnBoard();

    /**
     * Get tiles of current Wordle guess, the letters and color of tile.
     * @return tiles of current guess.
     */
    public Iterable<GridCell<WordleCharacter>> getCurrentGuess();
    
    /**
     * Get current game state. The game state expresses if the game is in active, game is over, etc.
     * @return current game state.
     */
    public GameState getGameState();

    
}
