package com.example.weatherapp.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weatherapp.R;
import com.example.weatherapp.model.HttpRequest;
import com.example.weatherapp.model.Weather;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ForecastFragment extends Fragment implements OnChartValueSelectedListener {
    private static String TAG = "ForecastFragment";

    //Đối tượng biểu đồ combined
    private CombinedChart mChart;

    //
    private TextView tvName;

    private Button btnReload;
    SharedPreferences sharedPreferences;

    public ForecastFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_forecast, container, false);


        tvName = v.findViewById(R.id.tvName);

        mChart = v.findViewById(R.id.combinedChart);
        mChart.getDescription().setEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setHighlightFullBarEnabled(false);
        mChart.setOnChartValueSelectedListener(this);


        sharedPreferences = getActivity().getSharedPreferences("search", Context.MODE_PRIVATE);


        tvName.setText("City: " + sharedPreferences.getString("name","Hanoi"));
        new HttpWeatherCity().execute(sharedPreferences.getString("id","1581130"));
//        new HttpWeatherCity().execute("1581130");
//        tvName.setText("City: " + "Hanoi");

        return v;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
//        Toast.makeText(this, "Value: "
//                + e.getY()
//                + ", index: "
//                + h.getX()
//                + ", DataSet index: "
//                + h.getDataSetIndex(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {


    }

    //Format parameter "dt_txt" JSON from API
    public static Calendar getTime(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

        Calendar calendar = null;
        try {
            Date date = sdf.parse(time);
            calendar = Calendar.getInstance();
            calendar.setTime(date);
//            System.out.println(calendar.get(Calendar.DAY_OF_MONTH));
//            System.out.println(calendar.get(Calendar.MONTH)+1);
//            System.out.println(calendar.get(Calendar.YEAR));
//            System.out.println(calendar.get(Calendar.HOUR));
//            System.out.println(calendar.get(Calendar.MINUTE));
//            System.out.println(calendar.get(Calendar.SECOND));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }


    public class HttpWeatherCity extends AsyncTask<String, Void , String> {

        HttpRequest request = new HttpRequest();
        ArrayList<Weather> arr = new ArrayList<>();
        HttpWeatherCity(){
        }

        @Override
        protected String doInBackground(String... params) {
            String url = "https://api.openweathermap.org/data/2.5/forecast?id="+params[0]+"&units=metric&appid=211ff006de9aba9ddd122331f87cdf8b";
            String response = request.sendGet(url);
//            System.out.println(response);
            return response;
        }

        @Override
        protected void onPostExecute(String response){
            try {
                JSONObject jsonObject = new JSONObject(response);

                //Todo: Get orther para
                JSONArray fullJson = jsonObject.getJSONArray("list");
                for(int i = 0; i < fullJson.length(); i++){

                    Weather weather = new Weather();

                    //Todo: Get para city
                    JSONObject cityJson = jsonObject.getJSONObject("city");
                    weather.setCity(cityJson.getString("name"));

                    JSONObject listJson = fullJson.getJSONObject(i);
                    JSONObject mainJson = listJson.getJSONObject("main");


                    //Todo: Get para temperature (set Celcius)
                    Double celcius = Double.parseDouble(mainJson.getString("temp"));
                    weather.setTemperature(Math.round(celcius)+"");
//                    System.out.println(Math.round(celcius)+ "°C");


                    //Todo: Get para pressure
                    weather.setPressure(mainJson.getString("pressure"));
//                    System.out.println(mainJson.getString("pressure"));


                    //Todo: Get para hudimity
                    weather.setHumidity(mainJson.getString("humidity"));
//                    System.out.println(mainJson.getString("humidity"));


                    //Todo: Get parameter rain (mm) forecast weather (if not parameter rain (=0))
                    try {
                        weather.setRain(listJson.getJSONObject("rain").getString("3h"));
//                        System.out.println(listJson.getJSONObject("rain").getString("3h"));
                    }
                    catch(JSONException e){
                        weather.setRain(0+"");
//                        System.out.println(0);
                    }

//                    //Todo: Get parameters date and time forecast weather
                    String date = listJson.getString("dt_txt");
                    Calendar c = getTime(date);
                    weather.setDate(c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.MONTH)+1);
//                  System.out.println(c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.MONTH)+1);

                    weather.setTime(c.get(Calendar.HOUR_OF_DAY)+ ":"+c.get(Calendar.MINUTE));
//                  System.out.println(c.get(Calendar.HOUR)+ ":"+c.get(Calendar.MINUTE));
//
//                    System.out.println(weather.toString());
                    arr.add(weather);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Tạo trục tung bên phải
            YAxis rightAxis = mChart.getAxisRight();
            rightAxis.setDrawGridLines(false);
            rightAxis.setAxisMinimum(0f);
            rightAxis.setStartAtZero(false);

            //Tạo trục tung bên trái
            YAxis leftAxis = mChart.getAxisLeft();
            leftAxis.setDrawGridLines(false);
            leftAxis.setAxisMinimum(0f);
            leftAxis.setStartAtZero(false);

            //Tạo các nhãn cho trục hoành
            final List<String> xLabel = new ArrayList<>();
            for(int i = 0; i < 5; i++){
                xLabel.add(arr.get(i).getTime());
            }

            //Tạo trục hoành
            XAxis xAxis = mChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setAxisMinimum(0f);
            xAxis.setGranularity(1f);
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return xLabel.get((int) value % xLabel.size());
                }
            });


            CombinedData data = new CombinedData();

            //Thêm dạng biểu đồ cho Combined Chart( ví dụ: ta thêm biểu đồ đường (Line Chart) như bên dưới)
            data.setData(generateLineChart(arr));

            xAxis.setAxisMaximum(data.getXMax() + 0.25f);

            mChart.setData(data);
            mChart.invalidate();
        }
    }
    //Tạo biểu đồ đường Line Chart
    private LineData generateLineChart(ArrayList<Weather>arr)  {

        LineData d = new LineData();

        int data[] = new int [arr.size()];
        for(int i = 0; i < arr.size(); i++){
            data[i] = Integer.parseInt(arr.get(i).getTemperature());
        }

        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int index = 0; index < 5; index++) {
            entries.add(new Entry(index, data[index]));
        }

        LineDataSet set = new LineDataSet(entries, "Temperature");
        set.setColor(Color.GREEN);
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.GREEN);
        set.setCircleRadius(5f);
        set.setFillColor(Color.GREEN);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.GREEN);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }
}
