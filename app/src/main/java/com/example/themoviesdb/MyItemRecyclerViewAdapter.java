package com.example.themoviesdb;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;



public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<String> mValues;
    Context ctx;

    public MyItemRecyclerViewAdapter(FragmentActivity activity, ArrayList<String> items) {
        mValues = items;
        ctx = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Glide.with(ctx).load(mValues.get(position)).into(holder.imageViewCover);
    }



    @Override
    public int getItemCount() {
        if(mValues != null)
            return mValues.size();
        else return 0;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView imageViewCover;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            imageViewCover = view.findViewById(R.id.image_view_cover);
        }

    }
}