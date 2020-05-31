package com.ayeshapp.spends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;

public class StatisticsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        tabLayout = findViewById(R.id.tablayout_statistics);
        viewPager = findViewById(R.id.viewpager_statistics);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentWeekly(),"Weekly");
        adapter.addFragment(new FragmentMonthly(),"Monthly");
        adapter.addFragment(new FragmentYearly(),"Yearly");
        adapter.addFragment(new FragmentPeriodically(),"Periodically");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

    }
}