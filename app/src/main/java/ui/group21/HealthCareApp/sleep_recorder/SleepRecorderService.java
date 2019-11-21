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

    private Date startDateTime;
    private Date endDateTime;
    private final ArrayList<Float> mLastHalf = new ArrayList<>();
    private final ArrayList<Float> mLastQuadter = new ArrayList<>();
    private final ArrayList<Float> mQuadterAvgs = new ArrayList<>();
    private final Handler mHandler = new Handler();
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            Float value = Float.valueOf(getData());
            Date currentDate = new Date();
            int second = currentDate.getSeconds();
            int minute = currentDate.getMinutes();
            if(minute % 15 == 0 && second < (DELAY_TIME / 1000) + 1) {
                updateQuadterAvgs();
            }
            if(mLastHalf.size() > 15) {
                mLastHalf.remove(0);
            }
            mLastHalf.add(value);
            mLastQuadter.add(value);

            sendLastHalfData();

            mHandler.postDelayed(mRunnable, DELAY_TIME);
        }
    };

    public SleepRecorderService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        startDateTime = new Date();
        mHandler.postDelayed(mRunnable, 0);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // TODO send start and end of measuring time
        sendQuadterAvgsData();
        endDateTime = new Date();
        mHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private float getData() {
        return (float) Math.random() * 100;
    }

    private void updateQuadterAvgs() {
        float sum = 0f;
        for(Float number : mLastQuadter) {
            sum += number.floatValue();
        }
        mQuadterAvgs.add(Float.valueOf(sum / mLastQuadter.size()));
        mLastQuadter.clear();
    }

    private void sendLastHalfData() {
        float[] arr = new float[mLastHalf.size()];
        for(int i = 0; i < mLastHalf.size(); ++i) {
            arr[i] = mLastHalf.get(i).floatValue();
        }
        Intent intent = new Intent();
        intent.setAction("LAST_HALF_SLEEPING_DATA");
        intent.putExtra("DATA", arr);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendQuadterAvgsData() {
        float[] arr = new float[mQuadterAvgs.size()];
        for(int i = 0; i < mQuadterAvgs.size(); ++i) {
            arr[i] = mQuadterAvgs.get(i).floatValue();
        }
        Intent intent = new Intent();
        intent.setAction("QUADTER_AVGS_SLEEPING_DATA");
        intent.putExtra("DATA", arr);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}