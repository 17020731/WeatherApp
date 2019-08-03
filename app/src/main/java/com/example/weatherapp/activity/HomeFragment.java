package com.example.weatherapp.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weatherapp.R;
import com.example.weatherapp.model.History;
import com.example.weatherapp.model.HttpRequest;
import com.example.weatherapp.model.SQLHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

public class HomeFragment extends Fragment {
    HttpRequest request = new HttpRequest();
    SQLHelper sqlHelper;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    private TextView tvCity, tvDes, tvNow, tvTemp, tvPres, tvHumi, tvWind;  //Dữ liệu thời tiết hiện tại
    private ImageView imgNowWeather;    //Hình ảnh hiện thị thời tiết hiện tại



    private TextView date1, date2, date3, date4, date5; //Hiện thị 5 ngày tiếp theo (ví dụ 7/7, 8/7, 9/7 ...)
    private TextView day1, day2, day3, day4, day5;      //Hiện thị các thứ trong tuần (ví dụ MON, TUE, WED,...)
    private TextView temp1, temp2, temp3, temp4, temp5; //Hiện thị nhiệt độ 5 ngày tiếp theo
    private ImageView imgWeather1, imgWeather2, imgWeather3, imgWeather4, imgWeather5;      //Hiện thị hình ảnh 5 ngày


    public HomeFragment (){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        tvCity = v.findViewById(R.id.tvCity);
        tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlHelper.deleteAll();
            }
        });
        tvDes = v.findViewById(R.id.tvDes);
        tvTemp = v.findViewById(R.id.tvTemp);
        tvPres = v.findViewById(R.id.tvPres);
        tvHumi = v.findViewById(R.id.tvHumi);
        tvWind = v.findViewById(R.id.tvWind);
        tvNow = v.findViewById(R.id.tvNow);
        imgNowWeather = v.findViewById(R.id.imgNowWeather);

        date1 = v.findViewById(R.id.date1);
        date2 = v.findViewById(R.id.date2);
        date3 = v.findViewById(R.id.date3);
        date4 = v.findViewById(R.id.date4);
        date5 = v.findViewById(R.id.date5);

        day1 = v.findViewById(R.id.day1);
        day2 = v.findViewById(R.id.day2);
        day3 = v.findViewById(R.id.day3);
        day4 = v.findViewById(R.id.day4);
        day5 = v.findViewById(R.id.day5);

        temp1 = v.findViewById(R.id.temp1);
        temp2 = v.findViewById(R.id.temp2);
        temp3 = v.findViewById(R.id.temp3);
        temp4 = v.findViewById(R.id.temp4);
        temp5 = v.findViewById(R.id.temp5);

        imgWeather1 = v.findViewById(R.id.imgWeather1);
        imgWeather2 = v.findViewById(R.id.imgWeather2);
        imgWeather3 = v.findViewById(R.id.imgWeather3);
        imgWeather4 = v.findViewById(R.id.imgWeather4);
        imgWeather5 = v.findViewById(R.id.imgWeather5);


        //Set up thời gian
        update_datetime();
        sharedPreferences = getActivity().getSharedPreferences("search", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        sqlHelper = new SQLHelper(getActivity());

        new HttpWeatherCity().execute(sharedPreferences.getString("id", "1581130"));
//        new HttpWeatherCity().execute("1581130");

        return v;

    }


    public ImageView formatWeatherImageView (int i){
        ImageView v = null;
        if(i == 1){
            v = imgWeather1;
        }
        if(i == 2){
            v = imgWeather2;
        }
        if(i == 3){
            v = imgWeather3;
        }
        if(i == 4){
            v = imgWeather4;
        }
        if(i == 5){
            v = imgWeather5;
        }
        return v;
    }

    public TextView formatTempTextView(int i){
        TextView tv = null;
        if(i == 1){
            tv = temp1;
        }
        if(i == 2){
            tv = temp2;
        }
        if(i == 3){
            tv = temp3;
        }
        if(i == 4){
            tv = temp4;
        }
        if(i == 5){
            tv = temp5;
        }
        return tv;
    }
    //format Day of Week (ex. Monday = "MON", Tuesday = "TUE")
    public String formatDayOfWeek(int day){
        String date = "";
        switch (day){
            case 1:
                date = "SUN";
                break;
            case 2:
                date = "MON";
                break;
            case 3:
                date = "TUE";
                break;
            case 4:
                date = "WED";
                break;
            case 5:
                date = "THU";
                break;
            case 6:
                date = "FRI";
                break;
            case 7:
                date = "SAT";
                break;
        }
        return date;
    }

    //Set DateTime for next 5 days
    public void update_datetime(){
//        Calendar c = getTime("2019-07-07 11:52:30");
        Calendar c = Calendar.getInstance();
        tvNow.setText(c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1));

        c.add(Calendar.DAY_OF_YEAR, 1);


        day1.setText(formatDayOfWeek(c.get(Calendar.DAY_OF_WEEK)));
        date1.setText(c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1));
