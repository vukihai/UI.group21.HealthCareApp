package ui.group21.HealthCareApp.heart_rate_monitor;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ui.group21.HealthCareApp.R;


public class HeartRateHomeFragment extends Fragment {
    private Button btnStart;

    public HeartRateHomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_heart_rate_home2, container, false);
        btnStart = v.findViewById(R.id.btn_measure_hr);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HeartRateMeasuringActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }
}
