package fr.iridia.weather.weather;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnKeyListener;

public class SearchActivity extends Activity {

    public static final String TAG = "SearchActivity";

    TextView searchField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchField = (EditText) findViewById(R.id.searchField);
        searchField.setOnKeyListener(new OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            searchFromField();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_entering_scroll, R.anim.left_leaving_scroll);
    }

    public void onBackButtonPressed(View v) {
        onBackPressed();
    }

    public void search(View v) {
        searchFromField();
    }

    public void searchFromField() {
        if (searchField.getText() != "") {
            Log.i(TAG, "Searching for " + searchField.getText());
        }
    }
}
