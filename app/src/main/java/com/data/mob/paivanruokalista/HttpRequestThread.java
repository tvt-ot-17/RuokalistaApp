package com.data.mob.paivanruokalista;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequestThread extends Thread
{
    public interface Listener
    {
        public void onHttpRequestDone(String message);
    }

    private String urlString;
    private Listener listener;

    public HttpRequestThread(String url, Listener listener)
    {
        urlString = url;
        this.listener = listener;
    }

    // http-pyyntö alustuksessa annettuun urliin. Palautetaan hakutulos stringinä
    // Listener.onHttpRequestDone()-funktiota käyttäen.
    @Override
    public void run()
    {
        HttpURLConnection urlConnection = null;
        String jsonString = "";

        try
        {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            jsonString = fromStream(in);

            in.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (urlConnection != null)
            {
                urlConnection.disconnect();
            }
        }
        listener.onHttpRequestDone(jsonString);
    }

    // apufunktio, jolla stream muunnetaan stringiksi.
    private String fromStream(InputStream in) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        String line;
        while ((line = reader.readLine()) != null)
        {
            out.append(line);
            out.append(newLine);
        }
        return out.toString();
    }
}
