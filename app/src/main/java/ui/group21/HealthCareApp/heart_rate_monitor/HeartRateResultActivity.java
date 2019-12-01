package ui.group21.HealthCareApp.heart_rate_monitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ui.group21.HealthCareApp.R;

/**
 * Kết quả đo nhịp tim. #1.4
 */
public class HeartRateResultActivity extends AppCompatActivity implements HeartRateConstant {
    RecyclerView rvUserHRStatus;
    Button btnDiscard, btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate_result);
        setTitle(getString(R.string.result));

        // bind view
        rvUserHRStatus = findViewById(R.id.rv_user_hr_status);
        btnDiscard = findViewById(R.id.btn_discard);
        btnSave = findViewById(R.id.btn_save);

        // recycler view
        rvUserHRStatus.setAdapter(new UserHeartRateStatusAdapter());
        rvUserHRStatus.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // save/discard button
        btnDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HeartRateResultActivity.this, HeartRateMonitorActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(EXTRA_TAB_NUMBER, 1);
                startActivity(intent);
            }
        });
    }

    public class UserHRStatusViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        ImageView image;
        public UserHRStatusViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.tv_user_hr_name);
            image = itemView.findViewById(R.id.img_user_hr_status);
        }
    }

    public class UserHeartRateStatusAdapter extends RecyclerView.Adapter<UserHRStatusViewHolder> {

        // fixed User Heart Rate Status Data
        int[] userHRStatusImgId = {R.drawable.heart_rate, R.drawable.heart_rate, R.drawable.heart_rate, R.drawable.heart_rate};
        String[] userHRStatusText = {"Nghỉ ngơi", "Vận động", "Mệt mỏi", "Phấn khích"};

        @NonNull
        @Override
        public UserHRStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_hr_status, parent, false);
            return new UserHRStatusViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull UserHRStatusViewHolder holder, int position) {
            String text = userHRStatusText[position];
            int imgId = userHRStatusImgId[position];
            holder.text.setText(text);
            holder.image.setImageDrawable(getResources().getDrawable(R.drawable.heart_rate));
        }

        @Override
        public int getItemCount() {
            return userHRStatusText.length;
        }
    }
}
