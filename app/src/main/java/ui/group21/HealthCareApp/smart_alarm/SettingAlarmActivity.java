package ui.group21.HealthCareApp.smart_alarm;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import ui.group21.HealthCareApp.R;

public class SettingAlarmActivity extends AppCompatActivity {
    TimePicker timePicker;
    private TextView mTxtExit;
    private TextView mTxtSave;
    int hour, minute;
    private String mTxtTime;
    private Switch mSwtRung;
    private Switch mSwtMusic;
    private Boolean mCheckMusic = false;
    private Boolean mCheckRung = false;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_alarm);
        initView();
        initAction();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initAction() {

        mTxtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hour = timePicker.getCurrentHour();
                minute = timePicker.getCurrentMinute();
                mTxtTime = hour + ":" + minute;
                if(mSwtMusic.isChecked()) mCheckMusic = true;
                else mCheckMusic = false;
                if(mSwtRung.isChecked()) mCheckRung = true;
                else mCheckRung = false;
                Log.d("âdada", mTxtTime +" ? " +mCheckMusic+" ? "+mCheckRung );
                Intent intent = new Intent(v.getContext(), SmartAlarmActivity.class);
                intent.putExtra("time", mTxtTime);
                intent.putExtra("music",mCheckMusic);
                intent.putExtra("rung", mCheckRung);
                v.getContext().startActivity(intent);
            }
        });
    }

    private void initView() {
        timePicker = findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        mTxtExit = findViewById(R.id.txt_exit);
        mTxtSave = findViewById(R.id.txt_save);
        mSwtMusic = findViewById(R.id.swt_music);
        mSwtRung = findViewById(R.id.swt_rung);

    }
}
