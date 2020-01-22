package net.ion.et.sspsvoice;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class ApiHandler extends AsyncTask<String, String, String>
{
    public String apiUrl = "http://ssps-api.tamm.io/api";
    HttpURLConnection mConnection;
    MainActivity mMainActivity;

    public ApiHandler(MainActivity mainActivity)
    {
        this.mMainActivity = mainActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String result = callGetApi(strings[0], null);
        return result;
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        this.mMainActivity.test(result);
    }

    public String callGetApi(String path, Map<String, Object> params)
    {
        try
        {
            //URL url = new URL(apiUrl + path + p.toString());
            URL url = new URL("http://ssps-api.tamm.io/api/common/games");
            // URL url = new URL("http://192.168.10.106:8091/api/common/games");
            mConnection = (HttpURLConnection)url.openConnection();

            mConnection.setRequestMethod("GET");
            mConnection.setDoInput(true);
            // mConnection.setRequestProperty("Accept-Charset", "UTF-8");

            if (mConnection.getResponseCode() != HttpURLConnection.HTTP_OK)
            {
                System.out.println(mConnection.getResponseCode());
                return null;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(mConnection.getInputStream()));

            String line;
            String data = "";

            while ((line = br.readLine()) != null)
            {
                data += line;
            }

            mConnection.disconnect();

            return data;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
