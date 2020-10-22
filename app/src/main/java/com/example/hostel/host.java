package com.example.hostel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class host extends AppCompatActivity {
    NavController controller;
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        controller = Navigation.findNavController(this, R.id.host_fragment);
        navigationView = findViewById(R.id.bottom_bar);
        NavigationUI.setupWithNavController(navigationView, controller);
        naviControl();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        run();
    }

    private void run() {
        Intent i = new Intent(host.this, MainActivity.class);
        startActivity(i);
        // close this activity
        finish();

    }

    void naviControl() {
        controller.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override

            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
//                title.setText(destination.getLabel());

                switch (destination.getId()) {
                    case R.id.dashboardFragment:
                    case R.id.hostelsFragment:
                    case R.id.usersFragment:
                    case R.id.profileFragment: {
                        navigationView.setVisibility(View.VISIBLE);
                        break;
                    }
                    default:
                        navigationView.setVisibility(View.GONE);
                }
            }
        });
    }
}