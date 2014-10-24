package smartfoxlabs.higherlower;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import Core.Game;
import Gestures.GameGestureListener;


public class MainActivity extends BaseActivity implements GameGestureListener.SimpleGestureListener {

    TextView txt;
    TextView score;
    TextView time;
    Game game;
    ProgressBar pb;
    public static final int TIMER_INTERVAL_SECOND = 1000;
    public static final int TIMER_INTERVAL_PB_UPDATE = 250;
    public static final String RESULT_CODE = "RESULT";

    Handler timerHandler = new Handler();

    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            game.subTimer();
            time.setText(String.valueOf(game.getTime()));
            //pb.setProgress(game.getTime());
            if (game.getTime() > 0) {
                timerHandler.postDelayed(this, TIMER_INTERVAL_SECOND);
            }
            else {
                if(pb.getProgress() > 0)
                    pb.setProgress(0);
                timerHandler.removeCallbacks(timerRunnable);
                timerHandler.removeCallbacks(progressRunnable);
                onGameEnd();
            }
        }
    };

    Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            pb.setProgress(pb.getProgress() - 1);
            if (game.getTime() > 0) {
                timerHandler.postDelayed(this, TIMER_INTERVAL_PB_UPDATE);
            }
        }
    };

    private GameGestureListener detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt = (TextView) findViewById(R.id.tvNumber);
        score = (TextView) findViewById(R.id.tVScoreValue);
        time = (TextView) findViewById(R.id.textView4);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setMax(game.MAX_TIME_LIMIT * 4);
        pb.setProgress(game.MAX_TIME_LIMIT * 4);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateNumber(view);
            }
        });
        game = new Game();
        updateUI();
        detector = new GameGestureListener(this,this);

    }

    public void generateNumber(View v) {

    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent me){
        // Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    public void updateUI() {
        txt.setText(game.getCurrentNumberString());
        if(txt.getText().toString().length() > 9)
            txt.setTextSize(48f);
        else txt.setTextSize(64f);
        score.setText(String.valueOf(game.getScore()));
    }


    public void startGame(View v) {
        game.start();
        time.setText(String.valueOf(Game.MAX_TIME_LIMIT));
        pb.setProgress(game.MAX_TIME_LIMIT * 4);
        updateUI();
        timerHandler.postDelayed(timerRunnable,TIMER_INTERVAL_SECOND);
        timerHandler.postDelayed(progressRunnable,TIMER_INTERVAL_PB_UPDATE);
    }


    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

    @Override
    public void onSwipe(int direction) {
        if(game.getEndGame()) {
            return;
        }
        boolean flag = false;
        switch (direction) {
            case GameGestureListener.SWIPE_UP :
                flag = true;
                break;
            case GameGestureListener.SWIPE_DOWN :
                flag = false;
                break;
            default: break;
        }
        game.checkAnswer(flag);
        updateUI();
    }

    public void onGameEnd() {
        Intent resultActivity = new Intent(getApplicationContext(),ResultActivity.class);
        resultActivity.putExtra(RESULT_CODE,game.getScore());
        startActivity(resultActivity);
    }
    @Override
    public void onDoubleTap() {

    }
}
