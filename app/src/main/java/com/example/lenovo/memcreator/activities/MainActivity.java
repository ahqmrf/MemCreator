package com.example.lenovo.memcreator.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.lenovo.memcreator.R;
import com.example.lenovo.memcreator.adapters.HomePagerAdapter;
import com.example.lenovo.memcreator.fragments.CreateMemoryFragment;
import com.example.lenovo.memcreator.fragments.FeedsFragment;
import com.example.lenovo.memcreator.fragments.GalleryFragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private int[] tabIconResIds = {R.drawable.ic_memory_white_48dp,
            R.drawable.ic_note_add_white_48dp,
            R.drawable.ic_photo_album_white_48dp};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.setDrawerListener(mToggle);
        mToggle.syncState();

        mViewPager = (ViewPager) findViewById(R.id.pager);
        setUpViewPager(mViewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);

        for (int position = 0; position < mTabLayout.getTabCount(); position++) {
            mTabLayout.getTabAt(position).setIcon(tabIconResIds[position]);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpViewPager(ViewPager viewPager) {
        HomePagerAdapter adapter = new HomePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FeedsFragment());
        adapter.addFragment(new CreateMemoryFragment());
        adapter.addFragment(new GalleryFragment());
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showProfile(MenuItem item) {
        mDrawerLayout.closeDrawers();
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    public void showSettings(MenuItem item) {
        mDrawerLayout.closeDrawers();
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    public void exitApp(MenuItem item) {
        System.exit(0);
    }
}
