package ui.group21.HealthCareApp.smart_alarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import ui.group21.HealthCareApp.R;

/**
 * Báo thức thông minh 1.13 và 1.14.
 */
public class SmartAlarmActivity extends AppCompatActivity {
    FloatingActionButton add;
    private RecyclerView mRcvTime;
    private AlarmAdapter mAlarmAdapter;
    private ArrayList<Alarm> mListAlarm = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_alarm);
        initView();
        initData();
        initAction();
    }

    private void initData() {
        mListAlarm = new ArrayList<>();
        mListAlarm.add(new Alarm("4:00","true","true"));
    }

    @SuppressLint("WrongConstant")
    private void initAction() {
        mAlarmAdapter = new AlarmAdapter(this, mListAlarm);
//        mAlarmAdapter.setAlarmClick(this);
        mRcvTime.setAdapter(mAlarmAdapter);
        mRcvTime.setLayoutManager(new LinearLayoutManager(SmartAlarmActivity.this, LinearLayout.VERTICAL, false));
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SettingAlarmActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    private void initView() {
        add = findViewById(R.id.add);
        mRcvTime = findViewById(R.id.rcy_alarm);
    }

}
