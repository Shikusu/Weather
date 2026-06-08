package shi.application.weather;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import shi.application.weather.utility.LocalisationUtility;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    LocalisationUtility localisationUtility;

    private final ActivityResultLauncher<String[]> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                Boolean fineGranted  = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    fineGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                }
                Boolean coarseGranted = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    coarseGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);
                }
                if (Boolean.TRUE.equals(fineGranted) || Boolean.TRUE.equals(coarseGranted)) {
                    localisationUtility.getCurrentGPSLocation();
                } else {
                    Toast.makeText(this, "Permission denied. Can't update weather.", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localisationUtility = new LocalisationUtility(this);
        localisationUtility.setLocationCallback(new LocalisationUtility.LocationCallback() {
            @Override
            public void onLocationReceived(double latitude, double longitude) {
                Toast.makeText(MainActivity.this, "it changed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLocationFailed(String reason) {
                Toast.makeText(MainActivity.this, reason, Toast.LENGTH_SHORT).show();
            }
        });
        localisationUtility.checkLocationPermissions(permissionLauncher);

        setContentView(R.layout.activity_main);
        bottomNav=findViewById(R.id.bottomNav);

        if (savedInstanceState == null) {
            replaceFragment(new PersonalWeatherFragment());
        }

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_personal) {
                replaceFragment(new PersonalWeatherFragment());
                return true;
            } else if (itemId == R.id.nav_global) {
                replaceFragment(new GlobalWeatherFragment());
                return true;
            }

            return false;
        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right
                )
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}