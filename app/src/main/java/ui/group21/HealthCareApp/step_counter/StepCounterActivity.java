package ui.group21.HealthCareApp.step_counter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;

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
    private Dialog mEditTargetDialog;
    private int target;
    private int step_count_today;
    private BarChart mStepChart;
    private CircularProgressBar mCircleStep;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        target = 2000;
        step_count_today = 1679;

        setContentView(R.layout.activity_step_counter);
        mStepChart = findViewById(R.id.historyStepCounterChart);
        mEditTargetDialog = new Dialog(StepCounterActivity.this);
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

    private void updateProgress() {
        mCircleStep.setProgress(step_count_today * 100 / target);
    }

    private void setChartData() {
        int start = 0;
        int range = 1000;
        int count = 30;
        int chartColor=Color.rgb(116, 185, 255);
        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = start; i < start + count; i++) {
            float val = (float) (Math.random() * (range + 1));
            values.add(new BarEntry(i, val));
        }
        BarDataSet set = new BarDataSet(values,"Tháng ");
        set.setColor(chartColor);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);
        BarData data = new BarData(dataSets);
        data.setValueTextSize(0f);
//        data.setValueTypeface(tfLight);
        data.setBarWidth(0.9f);
        data.setValueTextColor(chartColor);
        mStepChart.fitScreen();
        mStepChart.setData(data);
    }
}
