package org.example.wordle.controller;

import org.example.wordle.controller.AI.*;
import org.example.wordle.model.WordleDictionary;
import org.example.wordle.model.GameState;
import org.example.wordle.model.word.WordleWord;
import org.example.wordle.view.gameView.WordleView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class WordleAIController extends WordleController {

    private IStrategy AI;
    private WordleWord feedback;

    private Timer timer;

    public WordleAIController(ControllableWordleModel model, WordleView view) {
        super(model, view);

        WordleDictionary wordleDictionary = model.getDictionary();
        this.timer = new Timer(model.getTimerDelay(), this::clockTick);
        //this.AI = new RandomStrategy(dictionary);
        //this.AI = new EliminateStrategy(dictionary);
        //this.AI = new FrequencyStrategy(dictionary);
        this.AI = new MyStrategy(wordleDictionary);

        view.addKeyListener(this);
        view.setFocusable(true);

        this.timer.start();
    }

    public void clockTick(ActionEvent e) {
        if (model.getGameState() == GameState.GAME_OVER)
            return;

        String guess = AI.makeGuess(feedback);
        for (int i = 0; i < guess.length(); i++) {
            char c = guess.charAt(i);
            model.addCharacter(c);
        }
        feedback = model.makeGuess();
        // Stop guessing if it was correct
        if (feedback.allMatch())
            timer.stop();

        timer.setDelay(model.getTimerDelay());
        model.clockTick();
        view.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_1) {
            AI.reset();
            model.reset();
            feedback = null;
            this.timer.start();
            view.repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

}
