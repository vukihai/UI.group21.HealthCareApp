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
    int setHour, setMinute;
    Boolean setCheckMusic,setCheckRung;
    private String mTxtTime;
    private Switch mSwtRung;
    private Switch mSwtMusic;
    private Boolean mCheckMusic = false;
    private Boolean mCheckRung = false;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Cài đặt báo thức");
        setContentView(R.layout.activity_setting_alarm);
        getData();
        initView();
        initAction();
    }

    private void getData() {
        setHour = getIntent().getIntExtra("setHour",99);
        setMinute = getIntent().getIntExtra("setMinute",99);
        setCheckMusic = getIntent().getBooleanExtra("setCheckMusic",false);
        setCheckRung = getIntent().getBooleanExtra("setCheckRung",false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initAction() {

        mTxtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hour = timePicker.getCurrentHour();
                minute = timePicker.getCurrentMinute();
                mCheckMusic = mSwtMusic.isChecked();
                mCheckRung = mSwtRung.isChecked();
                Intent intent = new Intent(v.getContext(), SmartAlarmActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("hour", hour);
                intent.putExtra("minute", minute);
                intent.putExtra("music",mCheckMusic);
                intent.putExtra("rung", mCheckRung);
                v.getContext().startActivity(intent);
            }
        });
        mTxtExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView() {
        timePicker = findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        mTxtExit = findViewById(R.id.txt_exit);
        mTxtSave = findViewById(R.id.txt_save);
        mSwtMusic = findViewById(R.id.swt_music);
        mSwtRung = findViewById(R.id.swt_rung);
        if(setHour != 99 && setMinute!=99){
            timePicker.setHour(setHour);
            timePicker.setMinute(setMinute);
        }
        if(setCheckMusic) mSwtMusic.setChecked(true);
        if(setCheckRung) mSwtRung.setChecked(true);
    }
}
