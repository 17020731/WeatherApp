package com.example.weatherapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weatherapp.R;
import com.example.weatherapp.model.History;

import java.util.ArrayList;

public class HistoryAdapter extends BaseAdapter {

    private ArrayList<History> mListHistory;
    private Context mContext;
    private LayoutInflater mLayoutInflator;


    public HistoryAdapter(ArrayList<History> mListWeather, Context mContext) {
        this.mListHistory = mListWeather;
        this.mContext = mContext;
        this.mLayoutInflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mListHistory.size();
    }

    @Override
    public Object getItem(int position) {
        return mListHistory.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        if(view == null){
            view = mLayoutInflator.inflate(R.layout.history_item, null);
        }
        ImageView imgHis = view.findViewById(R.id.imgHis);
        String img_src = mListHistory.get(position).getImg();
        switch (img_src) {
            case "sun": {
                imgHis.setImageResource(R.mipmap.sun);
                break;
            }
            case "fewclouds": {
                imgHis.setImageResource(R.mipmap.fewclouds);
                break;
            }
            case "scratteredclouds": {
                imgHis.setImageResource(R.mipmap.scratteredclouds);
                break;
            }

            case "brokenclouds": {
                imgHis.setImageResource(R.mipmap.brokencloud);
                break;
            }
            case "showerrain": {
                imgHis.setImageResource(R.mipmap.showerrain);
                break;
            }
            case "rain": {
                imgHis.setImageResource(R.mipmap.rain);
                break;
            }
            case "thunderstorm": {
                imgHis.setImageResource(R.mipmap.thunderstorm);
                break;
            }
            case "snow": {
                imgHis.setImageResource(R.mipmap.snow);
                break;
            }
            case "mist": {
                imgHis.setImageResource(R.mipmap.mist);
                break;
            }
        }

        TextView tvNameCity = view.findViewById(R.id.tvNameCity);
        tvNameCity.setText(mListHistory.get(position).getName_city());

        TextView temp = view.findViewById(R.id.temp);
        temp.setText(mListHistory.get(position).getTemp()+ " °C");

        TextView des = view.findViewById(R.id.des);
        des.setText(mListHistory.get(position).getDescription());

        TextView pres = view.findViewById(R.id.pres);
        pres.setText(mListHistory.get(position).getPressure()+ " hPa");

        TextView humi = view.findViewById(R.id.humi);
        humi.setText(mListHistory.get(position).getHumidity()+ " %");

        TextView tvTime = view.findViewById(R.id.tvTime);
        tvTime.setText(mListHistory.get(position).getDate_time());


        return view;
    }
}

