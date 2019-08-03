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
    private static int COUNT = 5;

    //Đối tượng biểu đồ combined
    private CombinedChart mChart;

    //Tên thành phố
    private TextView tvName;


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

        View v = inflater.inflate(R.layout.fragment_forecast, container, false);


        tvName = v.findViewById(R.id.tvName);

        mChart = v.findViewById(R.id.combinedChart);
        //Todo: Set up cho CombinedChart
        mChart.getDescription().setEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setHighlightFullBarEnabled(false);
        mChart.setOnChartValueSelectedListener(this);

        //Todo: Tạo đối tượng sharePreference để lấy tên thành phố khi ta search ở MainActivity
        sharedPreferences = getActivity().getSharedPreferences("search", Context.MODE_PRIVATE);



        //Todo: Set up tên thành phố, và get dữ liệu
        tvName.setText("City: " + sharedPreferences.getString("name","Hanoi"));
        new HttpWeatherCity().execute(sharedPreferences.getString("id","1581130"));

        return v;
    }

    //Todo: Các hàm implement bắt buộc (không sử dụng)
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



    //Phương thức để định dạng JSON "dt_txt" về calendar
    public static Calendar getTime(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

        Calendar calendar = null;
        try {
            Date date = sdf.parse(time);
            calendar = Calendar.getInstance();
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    //Tạo biểu đồ đường Line Chart
    private LineData generateLineChart(ArrayList<Weather>arr)  {

        LineData d = new LineData();

        int data[] = new int [arr.size()];
        for(int i = 0; i < arr.size(); i++){
            data[i] = Integer.parseInt(arr.get(i).getTemperature());
        }

        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int index = 0; index < COUNT; index++) {
            entries.add(new Entry(index, data[index]));
        }

        LineDataSet set = new LineDataSet(entries, "Temperature");
        set.setColor(Color.RED);
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.RED);
        set.setCircleRadius(5f);
        set.setFillColor(Color.RED);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.RED);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }

    public class HttpWeatherCity extends AsyncTask<String, Void , String> {


        HttpRequest request = new HttpRequest();    //Đối tượng để thực hiện kết nối và lấy dữ liệu
        ArrayList<Weather> arr = new ArrayList<>();     //mảng Weather trong 5 ngày /3 giờ

        HttpWeatherCity(){
        }

        @Override
        protected String doInBackground(String... params) {
            //Todo: Đường dẫn lấy dữ liệu dự báo trong 5 ngày/ 3 giờ
            String url = "https://api.openweathermap.org/data/2.5/forecast?id="+params[0]+"&units=metric&appid=211ff006de9aba9ddd122331f87cdf8b";
            // Nhận kết quả trả về từ đường dẫn
            String response = request.sendGet(url);

            return response;
        }

        @Override
        protected void onPostExecute(String response){

            //TODO: Xử lí JSON
            try {
                JSONObject jsonObject = new JSONObject(response);

                //Todo: Lấy dữ liệu
                JSONArray fullJson = jsonObject.getJSONArray("list");
                for(int i = 0; i < fullJson.length(); i++){

                    Weather weather = new Weather();

                    //Todo: Lấy tên thành phố
                    JSONObject cityJson = jsonObject.getJSONObject("city");
                    //Todo: Set text tên thành phố
                    weather.setCity(cityJson.getString("name"));

                    JSONObject listJson = fullJson.getJSONObject(i);
                    JSONObject mainJson = listJson.getJSONObject("main");


                    //Todo: Lấy nhiệt độ (Đơn vị: Celcius)
                    Double celcius = Double.parseDouble(mainJson.getString("temp"));
                    weather.setTemperature(Math.round(celcius)+"");
//                    System.out.println(Math.round(celcius)+ "°C");


                    //Todo: Lấy áp suất (Đơn vị: hPa)
                    weather.setPressure(mainJson.getString("pressure"));
//                    System.out.println(mainJson.getString("pressure"));


                    //Todo: Lấy độ ẩm (Đơn vị: %)
                    weather.setHumidity(mainJson.getString("humidity"));
//                    System.out.println(mainJson.getString("humidity"));


                    //Todo: Lấy lượng mưa (mm) (nếu không có trong responese (rain=0))
                    try {
                        weather.setRain(listJson.getJSONObject("rain").getString("3h"));
//                        System.out.println(listJson.getJSONObject("rain").getString("3h"));
                    }
                    catch(JSONException e){
                        weather.setRain(0+"");
//                        System.out.println(0);
                    }

//                    //Todo: Lấy date và time
                    String date = listJson.getString("dt_txt");
                    Calendar c = getTime(date);
                    weather.setDate(c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.MONTH)+1);

                    weather.setTime(c.get(Calendar.HOUR_OF_DAY)+ ":"+c.get(Calendar.MINUTE));
//
                    //Todo: Thêm weather vào mảng weather
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
            for(int i = 0; i < COUNT; i++){
                //Todo: Xử lí số liệu 12h
                if(arr.get(i).getTime().equals("0:0") && arr.get(i+1).getTime().equals("15:0")){
                    xLabel.add("12:0");
                }
                else
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

}
