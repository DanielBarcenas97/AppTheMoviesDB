package com.example.themoviesdb.ui;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.themoviesdb.data.service.MyLocationService;
import com.example.themoviesdb.databinding.FragmentLocationBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.jetbrains.annotations.NotNull;

public class LocationFragment extends Fragment {

    private FragmentLocationBinding Binding;

    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 60*60;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    static LocationFragment instance;

    public static LocationFragment getInstance(){
        return instance;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Binding = FragmentLocationBinding.inflate(inflater, container, false);
        View view = Binding.getRoot();
        instance = this;
        init();
        return view;
    }

    private void init() {
        Dexter.withContext(requireContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION).
                withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        updateLocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(getContext(), "You must accept this location", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();

    }

    private void updateLocation() {
        buildLocationRequest();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext()
                ,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent());
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(requireContext(), MyLocationService.class);
        intent.setAction(MyLocationService.ACTION_PROCESS_UPDATE);
        return PendingIntent.getBroadcast(requireContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setSmallestDisplacement(10f);
    }

    public void updateTextView(String value){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Binding.tvLocation.setText(value);
            }
        });

    }

}