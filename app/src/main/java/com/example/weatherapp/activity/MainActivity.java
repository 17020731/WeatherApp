package com.example.weatherapp.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.weatherapp.R;
import com.example.weatherapp.adapter.ViewPageAdapter;
import com.example.weatherapp.model.City;
import com.example.weatherapp.model.Database;
import com.example.weatherapp.model.SQLHelper;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {


    private ViewPager mViewPage;
    private ViewPageAdapter mPageAdapter;
    private TabLayout tabLayout;

    SQLHelper sqlHelper;

    private AutoCompleteTextView autoSearch;
    private ImageButton btnSearch;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        mViewPage = findViewById(R.id.mViewPager);

        final Fragment homeFragment = new HomeFragment();
        final Fragment forecastFragment = new ForecastFragment();
        final Fragment historyFragment = new HistoryFragment();
//        final Fragment mapFragment = new MapFragment();


        final ArrayList<Fragment> mListFragment = new ArrayList<>();
        mListFragment.add(homeFragment);
        mListFragment.add(forecastFragment);
        mListFragment.add(historyFragment);
//        mListFragment.add(mapFragment);

        mPageAdapter = new ViewPageAdapter
                (getSupportFragmentManager(), mListFragment);
        mViewPage.setAdapter(mPageAdapter);
        mViewPage.setCurrentItem(0, true);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mViewPage);

        btnSearch = findViewById(R.id.btnSearch);
        autoSearch = findViewById(R.id.edSearch);




        sqlHelper = new SQLHelper(this);
        sp = getSharedPreferences("search", Context.MODE_PRIVATE);

        Database database = new Database(this);
        Set<City> set = database.getUsersList();
        final List<City> data = new ArrayList<>();
        data.addAll(set);

        final ArrayList<String>city_name = new ArrayList<>();
        for(City c : data){
            city_name.add(c.getName());
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,R.layout.search_item, city_name);
        autoSearch.setAdapter(arrayAdapter);
        autoSearch.setThreshold(1);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = autoSearch.getText().toString().trim();
                if (!name.isEmpty()) {
                    if (city_name.indexOf(name) != -1) {
                        int index = city_name.indexOf(name);
                        String id = data.get(index).getId();
                        autoSearch.setText("");
                        editor = sp.edit();

                        editor.putString("id", id);
                        editor.putString("name", name);
                        editor.commit();

                        mViewPage.setCurrentItem(0, true);


                        getSupportFragmentManager().beginTransaction().detach(homeFragment).attach(homeFragment).commit();

                        getSupportFragmentManager().beginTransaction().detach(forecastFragment).attach(forecastFragment).commit();

                    }
                    else{
                        autoSearch.setText("");
                        Toast.makeText(getApplicationContext(),"Không có dữ liệu!!\n Mời nhập lại!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
