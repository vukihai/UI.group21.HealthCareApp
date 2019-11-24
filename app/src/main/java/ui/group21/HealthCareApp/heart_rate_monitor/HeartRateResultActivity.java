package ui.group21.HealthCareApp.heart_rate_monitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ui.group21.HealthCareApp.R;

/**
 * Kết quả đo nhịp tim. #1.4
 */
public class HeartRateResultActivity extends AppCompatActivity implements HeartRateConstant {
    RecyclerView rvUserHRStatus;
    TextView txtHrValue, txtInProgressHrValue, txtExpectedMinValue, txtExpectedMaxValue, txtMinValue, txtMaxValue;
    Button btnDiscard, btnSave;
    Guideline glExpMin, glExpMax, glCurrentHR;
    int bpmValue, expectMinValue, expectMaxValue, minValue, maxValue;

    public interface OnUserStatusChanged {
        void onChanged(int statusCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate_result);
        setTitle(getString(R.string.result));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get intent extra
        bpmValue = getIntent().getIntExtra(EXTRA_HEART_RATE_VALUE, -1);
        minValue = 40;
        maxValue = 120;
        expectMinValue = 61;
        expectMaxValue = 76;

        // bind view
        rvUserHRStatus = (RecyclerView) findViewById(R.id.rv_user_hr_status);

        txtHrValue = (TextView) findViewById(R.id.tv_heart_rate_value);
        txtInProgressHrValue = (TextView) findViewById(R.id.tv_current_hr);
        txtExpectedMinValue = (TextView) findViewById(R.id.tv_expect_hr_min);
        txtExpectedMaxValue = (TextView) findViewById(R.id.tv_expect_hr_max);
        txtMinValue = (TextView) findViewById(R.id.tv_hr_min);
        txtMaxValue = (TextView) findViewById(R.id.tv_hr_max);

        glExpMin = (Guideline) findViewById(R.id.guideline_expect_min_hr);
        glExpMax = (Guideline) findViewById(R.id.guideline_expect_max_hr);
        glCurrentHR = (Guideline) findViewById(R.id.guideline_current_hr);

        // recycler view
        UserHeartRateStatusAdapter mAdapter = new UserHeartRateStatusAdapter();
        mAdapter.setOnUserStatusChangedCallback(new OnUserStatusChanged() {
            @Override
            public void onChanged(int statusCode) {
                minValue = userHRMinValue[statusCode];
                maxValue = userHRMaxValue[statusCode];
                expectMinValue = userHRExpectedMinValue[statusCode];
                expectMaxValue = userHRExpectedMaxValue[statusCode];
                setHeartRateValue();
            }
        });
        rvUserHRStatus.setAdapter(mAdapter);
        rvUserHRStatus.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        setHeartRateValue();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_heart_rate_result_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                saveResult();
                return true;
            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
            default:
                super.onOptionsItemSelected(item);
        }
        return false;
    }

    public void saveResult() {
        Intent intent = new Intent(HeartRateResultActivity.this, HeartRateMonitorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(EXTRA_TAB_NUMBER, 1);
        startActivity(intent);
    }

    public void changeGuidelineWithAnim(final Guideline guideline, float value){
        final ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams)guideline.getLayoutParams();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(lp.guidePercent, value);
        valueAnimator.setDuration(500);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                guideline.setGuidelinePercent((float)valueAnimator.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }

    public void setHeartRateValue() {
        txtHrValue.setText("" + bpmValue);
        txtMinValue.setText("" + minValue);
        txtMaxValue.setText("" + maxValue);
        txtExpectedMinValue.setText("" + expectMinValue);
        txtExpectedMaxValue.setText("" + expectMaxValue);
        if (Math.abs(bpmValue - expectMinValue) > 2 && Math.abs(bpmValue - expectMaxValue) > 2 && Math.abs(bpmValue - minValue) > 2 && Math.abs(bpmValue - maxValue) > 2) {
            txtInProgressHrValue.setText("" + bpmValue);
        } else {
            txtInProgressHrValue.setText("");
        }
        if (bpmValue <= minValue) {
            changeGuidelineWithAnim(glCurrentHR,0.02f);
            txtMinValue.setText("");
        } else if (bpmValue >= maxValue) {
            changeGuidelineWithAnim(glCurrentHR,0.98f);
            txtMaxValue.setText("");
        } else {
            changeGuidelineWithAnim(glCurrentHR,0.04f + (float) (bpmValue - minValue) / (float) (maxValue - minValue) * 0.92f);
        }
        changeGuidelineWithAnim(glExpMin,0.04f + (float) (expectMinValue - minValue) / (float) (maxValue - minValue) * 0.92f);
        changeGuidelineWithAnim(glExpMax, 0.04f + (float) (expectMaxValue - minValue) / (float) (maxValue - minValue) * 0.92f);
    }

    /**
     * Recycler View - View Holder
     */
    public class UserHRStatusViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView text;
        ImageView image;

        public UserHRStatusViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.text = itemView.findViewById(R.id.tv_user_hr_name);
            this.image = itemView.findViewById(R.id.img_user_hr_status);
        }
    }

    /**
     * Recycler View - Adapter
     */
    public class UserHeartRateStatusAdapter extends RecyclerView.Adapter<UserHRStatusViewHolder> {

        // interface for sending user status
        OnUserStatusChanged onUserStatusChangedCallback;

        @NonNull
        @Override
        public UserHRStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_hr_status, parent, false);
            return new UserHRStatusViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull UserHRStatusViewHolder holder, final int position) {
            String text = userHRStatusText[position];
            int imgId = userHRStatusImgId[position];
            holder.text.setText(text);
            holder.image.setImageDrawable(getResources().getDrawable(userHRStatusImgId[position]));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onUserStatusChangedCallback.onChanged(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return userHRStatusText.length;
        }

        public void setOnUserStatusChangedCallback(OnUserStatusChanged onUserStatusChangedCallback) {
            this.onUserStatusChangedCallback = onUserStatusChangedCallback;
        }
    }
}
