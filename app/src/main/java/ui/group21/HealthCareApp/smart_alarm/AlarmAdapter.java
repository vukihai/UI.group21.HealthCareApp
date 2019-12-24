package ui.group21.HealthCareApp.smart_alarm;

import android.app.AlarmManager;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ui.group21.HealthCareApp.R;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {
    private ArrayList<Alarm> mListAlarm ;
    private Context mContext;
    private OnAlarmClickListener mOnAlarmClickListener;

    public AlarmAdapter(Context context, ArrayList<Alarm> ListAlarm){
        mContext = context;
        mListAlarm = ListAlarm;
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
        holder.mTxtTime.setText(mListAlarm.get(position).getTime());
        holder.mSwtAlarm.setChecked(true);
    }

    @Override
    public int getItemCount() {
        return mListAlarm.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTxtTime;
        private ImageView mDelete;
        private Switch mSwtAlarm;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTxtTime = itemView.findViewById(R.id.txt_time);
            mDelete = itemView.findViewById(R.id.ic_delete);
            mSwtAlarm = itemView.findViewById(R.id.swt_alarm);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnAlarmClickListener != null){
                        mOnAlarmClickListener.onAlarmClick(getAdapterPosition());
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mDelete.setVisibility(View.VISIBLE);
                    Log.d("ADADADAD","Long click");
                    return true;
                }
            });
            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnAlarmClickListener != null){
                        mOnAlarmClickListener.onDelClick(getAdapterPosition());
                    }
                }
            });
        }
    }
    public void setAlarmClick(OnAlarmClickListener onAlarmClickListener){
        this.mOnAlarmClickListener = onAlarmClickListener;
    }
    public void setDelClick(OnAlarmClickListener onAlarmClickListener){
        this.mOnAlarmClickListener = onAlarmClickListener;
    }
}
