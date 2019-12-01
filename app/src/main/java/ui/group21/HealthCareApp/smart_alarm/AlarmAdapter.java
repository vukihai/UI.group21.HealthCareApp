package ui.group21.HealthCareApp.smart_alarm;

import android.app.AlarmManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ui.group21.HealthCareApp.R;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {
    private ArrayList<Alarm> mListAlarm = new ArrayList<>();
    private Context mContext;
    private OnAlarmClickListener mOnAlarmClickListener;

    public AlarmAdapter(Context context, ArrayList<Alarm> ListAlarm){
        context = mContext;
        ListAlarm = mListAlarm;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View alarmView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_smart_alarm, parent, false);
        return new ViewHolder(alarmView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mListAlarm.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTxtTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTxtTime = itemView.findViewById(R.id.txt_time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnAlarmClickListener != null){
                        mOnAlarmClickListener.onAlarmClick(getAdapterPosition());
                    }
                }
            });
        }
    }
    public void setAlarmClick(OnAlarmClickListener onAlarmClickListener){
        this.mOnAlarmClickListener = onAlarmClickListener;
    }
}
