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
import butterknife.OnPageChange;
import butterknife.OnTouch;
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
    @BindView(R.id.spinnerRecycler)
    Spinner spinnerRecycler;
    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setupViewPager(viewPager);
        tabs.setupWithViewPager(viewPager);

        spinner.setVisibility(View.VISIBLE);
        spinnerRecycler.setVisibility(View.GONE);

        mSettings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        int listSortType = mSettings.getInt("listSortType", -1);
        if (listSortType != -1)
            spinner.setSelection(listSortType);
        else
            spinner.setSelection(0);

        int recyclerSortType = mSettings.getInt("recyclerSortType", -1);
        if (recyclerSortType != -1)
            spinnerRecycler.setSelection(recyclerSortType);
        else
            spinnerRecycler.setSelection(0);
    }

    @OnPageChange(R.id.viewpager)
    public void onViewpagerPageChange(int position) {
        if (position == 0) {
            spinner.setVisibility(View.VISIBLE);
            spinnerRecycler.setVisibility(View.GONE);
        } else {
            spinnerRecycler.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt("listSortType", spinner.getSelectedItemPosition());
        editor.putInt("recyclerSortType", spinnerRecycler.getSelectedItemPosition());
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
