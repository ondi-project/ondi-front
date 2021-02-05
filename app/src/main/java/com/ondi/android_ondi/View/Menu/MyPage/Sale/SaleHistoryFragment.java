package com.ondi.android_ondi.View.Menu.MyPage.Sale;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.ondi.android_ondi.Adapter.PagerAdpater;
import com.ondi.android_ondi.R;

public class SaleHistoryFragment extends Fragment {
    View mainView;
    public SaleHistoryFragment() {
        // Required empty public constructor
    }

    public static SaleHistoryFragment newInstance(String param1, String param2) {
        SaleHistoryFragment fragment = new SaleHistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_sale_history, container, false);
        initView();
        return  mainView;
    }

    private void initView() {
        TabLayout tabs = mainView.findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("판매중"));
        tabs.addTab(tabs.newTab().setText("판매완료"));
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);

        ViewPager viewPager = mainView.findViewById(R.id.viewpager_sale);
        PagerAdapter pagerAdapter = new PagerAdpater(getActivity().getSupportFragmentManager(), 2);
        viewPager.setAdapter(pagerAdapter);

        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

    }
}