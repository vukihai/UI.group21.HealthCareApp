package ui.group21.HealthCareApp.sleep_recorder;

import android.app.DatePickerDialog;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;


public class SleepRecorderService extends Service {
    private Date startDateTime;
    private Date endDateTime;
    private final ArrayList<Float> mLastHalf = new ArrayList<>();
    private final ArrayList<Float> mLastQuadter = new ArrayList<>();
    private final ArrayList<Float> mQuadterAvgs = new ArrayList<>();
    private final Handler mHandler = new Handler();
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            Float value = new Float(getData());
            Date currentDate = new Date();
            int minute = currentDate.getMinutes();
            if(minute % 15 == 0) {
                updateQuadterAvgs();
            }
            if(mLastHalf.size() > 60) {
                mLastHalf.remove(0);
            }
            mLastHalf.add(value);
            mLastQuadter.add(value);
            mHandler.postDelayed(mRunnable, 30000);
        }
    };

    public SleepRecorderService() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        startDateTime = new Date();
        mHandler.postDelayed(mRunnable, 0);
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
        mQuadterAvgs.add(new Float(sum / mLastQuadter.size()));
        mLastQuadter.clear();
    }

    private void notifyDataChanged() {

    }
}
