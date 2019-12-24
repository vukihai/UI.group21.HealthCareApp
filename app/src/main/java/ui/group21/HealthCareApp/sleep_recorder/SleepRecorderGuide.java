package ui.group21.HealthCareApp.sleep_recorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Html;
import android.transition.Slide;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import ui.group21.HealthCareApp.R;

public class SleepRecorderGuide extends AppCompatActivity {
     private Button okBtn;
     private ViewPager mViewPager;
     private LinearLayout mLinearLayout;
     private SliderAdapter sliderAdapter;
     private TextView[] mDots;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Theo dõi giấc ngủ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_recorder_guide);
        okBtn= findViewById(R.id.ok_sleep_recorder_guide);
        sliderAdapter = new SliderAdapter(this);
        mViewPager= findViewById(R.id.help_pager);
        mViewPager.setAdapter(sliderAdapter);
        mLinearLayout= findViewById(R.id.dots);
        mViewPager.addOnPageChangeListener(viewListener);
        addDotsIndicator(0);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SleepRecorderGuide.this, SleepRecorderActivity.class);
                startActivity(i);
            }
        });
    }
    public void addDotsIndicator(int pos){
        mDots = new TextView[4];
        mLinearLayout.removeAllViews();
        for(int i=0 ; i< mDots.length;i++){
            mDots[i]= new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.blue));
            mLinearLayout.addView(mDots[i]);
        }

        if(mDots.length >0){
            mDots[pos].setTextColor(getResources().getColor(R.color.white));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
