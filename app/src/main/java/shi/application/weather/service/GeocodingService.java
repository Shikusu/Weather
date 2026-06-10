package shi.application.weather.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import shi.application.weather.Model.GeocodingResponse;

public interface GeocodingService {

    @GET("v1/search")
    Call<GeocodingResponse> searchCity(
            @Query("name") String cityName,
            @Query("count") int count,        // how many results to return
            @Query("language") String language
    );
}
