package shi.application.weather.utility;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import shi.application.weather.Model.OpenMeteoResponse;// In WeatherViewModel.java
import shi.application.weather.repository.WeatherRepository;

public class WeatherViewModel extends AndroidViewModel {
    private final WeatherRepository repository = new WeatherRepository();
    private final MutableLiveData<OpenMeteoResponse> weatherData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(true);
    private long lastFetchTime = 0;
    private static final long CACHE_DURATION = 10 * 60 * 1000;
    private final MutableLiveData<String> cityAndState = new MutableLiveData<>();

    public LiveData<Boolean> isLoading() {
        return _isLoading;
    }

    public WeatherViewModel(@NonNull Application application) {
        super(application);
    }


    public void loadWeatherByCoords(double lat, double lon) {
        _isLoading.setValue(true);
        repository.fetchWeatherByCoords(lat, lon, new WeatherRepository.WeatherCallback() {
            @Override
            public void onSuccess(OpenMeteoResponse data) {
                weatherData.postValue(data);
                _isLoading.setValue(false);
                lastFetchTime = System.currentTimeMillis();
            }

            @Override
            public void onFailure(String error) {
                _isLoading.setValue(false);
                errorMessage.postValue(error);
            }
        });
    }

    public boolean isCacheValid() {
        return weatherData.getValue() != null &&
                (System.currentTimeMillis() - lastFetchTime) < CACHE_DURATION;
    }

    public LiveData<String> getCityAndState() {
        return cityAndState;
    }

    public void setCityAndState(String city, String state) {
        cityAndState.setValue(city + ", " + state);
    }

    public LiveData<OpenMeteoResponse> getWeatherData() {
        return weatherData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}
