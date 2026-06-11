package shi.application.weather;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import java.util.ArrayList;
import java.util.List;

import shi.application.weather.Model.GeocodingResponse;
import shi.application.weather.Model.OpenMeteoResponse;
import shi.application.weather.repository.WeatherRepository;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GlobalWeatherFragment #newInstance} factory method to
 * create an instance of this fragment.
 */

public class GlobalWeatherFragment extends Fragment {

    private WeatherRepository weatherRepository;
    private MaterialAutoCompleteTextView searchBar;
    private ArrayAdapter<String> searchAdapter;

    private List<GeocodingResponse.GeoResult> currentSearchResults = new ArrayList<>();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_global_weather, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        weatherRepository = new WeatherRepository();
        searchBar = view.findViewById(R.id.etSearch);

        searchAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, new ArrayList<>());
        searchBar.setAdapter(searchAdapter);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (query.length() > 2) {
                    fetchCitySuggestions(query);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        searchBar.setOnItemClickListener((parent, view1, position, id) -> {
            if (position < currentSearchResults.size()) {
                GeocodingResponse.GeoResult selectedLocation = currentSearchResults.get(position);

                searchBar.clearFocus();

                fetchWeatherAndOpenPreview(selectedLocation);
            }
        });
    }

    private void fetchCitySuggestions(String query) {
        weatherRepository.searchCity(query, new WeatherRepository.GeocodingCallback() {
            @Override
            public void onSuccess(GeocodingResponse data) {
                if (data != null && data.results != null) {
                    currentSearchResults = data.results;

                    List<String> displayNames = new ArrayList<>();
                    for (GeocodingResponse.GeoResult result : data.results) {
                        String region = result.state != null ? result.state : result.country;
                        displayNames.add(result.name + " (" + region + ")");
                    }

                    searchAdapter.clear();
                    searchAdapter.addAll(displayNames);
                    searchAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    private void fetchWeatherAndOpenPreview(GeocodingResponse.GeoResult location) {
        weatherRepository.fetchWeatherByCoords(location.latitude, location.longitude, new WeatherRepository.WeatherCallback() {
            @Override
            public void onSuccess(OpenMeteoResponse weatherData) {
                if (weatherData != null && weatherData.current != null) {

                    double temp = weatherData.current.temperature;
                    int weatherCode = weatherData.current.weatherCode;

                    WeatherPreviewBottomSheet bottomSheet = new WeatherPreviewBottomSheet(
                            location.name,
                            temp,
                            weatherCode
                    );
                    bottomSheet.show(getParentFragmentManager(), "WeatherPreview");
                }
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getContext(), "Impossible de charger la météo: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}