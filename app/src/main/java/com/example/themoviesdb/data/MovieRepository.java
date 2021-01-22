package com.example.themoviesdb.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.themoviesdb.app.MyApp;
import com.example.themoviesdb.data.local.MovieRoomDatabase;
import com.example.themoviesdb.data.local.dao.MovieDao;
import com.example.themoviesdb.data.local.entity.MovieEntity;
import com.example.themoviesdb.data.network.NetworkBoundResource;
import com.example.themoviesdb.data.network.Resource;
import com.example.themoviesdb.data.remote.ApiConstants;
import com.example.themoviesdb.data.remote.MovieApiService;
import com.example.themoviesdb.data.remote.RequestInterceptor;
import com.example.themoviesdb.data.remote.model.MoviesResponse;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRepository {

    private final MovieApiService movieApiService;
    private final MovieDao movieDao;


    public MovieRepository() {

        //Local > Room
        MovieRoomDatabase movieRoomDatabase = Room.databaseBuilder(
                MyApp.getContext(),
                MovieRoomDatabase.class,
                "db_movies"
        ).build();
        movieDao = movieRoomDatabase.getMovieDao();

        //Request Interceptor: incluir en la cabecera(URL) de la petición el Token o APIKEY que autoriza al usuario
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.addInterceptor(new RequestInterceptor());
        OkHttpClient client = okHttpClientBuilder.build();

        //Remote > Retrofit
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(ApiConstants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        movieApiService = retrofit.create(MovieApiService.class);

    }

    public LiveData<Resource<List<MovieEntity>>> getPopularMovies(){
        //Tipo que devuelve Room (BD local), Tipo que devuelve la API con Retrofit
        return new NetworkBoundResource<List<MovieEntity>, MoviesResponse>(){

            @Override
            protected void saveCallResult(@NonNull MoviesResponse item) {
                movieDao.saveMovies(item.getResults());
            }

            @NonNull
            @Override
            protected LiveData<List<MovieEntity>> loadFromDb() {
                //Regresa los datos que tengamos en la BD local
                return movieDao.loadMovies();
            }

            @NonNull
            @Override
            protected Call<MoviesResponse> createCall() {
                //Obtenemos los datos de la API remota
               return movieApiService.loadPopularMovies(1);
            }
        }.getAsLiveData();
    }


}
