package ui.group21.HealthCareApp.sleep_recorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

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

    private final BroadcastReceiver mQuadterAvgsBroadcastReceiver = new BroadcastReceiver() {
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

    private LineData generateLineData(float[] data) {
        List<Entry> entries = new ArrayList<>();
        for(int i = 0; i < data.length; ++i) {
            entries.add(new Entry((float) i, data[i]));
        }
        LineDataSet dataSet = new LineDataSet(entries, "Timeline");
        return new LineData(dataSet);
    }

    private void initViews() {
        mStartRecordingBtn         = findViewById(R.id.buttonStartRecording);
        mStopRecordingBtn          = findViewById(R.id.buttonStopRecording);
        mHistoryBtn                = findViewById(R.id.buttonHistory);
        mSleepingTimelineChart     = findViewById(R.id.chartSleepingTimeline);

        mSleepingTimelineChart.setDrawGridBackground(false);
        mSleepingTimelineChart.setBackgroundColor(Color.BLACK);
        mSleepingTimelineChart.setDrawBorders(false);

        findViewById(R.id.linearStartAndHistoryContainer).setVisibility(View.VISIBLE);
        findViewById(R.id.linearStopContainer).setVisibility((View.INVISIBLE));
    }

    private void initActions() {
        IntentFilter lastHalfFilter = new IntentFilter();
        lastHalfFilter.addAction("LAST_HALF_SLEEPING_DATA");
        LocalBroadcastManager.getInstance(this).registerReceiver(mLastHalfBroadcastReceiver, lastHalfFilter);

        IntentFilter quadterAvgsFilter = new IntentFilter();
        quadterAvgsFilter.addAction("QUADTER_AVGS_SLEEPING_DATA");
        LocalBroadcastManager.getInstance(this).registerReceiver(mQuadterAvgsBroadcastReceiver, quadterAvgsFilter);

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
}