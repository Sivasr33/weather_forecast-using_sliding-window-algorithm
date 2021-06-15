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
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    Button button,getWeather;
    ImageView imageView;
    public static String wcountry,wstate,cityid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        editText = findViewById(R.id.editTextTextPersonName);
        button = findViewById(R.id.button);
        getWeather=findViewById(R.id.get_weather);

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
                        get_data.city_id=cityid;
                        Intent i =new Intent(getApplicationContext(),list_views.class);
                        startActivity(i);
                    }
                });
            }
        });

    }
    public static class get_data {
        public static String city_id="";
    }
    public void findWeather()
    {
        final String city = editText.getText().toString();
        String url ="http://api.openweathermap.org/data/2.5/weather?q="+state+"&appid=14c3311e791800aaf7f3f1ddb366f262&units=metric";
        //requets a string response from the provided url
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //finds city id
                            cityid = jsonObject.getString("id");
                            get_data.city_id=cityid;
                            
//                         //finds icon
//                         JSONArray jsonArray = jsonObject.getJSONArray("weather");
//                         JSONObject obj = jsonArray.getJSONObject(0);
//                         String icon = obj.getString("icon");
//                         Picasso.get().load("http://openweathermap.org/img/wn/"+icon+"@2x.png").into(imageView);

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
