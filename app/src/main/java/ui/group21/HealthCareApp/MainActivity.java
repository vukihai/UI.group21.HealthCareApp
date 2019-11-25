package ui.group21.HealthCareApp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;

import ui.group21.HealthCareApp.heart_rate_monitor.HeartRateMonitorActivity;
import ui.group21.HealthCareApp.route_tracker.RouteTrackerActivity;
import ui.group21.HealthCareApp.sleep_recorder.SleepRecorderActivity;
import ui.group21.HealthCareApp.step_counter.StepCounterActivity;

/**
 * Màn hình chính #1.1
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    DrawerLayout mDrawerLayout;
    Button heartRateButton, stepButton, routeButton, sleepButton;
    Intent heartRateIntent, stepIntent,routeIntent,sleepIntent;
    Context thisContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thisContext = this;
        // toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        actionbar.setTitle("");

        initView();

        //drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    private void initView() {
        heartRateButton = findViewById(R.id.btn_heart_rate);
        stepButton = findViewById(R.id.btn_step);
        routeButton = findViewById(R.id.btn_route);
        sleepButton = findViewById(R.id.btn_sleep);

        heartRateButton.setOnClickListener(this);
        stepButton.setOnClickListener(this);
        routeButton.setOnClickListener(this);
        sleepButton.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        //start activity listener for button click
        switch (v.getId()){
            case R.id.btn_heart_rate:
                heartRateIntent = new Intent(thisContext, HeartRateMonitorActivity.class);
                startActivity(heartRateIntent);
                break;
            case R.id.btn_step:
                stepIntent = new Intent(thisContext, StepCounterActivity.class);
                startActivity(stepIntent);
                break;
            case R.id.btn_route:
                routeIntent = new Intent(thisContext, RouteTrackerActivity.class);
                startActivity(routeIntent);
                break;
            case R.id.btn_sleep:
                sleepIntent = new Intent(thisContext, SleepRecorderActivity.class);
                startActivity(sleepIntent);
                break;
        }
    }
}
