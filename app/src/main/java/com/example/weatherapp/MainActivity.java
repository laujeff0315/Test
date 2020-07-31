package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText city;
    TextView weatherInfo;

    public void checkWeather(View view) {



        DownloadJSON task = new DownloadJSON();
        String result = null;
        try {
            String location = URLEncoder.encode(city.getText().toString(),"UTF-8") ;
            result = task.execute("https://openweathermap.org/data/2.5/weather?q="+ location+"&appid=439d4b804bc8187953eb36d2a8c26a02").get();

            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(city.getWindowToken(),0);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city = findViewById(R.id.cityText);
        weatherInfo = findViewById(R.id.weatherInfo);


    }


    public class DownloadJSON extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                StringBuilder result = new StringBuilder();
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result.append(current);
                    data = reader.read();
                }
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();

                Toast.makeText(MainActivity.this, "Could not find weather.", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);

                String cityName = jsonObject.getString("name");
                String weather = jsonObject.getString("weather");
                String temperature = jsonObject.getString("main");
                String info = "";

                info+= "Name：" + cityName + "\n";



                JSONArray weatherArr = new JSONArray(weather);
                for (int i = 0; i <weatherArr.length();i++) {
                    JSONObject jsonPart = weatherArr.getJSONObject(i);

                    info += "main: "+ jsonPart.getString("main") + "\n" ;
                    info += "description: "+jsonPart.getString("description")+"\n";


                }

                /*JSONArray tempArr = new JSONArray(temperature);

                for (int i = 0; i< tempArr.length(); i++) {
                    JSONObject tempPart = tempArr.getJSONObject(i);

                    info += "Temperature: "+ tempPart.getString("temp")+ "C \n";
                    info += "Feels Like: "+ tempPart.getString("feels_like")+ "C \n";

                    error occured whilst executing this
                    }
                 */







                weatherInfo.setText(info);



            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}