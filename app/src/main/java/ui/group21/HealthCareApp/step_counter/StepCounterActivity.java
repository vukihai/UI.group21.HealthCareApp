package ui.group21.HealthCareApp.step_counter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ui.group21.HealthCareApp.R;

import static java.lang.Integer.parseInt;

/**
 * đếm bước. #1.5 và #1.6
 */
public class StepCounterActivity extends AppCompatActivity {
    private Button mEditTargetBtn;
    private Button mSaveBtn;
    private Button mCancelBtn;
    private TextView mStepCountTodayTextView;
    private EditText mEditTargetValue;
    private TextView mStatusTextView;
    private TextView mTimeWalkTextView;
    private TextView mCalosTextView;
    private TextView mDistanceTextView;
    private Dialog mEditTargetDialog;
    private int target;
    private int step_count_today;
    private LineChart mStepChart;
    private CircularProgressBar mCircleStep;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Đếm bước");
        setTitleColor(Color.BLACK);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        target = 2000;
        setContentView(R.layout.activity_step_counter);
        mStepChart = findViewById(R.id.historyStepCounterChart);
        mEditTargetDialog = new Dialog(StepCounterActivity.this);
        mCalosTextView=findViewById(R.id.tv_calos_step_counter);
        mTimeWalkTextView= findViewById(R.id.tv_time_walk_step_counter);
        mDistanceTextView=findViewById(R.id.tv_distance_step_counter);
        mStatusTextView=findViewById(R.id.tv_status_step_counter);
        mStepCountTodayTextView = findViewById(R.id.num_step_today);
        mStepCountTodayTextView.setText(String.valueOf(step_count_today));
        mEditTargetBtn = findViewById(R.id.change_target_btn);
        mEditTargetBtn.setText(String.valueOf(target));
        mEditTargetDialog.setContentView(R.layout.dialog_edit_target_step);
        mEditTargetValue = mEditTargetDialog.findViewById(R.id.target_value_editText);
        mSaveBtn = mEditTargetDialog.findViewById(R.id.save_target_action);
        mCancelBtn = mEditTargetDialog.findViewById(R.id.cancel_target_action);
        mCircleStep = findViewById(R.id.circle_step);
// Set Progress
        mCircleStep.setProgress(65f);
        mEditTargetBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mEditTargetValue.setText(String.valueOf(target));
                        mEditTargetDialog.show();
                    }
                }
        );
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditTargetDialog.dismiss();
                target = parseInt(mEditTargetValue.getText().toString());
                mEditTargetBtn.setText(String.valueOf(target));
                updateProgress();
            }
        });
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditTargetDialog.dismiss();
            }
        });
        setChartData();

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

    private void updateProgress() {
        float percent=step_count_today * 100 / target;
        float distante= (float) (step_count_today*0.00045);
        mTimeWalkTextView.setText(String.format("%.2f",distante/5).concat( " giờ"));
        mCalosTextView.setText(String.format("%.2f",step_count_today*0.85/40).concat( " kcalos"));
        mDistanceTextView.setText(String.format("%.2f",distante).concat(" km"));
        mCircleStep.setProgress(percent);
        if(percent>=100) mStatusTextView.setText("Xuất sắc, bạn đã vượt mục tiêu");
        else if(percent>70) mStatusTextView.setText("Cố lên, bạn sắp hoàn thành mục tiêu");
        else mStatusTextView.setText("Cần đi lại nhiều hơn");
    }

    private void setChartData() {
        Date today = new Date(); // Fri Jun 17 14:54:28 PDT 2016
        Calendar cal = Calendar.getInstance();
        cal.setTime(today); // don't forget this if date is arbitrary e.g. 01-01-2014
        int dayOfMonth =26; // 17
        Log.e("step_counter", String.valueOf(dayOfMonth));
        int range = 5000;
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                String[] days = generate_X_label(10);
                return days[(int) value];
            }

        };
        int chartColor = Color.rgb(116, 185, 255);
        List<Entry> valsComp1 = new ArrayList<Entry>();
        for (int i = 1; i <= dayOfMonth; i++) {
            float val = (float) (Math.random() * (range + 1));
            float x = (float) (i);
            if(i==dayOfMonth){
                step_count_today=(int)val;
                mStepCountTodayTextView.setText(String.valueOf(step_count_today));
                updateProgress();
            }
            valsComp1.add(new Entry(x, val));
        }
        LineDataSet lineDataSet = new LineDataSet(valsComp1, "Số bước chân");
        lineDataSet.setColor(chartColor);
        lineDataSet.setLineWidth(3f);
        lineDataSet.setValueTextSize(10f);
        LineData lineData = new LineData(lineDataSet);
        XAxis xAxis = mStepChart.getXAxis();
        xAxis.setGranularity(1f);
//        xAxis.setValueFormatter(formatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        mStepChart.getDescription().setText("Tháng này");
        mStepChart.fitScreen();
        mStepChart.setData(lineData);
        mStepChart.getAxisLeft().setEnabled(false);
        mStepChart.getAxisRight().setEnabled(false);
        mStepChart.setDrawBorders(false);
        mStepChart.setVisibleXRangeMinimum(6);
        mStepChart.setVisibleXRangeMaximum(6);
        mStepChart.moveViewToX(dayOfMonth - 6);
        mStepChart.setActivated(false);
        mStepChart.setAutoScaleMinMaxEnabled(true);
        mStepChart.setScaleEnabled(false);
        mStepChart.getLegend().setEnabled(false);
    }

    private String[] generate_X_label( int month){
        int size;
        if(month==2) size=28;
        else if(month==1 || month==3 || month==5||month==7||month==8||month==10||month==12) size=31;
        else size=30;
        String[] value= new String[size+1];
        for(int i=1; i<=size;i++){
            value[i]=String.valueOf(i);
        }

        return value;
    }
}
