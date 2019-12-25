package ui.group21.HealthCareApp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import ui.group21.HealthCareApp.calos_history.CaloHistoryActivity;
import ui.group21.HealthCareApp.heart_rate_monitor.HeartRateMonitorActivity;
import ui.group21.HealthCareApp.route_tracker.RouteTrackerActivity;
import ui.group21.HealthCareApp.sleep_recorder.SleepRecorderActivity;
import ui.group21.HealthCareApp.smart_alarm.SmartAlarmActivity;
import ui.group21.HealthCareApp.step_counter.StepCounterActivity;

/**
 * Màn hình chính #1.1
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    DrawerLayout mDrawerLayout;
    ListView mListView;
    Button heartRateButton, stepButton, routeButton, sleepButton, smartAlarmButton, profileButton, caloButton, goalButton;
    Intent heartRateIntent, stepIntent,routeIntent,sleepIntent, smartAlarmIntent, profileIntent,caloIntent;
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
        mDrawerLayout.setBackgroundResource(R.drawable.gradient);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        if(menuItem.getItemId()==R.id.nav_profile){
                            profileIntent = new Intent(thisContext, UserProfileActivity.class);
                            startActivity(profileIntent);
                        }
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
        goalButton = findViewById(R.id.btn_goal);
        smartAlarmButton = findViewById(R.id.btn_smart_alarm);
        profileButton = findViewById(R.id.btn_long_sit_reminder);
        caloButton=findViewById(R.id.btn_calo_history);
        heartRateButton.setOnClickListener(this);
        stepButton.setOnClickListener(this);
        routeButton.setOnClickListener(this);
        sleepButton.setOnClickListener(this);
        smartAlarmButton.setOnClickListener(this);
        profileButton.setOnClickListener(this);
        goalButton.setOnClickListener(this);
        caloButton.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("main_activity",String.valueOf(item.getItemId() == R.id.nav_profile));
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
                try {
                    Intent i = new Intent();
                    i.setAction("cf.bautroixa.heartratemonitor.ACCESS");
                    startActivity(i);
                } catch (Exception e) {
                    heartRateIntent = new Intent(thisContext, HeartRateMonitorActivity.class);
                    startActivity(heartRateIntent);
                }
                break;
            case R.id.btn_goal:
                Toast.makeText(this, "Tính năng sắp ra mắt", Toast.LENGTH_SHORT).show();
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
            case R.id.btn_smart_alarm:
                smartAlarmIntent = new Intent(thisContext, SmartAlarmActivity.class);
                startActivity(smartAlarmIntent);
                break;
            case R.id.btn_long_sit_reminder:
//                profileIntent = new Intent(thisContext, UserProfileActivity.class);
//                startActivity(profileIntent);
                Toast.makeText(this, "Tính năng sắp ra mắt", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_calo_history:
                Log.d("calo","BTN CLICK CALO");
                caloIntent = new Intent(thisContext, CaloHistoryActivity.class);
                startActivity(caloIntent);
                break;
        }
    }
}
