package shi.application.weather.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String WEATHER_BASE_URL = "https://api.open-meteo.com/";
    private static final String GEO_BASE_URL = "https://geocoding-api.open-meteo.com/";

    private static Retrofit weatherInstance;
    private static Retrofit geoInstance;

    public static Retrofit getWeatherInstance() {
        if (weatherInstance == null) {
            weatherInstance = new Retrofit.Builder()
                    .baseUrl(WEATHER_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return weatherInstance;
    }

    public static Retrofit getGeoInstance() {
        if (geoInstance == null) {
            geoInstance = new Retrofit.Builder()
                    .baseUrl(GEO_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return geoInstance;
    }
}
