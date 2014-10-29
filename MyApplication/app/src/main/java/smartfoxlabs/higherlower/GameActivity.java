package smartfoxlabs.higherlower;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;

import Core.Game;
import Core.GameArcade;
import Core.GameNormal;
import Gestures.GameGestureListener;


public class GameActivity extends BaseActivity implements GameGestureListener.SimpleGestureListener {

    TextView txt;
    TextView score;
    TextView time;
    Game game;
    ProgressBar pb;
    int mode;
    public static final int TIMER_INTERVAL_SECOND = 1000;
    public static final int TIMER_INTERVAL_PB_UPDATE = 250;
    public static final String RESULT_CODE = "RESULT";

    Handler timerHandler = new Handler();

    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            game.subTimer();
            if(game.getTime() > 0)
                time.setText(String.format(getResources().getString(R.string.time_value), game.getTime()));
            else time.setText(String.format(getResources().getString(R.string.time_value),0));
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
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        mode = getIntent().getIntExtra(MenuActivity.GAME_MODE, MenuActivity.TIME_MODE);
        initGame(mode);
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
        detector = new GameGestureListener(this,this);
        time.setText(String.format(getResources().getString(R.string.time_value), Game.MAX_TIME_LIMIT));
        updateUI();
        Locale current = getResources().getConfiguration().locale;
        game.gameLocal = current;
    }

    private void initGame(int mode) {
        switch (mode) {
            case MenuActivity.TIME_MODE:
                game = new GameNormal();
                break;
            case MenuActivity.ARCADE_MODE:
                game = new GameArcade();
                break;
            default:
                game = new GameNormal();
                break;
        }
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
        v.setVisibility(View.GONE);
        game.start();
        time.setText(String.format(getResources().getString(R.string.time_value), Game.MAX_TIME_LIMIT));
        pb.setProgress(game.MAX_TIME_LIMIT * 4);
        updateUI();
        timerHandler.postDelayed(timerRunnable,TIMER_INTERVAL_SECOND);
        timerHandler.postDelayed(progressRunnable,TIMER_INTERVAL_PB_UPDATE);
    }


    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
        timerHandler.removeCallbacks(progressRunnable);
    }

    @Override
    public void onSwipe(int direction) {
        if(game.getEndGame() || game.isRunning() == false) {
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
        boolean asnwer = game.checkAnswer(flag);
        if(game instanceof GameArcade)
            pb.setProgress(game.getTime() * 4);
        animateNumber(asnwer);
        updateUI();
    }

    private void animateNumber(boolean asnwer) {
        if (!asnwer) {
            int colorFrom = Color.BLACK;
            int colorTo = Color.RED;
            int duration = 250;
            Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            v.vibrate(duration);
            ObjectAnimator.ofObject(txt, "textColor", new ArgbEvaluator(), colorFrom, colorTo)
                    .setDuration(duration)
                    .start();
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    txt.setTextColor(Color.BLACK);
                }
            }, duration + 50);
            Animation shake = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
            txt.startAnimation(shake);
        }
    }

    public void onGameEnd() {
        Intent resultActivity = new Intent(getApplicationContext(),ResultActivity.class);
        resultActivity.putExtra(RESULT_CODE,game.getScore());
        resultActivity.putExtra(MenuActivity.GAME_MODE,mode);
        startActivity(resultActivity);
        this.finish();
    }
    @Override
    public void onDoubleTap() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
