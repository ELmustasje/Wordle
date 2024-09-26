package org.example.wordle.view.gameView;

import org.example.wordle.model.word.AnswerType;

import java.awt.*;

public interface ColorTheme {
  
  Color getCellColor(AnswerType ansType);
  
  Color getFrameColor();

  Color getBackgroundColor();
  
}
