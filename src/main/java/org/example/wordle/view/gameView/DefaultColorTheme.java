package org.example.wordle.view.gameView;

import org.example.wordle.model.word.AnswerType;

import java.awt.*;

public class DefaultColorTheme implements ColorTheme {
  
  @Override
  public Color getCellColor(AnswerType ansType) {
    Color color = switch(ansType) {
      case BLANK -> Color.WHITE;
      case WRONG -> Color.GRAY;
      case MISPLACED -> Color.YELLOW;
      case CORRECT -> Color.GREEN;
      default -> throw new IllegalArgumentException(
        "No available color for '" + ansType + "'");
    };
    return color;
  }

  @Override
  public Color getFrameColor() {
    return new Color(0, 0, 0, 0);
  }

  @Override
  public Color getBackgroundColor() {
    return null;
  }
  
}
