package ui.group21.HealthCareApp.sleep_recorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import ui.group21.HealthCareApp.R;

/**
 * theo dõi giấc ngủ. #1.11
 */
public class SleepRecorderActivity extends AppCompatActivity {

    private Button mStartRecordingBtn;
    private Button mStopRecordingBtn;
    private Button mHistoryBtn;
    private LineChart mSleepingTimelineChart;

    private final BroadcastReceiver mLastHalfBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            float[] data = intent.getFloatArrayExtra("DATA");
            updateSleepingTimelineChart(generateLineData(data));
        }
    };

    private final BroadcastReceiver mQuarterAvgsBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    private final View.OnClickListener mStartRecordingBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startRecording();
        }
    };

    private final View.OnClickListener mStopRecordingBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            stopRecording();
        }
    };

    private final View.OnClickListener mHistoryBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_recorder);
        initViews();
        initActions();
    }

    private void initViews() {
        mStartRecordingBtn         = findViewById(R.id.buttonStartRecording);
        mStopRecordingBtn          = findViewById(R.id.buttonStopRecording);
        mHistoryBtn                = findViewById(R.id.buttonHistory);

        mSleepingTimelineChart     = findViewById(R.id.chartSleepingTimeline);

        mSleepingTimelineChart.setViewPortOffsets(0f, 0f, 0f, 0f);

        mSleepingTimelineChart.getAxisLeft().setDrawAxisLine(false);
        mSleepingTimelineChart.getAxisLeft().setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

        mSleepingTimelineChart.getAxisRight().setDrawGridLines(false);
        mSleepingTimelineChart.getAxisRight().setDrawAxisLine(false);
        mSleepingTimelineChart.getAxisRight().setDrawLabels(false);

        mSleepingTimelineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        mSleepingTimelineChart.getXAxis().setDrawGridLines(false);
        mSleepingTimelineChart.getXAxis().setDrawAxisLine(false);

        mSleepingTimelineChart.getDescription().setEnabled(false);
        mSleepingTimelineChart.getLegend().setEnabled(false);

        findViewById(R.id.linearStartAndHistoryContainer).setVisibility(View.VISIBLE);
        findViewById(R.id.linearStopContainer).setVisibility((View.INVISIBLE));
    }

    private void initActions() {
        IntentFilter lastHalfFilter = new IntentFilter();
        lastHalfFilter.addAction(SleepRecorderService.ACTION_GET_REALTIME_DATA);
        LocalBroadcastManager.getInstance(this).registerReceiver(mLastHalfBroadcastReceiver, lastHalfFilter);

        IntentFilter quarterAvgsFilter = new IntentFilter();
        quarterAvgsFilter.addAction(SleepRecorderService.ACTION_GET_AVGS_DATA);
        LocalBroadcastManager.getInstance(this).registerReceiver(mQuarterAvgsBroadcastReceiver, quarterAvgsFilter);

        mStartRecordingBtn.setOnClickListener(mStartRecordingBtnOnClickListener);
        mStopRecordingBtn.setOnClickListener(mStopRecordingBtnOnClickListener);
        mHistoryBtn.setOnClickListener(mHistoryBtnOnClickListener);
    }

    private void startRecording() {
        findViewById(R.id.linearStartAndHistoryContainer).setVisibility(View.INVISIBLE);
        findViewById(R.id.linearStopContainer).setVisibility((View.VISIBLE));
        startService(new Intent(this, SleepRecorderService.class));
    }

    private void stopRecording() {
        findViewById(R.id.linearStartAndHistoryContainer).setVisibility(View.VISIBLE);
        findViewById(R.id.linearStopContainer).setVisibility((View.INVISIBLE));
        stopService(new Intent(this, SleepRecorderService.class));
    }

    private void updateSleepingTimelineChart(LineData lineData) {
        mSleepingTimelineChart.setData(lineData);
        mSleepingTimelineChart.invalidate();
    }

    private LineData generateLineData(float[] data) {
        List<Entry> entries = new ArrayList<>();
        for(int i = 0; i < data.length; ++i) {
            entries.add(new Entry((float) i, data[i]));
        }
        LineDataSet dataSet = new LineDataSet(entries, "Timeline");
        dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);
        dataSet.setDrawFilled(true);
        if (Utils.getSDKInt() >= 18) {
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.sleeping_depth_gradient);
            dataSet.setFillDrawable(drawable);
        } else {
            dataSet.setFillColor(Color.BLACK);
        }
        return new LineData(dataSet);
    }
}