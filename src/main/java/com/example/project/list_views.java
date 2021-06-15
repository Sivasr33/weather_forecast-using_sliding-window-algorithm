package com.example.project;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class list_views extends AppCompatActivity {

    ListView listView;
    DateFormat date = new SimpleDateFormat("E, MMMdd ");
    int i,j;
    int[][] C7days=new int[7][3], fc7days=new int[7][3];
    int[][] P14days=new int[14][3];
    String []days=new String[7];
    String []maxTemp=new String[7],minTemp=new String[7],Humdity=new String[7],Temp_tot=new String[7];
    int month=0,day=0;

    //dateformat
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:MM:SS");
    Calendar clr =Calendar.getInstance();

    //decimal format
    DecimalFormat df = new DecimalFormat("#.00");

    String city_id="";
    String key = "2827b08d7b7025e36711229c234397a8";
    String url1 = "",url2="";
    //"https://history.openweathermap.org/data/2.5/history/city?id="+city_id+"&appid="+key

    public static class DownloadJson extends AsyncTask<String, Void , String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection httpURLConnection;
            InputStream inputstream;
            InputStreamReader inputstreamReader;
            try {
                url=new URL(urls[0]);
                httpURLConnection= (HttpURLConnection) url.openConnection();
                inputstream=httpURLConnection.getInputStream();
                inputstreamReader=new InputStreamReader(inputstream);

                int data = inputstreamReader.read();

                while(data != -1) {
                    result+=((char) data);
                    data = inputstreamReader.read();
                }

            } catch (Exception e) {   e.printStackTrace();   }

            return result;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_views);

        city_id= MainActivity3.get_data.city_id;

        pastWeek(city_id);
        pastYear();

        //myadapter
        listView=findViewById(R.id.listView);
        MyAdapter adapter = new MyAdapter(getApplicationContext(),days,Temp_tot,Humdity);
        listView.setAdapter(adapter);
    }

    private void pastWeek(String city_id) {

        try {
            for(int i=7,j=0; i>0; i--) {
                clr.add(Calendar.DATE, -i); // before 7 days from yesterday
                long start = dateFormat.parse(dateFormat.format(clr.getTime()) + "").getTime() / 1000;

                clr.add(Calendar.HOUR_OF_DAY, 1);      // add one hour
                long end=dateFormat.parse(dateFormat.format(clr.getTime()) + "").getTime() / 1000;

                url1 ="https://history.openweathermap.org/data/2.5/history/city?id="+city_id+"&start="+start+"&end="+
                        end+"&appid="+key;

                // json executing
                try {
                    String result1= new DownloadJson().execute(url1).get();
                    JSONObject obj=new JSONObject(result1);
                    JSONArray list= obj.getJSONArray("list"); // []
                    JSONObject listitems= (JSONObject) list.get(0); // {}
                    JSONObject mainI = (JSONObject) listitems.get("main");
                    String temp_min1= df.format(Double.parseDouble(mainI.getString("temp_min"))-273.15);
                    String temp_max1= df.format(Double.parseDouble(mainI.getString("temp_max"))-273.15);
                    String humidity1= df.format(Double.parseDouble(mainI.getString("humidity")));

                    C7days[j][0]=  (int) Double.parseDouble(temp_max1);
                    C7days[j][1]=  (int) Double.parseDouble(temp_min1);
                    C7days[j][2]= (int) Double.parseDouble(humidity1);

                    j++;
                }
                catch (Exception e) {    e.printStackTrace();  }

                clr.add(Calendar.HOUR_OF_DAY, -1); // substract 1 hour
                clr.add(Calendar.DATE,i);
            }

        }
        catch (Exception e) { e.printStackTrace(); }
    }

    private void pastYear() {
        clr.add(Calendar.YEAR, -1); // now here, 2021-1 = 2020

        for(int i=7,j=0;i>0;i--){
            clr.add(Calendar.DATE, -i); // before 7 days
            month=clr.get(Calendar.MONTH)+1;
            day = clr.get(Calendar.DAY_OF_MONTH);
            //https://history.openweathermap.org/data/2.5/aggregated/day?id=1273874&month=5&day=16&appid=2827b08d7b7025e36711229c234397a8
            //https://history.openweathermap.org/data/2.5/aggregated/day?q=Cochin,IN&month=5&day=16&appid=2827b08d7b7025e36711229c234397a8

            url2="https://history.openweathermap.org/data/2.5/aggregated/day?id="+city_id
                    +"&month="+month+"&day="+day+"&appid="+key;

            try {
                String res1= new DownloadJson().execute(url2).get();
                JSONObject obj = new JSONObject(res1);
                JSONObject res =obj.getJSONObject("result"); // it contains temp,humidity
                JSONObject temp= res.getJSONObject("temp");
                JSONObject humid= res.getJSONObject("humidity");

                String temp_max2=df.format(Double.parseDouble(temp.getString("record_max"))-273.15);
                String temp_min2=df.format(Double.parseDouble(temp.getString("record_min"))-273.15);
                String humidity2=df.format(Double.parseDouble(humid.getString("median")));

                P14days[j][0]=(int) Double.parseDouble(temp_max2);
                P14days[j][1]=(int) Double.parseDouble(temp_min2);
                P14days[j][2]=(int) Double.parseDouble(humidity2);

                j++;

                clr.add(Calendar.DATE,i);

            }catch (Exception e) {  e.printStackTrace(); }
        }

        for(int i=0,j=7;i<7;i++){
            clr.add(Calendar.DATE, i); // next 7 days
            month=clr.get(Calendar.MONTH)+1;
            day = clr.get(Calendar.DAY_OF_MONTH);
            url2="https://history.openweathermap.org/data/2.5/aggregated/day?id="+city_id
                    +"&month="+month+"&day="+day+"&appid="+key;

            try {
                String result1= new DownloadJson().execute(url2).get();
                JSONObject obj=new JSONObject(result1);
                JSONObject res =obj.getJSONObject("result"); // it contains temp,humidity
                JSONObject temp= res.getJSONObject("temp");
                JSONObject humid= res.getJSONObject("humidity");

                String temp_max2=df.format(Double.parseDouble(temp.getString("record_max"))-273.15);
                String temp_min2=df.format(Double.parseDouble(temp.getString("record_min"))-273.15);
                String humidity2=df.format(Double.parseDouble(humid.getString("median")));

                P14days[j][0]=(int) Double.parseDouble(temp_max2);
                P14days[j][1]=(int) Double.parseDouble(temp_min2);
                P14days[j][2]=(int) Double.parseDouble(humidity2);

                j++;
            }catch (Exception e) {  e.printStackTrace(); }

            clr.add(Calendar.DATE,-i); // resume today (year - 1)
        }

        clr.add(Calendar.YEAR, 1);  // 2021


        SlidingWin s= new SlidingWin(C7days,P14days);
        fc7days= s.fc7days;
        Log.d("inlist",""+ Arrays.deepToString(fc7days));

        // future weather of list view
        for(i=0;i<7;i++){
            clr.add(Calendar.DATE, i);
            if(i==0) { days[i]="Today"; }
            else {  days[i]=date.format(clr.getTime()); }
            clr.add(Calendar.DATE,-i);

            maxTemp[i]=""+(fc7days[i][0]);
            maxTemp[i]=""+(fc7days[i][0]);
            Temp_tot[i]=maxTemp[i]+"° / "+minTemp[i]+"°C";
            Humdity[i]=fc7days[i][2]+"%";
        }
    }

    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String[] rDays,rTemp,rHumdity;

        MyAdapter(Context c, String[] Days, String[] Temp, String[] Humdity) {
            super(c, R.layout.row_item, R.id.day, Days);
            this.context = c;
            this.rDays = Days;
            this.rTemp = Temp;
            this.rHumdity = Humdity;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=layoutInflater.inflate(R.layout.row_item,parent,false);

            TextView days=row.findViewById(R.id.day);
            TextView Temp_tot = row.findViewById(R.id.max_min);
            TextView Humdity= row.findViewById(R.id.humidity);

            // resources on view
            days.setText(rDays[position]);
            Temp_tot.setText(rTemp[position]);
            Humdity.setText(rHumdity[position]);

            return row;
        }
    }
}
