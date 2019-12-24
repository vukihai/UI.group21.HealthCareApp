package ui.group21.HealthCareApp.heart_rate_monitor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import ui.group21.HealthCareApp.R;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

/**
 * đo nhịp tim #1.3
 */
public class HeartRateMonitorActivity extends AppCompatActivity implements HeartRateConstant {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private static FloatingActionButton fab;
    public static boolean showTutorial = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate_monitor);
        setTitle(getString(R.string.heart_rate_monitor));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        fab = findViewById(R.id.fab_measure_hr);
        fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                viewPager.setCurrentItem(0);
                Intent intent = new Intent(v.getContext(), HeartRateMeasuringActivity.class);
                startActivity(intent);
            }
        });

        viewPager.setAdapter(new TabViewPager(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 1: // trend tab
                        fab.show();
                        break;
                    default:
                        fab.hide();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("Chọn trải nghiệm")
                .setMessage("Bấm Trải nghiệm để cài đặt và trải nghiệm ứng dụng hoàn thiện thực tế hoặc bấm Xem giao diện để tiếp tục xem giao diện mẫu!")
                .setCancelable(false)
                .setNegativeButton("Xem giao diện", (dialog1, which) -> {
                    dialog1.cancel();
                })
                .setPositiveButton("Trải nghiệm", (dialog1, which) -> {
                    try {
                        Intent i = new Intent();
                        i.setAction("cf.bautroixa.heartratemonitor.ACCESS");
                        startActivity(i);
                    } catch (Exception e) {
                        Dialog dldialog = new AlertDialog.Builder(this)
                                .setTitle("Tải xuống gói mở rộng")
                                .setMessage("Ứng dụng cần cài đặt gói mở rộng để có thể đo nhịp tim và thống kê, bấm Tải xuống để cài đặt gói mở rộng hoặc bấm Hủy để tiếp tục xem giao diện!")
                                .setCancelable(false)
                                .setNegativeButton("Hủy", (dialog2, which2) -> {
                                    dialog2.cancel();
                                })
                                .setPositiveButton("Tải xuống", (dialog2, which2) -> {
                                    Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse("http://bit.ly/bautroixahr"));
                                    startActivity(browse);
                                }).create();
                        dldialog.show();
                    }
                }).create();
        dialog.show();

    }

    private void showFabTutorial() {
        new MaterialTapTargetPrompt.Builder(HeartRateMonitorActivity.this)
                .setTarget(fab)
                .setPrimaryText(R.string.hr_alter_measure_btn_tutorial)
                .setPromptBackground(new MTTPCustom.DimmedCirclePromptBackground())
                .show();
        showTutorial = false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        int activeTabNumber = intent.getIntExtra(EXTRA_TAB_NUMBER, 0);
        Log.d(getLocalClassName(), "active tab= " + activeTabNumber);
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
            switch (position) {
                case 0:
                    return new HeartRateHomeFragment();
                case 1:
                    HeartRateTrendFragment trendFragment = new HeartRateTrendFragment();
                    trendFragment.setOnTutorialFinishedListener(() -> {
                        showFabTutorial();
                    });
                    return trendFragment;
                default:
                    return new HeartRateHomeFragment();
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.heart_rate_monitor);
                case 1:
                    return getString(R.string.trending);
                default:
                    return getString(R.string.trending);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