//
        c.add(Calendar.DAY_OF_YEAR, 1);
        day2.setText(formatDayOfWeek(c.get(Calendar.DAY_OF_WEEK)));
        date2.setText(c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1));
//
        c.add(Calendar.DAY_OF_YEAR, 1);
        day3.setText(formatDayOfWeek(c.get(Calendar.DAY_OF_WEEK)));
        date3.setText(c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1));
//
        c.add(Calendar.DAY_OF_YEAR, 1);
        day4.setText(formatDayOfWeek(c.get(Calendar.DAY_OF_WEEK)));
        date4.setText(c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1));
//
        c.add(Calendar.DAY_OF_YEAR, 1);
        day5.setText(formatDayOfWeek(c.get(Calendar.DAY_OF_WEEK)));
        date5.setText(c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1));

    }




    // Theo ten thanh pho
    public class HttpWeatherCity extends AsyncTask<String, Void, String> {


        public HttpWeatherCity(){

        }
        @Override
        protected String doInBackground(String... params) {


            String urlCurrent = "https://api.openweathermap.org/data/2.5/weather?id="+params[0]+"&units=metric&appid=211ff006de9aba9ddd122331f87cdf8b";
            String urlDaily = "https://api.openweathermap.org/data/2.5/forecast/daily?id="+params[0]+"&units=metric&appid=211ff006de9aba9ddd122331f87cdf8b&cnt=6";

            String response1 = request.sendGet(urlCurrent);
            String response2 = request.sendGet(urlDaily);
            String response = response1 +"\n" + response2;

            return response;
        }

        @Override
        protected void onPostExecute(String response) {

            int index = response.indexOf("\n");
            String response1 = response.substring(0, index);
            String response2 = response.substring(index+1);

            History history = new History();
            sharedPreferences = getActivity().getSharedPreferences("search", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            try {
                Calendar c = Calendar.getInstance();
                history.setDate_time(c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"\n"
                        +c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE));

                //todo:Put TIME
                editor.putString("time", c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"\n"
                        +c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE));
                JSONObject jsonObject = new JSONObject(response1);
                String nameCity = jsonObject.getString("name");
                history.setName_city(nameCity);
                tvCity.setText(nameCity);

                //todo:Put CITY
                editor.putString("city", nameCity);
//                System.out.println(nameCity);


                //Todo: Lấy latiude và longtiude

                JSONObject jsonCoor = jsonObject.getJSONObject("coord");
//                App.LAT = jsonCoor.getString("lat");
//                App.LON = jsonCoor.getString("lon");
                editor.putString("lat", jsonCoor.getString("lat"));
                editor.putString("lon", jsonCoor.getString("lon"));
                //Todo:Lấy mô tả thời tiết

                JSONArray weatherJson = jsonObject.getJSONArray("weather");
                JSONObject weather = weatherJson.getJSONObject(0);
                String weatherDes = weather.getString("description");
                history.setDescription(weatherDes);
                tvDes.setText(weatherDes);

                //todo:Put DES
                editor.putString("des", weatherDes);

//                System.out.println(weatherDes);

                //Todo: Lấy nhiệt độ

                JSONObject mainJson = jsonObject.getJSONObject("main");
                String temp = mainJson.getString("temp");
                double d1 = Double.parseDouble(temp);
                int i1 = (int) d1;
                history.setTemp(i1);
                tvTemp.setText(i1+"°C");

                //todo:Put TEMP
                editor.putInt("temp", i1);
//                System.out.println(i1);

                //Todo: Lấy áp suất

                String pressure = mainJson.getString("pressure");


                double d2 = Double.parseDouble(pressure);
                int i2 = (int) d2;
                history.setPressure(i2);
                tvPres.setText(i2+" hPa");

                //todo:Put PRESSSURE
                editor.putInt("pres", i2);
//                System.out.println(i2);

                //Todo: Lấy độ ẩm

                String humidity = mainJson.getString("humidity");


                double d3 = Double.parseDouble(humidity);
                int i3 = (int) d3;
                history.setHumidity(i3);
                tvHumi.setText(" " + i3+" %");

                //todo: Put Humidity
                editor.putInt("humi", i3);
//                System.out.println(i3);

                //Todo: Lấy tốc độ gió

                JSONObject windJson = jsonObject.getJSONObject("wind");
                String speedWind = windJson.getString("speed");

                double d4 = Double.parseDouble(speedWind);
                int i4 = (int) d4;

                tvWind.setText(i4+" m/s");
//                System.out.println(i4);

                //Todo: Đọc ảnh thời tiết
                String weatherIcon = weather.getString("icon");
//                System.out.println(weatherIcon);
                String weatherImg = "";
                switch (weatherIcon){
                    case "01d":
                    case "01n": {
                        weatherImg = "sun";
                        imgNowWeather.setImageResource(R.mipmap.sun);
                        break;
                    }
                    case "02d":
                    case "02n":{
                        weatherImg = "fewclouds";
                        imgNowWeather.setImageResource(R.mipmap.fewclouds);
                        break;
                    }
                    case "03d":
                    case "03n":{
                        weatherImg = "scratteredclouds";
                        imgNowWeather.setImageResource(R.mipmap.scratteredclouds);
                        break;
                    }
                    case "04d":
                    case "04n":{
                        weatherImg = "brokenclouds";
                        imgNowWeather.setImageResource(R.mipmap.brokencloud);
                        break;
                    }
                    case "09d":
                    case "09n":{
                        weatherImg = "showerrain";
                        imgNowWeather.setImageResource(R.mipmap.showerrain);
                        break;
                    }
                    case "10d":
                    case "10n":{
                        weatherImg = "rain";
                        imgNowWeather.setImageResource(R.mipmap.rain);
                        break;
                    }
                    case "11d":
                    case "11n":{
                        weatherImg = "thunderstorm";
                        imgNowWeather.setImageResource(R.mipmap.thunderstorm);
                        break;
                    }
                    case "13d":
                    case "13n":{
                        weatherImg = "snow";
                        imgNowWeather.setImageResource(R.mipmap.snow);
                        break;
                    }
                    case "50d":
                    case "50n":{
                        weatherImg = "mist";
                        imgNowWeather.setImageResource(R.mipmap.mist);
                        break;
                    }

                }
                history.setImg(weatherImg);
                editor.putString("img", weatherImg);
                editor.commit();

                sqlHelper.insertHistory(history);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {

                JSONObject jsonObject = new JSONObject(response2);

                JSONArray listJson = jsonObject.getJSONArray("list");
                for(int i = 1; i < listJson.length(); i++){
                    JSONObject weatherJson = listJson.getJSONObject(i);
                    JSONObject tempJson = weatherJson.getJSONObject("temp");

                    Double celcius = Double.parseDouble(tempJson.getString("day"));
                    formatTempTextView(i).setText(Math.round(celcius)+ "°C");

//                    System.out.println(tempJson.getString("day"));

                    JSONArray weatherArr = weatherJson.getJSONArray("weather");
                    JSONObject weather = weatherArr.getJSONObject(0);
                    //Todo: Đọc ảnh thời tiết
                    String weatherIcon = weather.getString("icon");
//                    System.out.println(weatherIcon);
                    switch (weatherIcon){
                        case "01d":
                        case "01n": {

                            formatWeatherImageView(i).setImageResource(R.mipmap.sun);
                            break;
                        }
                        case "02d":
                        case "02n":{
                            formatWeatherImageView(i).setImageResource(R.mipmap.fewclouds);
                            break;
                        }
                        case "03d":
                        case "03n":{
                            formatWeatherImageView(i).setImageResource(R.mipmap.scratteredclouds);
                            break;
                        }
                        case "04d":
                        case "04n":{
                            formatWeatherImageView(i).setImageResource(R.mipmap.brokencloud);
                            break;
                        }
                        case "09d":
                        case "09n":{
                            formatWeatherImageView(i).setImageResource(R.mipmap.showerrain);
                            break;
                        }
                        case "10d":
                        case "10n":{
                            formatWeatherImageView(i).setImageResource(R.mipmap.rain);
                            break;
                        }
                        case "11d":
                        case "11n":{
                            formatWeatherImageView(i).setImageResource(R.mipmap.thunderstorm);
                            break;
                        }
                        case "13d":
                        case "13n":{
                            formatWeatherImageView(i).setImageResource(R.mipmap.snow);
                            break;
                        }
                        case "50d":
                        case "50n":{
                            formatWeatherImageView(i).setImageResource(R.mipmap.mist);
                            break;
                        }
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
