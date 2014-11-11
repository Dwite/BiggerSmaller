package smartfoxlabs.biggersmaller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.games.Games;

import java.util.ArrayList;

import Adapters.ResultMenuAdapter;
import Models.ResultMenuItem;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MenuActivity extends BaseActivity {


    ResultMenuAdapter adapter;
    String[] menuNames;
    ArrayList<Integer> menuIcons;
    ArrayList<ResultMenuItem> menuItems;

    @InjectView(R.id.iVHelp)
    ImageView helpButton;

    @InjectView(R.id.listView)
    ListView lstMenu;

    @InjectView(R.id.RLHelp)
    RelativeLayout help;

    @InjectView(R.id.RLMENU)
    RelativeLayout menu;

    @InjectView(R.id.LLSettings)
    LinearLayout settings;

    @InjectView(R.id.bnSettings)
    ImageButton btnSettings;

    @InjectView(R.id.tvVibroValue)
    TextView tvVibro;
    @InjectView(R.id.tVSoundValue)
    TextView tvSound;

    public static final String GAME_MODE = "MODE";
    public static final int TIME_MODE = 0;
    public static final int ARCADE_MODE = 1;
    public static final int MULTIPLAYER_MODE = 2;
    Animation slideDown;
    Animation slideUp;

    private boolean settingSound = true;
    private boolean settingVibro = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        try {
            ((HigherLowerApplication) getApplication()).getTracker(HigherLowerApplication.TrackerName.GLOBAL_TRACKER);

            GoogleAnalytics.getInstance(this).getLogger()
                    .setLogLevel(Logger.LogLevel.VERBOSE);
            // Get tracker.
            Tracker t = ((HigherLowerApplication)this.getApplication()).getTracker(
                    HigherLowerApplication.TrackerName.GLOBAL_TRACKER);

            // Set screen name.
            // Where path is a String representing the screen name.
            t.setScreenName("MenuActivityS");
            // Send a screen view.
            t.send(new HitBuilders.AppViewBuilder().build());
        }
        catch (Exception e) {

        }
        ButterKnife.inject(this);
        slideDown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_down);
        slideUp = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up);
        slideUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                menu.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        menuNames = getResources().getStringArray(R.array.main_menu);
        menuItems = new ArrayList<ResultMenuItem>();
        menuIcons = new ArrayList<Integer>();
        menuIcons.add(R.drawable.ic_clock);
        menuIcons.add(R.drawable.ic_arcade);
        menuIcons.add(R.drawable.ic_multiplayer);
        for(int i = 0 ; i < menuNames.length; i++) {
            ResultMenuItem menuItem = new ResultMenuItem(menuIcons.get(i),menuNames[i]);
            menuItems.add(menuItem);
        }
        adapter = new ResultMenuAdapter(getApplicationContext(), menuItems,1);
        lstMenu.setAdapter(adapter);
        lstMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent gameTime = new Intent(getApplicationContext(),GameActivity.class);
                switch (i) {
                    case 0 : {
                        gameTime.putExtra(GAME_MODE, TIME_MODE);
                        startActivity(gameTime);
                        break;
                    }
                    case 1 :
                        //Toast.makeText(getApplicationContext(),"In development",Toast.LENGTH_SHORT).show();
                        gameTime.putExtra(GAME_MODE, ARCADE_MODE);
                        startActivity(gameTime);
                        break;
                    case 2 :
                        Toast.makeText(getApplicationContext(),getString(R.string.indev),Toast.LENGTH_SHORT).show();
                        //gameTime.putExtra(GAME_MODE,MULTIPLAYER_MODE);
                        //startActivity(gameTime);
                        break;
                    default: break;
                }
            }
        });
        if(!mSettings.contains(APP_PREFERENCES_HELP))
            showHelp(helpButton);
        if(mSettings.contains(APP_PREFERENCES_SOUND)) {
            settingSound = mSettings.getBoolean(APP_PREFERENCES_SOUND,true);
        }
        if(mSettings.contains(APP_PREFERENCES_VIBRO)) {
            settingVibro = mSettings.getBoolean(APP_PREFERENCES_VIBRO,true);
        }


    }

    @OnClick(R.id.bnSettings)
    public void showSeetings() {
        tvSound.setText((settingSound)?R.string.on:R.string.off);
        tvVibro.setText((settingVibro)?R.string.on:R.string.off);
        btnSettings.setClickable(false);
        menu.setClickable(false);
        settings.setVisibility(View.VISIBLE);
        settings.startAnimation(slideUp);
    }

    @OnClick(R.id.iBClose)
    public void closeSettings() {
        btnSettings.setClickable(true);
        menu.setVisibility(View.VISIBLE);
        menu.setClickable(true);
        settings.setVisibility(View.GONE);
        settings.startAnimation(slideDown);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @OnClick(R.id.bnAchivments)
    public void showAchivments() {
        if(super.getApiClient().isConnected())
            startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()),1);
    }

    @OnClick(R.id.RlSound)
    public void setSound() {
        settingSound = !settingSound;
        tvSound.setText((settingSound)?R.string.on:R.string.off);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_SOUND, settingSound);
        editor.apply();
    }

    @OnClick(R.id.RLVibro)
    public void setVibro() {
        settingVibro = !settingVibro;
        tvVibro.setText((settingVibro)?R.string.on:R.string.off);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_VIBRO, settingVibro);
        editor.apply();
    }

    @OnClick(R.id.bnRecrods)
    public void showRecords() {
        if(super.getApiClient().isConnected())
            startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(getApiClient()),2);
    }
    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.slide_right, R.anim.slide_toright);
    }

    public void showHelp(View v) {
        help.setVisibility(View.VISIBLE);
        help.startAnimation(slideUp);
        v.setClickable(false);
    }

    public void okHelp(View v) {
        final View view = v;
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_HELP, true);
        editor.apply();

        slideDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        help.setVisibility(View.GONE);
        help.startAnimation(slideDown);
        helpButton.setClickable(true);
        menu.setVisibility(View.VISIBLE);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
