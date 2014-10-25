package smartfoxlabs.higherlower;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import Adapters.ResultMenuAdapter;
import Models.ResultMenuItem;


public class ResultActivity extends BaseActivity {

    int score;
    TextView tvScore;
    ListView lstMenu;
    ResultMenuAdapter adapter;
    String[] menuNames;
    ArrayList<Integer> menuIcons;
    ArrayList<ResultMenuItem> menuItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        score = getIntent().getIntExtra(MainActivity.RESULT_CODE, 0);
        tvScore = (TextView) findViewById(R.id.tVScoreValue);
        tvScore.setText(String.valueOf(score));
        lstMenu = (ListView) findViewById(R.id.lstResultMenu);
        menuNames = getResources().getStringArray(R.array.result_menu);
        menuItems = new ArrayList<ResultMenuItem>();
        menuIcons = new ArrayList<Integer>();
        menuIcons.add(R.drawable.ic_home);
        menuIcons.add(R.drawable.ic_refresh);
        menuIcons.add(R.drawable.ic_trophy);
        menuIcons.add(R.drawable.ic_share);
        for(int i = 0 ; i < menuNames.length; i++) {
            ResultMenuItem menuItem = new ResultMenuItem(menuIcons.get(i),menuNames[i]);
            menuItems.add(menuItem);
        }
        adapter = new ResultMenuAdapter(getApplicationContext(), menuItems,0);
        lstMenu.setAdapter(adapter);
        lstMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent gameTime = new Intent(getApplicationContext(),MainActivity.class);
                switch (i) {
                    case 0 : {
                        Intent mainScreen = new Intent(getApplicationContext(),MenuActivity.class);
                        mainScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainScreen);
                        break;
                    }
                    case 1 :
                        gameTime.putExtra(MenuActivity.GAME_MODE,MenuActivity.TIME_MODE);
                        startActivity(gameTime);
                        finish();
                        break;
                    case 2 :
                        Toast.makeText(getApplicationContext(),"In development",Toast.LENGTH_SHORT).show();
                        //gameTime.putExtra(GAME_MODE,MULTIPLAYER_MODE);
                        //startActivity(gameTime);
                        break;
                    default: break;
                }
            }
        });
    }
}
