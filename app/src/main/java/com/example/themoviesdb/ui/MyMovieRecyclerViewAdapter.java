package com.example.themoviesdb.ui;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.themoviesdb.R;
import com.example.themoviesdb.data.local.entity.MovieEntity;

import java.util.List;

import static com.example.themoviesdb.data.remote.ApiConstants.IMAGE_API_PREFIX;


public class MyMovieRecyclerViewAdapter extends RecyclerView.Adapter<MyMovieRecyclerViewAdapter.ViewHolder> {

    private  List<MovieEntity> mValues;
    Context ctx;

    public MyMovieRecyclerViewAdapter(Context context, List<MovieEntity> items) {
        mValues = items;
        ctx = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Glide.with(ctx).load(IMAGE_API_PREFIX + holder.mItem.getPosterPath()).into(holder.imageViewCover);
        holder.textTitle.setText(holder.mItem.getTitle());

    }


    public void setData(List<MovieEntity> movies){
        this.mValues = movies;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        if(mValues != null)
            return mValues.size();
        else return 0;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView imageViewCover;
        public final TextView textTitle;
        public MovieEntity mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            imageViewCover = view.findViewById(R.id.image_view_cover);
            textTitle = view.findViewById(R.id.text_title);

        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}