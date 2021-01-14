package com.example.hostel;

import com.google.firebase.database.DataSnapshot;

public interface DatabaseCallBack {
    void onSuccessCallBack(DataSnapshot snapshot,String str);
    void onFailureCallBack(String st);
}
