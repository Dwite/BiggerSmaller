package smartfoxlabs.higherlower;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import Core.Game;
import Core.NumberGenerator;


public class MainActivity extends Activity {

    TextView txt;
    TextView score;
    TextView time;
    Game game;
    ProgressBar pb;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            game.subTimer();
            time.setText(String.valueOf(game.getTime()));
            pb.setProgress(game.getTime());
            if (game.getTime() > 0)
                timerHandler.postDelayed(this, 1000);
            else timerHandler.removeCallbacks(timerRunnable);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        txt = (TextView) findViewById(R.id.tvNumber);
        score = (TextView) findViewById(R.id.textView2);
        time = (TextView) findViewById(R.id.textView4);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setMax(game.MAX_TIME_LIMIT);
        pb.setProgress(game.MAX_TIME_LIMIT);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateNumber(view);
            }
        });
        game = new Game();
        updateUI();
    }

    public void generateNumber(View v) {

    }

    public void updateUI() {
        txt.setText(game.getCurrentNumberString());
        score.setText(String.valueOf(game.getScore()));
    }


    public void startGame(View v) {
        game.start();
        time.setText(String.valueOf(Game.MAX_TIME_LIMIT));
        updateUI();
        timerHandler.postDelayed(timerRunnable,1000);
    }

    public void checkAnswer(View v) {
        boolean flag = false;
        if(v.getId() == R.id.button)
            flag = true;
        game.checkAnswer(flag);
        updateUI();
    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }
}
