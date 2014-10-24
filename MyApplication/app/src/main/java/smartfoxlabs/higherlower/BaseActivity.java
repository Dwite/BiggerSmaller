package smartfoxlabs.higherlower;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by dwite_000 on 24.10.2014.
 */
public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
}
