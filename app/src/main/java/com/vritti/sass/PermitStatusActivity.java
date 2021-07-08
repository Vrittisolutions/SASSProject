package com.vritti.sass;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.vritti.sass.fragment.ActivePermitFragment;
import com.vritti.sass.fragment.CancelledPermitFragment;
import com.vritti.sass.fragment.CompletedPermitFragment;
import com.vritti.sass.fragment.PendingPermitFragment;
import com.vritti.sass.model.CommonClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sharvari on 30-Nov-18.
 */

public class PermitStatusActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permit_status);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setLogo(R.mipmap.ic_logo);
        setSupportActionBar(toolbar);

       // CommonClass.hideKeyboard(PermitStatusActivity.this);

        Intent intent = getIntent();

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

       // viewPager.setOffscreenPageLimit(3);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        View root = tabLayout.getChildAt(0);

        if (root instanceof LinearLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);/*SHOW_DIVIDER_MIDDLE*/
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(getResources().getColor(R.color.green));
            drawable.setSize(1, 1);
            ((LinearLayout) root).setDividerPadding(0);
            ((LinearLayout) root).setDividerDrawable(drawable);
        }
    }
    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new ActivePermitFragment(), "Active");
        adapter.addFrag(new CancelledPermitFragment(), "Cancelled");
        adapter.addFrag(new CompletedPermitFragment(), "Completed");
        adapter.addFrag(new PendingPermitFragment(), "Pending");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
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

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
