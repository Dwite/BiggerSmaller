package smartfoxlabs.biggersmaller;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by dwite_000 on 04.11.2014.
 */
public class BaseUIActivity extends Activity{

    public static final String APP_PREFERENCES = "HigherLower";
    public static final String APP_PREFERENCES_SOUND = "Sound";
    public static final String APP_PREFERENCES_VIBRO = "Vibration";

    protected int RESULT_CODE;
    public static final int REQUEST_CODE = 1002;
    SharedPreferences mSettings;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
