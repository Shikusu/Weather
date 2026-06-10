package shi.application.weather.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OpenMeteoResponse {

    @SerializedName("current")
    public Current current;

    @SerializedName("hourly")
    public Hourly hourly;

    @SerializedName("daily")
    public Daily daily;

    public static class Current {
        @SerializedName("temperature_2m")
        public double temperature;

        @SerializedName("relative_humidity_2m")
        public int humidity;

        @SerializedName("apparent_temperature")
        public double feelsLike;

        @SerializedName("weather_code")
        public int weatherCode;

        @SerializedName("wind_speed_10m")
        public double windSpeed;

        @SerializedName("uv_index")
        public double uvIndex;

        @SerializedName("precipitation")
        public double precipitation;
    }

    public static class Hourly {
        @SerializedName("time")
        public List<String> time;

        @SerializedName("temperature_2m")
        public List<Double> temperature;

        @SerializedName("weather_code")
        public List<Integer> weatherCode;

        @SerializedName("precipitation_probability")
        public List<Integer> precipitationProbability;
    }

    public static class Daily {
        @SerializedName("time")
        public List<String> time;

        @SerializedName("temperature_2m_max")
        public List<Double> tempMax;

        @SerializedName("temperature_2m_min")
        public List<Double> tempMin;

        @SerializedName("weather_code")
        public List<Integer> weatherCode;

        @SerializedName("precipitation_probability_max")
        public List<Integer> precipitationProbability;
    }
}