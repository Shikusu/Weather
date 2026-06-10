package shi.application.weather;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import shi.application.weather.utility.ActivityManipulation;
import shi.application.weather.utility.LocalisationUtility;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View rootView = findViewById(R.id.bgGradientOverlay);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        if (savedInstanceState == null) {
            replaceFragment(new PersonalWeatherFragment());
        }


        ActivityManipulation activityManipulation = new ActivityManipulation();
        activityManipulation.updateBackgroundGradient(rootView);

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