package com.example.weatherapp.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;
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
    private SweetAlertDialog pDialog;
    String mode = "";
    private ImageView background;
    private Integer mListBackground [] = {R.mipmap.background_early_morning, R.mipmap.background_morning, R.mipmap.background_night_1, R.mipmap.background_night_2, R.mipmap.background_night_3, R.mipmap.background_midnight};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawer = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        mViewPage = findViewById(R.id.mViewPager);

        background = findViewById(R.id.background);
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

        Calendar rightNow = Calendar.getInstance();
        int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);

        if (currentHour >= 3 && currentHour <= 6){
            background.setBackgroundResource(mListBackground[0]);
        } else if (currentHour >= 7 && currentHour <= 18){
            background.setBackgroundResource(mListBackground[1]);
        } else if (currentHour >= 19 && currentHour >= 23){
            background.setBackgroundResource(mListBackground[new Random().nextInt(3)]+2);
        } else if (currentHour >= 0 && currentHour <= 2){
            background.setBackgroundResource(mListBackground[5]);
        }

        sqlHelper = new SQLHelper(this);
        sp = getSharedPreferences("search", Context.MODE_PRIVATE);
        editor = sp.edit();

        String lang = sp.getString("lang", "en");
        loadLocal(lang);

        mode = sp.getString("mode", "dark");
        if(mode.equals("light")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }


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


        navigationView.setItemIconTintList(null);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                Intent intent;
                switch (id) {
                    case R.id.dark_mode:
                        break;
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

                    case R.id.language:
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        View viewInflated = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_language, navigationView, false);
                        builder.setView(viewInflated);
                        final AlertDialog dialog = builder.create();

                        ImageView btnClose = viewInflated.findViewById(R.id.btnClose);
                        btnClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        RelativeLayout btnEn = viewInflated.findViewById(R.id.btnEn);
                        RelativeLayout btnJa = viewInflated.findViewById(R.id.btnJa);

                        final ImageView checkEn = viewInflated.findViewById(R.id.checkEn);
                        final ImageView checkJa = viewInflated.findViewById(R.id.checkJa);

                        if (sp.getString("lang", "en").equals("en")) {
                            checkEn.setVisibility(View.VISIBLE);
                            checkJa.setVisibility(View.INVISIBLE);

                        } else if (sp.getString("lang", "en").equals("ja")) {
                            checkJa.setVisibility(View.VISIBLE);
                            checkEn.setVisibility(View.INVISIBLE);
                        }

                        btnEn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                checkEn.setVisibility(View.VISIBLE);
                                checkJa.setVisibility(View.INVISIBLE);

                            }
                        });
                        btnJa.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                checkJa.setVisibility(View.VISIBLE);
                                checkEn.setVisibility(View.INVISIBLE);
                            }
                        });

                        TextView btnSubmit = viewInflated.findViewById(R.id.btnSubmit);
                        btnSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String lang_check = "";
                                if(checkEn.getVisibility() == View.VISIBLE){
                                    lang_check = "en";
                                } else if(checkJa.getVisibility() == View.VISIBLE){
                                    lang_check = "ja";
                                }
                                final SweetAlertDialog alertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.NORMAL_TYPE);
                                final String finalLang_check = lang_check;
                                alertDialog.setContentText("Do you want to change your language?")
                                        .setConfirmText("YES")
                                        .setCancelText("CANCEL")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                editor.putString("lang", finalLang_check);
                                                editor.commit();
                                                changeLanguage(finalLang_check);
                                                dialog.dismiss();
                                                alertDialog.dismiss();
                                            }
                                        })
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                alertDialog.dismiss();
                                            }
                                        })
                                        .showCancelButton(true)
                                        .show();


                            }
                        });
                        dialog.show();
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

            private void changeLanguage(String finalLang_check) {
            }

        });
        SwitchCompat drawerSwitch = (SwitchCompat) navigationView.getMenu().findItem(R.id.dark_mode).getActionView();
        setDayNightMode(drawerSwitch);
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


                        editor.putString("id", id);
                        editor.putString("name", name);
                        editor.commit();

                        mViewPage.setCurrentItem(0, true);


                        getSupportFragmentManager().beginTransaction().detach(homeFragment).attach(homeFragment).commit();

                        getSupportFragmentManager().beginTransaction().detach(forecastFragment).attach(forecastFragment).commit();

                    } else {
                        autoSearch.setText("");
                        Toast.makeText(getApplicationContext(), "Not existed!!\n Please reenter!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void setDayNightMode(SwitchCompat swMode){
        if(mode.equals("light")){
            swMode.setChecked(false);
        } else {
            swMode.setChecked(true);
        }
        swMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    editor.putString("mode", "dark");
                    editor.commit();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                } else {
                    editor.putString("mode", "light");
                    editor.commit();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });
    }

    public void changeLanguage(String lang){
        loadLocal(lang);

        pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pDialog.dismiss();
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, 1000);
    }
    public void loadLocal(String lang) {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale(new Locale(lang)); // API 17+ only.
        }
        res.updateConfiguration(conf, dm);
    }
}
