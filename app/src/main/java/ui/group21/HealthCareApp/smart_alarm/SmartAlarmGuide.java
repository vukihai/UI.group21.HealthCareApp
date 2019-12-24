package ui.group21.HealthCareApp.smart_alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ui.group21.HealthCareApp.R;

public class SmartAlarmGuide extends AppCompatActivity {
    private Button backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Báo thức thông minh");
        setContentView(R.layout.activity_smart_alarm_guide);
        backBtn= findViewById(R.id.ok_smart_alarm_guide);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SmartAlarmGuide.this, SmartAlarmActivity.class);
                startActivity(i);
            }
        });
    }
}
