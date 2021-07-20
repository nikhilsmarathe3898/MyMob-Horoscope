package com.example.mymob_horoscope;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.mymob_horoscope.Fragment.GeneralFragment;
import com.example.mymob_horoscope.Fragment.SystemFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class HomePageMain extends AppCompatActivity {

    private TabLayout tabLayout;
    Toolbar toolbar;
    public ViewPager viewPager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_main);

        getSupportActionBar().hide();
        toolbar = findViewById(R.id.tb_toolbar);
        toolbar.inflateMenu(R.menu.menu);
        viewPager = findViewById(R.id.vp_ViewPagerMain);
        addTabs(viewPager);

        tabLayout = findViewById(R.id.tl_tabLayoutMain);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void addTabs(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFrag(new GeneralFragment(), "GENERAL");
        viewPagerAdapter.addFrag(new SystemFragment(), "SYSTEM");
        viewPagerAdapter.addFrag(new SystemFragment(), "SOC");
        viewPagerAdapter.addFrag(new SystemFragment(), "MEMORY");
        viewPagerAdapter.addFrag(new SystemFragment(), "CAMERA");
        viewPagerAdapter.addFrag(new SystemFragment(), "BATTERY");
        viewPagerAdapter.addFrag(new SystemFragment(), "THERMAL");
        viewPagerAdapter.addFrag(new SystemFragment(), "SENSORS");
        viewPagerAdapter.addFrag(new SystemFragment(), "APPLICATION");
        viewPagerAdapter.addFrag(new SystemFragment(), "PARTITIONS");
        viewPagerAdapter.addFrag(new SystemFragment(), "NET");
        viewPager.computeScroll();
        viewPager.setAdapter(viewPagerAdapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}