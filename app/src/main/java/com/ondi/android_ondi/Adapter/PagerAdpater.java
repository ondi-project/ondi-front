package com.ondi.android_ondi.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ondi.android_ondi.View.Menu.MyPage.Sale.CompleteFragment;
import com.ondi.android_ondi.View.Menu.MyPage.Sale.ProgressFragment;

public class PagerAdpater extends FragmentPagerAdapter {

    ProgressFragment progressFragment = new ProgressFragment();
    CompleteFragment completeFragment = new CompleteFragment();

    int numTabs;
    public PagerAdpater(@NonNull FragmentManager fm, int numTabs) {
        super(fm, numTabs);
        this.numTabs = numTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return progressFragment;
            case 1:
                return completeFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
