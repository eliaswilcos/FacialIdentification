package edu.illinois.cs.cs125.facialidentification;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    /** Default logging tag for messages from the main activity. */
    private static final String TAG = "FacialRecog:Main";

    private TextView mTextMessage;

    /** Request queue for our API requests. */
    private static RequestQueue requestQueue;

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
        final Button button = findViewById(R.id.refresh_weather);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                startAPICall();
                // Code here executes on main thread after user presses button
            }
        });

        // Set up the queue for our API requests

        //startAPICall();
        /**
         * Make a call to the emotion API.
         */
        void startAPICall() {
            try {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        "http://api.openweathermap.org/data/2.5/weather?zip=61820,us&appid="
                                + BuildConfig.API_KEY,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(final JSONObject response) {
                                try {
                                    TextView temperature = findViewById(R.id.Temp);
                                    String temp = response.getJSONObject("main").getString("temp");
                                    double celcius = Double.parseDouble(temp);
                                    celcius -= 273;
                                    temp = Double.toString(celcius);
                                    Toast.makeText(getApplicationContext(), "temp " + temp, Toast.LENGTH_LONG).show();
                                    temperature.setText(temp);
                                    Log.d(TAG, response.toString(2));
                                } catch (JSONException ignored) {
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError error) {
                        Log.e(TAG, error.toString());
                    }
                });
                requestQueue.add(jsonObjectRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };
}
