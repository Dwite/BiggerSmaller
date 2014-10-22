package Core;

import android.content.Context;
import android.util.Log;

import java.util.Random;
import java.util.Timer;
import android.os.Handler;


/**
 * Created by dwite_000 on 23.10.2014.
 */
public class Game {
    private NumberGenerator generator;
    public static final int MAX_TIME_LIMIT = 10;
    private int score;
    private int mTime;
    private int currentNumber;
    private int prevNumber;
    private boolean endGame;



    public Game() {
        generator = new NumberGenerator();
        currentNumber = generator.genNumber();
        endGame = false;
    }

    public void start() {
        mTime = MAX_TIME_LIMIT;
        nextRound();
    }

    public void checkAnswer(boolean higher) {
        if (mTime <= 0) {
            endGame();
            return;
        }
        if ((currentNumber > prevNumber) && higher) {
            addScore();
        }
        else if((currentNumber < prevNumber) && !higher)
            addScore();
        else subScore();
        Log.d("result","current number = " + String.valueOf(currentNumber) + " previous number : "
                + String.valueOf(prevNumber) + " answer = " + String.valueOf(higher));
        nextRound();
    }

    public void nextRound() {
        if (mTime > 0) {
            prevNumber = currentNumber;
            do {
                currentNumber = generator.genNumber();
            }
            while (currentNumber == prevNumber);

        }
        else endGame();
    }

    public void endGame() {
        endGame = true;
    }

    public void restart() {
        score = 0;
        mTime = MAX_TIME_LIMIT;
    }

    private int getCurrentNumber() { return currentNumber; }

    public String getCurrentNumberString() {
        Random r = new Random();
        int type = r.nextInt(11);
        if(type == 10)
            return EnglishNumberConverter.convert(currentNumber);
        else return String.valueOf(getCurrentNumber());
    }
    private void addScore() { score++; }

    public boolean getEndGame() { return endGame; }

    private void subScore() { score--; }

    public int getScore() { return score; }

    public int getTime() { return mTime; }

    public void subTimer() { mTime--; }
}
