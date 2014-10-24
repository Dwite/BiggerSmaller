package smartfoxlabs.higherlower;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
                switch (i) {
                    case 0 : {
                        Intent gameTime = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(gameTime);
                        break;
                    }
                    case 1 : break;
                    case 2 : break;
                    default: break;
                }
            }
        });
    }

}
