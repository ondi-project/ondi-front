package com.ondi.android_ondi.View.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ondi.android_ondi.R;
import com.ondi.android_ondi.View.Menu.Category.CategoryFragment;
import com.ondi.android_ondi.View.Menu.History.HistoryFragment;
import com.ondi.android_ondi.View.Menu.Home.HomeFragment;
import com.ondi.android_ondi.View.Menu.MyPage.MyPageFragment;
import com.ondi.android_ondi.View.Menu.Transaction.TransactionFragment;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HomeFragment homeFragment = new HomeFragment();
    private HistoryFragment historyFragment = new HistoryFragment();
    private TransactionFragment transactionFragment = new TransactionFragment();
    private CategoryFragment categoryFragment = new CategoryFragment();
    private MyPageFragment myPageFragment = new MyPageFragment();

    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar(); //커스텀 툴바 적용
        setFragmentManager(); //바텀 네비게이션

    }

    private void setFragmentManager() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        fragmentManager.beginTransaction().replace(R.id.layout_main_container,homeFragment).commitAllowingStateLoss();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_menu_home: {
                        fragmentManager.beginTransaction().replace(R.id.layout_main_container, homeFragment).setReorderingAllowed(true).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu_history: {
                        fragmentManager.beginTransaction().replace(R.id.layout_main_container,historyFragment).setReorderingAllowed(true).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu_transaction: {
                        fragmentManager.beginTransaction().replace(R.id.layout_main_container,transactionFragment).setReorderingAllowed(true).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu_category: {
                        fragmentManager.beginTransaction().replace(R.id.layout_main_container, categoryFragment).setReorderingAllowed(true).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu_mypage: {
                        fragmentManager.beginTransaction().replace(R.id.layout_main_container, myPageFragment).setReorderingAllowed(true).commitAllowingStateLoss();
                        break;
                    }
                }
                return true;
            }
        });
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

}