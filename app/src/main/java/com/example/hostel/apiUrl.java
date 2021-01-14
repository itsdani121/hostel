package com.example.hostel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface apiUrl {
     String Base_url = "https://jsonplaceholder.typicode.com/";

    Retrofit retrofit = new Retrofit.Builder().baseUrl(Base_url).addConverterFactory(GsonConverterFactory.create()).build();


}
