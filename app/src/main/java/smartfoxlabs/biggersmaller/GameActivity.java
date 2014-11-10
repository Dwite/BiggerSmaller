package smartfoxlabs.biggersmaller;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.util.Locale;

import Core.Game;
import Core.GameArcade;
import Core.GameNormal;
import Gestures.GameGestureListener;
import butterknife.ButterKnife;
import butterknife.InjectView;


public class GameActivity extends BaseActivity implements GameGestureListener.SimpleGestureListener {

    @InjectView(R.id.tvNumber)
    TextView txt;

    @InjectView(R.id.tVScoreValue)
    TextView score;

    @InjectView(R.id.textView4)
    TextView time;

    @InjectView(R.id.tvNumberBackground)
    TextView txtBackround;

    @InjectView(R.id.progressBar)
    ProgressBar pb;

    Game game;

    int mode;
    public static final int TIMER_INTERVAL_SECOND = 1000;
    public static final int TIMER_INTERVAL_PB_UPDATE = 250;
    public static final String RESULT_CODE = "RESULT";
    Animation swipeTop;
    Animation swipeBot;
    MediaPlayer player;

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

    @Override
    protected void onResume() {
        super.onResume();
        if (game != null && game.isRunning()) {
            timerHandler.postDelayed(timerRunnable, TIMER_INTERVAL_SECOND);
            timerHandler.postDelayed(progressRunnable, TIMER_INTERVAL_PB_UPDATE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private GameGestureListener detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        mode = getIntent().getIntExtra(MenuActivity.GAME_MODE, MenuActivity.TIME_MODE);
        initGame(mode);
        pb.setMax(game.MAX_TIME_LIMIT * 4);
        pb.setProgress(game.MAX_TIME_LIMIT * 4);
        player = new MediaPlayer();
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
        swipeTop = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.swipe_to_top);
        swipeBot = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.swipe_to_bottom);
        Animation.AnimationListener animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                txtBackround.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        swipeTop.setAnimationListener(animationListener);
        swipeBot.setAnimationListener(animationListener);
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

    @Override
    protected void onStop() {
        super.onStop();
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
        txtBackround.setText(game.getPrevNumber());
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
        animateBackground(!flag);
        //TODO DAYM STUPID MP SYSTEM IN ADNROID SUXX
        //playSound(asnwer);
        updateUI();
    }
    private void animateBackground(boolean asnwer) {
        txtBackround.setVisibility(View.VISIBLE);
        txtBackround.startAnimation((asnwer)?swipeTop:swipeBot);
    }

    private void animateNumber(boolean asnwer) {
        if (!asnwer) {
            int colorFrom = Color.BLACK;
            int colorTo = Color.RED;
            int duration = 250;
            boolean vibrate = true;
            if(mSettings.contains(APP_PREFERENCES_VIBRO)) {
                vibrate = mSettings.getBoolean(APP_PREFERENCES_VIBRO,true);
            }
            if (vibrate) {
                Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                v.vibrate(duration);
            }
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
            //Animation shake = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
            //txt.startAnimation(shake);
        }
    }

    public void playSound(boolean positive) {
            String filename = "";
            if (player.isPlaying()) {
                player.stop();
                player.release();
            }
            if (positive) {
                player.release();
                filename = "ok.wav";
                //player = MediaPlayer.create(getApplicationContext(), R.raw.ok);
                //player.start();
            }
            else
            {
                player.release();
                filename = "fail.wav";
                player = MediaPlayer.create(getApplicationContext(), R.raw.error3);
                player.start();
            }
            /*AssetFileDescriptor audioFile = null;
        try {
            if (player.isPlaying()) {
                player.stop();
                player.release();
                player = new MediaPlayer();
            }

            AssetFileDescriptor descriptor = getAssets().openFd(filename);
            player.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            player.prepare();
            player.setVolume(1f, 1f);
            player.setLooping(false);
            player.start();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

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
