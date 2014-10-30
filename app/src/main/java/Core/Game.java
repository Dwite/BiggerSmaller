package Core;

import java.util.Locale;
import java.util.Random;


/**
 * Created by dwite_000 on 23.10.2014.
 */
public abstract class Game {
    private NumberGenerator generator;
    public static int MAX_TIME_LIMIT;
    private int score;
    protected int mTime;
    protected int currentNumber;
    protected int prevNumber;
    protected boolean mRunning;
    private boolean endGame;
    public Locale gameLocal;

    public Game() {
        generator = new NumberGenerator();
        currentNumber = generator.genNumber();
        endGame = false;
        mRunning = false;
    }

    public void start() {
        mTime = MAX_TIME_LIMIT;
        endGame = false;
        score = 0;
        mRunning = true;
        nextRound();
    }

    public abstract boolean checkAnswer(boolean higher); /*{
        boolean result = checkCorrect(higher);
        Log.d("result","current number = " + String.valueOf(currentNumber) + " previous number : "
                + String.valueOf(prevNumber) + " answer = " + String.valueOf(higher));
        nextRound();
        return result;
    }*/

    protected boolean checkCorrect(boolean higher) {
        boolean result = false;
        if (mTime <= 0) {
            endGame();
            return false;
        }
        if ((currentNumber > prevNumber) && higher) {
            addScore();
            result = true;
        }
        else if((currentNumber < prevNumber) && !higher) {
            addScore();
            result = true;
        }
        else subScore();
        return result;
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
        mRunning = false;
    }

    public void restart() {
        score = 0;
        mTime = MAX_TIME_LIMIT;
        endGame = false;
        mRunning = false;
    }

    private int getCurrentNumber() { return currentNumber; }

    public String getCurrentNumberString() {
        Random r = new Random();
        int type = r.nextInt(20);

        if(type == 16 && score >= 10)
        {   NumberConverter converter;
            String locale = gameLocal.getDisplayLanguage();
            if (locale.equals("русский")) {
                converter = new RussianNumberConverter();
            }
            else {
                converter = new EnglishNumberConverter();
            }
            return converter.convert(currentNumber);
        }

        if(type == 17 && score >= 20) {
            String number = "";
            int partOne = r.nextInt(currentNumber);
            int partTwo = currentNumber - partOne;
            number = String.valueOf(partOne) + " + " + String.valueOf(partTwo);
            return number;
        }
        else return String.valueOf(getCurrentNumber());
    }

    public String getPrevNumber() {
        return String.valueOf(prevNumber);
    }
    private void addScore() { score++; }

    public boolean getEndGame() { return endGame; }

    private void subScore() { score--; }

    public int getScore() { return score; }

    public int getTime() { return mTime; }

    public void subTimer() { mTime--; }

    public boolean isRunning() { return mRunning; }
}
