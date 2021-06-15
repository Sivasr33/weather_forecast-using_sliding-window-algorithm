package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class MainActivity3 extends AppCompatActivity {
    EditText editText;
    Button button,getWeather;
    ImageView imageView;
    TextView temperature, time, longitude, latitude, humidity, sunrise, sunset, pressure, windspeed, country,
             state_name, max_temp, min_temp, feelslike;
    public static String wcountry,wstate,cityid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        editText = findViewById(R.id.editTextTextPersonName);
        button = findViewById(R.id.button);
        getWeather=findViewById(R.id.get_weather);
        imageView = findViewById(R.id.imageView);
        temperature = findViewById(R.id.textView3);
        time = findViewById(R.id.textView2);

        longitude = findViewById(R.id.longitude);
        latitude = findViewById(R.id.latitude);
        humidity = findViewById(R.id.humidity);
        sunrise = findViewById(R.id.sunrise);
        sunset = findViewById(R.id.sunset);
        pressure = findViewById(R.id.pressure);
        windspeed = findViewById(R.id.wind);
        country = findViewById(R.id.country);
        state_name = findViewById(R.id.city_nam);
        max_temp = findViewById(R.id.temp_max);
        min_temp = findViewById(R.id.min_temp);
        feelslike = findViewById(R.id.feels);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                findWeather();
                get_data.cityName=editText.getText().toString();
                getWeather.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        get_data.country=wcountry;
                        get_data.city_id=cityid;
                        Log.d("c7",get_data.country+", "+get_data.cityName+", "+get_data.city_id);
                        Intent i =new Intent(getApplicationContext(),list_views.class);
                        startActivity(i);
                    }
                });
            }
        });

    }
    public static class get_data {
        public static String city_id="",country="",cityName="";
    }
    public void findWeather()
    {
        final String state = editText.getText().toString();
        String url ="http://api.openweathermap.org/data/2.5/weather?q="+state+"&appid=14c3311e791800aaf7f3f1ddb366f262&units=metric";
        //requets a string response from the provided url
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //finds temperature
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject object = jsonObject.getJSONObject("main");
                            double wtemp = object.getDouble("temp");
                            temperature.setText("Temperature\n"+wtemp+"°C");

                            //finds country
                            JSONObject object8 = jsonObject.getJSONObject("sys");
                            wcountry = object8.getString("country");
                            country.setText(wcountry+"  :");

                            //finds city
                            wstate = jsonObject.getString("name");
                            state_name.setText(wstate);

                            //finds city id
                            cityid = jsonObject.getString("id");
                            get_data.city_id=cityid;
                            list_views l =new list_views();
                            l.city_id=cityid;

                            //finds icon
                            JSONArray jsonArray = jsonObject.getJSONArray("weather");
                            JSONObject obj = jsonArray.getJSONObject(0);
                            String icon = obj.getString("icon");
                            Picasso.get().load("http://openweathermap.org/img/wn/"+icon+"@2x.png").into(imageView);

                            //finds date & time
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat std = new SimpleDateFormat("HH:mm a \nE, MMM dd yyyy");
                            String date = std.format(calendar.getTime());
                            time.setText(date);

                            //finds latitude
                            JSONObject object2 = jsonObject.getJSONObject("coord");
                            double find_latitude = object2.getDouble("lat");
                            latitude.setText(find_latitude+"°  N");

                            //finds longitude
                            JSONObject object3 = jsonObject.getJSONObject("coord");
                            double find_longitude = object3.getDouble("lon");
                            longitude.setText(find_longitude+"°  E");

                            //finds humidity
                            JSONObject object4 = jsonObject.getJSONObject("main");
                            int find_humidity = object4.getInt("humidity");
                            humidity.setText(find_humidity+"  %");

                            //finds sunrise
                            JSONObject object5 = jsonObject.getJSONObject("sys");
                            String find_sunrise = object5.getString("sunrise");
                            sunrise.setText(find_sunrise+"  am");

                            //finds sunrise
                            JSONObject object6 = jsonObject.getJSONObject("sys");
                            String find_sunset = object6.getString("sunset");
                            sunset.setText(find_sunset+"  pm");

                            //finds pressure
                            JSONObject object7 = jsonObject.getJSONObject("main");
                            String find_pressure = object7.getString("pressure");
                            pressure.setText(find_pressure+"  hPa");

                            //finds windspeed
                            JSONObject object9 = jsonObject.getJSONObject("wind");
                            String find_windspeed = object9.getString("speed");
                            windspeed.setText(find_windspeed+"  km/h");

                            //finds minimum temperature
                            JSONObject object10 = jsonObject.getJSONObject("main");
                            double find_tempmin = object10.getDouble("temp_min");
                            min_temp.setText("Min Temp\n"+find_tempmin+" °C");

                            //finds maximum temperature
                            JSONObject object12 = jsonObject.getJSONObject("main");
                            double find_tempmax = object12.getDouble("temp_max");
                            max_temp.setText("Max Temp\n"+find_tempmax+" °C");

                            //find feels
                            JSONObject object13 = jsonObject.getJSONObject("main");
                            double find_feelslike = object13.getDouble("feels_like");
                            feelslike.setText(find_feelslike+" °C");



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() { //handles error
            @Override
            public void onErrorResponse(VolleyError error) {
                String s = null;
                if (error instanceof TimeoutError || error instanceof NoConnectionError) { //reuest has either time out/no net connection
                    s="internet connection";
                } else if (error instanceof ServerError) {  //server responded with a error response
                    s="server error/city does not exist";
                }
                Toast.makeText(MainActivity3.this,"Failure due to "+s,Toast.LENGTH_SHORT).show();
                //error.getLocalizedMessage()
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity3.this);
        requestQueue.add(stringRequest);  //add the request to the request queue
    }

}