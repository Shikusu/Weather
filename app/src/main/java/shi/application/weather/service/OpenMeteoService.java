package shi.application.weather.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import shi.application.weather.Model.OpenMeteoResponse;

public interface OpenMeteoService {

    @GET("v1/forecast")
    Call<OpenMeteoResponse> getWeather(
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("current") String currentParams,
            @Query("hourly") String hourlyParams,
            @Query("daily") String dailyParams,
            @Query("timezone") String timezone,
            @Query("forecast_days") int forecastDays
    );
}