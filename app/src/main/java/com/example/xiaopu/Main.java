package com.example.xiaopu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.xiaopu.adapter.MyFragmentPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

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

    void setbuttonAlpha(){
        int selectedItemId = bottomNavigationView.getSelectedItemId();
        MenuItem menuItem = bottomNavigationView.getMenu().getItem(selectedItemId);
        Drawable icon = menuItem.getIcon();
        icon.setAlpha(100);
    }

}