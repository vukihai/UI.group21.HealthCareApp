package ui.group21.HealthCareApp.heart_rate_monitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ui.group21.HealthCareApp.R;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

/**
 * Kết quả đo nhịp tim. #1.4
 */
public class HeartRateResultActivity extends AppCompatActivity implements HeartRateConstant {
    RecyclerView rvUserHRStatus;
    TextView txtHrValue, txtInProgressHrValue, txtExpectedMinValue, txtExpectedMaxValue, txtMinValue, txtMaxValue, txtAverageRange;
    EditText editNotes;
    Guideline glExpMin, glExpMax, glCurrentHR;
    int bpmValue, expectMinValue, expectMaxValue, minValue, maxValue;
    String statusName;
    public static boolean showTutorial = true;

    public interface OnUserStatusChanged {
        void onChanged(int statusCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate_result);
        setTitle(getString(R.string.result));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        // get intent extra
        bpmValue = getIntent().getIntExtra(EXTRA_HEART_RATE_VALUE, -1);
        minValue = 40;
        maxValue = 120;
        expectMinValue = 61;
        expectMaxValue = 76;

        // bind view
        rvUserHRStatus = (RecyclerView) findViewById(R.id.rv_user_hr_status);
        editNotes = (EditText) findViewById(R.id.edit_notes);

        txtHrValue = (TextView) findViewById(R.id.tv_heart_rate_value);
        txtInProgressHrValue = (TextView) findViewById(R.id.tv_current_hr);
        txtExpectedMinValue = (TextView) findViewById(R.id.tv_expect_hr_min);
        txtExpectedMaxValue = (TextView) findViewById(R.id.tv_expect_hr_max);
        txtMinValue = (TextView) findViewById(R.id.tv_hr_min);
        txtMaxValue = (TextView) findViewById(R.id.tv_hr_max);
        txtAverageRange = (TextView) findViewById(R.id.tv_hr_range_description);

        glExpMin = (Guideline) findViewById(R.id.guideline_expect_min_hr);
        glExpMax = (Guideline) findViewById(R.id.guideline_expect_max_hr);
        glCurrentHR = (Guideline) findViewById(R.id.guideline_current_hr);

        // recycler view
        UserHeartRateStatusAdapter mAdapter = new UserHeartRateStatusAdapter();
        mAdapter.setOnUserStatusChangedCallback(statusCode -> {
            minValue = userHRMinValue[statusCode];
            maxValue = userHRMaxValue[statusCode];
            expectMinValue = userHRExpectedMinValue[statusCode];
            expectMaxValue = userHRExpectedMaxValue[statusCode];
            statusName = getString(userHRStatusText[statusCode]);
            setHeartRateViewItem();
        });
        rvUserHRStatus.setAdapter(mAdapter);
        rvUserHRStatus.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        setHeartRateViewItem();
        if (showTutorial){
            tutorialChooseStatus();
        }
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

    public static void changeGuidelineWithAnim(final Guideline guideline, float value){
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

    public void setHeartRateViewItem() {
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

    public void tutorialChooseStatus(){
        new MaterialTapTargetPrompt.Builder(this)
                .setTarget(rvUserHRStatus)
                .setPrimaryText(R.string.hr_status_tutorial)
                .setPromptBackground(new MTTPCustom.DimmedRectPromptBackground())
                .setPromptFocal(new RectanglePromptFocal())
                .setPromptStateChangeListener((prompt, state) -> {
                    if (state == MaterialTapTargetPrompt.STATE_DISMISSED || state == MaterialTapTargetPrompt.STATE_FINISHED){
                        tutorialHRRange();
                    }
                })
                .show();
    }

    public void tutorialHRRange(){
        new MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.progress_hr_status_range)
                .setPrimaryText(R.string.hr_status_range_tutorial)
                .setPromptBackground(new MTTPCustom.DimmedCirclePromptBackground())
                .setPromptStateChangeListener((prompt, state) -> {
                    if (state == MaterialTapTargetPrompt.STATE_DISMISSED || state == MaterialTapTargetPrompt.STATE_FINISHED){
                        tutorialWriteNotes();
                    }
                })
                .show();
    }

    public void tutorialWriteNotes(){
        new MaterialTapTargetPrompt.Builder(this)
                .setTarget(editNotes)
                .setPrimaryText(R.string.hr_notes_tutorial)
                .setPromptBackground(new MTTPCustom.DimmedRectPromptBackground())
                .setPromptFocal(new RectanglePromptFocal())
                .setPromptStateChangeListener((prompt, state) -> {
                    if (state == MaterialTapTargetPrompt.STATE_DISMISSED || state == MaterialTapTargetPrompt.STATE_FINISHED){
                        tutorialSave();
                    }
                })
                .show();
    }

    public void tutorialSave(){
        new MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.menu_save)
                .setPrimaryText(R.string.hr_measure_save_tutorial)
                .setPromptBackground(new MTTPCustom.DimmedCirclePromptBackground())
                .show();
        showTutorial = false;
    }

    /**
     * Recycler View - View Holder
     */
    public class UserHRStatusViewHolder extends RecyclerView.ViewHolder {
        View itemView, circleHRStatus;
        TextView text;
        ImageView image;

        public UserHRStatusViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.circleHRStatus = itemView.findViewById(R.id.circle_item_hr_status);
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
        int selected = 0;

        @NonNull
        @Override
        public UserHRStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_hr_status, parent, false);
            return new UserHRStatusViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final UserHRStatusViewHolder holder, final int position) {
            String text = getResources().getString(userHRStatusText[position]);
            text = text.substring(0, 1).toUpperCase() + text.substring(1);
            int imgId = userHRStatusImgId[position];
            holder.text.setText(text);
            holder.image.setImageDrawable(getResources().getDrawable(userHRStatusImgId[position]));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onUserStatusChangedCallback.onChanged(position);
                    notifyItemChanged(position, Payload.CHECKED_BACKGROUND_COLOR);
                    notifyItemChanged(selected, Payload.UNCHECKED_BACKGROUND_COLOR);
                    selected = position;
                }
            });
            onUserStatusChangedCallback.onChanged(0);
        }

        @Override
        public void onBindViewHolder(@NonNull UserHRStatusViewHolder holder, int position, @NonNull List<Object> payloads) {
            if (!payloads.isEmpty()){
                for (Object payload : payloads){
                    if (Payload.CHECKED_BACKGROUND_COLOR == (int)payload){
                        holder.circleHRStatus.setActivated(true);
                    } else if (Payload.UNCHECKED_BACKGROUND_COLOR == (int)payload){
                        holder.circleHRStatus.setActivated(false);
                    }
                }
            } else {
                super.onBindViewHolder(holder, position, payloads);
            }
        }

        @Override
        public int getItemCount() {
            return userHRStatusText.length;
        }

        public void setOnUserStatusChangedCallback(OnUserStatusChanged onUserStatusChangedCallback) {
            this.onUserStatusChangedCallback = onUserStatusChangedCallback;
        }
    }

    public static class Payload {
        public static int CHECKED_BACKGROUND_COLOR = 1;
        public static int UNCHECKED_BACKGROUND_COLOR = 0;
    }
}
