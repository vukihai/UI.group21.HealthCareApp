package ui.group21.HealthCareApp.heart_rate_monitor;

import ui.group21.HealthCareApp.R;

public interface HeartRateConstant {
    String EXTRA_TAB_NUMBER = "extra_tab_number";
    String EXTRA_HEART_RATE_VALUE = "extra_hr_value";

    // fixed User Heart Rate Status Data
    int[] userHRStatusImgId = {R.drawable.icon_hr_rest, R.drawable.icon_hr_run, R.drawable.icon_hr_tired, R.drawable.icon_hr_excited};
    int[] userHRStatusText = {R.string.hr_resting, R.string.hr_after_exercise, R.string.hr_tired, R.string.hr_excited};
    int[] userHRExpectedMaxValue = {76, 120, 76, 100};
    int[] userHRExpectedMinValue = {61, 80, 61, 70};
    int[] userHRMaxValue = {120, 150, 120, 120};
    int[] userHRMinValue = {40, 40, 40, 40};
}
