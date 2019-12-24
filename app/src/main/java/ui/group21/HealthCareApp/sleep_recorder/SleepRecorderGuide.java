package ui.group21.HealthCareApp.sleep_recorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ui.group21.HealthCareApp.R;

public class SleepRecorderGuide extends AppCompatActivity {
     private Button okBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_recorder_guide);
        okBtn= findViewById(R.id.ok_sleep_recorder_guide);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SleepRecorderGuide.this, SleepRecorderActivity.class);
                startActivity(i);
            }
        });
    }
}
