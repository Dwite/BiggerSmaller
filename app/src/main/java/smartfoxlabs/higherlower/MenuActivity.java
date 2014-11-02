package smartfoxlabs.higherlower;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import Adapters.ResultMenuAdapter;
import Models.ResultMenuItem;
import butterknife.ButterKnife;
import butterknife.InjectView;


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

    public static final String GAME_MODE = "MODE";
    public static final int TIME_MODE = 0;
    public static final int ARCADE_MODE = 1;
    public static final int MULTIPLAYER_MODE = 2;
    Animation slideDown;
    Animation slideUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
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
}
