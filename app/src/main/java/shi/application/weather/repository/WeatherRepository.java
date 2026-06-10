package shi.application.weather.repository;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shi.application.weather.Model.GeocodingResponse;
import shi.application.weather.Model.OpenMeteoResponse;
import shi.application.weather.retrofit.RetrofitClient;
import shi.application.weather.service.GeocodingService;
import shi.application.weather.service.OpenMeteoService;

public class WeatherRepository {

    // Params to request from Open-Meteo
    private static final String CURRENT_PARAMS =
            "temperature_2m,relative_humidity_2m,apparent_temperature," +
                    "weather_code,wind_speed_10m,precipitation";

    private static final String HOURLY_PARAMS =
            "temperature_2m,weather_code,precipitation_probability";

    private static final String DAILY_PARAMS =
            "temperature_2m_max,temperature_2m_min,weather_code," +
                    "precipitation_probability_max";

    private final OpenMeteoService weatherService;
    private final GeocodingService geocodingService;

    public interface WeatherCallback {
        void onSuccess(OpenMeteoResponse data);
        void onFailure(String error);
    }

    public interface GeocodingCallback {
        void onSuccess(GeocodingResponse data);
        void onFailure(String error);
    }

    public WeatherRepository() {
        weatherService = RetrofitClient.getWeatherInstance()
                .create(OpenMeteoService.class);
        geocodingService = RetrofitClient.getGeoInstance()
                .create(GeocodingService.class);
    }

    public void fetchWeatherByCoords(double lat, double lon, WeatherCallback callback) {
        weatherService.getWeather(
                lat, lon,
                CURRENT_PARAMS,
                HOURLY_PARAMS,
                DAILY_PARAMS,
                "auto",
                7
        ).enqueue(new Callback<OpenMeteoResponse>() {
            @Override
            public void onResponse(@NonNull Call<OpenMeteoResponse> call,
                                   @NonNull Response<OpenMeteoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<OpenMeteoResponse> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public void searchCity(String cityName, GeocodingCallback callback) {
        geocodingService.searchCity(cityName, 5, "en")
                .enqueue(new Callback<GeocodingResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<GeocodingResponse> call,
                                           @NonNull Response<GeocodingResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body());
                        } else {
                            callback.onFailure("City not found");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<GeocodingResponse> call, @NonNull Throwable t) {
                        callback.onFailure(t.getMessage());
                    }
                });
    }

    public void fetchWeatherByCity(String cityName, WeatherCallback weatherCallback,
                                   GeocodingCallback geoCallback) {
        searchCity(cityName, new GeocodingCallback() {
            @Override
            public void onSuccess(GeocodingResponse geoData) {
                if (geoData.results == null || geoData.results.isEmpty()) {
                    weatherCallback.onFailure("No results found for: " + cityName);
                    return;
                }
                geoCallback.onSuccess(geoData);
            }

            @Override
            public void onFailure(String error) {
                weatherCallback.onFailure(error);
            }
        });
    }
}