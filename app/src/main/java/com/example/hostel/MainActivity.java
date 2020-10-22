package com.example.hostel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.ThemeUtils;

import android.app.Application;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ftinc.scoop.util.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "tag";
    Button logIn, signUp;

    public static final String File_name = "my_file";
    EditText emailText, PasswordText;
    private Database mDatabase;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logIn = findViewById(R.id.login_btn);
        signUp = findViewById(R.id.signUp_btn);
        emailText = findViewById(R.id.email_et);
        PasswordText = findViewById(R.id.password_et);
        onClick();
        loadData();
        initRef();
    }

    private void initRef() {
        mDatabase = Database.getInstance();
    }


    void onClick() {
        logIn.setOnClickListener(new View.OnClickListener() {
            String value1, value2;

            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();

                if( (emailText.getText().toString().matches(emailPattern))&&(PasswordText.getText().toString().matches(emailPattern))) {
                    value1 = emailText.getText().toString();
                    value2 = PasswordText.getText().toString();
                    if (user != null) {
                        run();
                        setSharedPref();

                        profile mProfile = new profile();
                        String uId = user.getUid();
                        String phoneNumber = user.getPhoneNumber();
                        mProfile.setuID(uId);
                        mProfile.setPhoneNumber(phoneNumber);
                        mProfile.setPassword(value2);
                        mProfile.setEmailAddress(value1);
                        mDatabase.create(uId, mProfile, new CallBackListener() {
                            @Override
                            public void addSuccessFull(String str) {
                                Toast.makeText(MainActivity.this, "Data Added in List", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void addFailure(String str) {
                                Toast.makeText(MainActivity.this, "Data Not Added But Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    emailText.setError("Please Enter Valid Email Address");
                    PasswordText.setError("Please Enter Valid Password");
                }
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                move();
            }
        });
    }


    void setSharedPref() {
        SharedPreferences sp = getSharedPreferences(File_name, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("email", emailText.getText().toString());
        editor.apply();
        editor.putString("password", PasswordText.getText().toString());
        editor.apply();


    }

    void loadData() {
        SharedPreferences prefs = getSharedPreferences(File_name, MODE_PRIVATE);
        String emails = prefs.getString("email", "");
        String pass = prefs.getString("password", "");
        emailText.setText(emails);
        PasswordText.setText(pass);

    }


    void move() {
        Intent i = new Intent(MainActivity.this, splash.class);
        startActivity(i);

        // close this activity
        finish();

    }

    private void run() {
        Intent i = new Intent(MainActivity.this, host.class);
        startActivity(i);
        // close this activity
        finish();

    }

    void fragmentBegin() {

        // Add your parameters
        dashboard fragment = dashboard.newInstance(10);
        // R.id.container - the id of a view that will hold your fragment; usually a FrameLayout
        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.dashboards, new dashboard())
                .commit();
    }


}