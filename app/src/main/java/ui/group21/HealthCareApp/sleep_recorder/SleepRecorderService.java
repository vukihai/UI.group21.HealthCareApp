package ui.group21.HealthCareApp.sleep_recorder;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.Date;


public class SleepRecorderService extends Service {

    private final int DELAY_TIME = 1000;

    private final Handler mHandler = new Handler();
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

            mHandler.postDelayed(mRunnable, DELAY_TIME);
        }
    };

    public SleepRecorderService() {}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Đã bắt đầu theo dõi giấc ngủ", Toast.LENGTH_SHORT).show();
        mHandler.postDelayed(mRunnable, 0);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Đã ngừng theo dõi giấc ngủ", Toast.LENGTH_SHORT).show();
        mHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}