package com.example.lenovo.memcreator.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by Lenovo on 1/19/2017.
 */

public class HomePagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> fragmentArrayList;
    public HomePagerAdapter(FragmentManager manager) {
        super(manager);
        fragmentArrayList = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }

    public void addFragment(Fragment fragment) {
        fragmentArrayList.add(fragment);
    }
}
