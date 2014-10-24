package smartfoxlabs.higherlower;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
