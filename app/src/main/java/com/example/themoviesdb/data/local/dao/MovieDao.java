package com.example.themoviesdb.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.themoviesdb.data.local.entity.MovieEntity;

import java.util.List;

public interface MovieDao {

    @Query("SELECT * FROM movies")
    LiveData<List<MovieEntity>> loadMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveMovies(List<MovieEntity> movieEntityList);



}
