package com.example.themoviesdb.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.themoviesdb.R;
import com.example.themoviesdb.databinding.FragmentLocationBinding;
import com.example.themoviesdb.databinding.FragmentUploadPhotoBinding;

import org.jetbrains.annotations.NotNull;

public class LocationFragment extends Fragment {

    private FragmentLocationBinding Binding;
    public LocationFragment() {}

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Binding = FragmentLocationBinding.inflate(inflater,container,false);
        View view = Binding.getRoot();
        init();
        return view;
    }

    private void init(){


    }

}