package com.ondi.android_ondi.View.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ondi.android_ondi.R;
import com.ondi.android_ondi.View.Menu.Category.CategoryFragment;
import com.ondi.android_ondi.View.Menu.History.HistoryFragment;
import com.ondi.android_ondi.View.Menu.Home.HomeFragment;
import com.ondi.android_ondi.View.Menu.MyPage.MyPageFragment;
import com.ondi.android_ondi.View.Menu.Register.RegisterFragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HomeFragment homeFragment = new HomeFragment();
    private HistoryFragment historyFragment = new HistoryFragment();
    private RegisterFragment registerFragment = new RegisterFragment();
    private CategoryFragment categoryFragment = new CategoryFragment();
    private MyPageFragment myPageFragment = new MyPageFragment();
    private BottomNavigationView bottomNavigationView;

    private static final int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar(); //커스텀 툴바 적용
        setFragmentManager(); //바텀 네비게이션

    }

    private void setFragmentManager() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        fragmentManager.beginTransaction().replace(R.id.layout_main_container, homeFragment).commitAllowingStateLoss();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_menu_home: {
                        fragmentManager.beginTransaction().replace(R.id.layout_main_container, homeFragment).setReorderingAllowed(true).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu_history: {
                        fragmentManager.beginTransaction().replace(R.id.layout_main_container, historyFragment).setReorderingAllowed(true).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu_transaction: {
                        fragmentManager.beginTransaction().replace(R.id.layout_main_container, registerFragment).setReorderingAllowed(true).commitAllowingStateLoss();
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
        actionBar.setHomeAsUpIndicator(R.drawable.icon_ionic_ios_arrow_back);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "사진 최대 12장 선택 가능"), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                ArrayList<Uri> uriArrayList = new ArrayList<>();
                ArrayList<Bitmap> bitmapList = new ArrayList<>();
                if (data.getClipData() != null) { // 사진 여러개 선택한 경우
                    int count = data.getClipData().getItemCount();
                    if (count > 12) {
                        Toast.makeText(this, "사진은 최대 12장 선택 가능합니다.", Toast.LENGTH_SHORT);
                        return;
                    }
                    for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                        //첫번째 선택 이미지를 썸네일 이미지로
                        try {
                            InputStream in;
                            in = getContentResolver().openInputStream(data.getClipData().getItemAt(i).getUri());
                            Bitmap img = BitmapFactory.decodeStream(in);
                            in.close();
                            bitmapList.add(img);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        uriArrayList.add(imageUri);
                        //이미지 들고있다가 등록 버튼 누를 시 서버에 저장.
                    }
                    registerFragment.setImageView(bitmapList, data.getClipData().getItemCount());
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
        }
    }


}