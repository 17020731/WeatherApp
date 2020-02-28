package com.example.weatherapp.main;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPageAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mListFragment;
    public ViewPageAdapter(FragmentManager fm, ArrayList<Fragment> mListFragment) {
        super(fm);
        this.mListFragment = mListFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return this.mListFragment.get(position);
    }

    @Override
    public int getCount() {
        return this.mListFragment.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Home";
        } else if (position == 1) {
            return "Forecast";
        }else if(position == 2) {
            return "History";
        }else {
            return "Map";
        }
    }
}
