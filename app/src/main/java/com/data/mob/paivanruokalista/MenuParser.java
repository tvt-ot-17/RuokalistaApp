package com.data.mob.paivanruokalista;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuParser {

    // jäsennellään amicalta pyydetty json-muotoinen ruokalista stringeiksi arraylistiin
    // ensimmäinen string on ravintolan nimi, toinen on aukioloajat ja loput ovat ruokalistan jäseniä
    // palautetaan null, jos haku jäsentely epäonnistui.
    static public ArrayList<String> parseMenu(String jsonString)
    {
        ArrayList<String> lunches = null;
        try
        {
            lunches = new ArrayList<String>();
            JSONObject object = new JSONObject(jsonString);

            String restaurantName = object.getString("RestaurantName");
            lunches.add(restaurantName);

            String lunchTimes = object.getJSONArray("MenusForDays").getJSONObject(0).getString("LunchTime");
            lunchTimes = lunchTimes.replace("/", "\n");
            lunches.add(lunchTimes);

            JSONArray menus = object.getJSONArray("MenusForDays").getJSONObject(0).getJSONArray("SetMenus");
            for(int i = 0; i < menus.length(); i++)
            {
                String lunch = "";
                JSONArray components = menus.getJSONObject(i).getJSONArray("Components");
                for(int j = 0; j < components.length(); j++)
                {
                    lunch += components.getString(j) + "\n";
                }
                if(!lunch.isEmpty())
                {
                    lunch = "\n" + lunch;
                    lunches.add(lunch);
                }
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return lunches;
    }
}
