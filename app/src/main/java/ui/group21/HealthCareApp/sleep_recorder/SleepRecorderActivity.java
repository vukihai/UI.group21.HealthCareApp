package ui.group21.HealthCareApp.sleep_recorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ui.group21.HealthCareApp.R;

/**
 * theo dõi giấc ngủ. #1.11
 */
public class SleepRecorderActivity extends AppCompatActivity {
    private Button mSleepRecordingButton;
    private Button mGuideButton;
    private BarChart mSleepStaticChart;

    private boolean isRecording = false;

    private final int DAYS = 16;

    private final View.OnClickListener mSleepRecordingButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            isRecording = !isRecording;
            if(isRecording) {
                mSleepRecordingButton.setText("Tạm dừng");
                startRecording();
            } else {
                mSleepRecordingButton.setText("Bắt đầu");
                stopRecording();
            }
        }
    };

    private final View.OnClickListener mGuideButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showGuide();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Theo dõi giấc ngủ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_sleep_recorder);
        initViews();
        initActions();
        drawChart();
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

    private void initViews() {
        mSleepRecordingButton = findViewById(R.id.sleepRecordingButton);
        mGuideButton = findViewById(R.id.guideButton);
        mSleepStaticChart = findViewById(R.id.sleepStaticChart);
    }

    private void initActions() {
        mGuideButton.setOnClickListener(mGuideButtonOnClickListener);
        mSleepRecordingButton.setOnClickListener(mSleepRecordingButtonOnClickListener);
        
        isRecording = isServiceRunning();
        if(isRecording) {
            mSleepRecordingButton.setText("Tạm dừng");
        } else {
            mSleepRecordingButton.setText("Bắt đầu");
        }
    }

    private void drawChart() {
        final ArrayList<String> xLabels = new ArrayList<>();
        final List<BarEntry> entries = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        int today = cal.get(Calendar.DAY_OF_MONTH);

        for (int i = 1; i < today; ++i) {
            float nremValue = (float) (Math.random() * ((50 - 10) + 1)) + 10;
            float remValue = (float) (Math.random() * ((30 - 20) + 1)) + 20;
            float wakeUpValue = (float) (Math.random() * ((20 - 10) + 1)) + 10;
            BarEntry stackedEntry = new BarEntry(i, new float[] {nremValue, remValue, wakeUpValue});
            xLabels.add((new Integer(i)).toString() + "/" + (new Integer(cal.get(Calendar.MONTH))).toString());
            entries.add(stackedEntry);
        }

        BarDataSet set = new BarDataSet(entries, "");
        set.setStackLabels(new String[]{"Ngủ sâu", "Ngủ nông", "Tỉnh táo"});
        set.setDrawIcons(false);
        int[] colors = new int[3];
        System.arraycopy(ColorTemplate.MATERIAL_COLORS, 0, colors, 0, 3);
        set.setColors(colors);

        BarData data = new BarData(set);

        mSleepStaticChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        mSleepStaticChart.getXAxis().setDrawGridLines(false);
        mSleepStaticChart.getXAxis().setDrawAxisLine(false);
        mSleepStaticChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xLabels));

        mSleepStaticChart.getAxisLeft().setDrawAxisLine(false);
        mSleepStaticChart.getAxisLeft().setDrawGridLines(false);

        mSleepStaticChart.getAxisRight().setDrawAxisLine(false);
        mSleepStaticChart.getAxisRight().setDrawGridLines(false);
        mSleepStaticChart.getAxisRight().setDrawLabels(false);

        mSleepStaticChart.getDescription().setEnabled(false);


        mSleepStaticChart.setFitBars(true);

        mSleepStaticChart.setData(data);
        mSleepStaticChart.invalidate();
    }

    private void startRecording() {
        startService(new Intent(this, SleepRecorderService.class));
    }

    private void stopRecording() {
        stopService(new Intent(this, SleepRecorderService.class));
    }

    private void showGuide() {
        startActivity(new Intent(this, SleepRecorderGuide.class));
    }



    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (SleepRecorderService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


}