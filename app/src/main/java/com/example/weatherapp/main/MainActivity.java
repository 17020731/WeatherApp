package com.example.weatherapp.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapp.R;
import com.example.weatherapp.charts.ForecastFragment;
import com.example.weatherapp.histories.HistoryFragment;
import com.example.weatherapp.home.HomeFragment;
import com.example.weatherapp.models.City;
import com.example.weatherapp.models.Database;
import com.example.weatherapp.models.SQLHelper;
import com.example.weatherapp.setting.SettingActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {


    private ViewPager mViewPage;
    private ViewPageAdapter mPageAdapter;
    private TabLayout tabLayout;

    SQLHelper sqlHelper;

    private AutoCompleteTextView autoSearch;
    private ImageView btnMenu, btnSearch;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private CircleImageView avatar;
    private TextView name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        final ArrayList<String> city_name = new ArrayList<>();
        for (City c : data) {
            city_name.add(c.getName());
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.search_item, city_name);
        autoSearch.setAdapter(arrayAdapter);
        autoSearch.setThreshold(1);

        drawer = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                Intent intent;
                switch (id) {

                    case R.id.home:
                        drawer.closeDrawer(Gravity.LEFT);
                        mViewPage.setCurrentItem(0);
                        break;

                    case R.id.chart:
                        drawer.closeDrawer(Gravity.LEFT);
                        mViewPage.setCurrentItem(1);
                        break;

                    case R.id.history:
                        drawer.closeDrawer(Gravity.LEFT);
                        mViewPage.setCurrentItem(2);
                        break;

                    case R.id.setting:
                        drawer.closeDrawer(Gravity.LEFT);
                        intent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.about:
                        drawer.closeDrawer(Gravity.LEFT);
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Weather Application")
                                .setMessage("Version" + ": 1.1.3\n" + "Version code" + ": 10")
                                .setPositiveButton("OK", null)
                                .show();
                        break;
                }
                return false;
            }
        });
        btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

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

                    } else {
                        autoSearch.setText("");
                        Toast.makeText(getApplicationContext(), "Không có dữ liệu!!\n Mời nhập lại!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
