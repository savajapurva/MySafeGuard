package com.example.loginscreen;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by abdalla on 2/18/18.
 */

public class PageAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

    PageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AddContactsFragement();
            case 1:
                return new SavedContactsFragement();
            case 2:
                return new HistoryFragment();
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return numOfTabs;
    }
}

