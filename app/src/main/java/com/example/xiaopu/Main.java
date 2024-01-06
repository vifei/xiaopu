package com.example.xiaopu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.xiaopu.adapter.MyFragmentPagerAdapter;
import com.example.xiaopu.bean.Talks;
import com.example.xiaopu.utils.HttpUtils;
import com.example.xiaopu.utils.TalksDbOpenHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Main extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private BottomNavigationView bottomNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPager();
        initBottomNavigationView();


    }

    private void initPager() {
        viewPager2 = findViewById(R.id.id_viewpager);
        bottomNavigationView = findViewById(R.id.nav_view);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new Index());
        fragments.add(new Talk());
        fragments.add(new Personality());

        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), getLifecycle(), fragments);
        viewPager2.setAdapter(myFragmentPagerAdapter);

        // 监听 ViewPager2 页面切换事件
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                // 根据页面的切换更新 BottomNavigationView 的选中状态
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }
        });
    }

    private void initBottomNavigationView() {


        // 设置 BottomNavigationView 的选中监听器
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_item1)
                {
                    viewPager2.setCurrentItem(0, true);
                    return true;
                } else if (item.getItemId() == R.id.navigation_item2) {
                    viewPager2.setCurrentItem(1, true);
                    return true;
                } else if (item.getItemId() == R.id.navigation_item3) {
                    viewPager2.setCurrentItem(2, true);
                    return true;
                }
                else {
                    return false;
                }
            }
        });

    }



}