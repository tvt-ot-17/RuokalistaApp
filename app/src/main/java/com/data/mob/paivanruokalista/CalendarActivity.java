package com.data.mob.paivanruokalista;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

public class CalendarActivity extends AppCompatActivity {

    private Hashtable<String, String> restaurantIds;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        restaurantIds = new Hashtable<String, String>();
        initializeRestaurantIds();

        // alustetaan spinneri
        spinner = (Spinner)(findViewById(R.id.restaurantSpinner));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, restaurantIds.keySet().toArray(new String[0]));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        CalendarView calendarView = (CalendarView)findViewById(R.id.calendarView);
        setDateRange(calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Intent intent = new Intent(getApplicationContext(), LunchMenuActivity.class);
                String selectedRestaurant = (String)spinner.getSelectedItem();

                intent.putExtra("restaurantId", restaurantIds.get(selectedRestaurant));
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day", dayOfMonth);
                startActivity(intent);
            }
        });
    }

    // alustaa restaurantIds-hastablen alkioilla
    private void initializeRestaurantIds()
    {
        restaurantIds.put("Kotkanpoika & Kultturelli", "0235");
        restaurantIds.put("Wallu", "0217");
        restaurantIds.put("Pykälä", "0212");
    }

    // asettaa aikaisimman valittavan päivämäärän viikon ensimmäiseksi päiväksi
    // ja viimeisen valittavan päivän viikon perjantaiksi.
    private void setDateRange(CalendarView calendarView)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

        calendarView.setMinDate(cal.getTimeInMillis());

        cal.add(Calendar.DAY_OF_YEAR, 4);

        calendarView.setMaxDate(cal.getTimeInMillis());
    }
}
