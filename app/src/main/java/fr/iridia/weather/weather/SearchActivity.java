package fr.iridia.weather.weather;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.OnKeyListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import fr.iridia.weather.weather.data.QuerryLocation;

public class SearchActivity extends Activity {

    public static final String TAG = "SearchActivity";

    TextView searchField;
    ListView listView;
    View listHeader;
    private ArrayAdapter<String> listAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        listHeader = getLayoutInflater().inflate(R.layout.search_header, null);

        ArrayList<String> data = new ArrayList<String>();
        listAdapter = new ArrayAdapter<String>(this, R.layout.search_row, data);

        listView = (ListView) findViewById(R.id.listView);

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
        listAdapter.clear();
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);

        final String querry = searchField.getText() + "";
        if (!querry.isEmpty()) {

            final List<QuerryLocation> querryLocationsList = new Vector<QuerryLocation>();
            final Context context = getBaseContext();
            final Activity act = this;

            listView.addHeaderView(listHeader);
            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ConnectivityManager conMgr = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
                    if (conMgr.getActiveNetworkInfo() == null
                            || !conMgr.getActiveNetworkInfo().isAvailable()
                            || !conMgr.getActiveNetworkInfo().isConnected()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(act);
                        builder.setMessage(R.string.data_disabled)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(final DialogInterface dialog, final int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        return;
                    }

                    Intent intent = new Intent(context, WeatherDetailsActivity.class);
                    intent.putExtra(WeatherDetailsActivity.DETAIL_ACTIVITY_KEY, WeatherDetailsActivity.DETAIL_ACTIVITY_TYPES.NAMED_GPS_COORDINATES);
                    intent.putExtra(WeatherDetailsActivity.DETAIL_ACTIVITY_EXTRA_KEY, querryLocationsList.get(position).name);
                    intent.putExtra(WeatherDetailsActivity.DETAIL_ACTIVITY_EXTRA_LATITUDE, querryLocationsList.get(position).latitude);
                    intent.putExtra(WeatherDetailsActivity.DETAIL_ACTIVITY_EXTRA_LONGITUDE, querryLocationsList.get(position).longitude);
                    startActivity(intent);
                }
            });

            //final Animation animationStart = AnimationUtils.loadAnimation(this, R.anim.left_entering_scroll);
            final Animation animationStart = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
            animationStart.setDuration(200);

            /*
            final Animation animationEnd = AnimationUtils.loadAnimation(this, R.anim.top_leaving_scroll);
            animationEnd.setDuration(200);
            animationEnd.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationStart(Animation paramAnimation) {}
                public void onAnimationRepeat(Animation paramAnimation) {}
                public void onAnimationEnd(Animation paramAnimation) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listView.removeHeaderView(listHeader);
                        }
                    });
                }
            });
            */

            listView.startAnimation(animationStart);

            new AsyncTask<Integer, Integer, Integer>() {
                protected Integer doInBackground(Integer... loc) {
                    InputStream input;
                    try {
                        input = getResources().openRawResource(R.raw.city_list);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(input, Charset.forName("UTF-8")));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            final String cityName       = line.split("\t")[1];
                            final String cityCountry    = line.split("\t")[4];
                            final String cityLatitude   = line.split("\t")[2];
                            final String cityLongitude  = line.split("\t")[3];
                            if(cityName.contains(searchField.getText()))
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        listAdapter.add(cityName + " (" + cityCountry + ")");
                                        querryLocationsList.add(new QuerryLocation(cityName, Double.valueOf(cityLongitude), Double.valueOf(cityLatitude)));
                                    }
                                });
                        }
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                    return 0;
                }

                @Override
                protected void onPostExecute(Integer result) {
                    //listView.startAnimation(animationEnd);
                    listView.removeHeaderView(listHeader);
                }
            }.execute();
        }
    }
}
