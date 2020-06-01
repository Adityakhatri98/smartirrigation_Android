package com.example.smartcrop;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;

class MyPagerAdapter extends FragmentStatePagerAdapter {
    int tabCount;
    FragmentManager manager;
    FragmentTransaction mCurTransaction;


    public MyPagerAdapter(FragmentManager supportFragmentManager, int tabCount)
    {
        super(supportFragmentManager);
        manager=supportFragmentManager;
        mCurTransaction = manager.beginTransaction();
        this.tabCount= tabCount;
    }
    @Override
    public Fragment getItem(int position) {

        Fragment fragment=null;
        switch (position){
            case 0:
                fragment = new HomeFragment();
                return fragment;
            case 1:
                fragment= new ChartFragment();
                return fragment;
            case 2:
                fragment = new LogsFragment();
                return fragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
