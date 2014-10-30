package Core;

import android.util.Log;

/**
 * Created by dwite_000 on 25.10.2014.
 */
public class GameArcade extends Game {

    public GameArcade() {
        super();
        MAX_TIME_LIMIT = 5;
        mTime = MAX_TIME_LIMIT;
    }

    @Override
    public boolean checkAnswer(boolean higher) {
        boolean result = super.checkCorrect(higher);
        Log.d("result", "current number = " + String.valueOf(currentNumber) + " previous number : "
                + String.valueOf(prevNumber) + " answer = " + String.valueOf(higher));
        if (result) {
            mTime += 3;
            if(mTime > 5)
                mTime = 5;
            nextRound();
        }
        else {
            mTime = 0;
            endGame();
        }
        return result;
    }
}
