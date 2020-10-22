package com.example.hostel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class splash extends Activity {
    private List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.PhoneBuilder().build()
    );
    private static final int result_code = 1122;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        runs();
    }

    void mAuthUser() {
        Intent intent = AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build();
        startActivityForResult(intent, result_code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == result_code) {
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if (response != null) {
                    move();
                }
            }
        }

    }

    private void runs() {
        final Handler mHandler = new Handler(Looper.getMainLooper());
        {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null) {
                        move();
                    } else {
                        mAuthUser();
                    }
                }
            }, 3000);

        }
    }

    void move() {
        Intent i = new Intent(splash.this, MainActivity.class);
        startActivity(i);

        // close this activity
        finish();
    }
}
