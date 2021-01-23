package com.example.themoviesdb.ui;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.themoviesdb.R;
import com.example.themoviesdb.databinding.FragmentUploadPhotoBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class UploadPhotoFragment extends Fragment {

        private Uri imageUri;
        private FragmentUploadPhotoBinding Binding;
        private static final int IMAGE_REQUEST = 2;
        private static final int RP_CAMERA = 100;
        private static final int REQUEST_CODE_CAMERA_PERMISSION = 2012;

        FirebaseStorage storage;
        public UploadPhotoFragment() {
            // Required empty public constructor
        }

        @Override
        public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
                Binding = FragmentUploadPhotoBinding.inflate(inflater,container,false);
                View view = Binding.getRoot();
                init();
                return view;
        }

        private void init(){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_PERMISSION);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 948);
                }

                storage = FirebaseStorage.getInstance();
                Binding.btnUpload.setVisibility(View.GONE);
                Binding.uploadGallery.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                openImage();
                        }
                });
                Binding.uploadCamara.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                takePhoto();
                        }
                });

        }

        private void openImage() {
                Intent intent = new Intent();
                intent.setType("image/");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,IMAGE_REQUEST);
        }

        private void takePhoto(){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,RP_CAMERA);
        }


        private void uploadImage(){

                final ProgressDialog pd = new ProgressDialog(getContext());
                pd.setMessage(getString(R.string.loading));
                pd.show();

                if(imageUri != null){
                        StorageReference fileRef = storage.getReference().child("uploads").child(System.currentTimeMillis()
                        +"."+ getFileExtension(imageUri));

                        fileRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                        String url = uri.toString();
                                                        Log.d("Download", url);
                                                        pd.dismiss();
                                                        Toast.makeText(getContext(),"UploadSuccess", Toast.LENGTH_LONG).show();
                                                }
                                        });
                                }
                        });
                }



        }

        private String getFileExtension(Uri imageUri) {
                ContentResolver contentResolver = requireContext().getContentResolver();

                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
        }


        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
                super.onActivityResult(requestCode, resultCode, data);

                if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK){
                        if (data != null) {
                                imageUri = data.getData();
                                uploadImage();
                        }

                }else if (requestCode == RP_CAMERA && resultCode == RESULT_OK){
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        imageUri = getImageUri(requireContext(),bitmap);;
                        uploadImage();

                }

        }

        public Uri getImageUri(Context inContext, Bitmap inImage) {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
                return Uri.parse(path);
        }
}

