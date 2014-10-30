package Core;

import android.util.Log;

/**
 * Created by dwite_000 on 25.10.2014.
 */
public class GameNormal extends Game {

    public GameNormal() {
        super();
        MAX_TIME_LIMIT = 45;
        mTime = MAX_TIME_LIMIT;
    }

    @Override
    public boolean checkAnswer(boolean higher) {
        boolean result = super.checkCorrect(higher);
        Log.d("result", "current number = " + String.valueOf(currentNumber) + " previous number : "
                + String.valueOf(prevNumber) + " answer = " + String.valueOf(higher));
        nextRound();
        return result;
    }
}
