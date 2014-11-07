package smartfoxlabs.higherlower;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;

import Utils.BaseGameActivity;

/**
 * Created by dwite_000 on 24.10.2014.
 */
public class BaseActivity extends BaseGameActivity {

    public static final String APP_PREFERENCES = "HigherLower";
    public static final String APP_PREFERENCES_SOUND = "Sound";
    public static final String APP_PREFERENCES_VIBRO = "Vibration";
    public static final String APP_PREFERENCES_HELP = "Tutorial";

    protected int RESULT_CODE;
    public static final int REQUEST_CODE = 1002;
    SharedPreferences mSettings;

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
        RESULT_CODE = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (RESULT_CODE == ConnectionResult.SUCCESS) {
            Log.d("Play", "connection success");
            //beginUserInitiatedSignIn();
            /*if(!getApiClient().isConnected() && !getApiClient().isConnecting()) {
                getApiClient().connect();
                Toast.makeText(getApplicationContext(),"sign in",Toast.LENGTH_SHORT).show();
            }*/
        }
        else {
            GooglePlayServicesUtil.getErrorDialog(RESULT_CODE,this,REQUEST_CODE).show();
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    public void onSignInFailed() {
        //Toast.makeText(getApplicationContext(),"sign failed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSignInSucceeded() {
        //Toast.makeText(getApplicationContext(),"sign ok",Toast.LENGTH_SHORT).show();
    }
}
