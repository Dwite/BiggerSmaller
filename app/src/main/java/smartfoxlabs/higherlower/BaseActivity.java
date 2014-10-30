package smartfoxlabs.higherlower;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by dwite_000 on 24.10.2014.
 */
public class BaseActivity extends Activity  {

    public static final String APP_PREFERENCES = "HigherLower";
    public static final String APP_PREFERENCES_SOUND = "Sound";
    public static final String APP_PREFERENCES_VIBRO = "Vibration";

    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }
}
