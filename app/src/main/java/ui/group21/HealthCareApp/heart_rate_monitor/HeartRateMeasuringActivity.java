package ui.group21.HealthCareApp.heart_rate_monitor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import ui.group21.HealthCareApp.R;

public class HeartRateMeasuringActivity extends AppCompatActivity {

    private Button btnStop;
    private Handler handler;
    private Runnable job;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate_measuring);
        setTitle(getString(R.string.heart_rate_measuring));

        // auto intent into result screen
        handler = new Handler();
        job = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(HeartRateMeasuringActivity.this, HeartRateResultActivity.class);
                startActivity(intent);
                finish();
            }
        };
        handler.postDelayed(job,3000);

        // Stop button
        btnStop = findViewById(R.id.btn_stop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(HeartRateMeasuringActivity.this, HeartRateMonitorActivity.class);
//                startActivity(intent);
                finish();
                handler.removeCallbacks(job);
            }
        });
    }
}
