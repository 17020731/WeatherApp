package com.example.weatherapp.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weatherapp.R;
import com.example.weatherapp.adapter.HistoryAdapter;
import com.example.weatherapp.model.History;
import com.example.weatherapp.model.SQLHelper;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    private ListView mLvData;
    private HistoryAdapter mAdapter;
    private ArrayList<History> mListHistory;

    private SQLHelper sqlHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        mLvData = v.findViewById(R.id.mLvHis);
        setdata();  //Lấy dữ liệu từ SQL cho mListHistory;
        mAdapter = new HistoryAdapter(mListHistory, getActivity());
        mLvData.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        return v;
    }

    //Phương thức lấy data History từ Database
    public void setdata(){
        sqlHelper = new SQLHelper(getActivity());
        mListHistory = sqlHelper.getArrayHistory();
    }
}
