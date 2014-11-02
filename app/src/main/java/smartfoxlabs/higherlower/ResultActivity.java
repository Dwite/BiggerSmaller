package smartfoxlabs.higherlower;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

    int score;
    ResultMenuAdapter adapter;
    String[] menuNames;
    ArrayList<Integer> menuIcons;
    ArrayList<ResultMenuItem> menuItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.inject(this);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
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
                        Toast.makeText(getApplicationContext(),getString(R.string.indev),Toast.LENGTH_SHORT).show();
                        //gameTime.putExtra(GAME_MODE,MULTIPLAYER_MODE);
                        //startActivity(gameTime);
                        break;
                    case 0: shareIt();
                        break;
                    default: break;
                }
            }
        });
    }

    private void shareIt() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = getString(R.string.share_start,score);
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "HigherLower");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
}
