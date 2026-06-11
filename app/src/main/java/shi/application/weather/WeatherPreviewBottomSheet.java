package shi.application.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Locale;

import shi.application.weather.Model.WeatherCodeMapper;

public class WeatherPreviewBottomSheet extends BottomSheetDialogFragment {

    private final String cityName;
    private final double temperature;
    private final int weatherCode;

    public WeatherPreviewBottomSheet(String cityName, double temperature, int weatherCode) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.weatherCode = weatherCode;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_weather_preview, container, false);

        TextView tvCityName = view.findViewById(R.id.tvPreviewCityName);
        TextView tvTemp = view.findViewById(R.id.tvPreviewTemp);
        ImageView weatherIcon = view.findViewById(R.id.ivPreviewWeatherIcon);
       // MaterialButton btnPin = view.findViewById(R.id.btnPinLocation);


        tvCityName.setText(cityName);
        tvTemp.setText(String.format(Locale.getDefault(),"%.1f°C", temperature));

        weatherIcon.setImageResource(WeatherCodeMapper.getClothingIcon(temperature,weatherCode));


/*
        Drawable drawable = weatherIcon.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }

        btnPin.setOnClickListener(v -> {
            // TODO: Save to local database / Pinned list
            dismiss();
        });
*/

        return view;
    }



}