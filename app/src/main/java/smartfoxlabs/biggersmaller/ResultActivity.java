package smartfoxlabs.biggersmaller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.games.Games;

import java.util.ArrayList;

import Adapters.ResultMenuAdapter;
import Models.ResultMenuItem;
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
        setMenu();
        setListListener();
        setNameBasedOnScore();

    }

    private void setMenu() {
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
    }

    private void setNameBasedOnScore() {
        String[] names = getResources().getStringArray(R.array.naming);
        String name = getString(R.string.bad_luck);
        if (score >= 0)
            name = names[0];
        if (score >= 10)
            name = names[1];
        if (score >= 25)
            name = names[2];
        if (score >= 40)
            name = names[3];
        if (score >= 50)
            name = names[4];
        if (score >= 100)
            name = names[5];
        if (score >= 180)
            name = names[6];
        tvScoreName.setText(name);
    }

    private void setListListener() {
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
                        if(mode == MenuActivity.TIME_MODE)
                            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
                                        getApiClient(), getString(R.string.leaderboard_time_mode)),
                                2);
                        else startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
                                        getApiClient(), getString(R.string.leaderboard_arcade)),
                                2);
                        break;
                    case 0: shareIt();
                        break;
                    default: break;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void setAchievements() {
        try {
            Games.Achievements.unlock(super.getApiClient(),getString(R.string.achievement_first_time));
            Games.Achievements.increment(super.getApiClient(),getString(R.string.achievement_nice_start),1);
            Games.Achievements.increment(super.getApiClient(),getString(R.string.achievement_brave_one),1);
            Games.Achievements.increment(super.getApiClient(),getString(R.string.achievement_half_road),1);
            Games.Achievements.increment(super.getApiClient(),getString(R.string.achievement_master),1);
            Games.Achievements.increment(super.getApiClient(),getString(R.string.achievement_ninja),1);
            if(score <= -25)
                Games.Achievements.unlock(super.getApiClient(),getString(R.string.achievement_bad_luck_brayan));
            else {
                if (score == 0)
                    Games.Achievements.unlock(super.getApiClient(),getString(R.string.achievement_lazy));
                if (score >= 10)
                    Games.Achievements.unlock(super.getApiClient(),getString(R.string.achievement_train_harder));
                if (score >= 25)
                    Games.Achievements.unlock(super.getApiClient(),getString(R.string.achievement_not_bad));
                if (score >= 40)
                    Games.Achievements.unlock(super.getApiClient(),getString(R.string.achievement_good_job));
                if (score >= 50)
                    Games.Achievements.unlock(super.getApiClient(),getString(R.string.achievement_smart_one));
                if (score >= 100)
                    Games.Achievements.unlock(super.getApiClient(),getString(R.string.achievement_einstein));
                if (score >= 180)
                    Games.Achievements.unlock(super.getApiClient(),getString(R.string.achievement_asian));
            }
            if(mode == MenuActivity.TIME_MODE)
                Games.Leaderboards.submitScore(super.getApiClient(),getString(R.string.leaderboard_time_mode),score);
            else if(mode == MenuActivity.ARCADE_MODE)
                Games.Leaderboards.submitScore(super.getApiClient(),getString(R.string.leaderboard_arcade),score);

        }
        catch (Exception e) {
            //Toast.makeText(getApplicationContext(), "exception", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void shareIt() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = getString(R.string.share_start,score);
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    @Override
    public void onSignInSucceeded() {
        super.onSignInSucceeded();
        setAchievements();
    }
}
