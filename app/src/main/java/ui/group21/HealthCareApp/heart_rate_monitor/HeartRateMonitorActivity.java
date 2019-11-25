package ui.group21.HealthCareApp.heart_rate_monitor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

import ui.group21.HealthCareApp.R;

/**
 * đo nhịp tim #1.3
 */
public class HeartRateMonitorActivity extends AppCompatActivity implements HeartRateConstant{

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate_monitor);
        setTitle(getString(R.string.heart_rate_monitor));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        viewPager.setAdapter(new TabViewPager(getSupportFragmentManager()));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        int activeTabNumber = intent.getIntExtra(EXTRA_TAB_NUMBER, 0);
        Log.d(getLocalClassName(), "active tab= "+activeTabNumber);
        viewPager.setCurrentItem(activeTabNumber);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class TabViewPager extends FragmentPagerAdapter {

        public TabViewPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0: return new HeartRateHomeFragment();
                case 1: return new HeartRateTrendFragment();
                default: return new HeartRateHomeFragment();
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0: return getString(R.string.heart_rate_monitor);
                case 1: return getString(R.string.trending);
                default: return getString(R.string.trending);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
