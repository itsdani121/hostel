package com.example.hostel;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Database {

    private static Database instance;

    public static Database getInstance() {
        if (instance == null)
            instance = new Database();
        return instance;
    }

    private final DatabaseReference mReference;

    public Database() {
        mReference = FirebaseDatabase.getInstance().getReference();
    }

    void create(String Uid, profile file, final CallBackListener listener) {
        mReference.child("profile").child(Uid).setValue(file)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            listener.addSuccessFull("data inclued");
                        } else {
                            listener.addFailure("data not enter");
                        }
                    }
                });
    }
}
