package com.yaroyazeed.weatherinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class Details extends AppCompatActivity {

    TextView cityName, countryName, weatherSummary, weatherDetails;
    ImageView cityImage;
    int mCityImage;
    ProgressBar progBar1, progBar2;
    private SwipeRefreshLayout swipeRefreshLayout;

    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appID = "24db1a52d7cc15f7fd1824a9801c2285";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        cityName = findViewById(R.id.cityName);
        countryName = findViewById(R.id.cityCountry);
        weatherSummary = findViewById(R.id.weatherSummary);
        cityImage = findViewById(R.id.cityImage);
        weatherDetails = findViewById(R.id.weatherInfo);

        progBar1 = findViewById(R.id.progressbar1);
        progBar2 = findViewById(R.id.progressbar2);


        Bundle bundle = getIntent().getExtras();
            cityName.setText(bundle.getString("CityName"));
            countryName.setText(bundle.getString("CountryName"));

            mCityImage = bundle.getInt("CityImage");
            cityImage.setImageResource(mCityImage);

            weatherSummary.setVisibility(View.GONE);
            weatherDetails.setVisibility(View.GONE);

            swipeRefreshLayout = findViewById(R.id.swipeRefresh);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeRefreshLayout.setRefreshing(true);
                    progBar1.setVisibility(View.VISIBLE);
                    progBar2.setVisibility(View.VISIBLE);
                    weatherDetails.setVisibility(View.GONE);
                    weatherSummary.setVisibility(View.GONE);
                    getWeatherDetails();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });

            getWeatherDetails();
    }

    public void getWeatherDetails (){
        String city = cityName.getText().toString().trim();

        String tempUrl  = url + "?q=" +city+ "&appid=" + appID;
        StringRequest stringRequest =  new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String weatherInfo = jsonObject.getString("main");
                    JSONArray jsonArray = jsonObject.getJSONArray("weather");



                    weatherInfo = weatherInfo.replace("temp", "Temperature");
                    weatherInfo = weatherInfo.replace(",", "\n");
                    weatherInfo = weatherInfo.replace("feels_like", "Feels like");
                    weatherInfo = weatherInfo.replace("_min", " Min");
                    weatherInfo = weatherInfo.replace("_max", " Max");
                    weatherInfo = weatherInfo.replace("pressure", "Pressure");
                    weatherInfo = weatherInfo.replace("humidity", "Humidity");
                    weatherInfo = weatherInfo.replace("sea_level", "Sea Level");
                    weatherInfo = weatherInfo.replace("grnd_level", "Ground Level");
                    weatherInfo = weatherInfo.replace("\"", "");
                    weatherInfo = weatherInfo.replace("{", "");
                    weatherInfo = weatherInfo.replace("}", "");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String desc = object.getString("description");
                        String upperDesc = desc.substring(0,1).toUpperCase() + desc.substring(1).toLowerCase();
                        weatherSummary.setText(upperDesc);
                    }

                    progBar1.setVisibility(View.GONE);
                    progBar2.setVisibility(View.GONE);

                    weatherDetails.setText(weatherInfo);
                    weatherSummary.setVisibility(View.VISIBLE);
                    weatherDetails.setVisibility(View.VISIBLE);

                } catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Details.this, error.toString().trim(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(Details.this);
        requestQueue.add(stringRequest);
    }
}