package com.data.mob.paivanruokalista;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class LunchMenuActivity extends AppCompatActivity implements HttpRequestThread.Listener {

    private String restaurantId = "";
    private String languageCode = "fi";
    private String date = "";

    private HttpRequestThread httpRequestThread;

    private ListView lunchListView;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_menu);

        int day = getIntent().getExtras().getInt("day");
        int month = getIntent().getExtras().getInt("month") + 1;
        int year = getIntent().getExtras().getInt("year");
        restaurantId = getIntent().getExtras().getString("restaurantId");
        date = year + "-" + month + "-" + day;


        String url = "https://www.amica.fi/modules/json/json/Index?costNumber=" + restaurantId +
                     "&language=" + languageCode + "&date=" + date;

        lunchListView = (ListView)findViewById(R.id.lunchListView);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        lunchListView.setAdapter(arrayAdapter);

        httpRequestThread = new HttpRequestThread(url, this);
        httpRequestThread.start();
    }

    @Override
    public void onHttpRequestDone(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> lunches = MenuParser.parseMenu(message);
                if(lunches != null && !lunches.isEmpty()) {
                    ((TextView)findViewById(R.id.nameTextView)).append(lunches.get(0));
                    ((TextView)findViewById(R.id.timeTextView)).append(lunches.get(1));
                    arrayAdapter.addAll(lunches.subList(2, lunches.size()));
                }
                else
                {
                    arrayAdapter.add(getResources().getString(R.string.menu_not_found));
                }
                arrayAdapter.notifyDataSetChanged();
            }
        });
    }
}
