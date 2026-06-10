package shi.application.weather;

import android.Manifest;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import shi.application.weather.Model.OpenMeteoResponse;
import shi.application.weather.Model.WeatherCodeMapper;
import shi.application.weather.utility.LocalisationUtility;
import shi.application.weather.utility.WeatherViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalWeatherFragment #newInstance} factory method to
 * create an instance of this fragment.
 */


public class PersonalWeatherFragment extends Fragment {

    private final ExecutorService executor =
            Executors.newSingleThreadExecutor();
    private LocalisationUtility localisationUtility;
    private WeatherViewModel weatherViewModel;

    private final ActivityResultLauncher<String[]> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                boolean fineGranted = Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_FINE_LOCATION));
                boolean coarseGranted = Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_COARSE_LOCATION));

                if (fineGranted || coarseGranted) {
                    localisationUtility.getCurrentGPSLocation();
                } else {
                    Toast.makeText(getContext(), "Permission denied.", Toast.LENGTH_SHORT).show();
                }
            });

    TextView tvLocationSub, tvTemperature, tvHumidity, tvFeelsLike, tvWindSpeed, tvWeatherDesc, tvUvIndex;
    ProgressBar loadingBar;
    NestedScrollView rootLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_weather, container, false);

        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        WeatherViewModel viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        tvLocationSub = view.findViewById(R.id.tvLocationSub);
        tvTemperature = view.findViewById(R.id.tvTemperature);
        tvHumidity = view.findViewById(R.id.tvHumidityVal);
        tvFeelsLike = view.findViewById(R.id.tvFeelsLike);
        tvWindSpeed = view.findViewById(R.id.tvWind);
        tvWeatherDesc = view.findViewById(R.id.tvCondition);
        tvUvIndex = view.findViewById(R.id.tvUvIndex);
        loadingBar = view.findViewById(R.id.loadingBar);
        rootLayout = view.findViewById(R.id.personalLayout);

        localisationUtility = new LocalisationUtility(requireContext());
        localisationUtility.setLocationCallback(new LocalisationUtility.LocationCallback() {
            @Override
            public void onLocationReceived(double latitude, double longitude) {
                if (!isAdded() || getContext() == null) return;
                try {
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        String city = address.getLocality();
                        String state = address.getAdminArea();

                        requireActivity().runOnUiThread(() -> {
                                    tvLocationSub.setText(String.format("%s, %s", city, state));
                                    weatherViewModel.setCityAndState(city, state);
                                }

                        );
                    }
                } catch (IOException error) {
                    Log.e("ERROR", "Geocoder failed", error);
                }
                executor.execute(() -> {

                    requireActivity().runOnUiThread(() -> {
                        if (weatherViewModel.isCacheValid()) {
                            updateUI(weatherViewModel.getWeatherData().getValue());
                        } else {
                            weatherViewModel.loadWeatherByCoords(latitude, longitude);
                        }
                    });
                });
            }

            @Override
            public void onLocationFailed(String reason) {
                if (!isAdded() || getContext() == null) {
                    return;
                }
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), reason, Toast.LENGTH_SHORT).show()
                );
            }
        });


        localisationUtility.checkLocationPermissions(permissionLauncher);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        weatherViewModel = new ViewModelProvider(requireActivity()).get(WeatherViewModel.class);
        weatherViewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                loadingBar.setVisibility(View.VISIBLE);
                rootLayout.setVisibility(View.GONE); //
            } else {
                loadingBar.setVisibility(View.GONE);
                rootLayout.setVisibility(View.VISIBLE);
            }
        });
        weatherViewModel.getWeatherData().observe(getViewLifecycleOwner(), this::updateUI);
        weatherViewModel.getCityAndState().observe(
                getViewLifecycleOwner(),
                location -> tvLocationSub.setText(location)
        );
        weatherViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error ->
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show()
        );
    }

    private void updateUI(OpenMeteoResponse data) {
        if (data == null) return;

        requireActivity().runOnUiThread(() -> {
            tvTemperature.setText(String.format(Locale.getDefault(), "%.1f°", data.current.temperature));
            tvHumidity.setText(String.format(Locale.getDefault(), "%d%%", data.current.humidity));
            tvFeelsLike.setText(String.format(Locale.getDefault(), "%.1f°", data.current.feelsLike));
            tvWindSpeed.setText(String.format(Locale.getDefault(), "%.1f km/h", data.current.windSpeed));
            tvWeatherDesc.setText(WeatherCodeMapper.getDescription(data.current.weatherCode));
            tvUvIndex.setText(WeatherCodeMapper.getUVLabel(data.current.uvIndex));
        });
    }

    @Override
    public void onDestroy() {
        executor.shutdown();
        super.onDestroy();
    }
}