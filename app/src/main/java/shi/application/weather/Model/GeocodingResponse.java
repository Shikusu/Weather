package shi.application.weather.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeocodingResponse {

    @SerializedName("results")
    public List<GeoResult> results;

    public static class GeoResult {
        @SerializedName("name")
        public String name;

        @SerializedName("latitude")
        public double latitude;

        @SerializedName("longitude")
        public double longitude;

        @SerializedName("country")
        public String country;

        @SerializedName("admin1")
        public String state;
    }
}