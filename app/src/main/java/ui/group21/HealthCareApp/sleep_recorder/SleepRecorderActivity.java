package ui.group21.HealthCareApp.sleep_recorder;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;

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

    private View.OnClickListener mStartRecordingBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startRecording();
        }
    };

    private View.OnClickListener mStopRecordingBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            stopRecording();
        }
    };

    private View.OnClickListener mHistoryBtnOnClickListener = new View.OnClickListener() {
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

    private LineData generateLineData() {
        int MAX_TIME = 60; // in second
        List<Entry> entries = new ArrayList<Entry>();
        for(int i = 0; i < MAX_TIME; ++i) {
            entries.add(new Entry((float) i, (float) (Math.random() * 100)));
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
        mStartRecordingBtn.setOnClickListener(mStartRecordingBtnOnClickListener);
        mStopRecordingBtn.setOnClickListener(mStopRecordingBtnOnClickListener);
        mHistoryBtn.setOnClickListener(mHistoryBtnOnClickListener);
    }

    private void startRecording() {
        findViewById(R.id.linearStartAndHistoryContainer).setVisibility(View.INVISIBLE);
        findViewById(R.id.linearStopContainer).setVisibility((View.VISIBLE));

        updateSleepingTimelineChart(generateLineData());
    }

    private void stopRecording() {
        findViewById(R.id.linearStartAndHistoryContainer).setVisibility(View.VISIBLE);
        findViewById(R.id.linearStopContainer).setVisibility((View.INVISIBLE));
    }

    private void updateSleepingTimelineChart(LineData lineData) {
        mSleepingTimelineChart.setData(lineData);
        mSleepingTimelineChart.invalidate();
    }
}
