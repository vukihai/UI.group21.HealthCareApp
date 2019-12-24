package ui.group21.HealthCareApp.calos_history;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import ui.group21.HealthCareApp.calos_history.CaloHistoryActivity;

import static java.lang.Integer.parseInt;

public class CaloHistoryActivity extends AppCompatActivity {
    private Button mEditTargetBtn;
    private Button mSaveBtn;
    private Button mCancelBtn;
    private TextView mCaloCountTodayTextView;
    private EditText mEditTargetValue;
    private TextView mStatusTextView;
    private Dialog mEditTargetDialog;
    private int target;
    private int Calo_count_today;
    private LineChart mCaloChart;
    private CircularProgressBar mCircleCalo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        target=5000;
        Log.d("calo","here");
        setContentView(R.layout.activity_calo_history);
        setTitle("Năng lượng tiêu thụ");
        setTitleColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mCaloChart = findViewById(R.id.historyCaloCounterChart);
        mStatusTextView=findViewById(R.id.tv_status_calo_history);
        mCaloCountTodayTextView = findViewById(R.id.num_calo_today);
        mCaloCountTodayTextView.setText(String.valueOf(Calo_count_today));
        mEditTargetDialog = new Dialog(CaloHistoryActivity.this);
        mEditTargetBtn = findViewById(R.id.change_calo_target_btn);
        mEditTargetBtn.setText(String.valueOf(target));
        mEditTargetDialog.setContentView(R.layout.dialog_edit_target_calos);
        mEditTargetValue = mEditTargetDialog.findViewById(R.id.target_calo_value_editText);
        mSaveBtn = mEditTargetDialog.findViewById(R.id.save_calo_target_action);
        mCancelBtn = mEditTargetDialog.findViewById(R.id.cancel_calo_target_action);
        mCircleCalo = findViewById(R.id.circle_calo);
// Set Progress
        mCircleCalo.setProgress(65f);
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
        float percent=Calo_count_today * 100 / target;
        float distante= (float) (Calo_count_today*0.00045);
        mCircleCalo.setProgress(percent);
        if(percent>=100) mStatusTextView.setText("Xuất sắc, bạn đã vượt mục tiêu");
        else if(percent>70) mStatusTextView.setText("Cố lên, bạn sắp hoàn thành mục tiêu");
        else mStatusTextView.setText("Cần hoạt động nhiều hơn để đốt cháy calo");
    }

    private void setChartData() {
        Date today = new Date(); // Fri Jun 17 14:54:28 PDT 2016
        Calendar cal = Calendar.getInstance();
        cal.setTime(today); // don't forget this if date is arbitrary e.g. 01-01-2014
        int dayOfMonth = 26; // 17
        final int currMonth=cal.get(Calendar.MONTH);
        Log.e("Calo_counter", String.valueOf(dayOfMonth));
        int range = 5000;
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                String[] days = generate_X_label(currMonth);
                return days[(int) value];
            }

        };
        int chartColor = Color.rgb(255, 87, 34);
        List<Entry> valsComp1 = new ArrayList<Entry>();
        for (int i = 1; i <= dayOfMonth; i++) {
            float val = (float) (Math.random() * (range + 1));
            float x = (float) (i);
            if(i==dayOfMonth){
                Calo_count_today=(int)val;
                mCaloCountTodayTextView.setText(String.valueOf(Calo_count_today));
                updateProgress();
            }
            valsComp1.add(new Entry(x, val));
        }
        LineDataSet lineDataSet = new LineDataSet(valsComp1, "Số bước chân");
        lineDataSet.setColor(chartColor);
        lineDataSet.setLineWidth(3f);
        lineDataSet.setValueTextSize(10f);
        LineData lineData = new LineData(lineDataSet);
        XAxis xAxis = mCaloChart.getXAxis();
        xAxis.setGranularity(1f);
//        xAxis.setValueFormatter(formatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        mCaloChart.getDescription().setText("Tháng này");
        mCaloChart.fitScreen();
        mCaloChart.setData(lineData);
        mCaloChart.getAxisLeft().setEnabled(false);
        mCaloChart.getAxisRight().setEnabled(false);
        mCaloChart.setDrawBorders(false);
        mCaloChart.setVisibleXRangeMinimum(6);
        mCaloChart.setVisibleXRangeMaximum(6);
        mCaloChart.moveViewToX(dayOfMonth - 6);
        mCaloChart.setActivated(false);
        mCaloChart.setAutoScaleMinMaxEnabled(true);
        mCaloChart.setScaleEnabled(false);
        mCaloChart.getLegend().setEnabled(false);
    }

    private String[] generate_X_label( int month){
        Log.d("calendar","aaaaaaaaaaaaaaaa");
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
