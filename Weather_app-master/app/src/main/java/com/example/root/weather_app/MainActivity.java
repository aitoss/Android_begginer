package com.example.root.weather_app;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText cityname;
    TextView information;

    public void search(View view){

        Log.i("City", cityname.getText().toString());

        InputMethodManager  manager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        manager.hideSoftInputFromWindow(cityname.getWindowToken(),0);



        try {
            Downloadtask downloadtask = new Downloadtask();
            String encoder = null;
            encoder = URLEncoder.encode(cityname.getText().toString(),"UTF-8");

            downloadtask.execute("http://api.openweathermap.org/data/2.5/weather?q="+encoder+"&appid=810c2c0da167d92ba0f326e8f0f985aa");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityname = (EditText)findViewById(R.id.cityname);
        information = (TextView)findViewById(R.id.information);

    }

    public class Downloadtask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection httpURLConnection = null;

            try {
                url = new URL(urls[0]);

                httpURLConnection = (HttpURLConnection)url.openConnection();

                InputStream inputStream = httpURLConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(inputStream);

                int data = reader.read();

                while (data != -1){
                    char curr = (char) data;

                    result += curr;

                    data = reader.read();
                }

                return result;

            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Could not find Weather", Toast.LENGTH_LONG).show();
            }

            return null;
        }

       @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


             try {

                 String message = "";

                JSONObject jsonObject = new JSONObject(result);

                String weatherinfo = jsonObject.getString("weather");

                JSONArray jsonArray = new JSONArray(weatherinfo);

                for (int i=0; i<jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);

                    String main = "";
                    String description = "";
                    String temp = "";
                    main = object.getString("main");

                   description = object.getString("description");

                    if (main != "" && description != ""){
                        message += main +" : " + description + "\r\n";
                    }


                }

                if (message != ""){

                    information.setText(message);
                }else {

                    Toast.makeText(MainActivity.this, "Could not find Weather", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {

                 Toast.makeText(MainActivity.this, "Could not find Weather", Toast.LENGTH_LONG).show();
            }

        }
    }
}
