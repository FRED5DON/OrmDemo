package ormdemo.freddon.com.ormdemo;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by fred on 16/7/8.
 */
public class BaseActivity extends AppCompatActivity {

    protected void showCustomToast(String s, String format) {
        Toast.makeText(this, format, Toast.LENGTH_LONG).show();
    }
}
