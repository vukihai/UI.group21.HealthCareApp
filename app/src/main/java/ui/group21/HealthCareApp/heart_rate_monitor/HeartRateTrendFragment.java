package ui.group21.HealthCareApp.heart_rate_monitor;

import android.graphics.RectF;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormatSymbols;
import java.util.ArrayList;

import ui.group21.HealthCareApp.R;

public class HeartRateTrendFragment extends Fragment implements OnChartValueSelectedListener {

    BarChart chart;
    TextView txtAvgValue, txtMaxValue, txtMinValue;
    RecyclerView rvHrHistory;
    private final RectF onValueSelectedRectF = new RectF();

    // demo data
    int[] maxValues = {120, 111, 132, 100, 99, 98, 103};
    int[] avgValues = {80, 86, 81, 99, 70, 77, 82};
    int[] minValues = {60, 61, 62, 63, 64, 65, 66};

    public HeartRateTrendFragment() {
    }

    public static HeartRateTrendFragment newInstance(String param1, String param2) {
        HeartRateTrendFragment fragment = new HeartRateTrendFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_heart_rate_trend, container, false);
        chart = view.findViewById(R.id.chart_weekly_hr);

        txtAvgValue = view.findViewById(R.id.tv_stat_avg_value);
        txtMinValue = view.findViewById(R.id.tv_stat_min_value);
        txtMaxValue = view.findViewById(R.id.tv_stat_max_value);

        rvHrHistory = view.findViewById(R.id.rv_hr_history);

        rvHrHistory.setAdapter(new HRHistoryAdapter());
        rvHrHistory.setLayoutManager(new LinearLayoutManager(getContext()));

        chart.setOnChartValueSelectedListener(this);
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);

        chart.setMaxVisibleValueCount(60);
        ValueFormatter xAxisFormatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return new DateFormatSymbols().getShortWeekdays()[(int) value % 7 +1];
            }
        };

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setTypeface(tfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        YAxis leftAxis = chart.getAxisLeft();
//        leftAxis.setTypeface(tfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(false);
        setData(7, 150);
        return view;
    }

    /**
     * History Recycler View: ViewHolder
     */
    public class HRHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtHrValue;
        ImageView imgHRStatus;
        public HRHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHRStatus = itemView.findViewById(R.id.img_user_hr_status);
            txtTitle = itemView.findViewById(R.id.tv_history_item_title);
            txtHrValue = itemView.findViewById(R.id.tv_history_hr_value);
        }
    }

    /**
     * History Recycler View: Adapter
     */
    public class HRHistoryAdapter extends RecyclerView.Adapter<HRHistoryViewHolder> {

        @NonNull
        @Override
        public HRHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_hr_history, parent, false);
            return new HRHistoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HRHistoryViewHolder holder, int position) {
            holder.txtHrValue.setText(""+120+"bpm");
            holder.txtTitle.setText("Go for a run"+position);
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }


    public void updateAvgMaxMin(int pos){
        if (pos < 0 || pos >= maxValues.length){
            return;
        }
        txtMaxValue.setText(""+maxValues[pos]);
        txtMinValue.setText(""+minValues[pos]);
        txtAvgValue.setText(""+avgValues[pos]);
    }

    private void setData(int count, float range) {

        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = (int) 0; i < count; i++) {
            values.add(new BarEntry(i, avgValues[i]));
        }

        BarDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        } else {
            set1 = new BarDataSet(values, "The year 2017");

            set1.setDrawIcons(false);

            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);

            chart.setData(data);
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        updateAvgMaxMin((int)e.getX());
    }

    @Override
    public void onNothingSelected() {    }
}
