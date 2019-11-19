package ui.group21.HealthCareApp.step_counter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

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
private CircularProgressBar mCircleStep;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        target=2000;
        step_count_today=1679;

        setContentView(R.layout.activity_step_counter);
        mEditTargetDialog = new Dialog(StepCounterActivity.this);
        mStepCountTodayTextView=findViewById(R.id.num_step_today);
        mStepCountTodayTextView.setText(String.valueOf(step_count_today));
        mEditTargetBtn = findViewById(R.id.change_target_btn);
        mEditTargetBtn.setText(String.valueOf(target));
        mEditTargetDialog.setContentView(R.layout.dialog_edit_target_step);
        mEditTargetValue=mEditTargetDialog.findViewById(R.id.target_value_editText);
        mSaveBtn=mEditTargetDialog.findViewById(R.id.save_target_action);
        mCancelBtn=mEditTargetDialog.findViewById(R.id.cancel_target_action);
        mCircleStep= findViewById(R.id.circle_step);
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
                target= parseInt(mEditTargetValue.getText().toString());
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

    }

    private void updateProgress(){
        mCircleStep.setProgress(step_count_today*100/target);
    }
}
