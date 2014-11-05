package smartfoxlabs.higherlower;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.games.Games;

import java.util.ArrayList;

import Adapters.ResultMenuAdapter;
import Core.Game;
import Models.ResultMenuItem;
import Utils.BaseGameActivity;
import butterknife.ButterKnife;
import butterknife.InjectView;


public class ResultActivity extends BaseActivity {


    @InjectView(R.id.tVScoreValue)
    TextView tvScore;

    @InjectView(R.id.lstResultMenu)
    ListView lstMenu;

    @InjectView(R.id.tvScoreStatus)
    TextView tvScoreName;

    int score;
    ResultMenuAdapter adapter;
    String[] menuNames;
    ArrayList<Integer> menuIcons;
    ArrayList<ResultMenuItem> menuItems;
    int mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.inject(this);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        mode = getIntent().getIntExtra(MenuActivity.GAME_MODE,MenuActivity.TIME_MODE);
        score = getIntent().getIntExtra(GameActivity.RESULT_CODE, 0);
        tvScore.setText(String.valueOf(score));
        menuNames = getResources().getStringArray(R.array.result_menu);
        menuItems = new ArrayList<ResultMenuItem>();
        menuIcons = new ArrayList<Integer>();
        menuIcons.add(R.drawable.ic_share);
        menuIcons.add(R.drawable.ic_refresh);
        menuIcons.add(R.drawable.ic_home);
        menuIcons.add(R.drawable.ic_trophy);
        for(int i = 0 ; i < menuNames.length; i++) {
            ResultMenuItem menuItem = new ResultMenuItem(menuIcons.get(i),menuNames[i]);
            menuItems.add(menuItem);
        }
        adapter = new ResultMenuAdapter(getApplicationContext(), menuItems,0);
        lstMenu.setAdapter(adapter);
        lstMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent gameTime = new Intent(getApplicationContext(),GameActivity.class);
                switch (i) {
                    case 2 : {
                        Intent mainScreen = new Intent(getApplicationContext(),MenuActivity.class);
                        mainScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainScreen);
                        break;
                    }
                    case 1 :
                        gameTime.putExtra(
                                MenuActivity.GAME_MODE,
                                getIntent().getIntExtra(MenuActivity.GAME_MODE,MenuActivity.TIME_MODE)
                        );
                        startActivity(gameTime);
                        finish();
                        break;
                    case 3 :
                        //Toast.makeText(getApplicationContext(),getString(R.string.indev),Toast.LENGTH_SHORT).show();
                        //gameTime.putExtra(GAME_MODE,MULTIPLAYER_MODE);
                        //startActivity(gameTime);
                        /* achivements
                        startActivityForResult(Games.Achievements.getAchievementsIntent(
                                getApiClient()), 1);
                        */

                        if(mode == MenuActivity.TIME_MODE)
                            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
                                        getApiClient(), getString(R.string.leaderboard_time_mode)),
                                2);
                        else startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
                                        getApiClient(), getString(R.string.leaderboard_arcade)),
                                2);
                        //startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(getApiClient()),2);
                        break;
                    case 0: shareIt();
                        break;
                    default: break;
                }
            }
        });
        String[] names = getResources().getStringArray(R.array.naming);
        String name = getString(R.string.bad_luck);
        if (score >= 0)
            name = names[0];
        if (score >= 5)
            name = names[1];
        if (score >= 15)
            name = names[2];
        if (score >= 25)
            name = names[3];
        if (score >= 50)
            name = names[4];
        if (score >= 100)
            name = names[5];
        if (score >= 180)
            name = names[6];
        if (score >= 1000)
            name = names[7];
        tvScoreName.setText(name);

    }

    private void setAchievements() {
        try {
            /*if(super.getApiClient().isConnected()) {
                Toast.makeText(getApplicationContext(),"connected",Toast.LENGTH_SHORT).show();
            }
            else
            if (super.getApiClient().isConnecting()) {
                Toast.makeText(getApplicationContext(),"connection",Toast.LENGTH_SHORT).show();
            }
            else {
                super.getApiClient().connect();
                Toast.makeText(getApplicationContext(), "disconnected", Toast.LENGTH_SHORT).show();
            }*/
            Games.Achievements.increment(super.getApiClient(),getString(R.string.achievement_nice_start),1);
            Games.Achievements.increment(super.getApiClient(),getString(R.string.achievement_not_a_coward),1);
            Games.Achievements.increment(super.getApiClient(),getString(R.string.achievement_half_road_completed),1);
            Games.Achievements.increment(super.getApiClient(),getString(R.string.achievement_higherlower_master),1);
            Games.Achievements.increment(super.getApiClient(),getString(R.string.achievement_higherlower_ninja),1);
            if(score < - 25)
                Games.Achievements.unlock(super.getApiClient(),getString(R.string.achievement_bad_luck_brian_));
            else {
                if (score == 0)
                    Games.Achievements.unlock(super.getApiClient(),getString(R.string.achievement_lazy_one_));
                if (score >= 5)
                    Games.Achievements.unlock(super.getApiClient(),getString(R.string.achievement_train_harder));
                if (score >= 15)
                    Games.Achievements.unlock(super.getApiClient(),getString(R.string.achievement_not_bad));
                if (score >= 25)
                    Games.Achievements.unlock(super.getApiClient(),getString(R.string.achievement_good_job));
                if (score >= 50)
                    Games.Achievements.unlock(super.getApiClient(),getString(R.string.achievement_smart_one));
                if (score >= 100)
                    Games.Achievements.unlock(super.getApiClient(),getString(R.string.achievement_einstein));
                if (score >= 180)
                    Games.Achievements.unlock(super.getApiClient(),getString(R.string.achievement_asian));
                if (score >= 1000)
                    Games.Achievements.unlock(super.getApiClient(),getString(R.string.achievement_godmode));
            }
            if(mode == MenuActivity.TIME_MODE)
                Games.Leaderboards.submitScore(super.getApiClient(),getString(R.string.leaderboard_time_mode),score);
            else if(mode == MenuActivity.ARCADE_MODE)
                Games.Leaderboards.submitScore(super.getApiClient(),getString(R.string.leaderboard_arcade),score);

        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "exception", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void shareIt() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = getString(R.string.share_start,score);
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "HigherLower");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    @Override
    public void onSignInSucceeded() {
        super.onSignInSucceeded();
        setAchievements();
    }
}
