package ui.group21.HealthCareApp.smart_alarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import ui.group21.HealthCareApp.R;

/**
 * Báo thức thông minh 1.13 và 1.14.
 */
public class SmartAlarmActivity extends AppCompatActivity {
    FloatingActionButton add;
    private RecyclerView mRcvTime;
    AlarmAdapter mAlarmAdapter;
    private ArrayList<Alarm> mListAlarm ;
    private Dialog mAlarmGuideDialog;
    public final String SHARED_PREFERENCES_NAME = "Alarm";
    private TextView mTxtCheckList;
    private Button mBtnGuide;
    private final OnAlarmClickListener mOnItemAlarmClickListener = new OnAlarmClickListener() {
        @Override
        public void onAlarmClick(int position) {
            Intent intent = new Intent(getApplicationContext(), SettingAlarmActivity.class);
            intent.putExtra("setHour",mListAlarm.get(position).getHour());
            intent.putExtra("setMinute",mListAlarm.get(position).getMinute());
            intent.putExtra("setCheckMusic",mListAlarm.get(position).getMusic());
            intent.putExtra("setCheckRung",mListAlarm.get(position).getRung());
            startActivity(intent);
        }

        @Override
        public void onDelClick(int position) {
            mListAlarm.remove(position);
            mAlarmAdapter.notifyDataSetChanged();
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(mListAlarm);
            editor.putString("List alarm", json);
            editor.apply();
            if(mListAlarm.size()==0){
                mTxtCheckList.setVisibility(View.VISIBLE);
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Báo thức thông minh");
        setContentView(R.layout.activity_smart_alarm);
//        initData();
        loadData();
        getData();
        initView();
        initAction();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("List alarm",null);
        Type type = new TypeToken<ArrayList<Alarm>>(){}.getType();
        mListAlarm = gson.fromJson(json,type);
        if(mListAlarm == null){
            mListAlarm = new ArrayList<>();
        }
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

    private void getData() {
//        String mTime = getIntent().getStringExtra("time");
        int hour = getIntent().getIntExtra("hour",99);
        int minute = getIntent().getIntExtra("minute", 99);
        Boolean mCheckMusic = getIntent().getBooleanExtra("music", false);
        Boolean mCheckRung = getIntent().getBooleanExtra("rung", false);
        String mTime;
        if(minute<10 && hour >10){
            mTime = hour + ":0" + minute;
        }
        else if (minute<10 && hour < 10){
            mTime = "0"+hour + ":0" + minute;
        }
        else if( minute >10 && hour <10){
            mTime = "0"+hour + ":" + minute;
        }
        else  mTime = hour + ":" + minute;
        if(!mTime.equals("99:99")) {
            mListAlarm.add(new Alarm(hour, minute, mTime, mCheckMusic, mCheckRung));
        }
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mListAlarm);
        editor.putString("List alarm", json);
        editor.apply();
    }

    private void initData() {
        mListAlarm = new ArrayList<>();
        mListAlarm.add(new Alarm(4, 45,"04:45",true,true));
        mListAlarm.add(new Alarm(6, 30,"06:30",true,false));
    }

    @SuppressLint("WrongConstant")
    private void initAction() {
        mAlarmAdapter = new AlarmAdapter(this, mListAlarm);
        mAlarmAdapter.setAlarmClick(mOnItemAlarmClickListener);
        mAlarmAdapter.setDelClick(mOnItemAlarmClickListener);
        mRcvTime.setAdapter(mAlarmAdapter);
        mRcvTime.setLayoutManager(new LinearLayoutManager(SmartAlarmActivity.this, LinearLayout.VERTICAL, false));
        add.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), SettingAlarmActivity.class);
            v.getContext().startActivity(intent);
        });
        mBtnGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlarmGuideDialog.show();
            }
        });
        if(mListAlarm.size()==0){
            mTxtCheckList.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        add = findViewById(R.id.add);
        mRcvTime = findViewById(R.id.rcy_alarm);
        mAlarmGuideDialog = new Dialog(SmartAlarmActivity.this);
        mAlarmGuideDialog.setContentView(R.layout.dialog_alarm_guide);
        mBtnGuide = findViewById(R.id.btn_guide);
        mTxtCheckList = findViewById(R.id.txt_checkList);
    }

}
