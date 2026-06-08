package shi.application.weather.utility;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationTokenSource;

public class LocalisationUtility {

    public interface LocationCallback {
        void onLocationReceived(double latitude, double longitude);
        void onLocationFailed(String reason);
    }

    private final FusedLocationProviderClient fusedLocationClient;
    private final Context context;
    private LocationCallback locationCallback;

    public LocalisationUtility(Context context) {
        this.context = context;
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public void setLocationCallback(LocationCallback callback) {
        this.locationCallback = callback;
    }

    // Pass the launcher in — it must be registered in the Activity
    public void checkLocationPermissions(ActivityResultLauncher<String[]> permissionLauncher) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getCurrentGPSLocation();
        } else {
            permissionLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }

    public void getCurrentGPSLocation() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();

        fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.getToken()
        ).addOnSuccessListener(location -> {
            if (locationCallback == null) return;
            if (location != null) {
                locationCallback.onLocationReceived(location.getLatitude(), location.getLongitude());
            } else {
                locationCallback.onLocationFailed("Could not fetch GPS location. Is location turned on?");
            }
        }).addOnFailureListener(e -> {
            if (locationCallback != null) {
                locationCallback.onLocationFailed("Failed getting location: " + e.getMessage());
            }
        });
    }
}