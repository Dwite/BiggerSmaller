package smartfoxlabs.higherlower;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import Adapters.ResultMenuAdapter;
import Models.ResultMenuItem;


public class MenuActivity extends BaseActivity {

    ListView lstMenu;
    ResultMenuAdapter adapter;
    String[] menuNames;
    ArrayList<Integer> menuIcons;
    ArrayList<ResultMenuItem> menuItems;

    public static final String GAME_MODE = "MODE";
    public static final int TIME_MODE = 0;
    public static final int ARCADE_MODE = 1;
    public static final int MULTIPLAYER_MODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        lstMenu = (ListView) findViewById(R.id.listView);
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
                        Toast.makeText(getApplicationContext(),"In development",Toast.LENGTH_SHORT).show();
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
        overridePendingTransition(R.animator.slide_right, R.animator.slide_toright);
    }
}
