package shi.application.weather.utility;

import android.view.View;

import java.util.Calendar;

import shi.application.weather.R;

public class ActivityManipulation {
    public void updateBackgroundGradient(View rootView) {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int backgroundResource;

        if (hour >= 5 && hour < 7) {
            backgroundResource = R.drawable.bg_gradient_dawn;
        } else if (hour >= 7 && hour < 12) {
            backgroundResource = R.drawable.bg_gradient_morning;
        } else if (hour == 12) {
            backgroundResource = R.drawable.bg_gradient_noon;
        } else if (hour >= 13 && hour < 18) {
            backgroundResource = R.drawable.bg_gradient_afternoon;
        } else if (hour >= 18 && hour < 21) {
            backgroundResource = R.drawable.bg_gradient_evening;
        } else {
            backgroundResource = R.drawable.bg_gradient_night;
        }

        rootView.setBackgroundResource(backgroundResource);
    }


}
