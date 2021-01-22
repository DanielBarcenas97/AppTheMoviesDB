package com.example.themoviesdb.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.themoviesdb.R;

public class UploadPhotoFragment extends Fragment {



        public UploadPhotoFragment() {
            // Required empty public constructor
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_upload_photo, container, false);
        }


        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);

        }
    }