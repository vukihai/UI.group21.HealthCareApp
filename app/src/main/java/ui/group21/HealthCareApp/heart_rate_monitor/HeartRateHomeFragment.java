package ui.group21.HealthCareApp.heart_rate_monitor;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ui.group21.HealthCareApp.R;

import static ui.group21.HealthCareApp.heart_rate_monitor.HeartRateResultActivity.changeGuidelineWithAnim;


public class HeartRateHomeFragment extends Fragment implements HeartRateConstant {
    private Button btnStart;
    private TextView txtHrDate, txtStatusName;
    private TextView txtHrValue, txtInProgressHrValue, txtExpectedMinValue, txtExpectedMaxValue, txtMinValue, txtMaxValue, txtAverageRange;
    private Guideline glExpMin, glExpMax, glCurrentHR;
    private int bpmValue, expectMinValue, expectMaxValue, minValue, maxValue;
    private Calendar hrDate;
    private String statusName;

    public HeartRateHomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_heart_rate_home2, container, false);

        txtHrDate = (TextView) v.findViewById(R.id.tv_hr_date2);
        txtStatusName = (TextView) v.findViewById(R.id.tv_hr_status_name2);
        txtHrValue = (TextView) v.findViewById(R.id.tv_heart_rate_value2);
        txtInProgressHrValue = (TextView) v.findViewById(R.id.tv_current_hr2);
        txtExpectedMinValue = (TextView) v.findViewById(R.id.tv_expect_hr_min2);
        txtExpectedMaxValue = (TextView) v.findViewById(R.id.tv_expect_hr_max2);
        txtMinValue = (TextView) v.findViewById(R.id.tv_hr_min2);
        txtMaxValue = (TextView) v.findViewById(R.id.tv_hr_max2);
        txtAverageRange = (TextView) v.findViewById(R.id.tv_hr_range_description2);

        glExpMin = (Guideline) v.findViewById(R.id.guideline_expect_min_hr2);
        glExpMax = (Guideline) v.findViewById(R.id.guideline_expect_max_hr2);
        glCurrentHR = (Guideline) v.findViewById(R.id.guideline_current_hr2);

        btnStart = v.findViewById(R.id.btn_measure_hr);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HeartRateMeasuringActivity.class);
                startActivity(intent);
            }
        });
        bpmValue = 98;
        hrDate = Calendar.getInstance();
        hrDate.set(2019, 10, 12, 12, 59);
        setStatus(0);
        return v;
    }

    public void setStatus(int statusCode) {
        minValue = userHRMinValue[statusCode];
        maxValue = userHRMaxValue[statusCode];
        expectMinValue = userHRExpectedMinValue[statusCode];
        expectMaxValue = userHRExpectedMaxValue[statusCode];
        statusName = getString(userHRStatusText[statusCode]);
        statusName = statusName.substring(0, 1).toUpperCase() + statusName.substring(1);
        setHeartRateViewItem();
    }

    public void setHeartRateViewItem() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd MMM", java.util.Locale.getDefault());
        txtHrDate.setText(simpleDateFormat.format(hrDate.getTime()));
        txtStatusName.setText(statusName);

        txtHrValue.setText("" + bpmValue);
        txtMinValue.setText("" + minValue);
        txtMaxValue.setText("" + maxValue);
        txtExpectedMinValue.setText("" + expectMinValue);
        txtExpectedMaxValue.setText("" + expectMaxValue);
        txtAverageRange.setText(String.format(getString(R.string.hr_avg_range), statusName));
        if (Math.abs(bpmValue - expectMinValue) > 3 && Math.abs(bpmValue - expectMaxValue) > 3 && Math.abs(bpmValue - minValue) > 3 && Math.abs(bpmValue - maxValue) > 3) {
            txtInProgressHrValue.setText("" + bpmValue);
        } else {
            txtInProgressHrValue.setText("");
        }
        if (bpmValue <= minValue) {
            changeGuidelineWithAnim(glCurrentHR, 0.02f);
            txtMinValue.setText("");
        } else if (bpmValue >= maxValue) {
            changeGuidelineWithAnim(glCurrentHR, 0.98f);
            txtMaxValue.setText("");
        } else {
            changeGuidelineWithAnim(glCurrentHR, 0.04f + (float) (bpmValue - minValue) / (float) (maxValue - minValue) * 0.92f);
        }
        changeGuidelineWithAnim(glExpMin, 0.04f + (float) (expectMinValue - minValue) / (float) (maxValue - minValue) * 0.92f);
        changeGuidelineWithAnim(glExpMax, 0.04f + (float) (expectMaxValue - minValue) / (float) (maxValue - minValue) * 0.92f);
    }
}
