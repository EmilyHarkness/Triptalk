package com.example.emily.triptalk.Login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.emily.triptalk.MainActivity;
import com.example.emily.triptalk.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemSelected;
import retrofit2.Converter;

/**
 * Created by emily on 22.06.2017.
 */

public class UsersActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.spinner)
    Spinner spinner;
    SharedPreferences mSettings;
    int sortType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setupViewPager(viewPager);
        tabs.setupWithViewPager(viewPager);

        mSettings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        sortType = mSettings.getInt("sortType", -1);
        if (sortType != -1)
            spinner.setSelection(sortType);
        else
            spinner.setSelection(0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        sortType = spinner.getSelectedItemPosition();
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt("sortType", sortType);
        editor.apply();
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new UsersListFragment(), "ListView");
        adapter.addFragment(new UsersRecyclerFragment(), "RecyclerView");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
