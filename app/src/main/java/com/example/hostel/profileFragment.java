package com.example.hostel;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import okhttp3.WebSocket;

public class profileFragment extends Fragment implements OnMapReadyCallback {
    GoogleMap map;
WebSocket webSocket;
    public profileFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment fragment= (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        fragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;
        LatLng sydney = new LatLng(31.451765693187525, 74.29762405724215);
        map.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in wasisoft"));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

}