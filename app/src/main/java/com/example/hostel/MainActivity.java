package com.example.hostel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.novoda.merlin.Connectable;
import com.novoda.merlin.Disconnectable;
import com.novoda.merlin.Merlin;
import com.novoda.merlin.MerlinsBeard;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "tag";
    Button logIn, signUp, delete;
    MapView mapView;
    GoogleMap map;
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
        delete = findViewById(R.id.delete_btn);
        onClick();
        loadData();
        initRef();
        // fetchValue();
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful()) {
                    String token = task.getResult().getToken();
                }
            }
        });
    }

    //for check internet connection
    public boolean isConnectingToInternet() {
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                final Network network = connectivityManager.getActiveNetwork();
                if (network != null) {
                    final NetworkCapabilities nc = connectivityManager.getNetworkCapabilities(network);

                    return (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
                }
            } else {
                NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
                for (NetworkInfo tempNetworkInfo : networkInfos) {
                    if (tempNetworkInfo.isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    void fetchValue() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String uId = user.getUid();
            mDatabase.fetchData("profile", uId, new DatabaseCallBack() {
                @Override
                public void onSuccessCallBack(DataSnapshot snapshot, String str) {
                    profile prof = snapshot.getValue(profile.class);
                    if (snapshot.exists()) {
                        emailText.setText(prof.getEmailAddress());
                        PasswordText.setText(prof.getPassword());
                        Toast.makeText(MainActivity.this, "Data Get from Database", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailureCallBack(String st) {
                    Toast.makeText(MainActivity.this, "Getting Data Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initRef() {
        mDatabase = Database.getInstance();
    }


    void onClick() {
        logIn.setOnClickListener(new View.OnClickListener() {
            String value1, value2;

            public void onClick(View v) {

                if (emailText.getText().toString().matches(emailPattern)) {
                    value1 = emailText.getText().toString();

                    if (!PasswordText.getText().toString().isEmpty()) {
                        value2 = PasswordText.getText().toString();
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            setSharedPref("email", value1);
                            profile mProfile = new profile();
                            String uId = user.getUid();
                            String phoneNumber = user.getPhoneNumber();
                            mProfile.setuID(uId);
                            mProfile.setPhoneNumber(phoneNumber);
                            mProfile.setPassword(value2);
                            mProfile.setEmailAddress(value1);
                            mDatabase.create("profile", uId, mProfile, new CallBackListener() {
                                @Override
                                public void addSuccessFull(String str) {
                                    Toast.makeText(MainActivity.this, "Data Added in List", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void addFailure(String str) {
                                    Toast.makeText(MainActivity.this, "Data Not Added But Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                            run();
                        } else {
                            move();
                        }
                    } else {
                        PasswordText.setError("Please Enter Valid Password");
                    }
                } else {
                    emailText.setError("Please Enter Valid Email");
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

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                final FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    final String Uid = user.getUid();
                    mDatabase.deleteData("profile", Uid);

                }
            }
        });
    }

    void setSharedPref(String key, String value) {
        SharedPreferences sp = getSharedPreferences(File_name, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    void loadData() {
        SharedPreferences prefs = getSharedPreferences(File_name, MODE_PRIVATE);
        String emails = prefs.getString("email", "");
        emailText.setText(emails);
    }


    public void move() {
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