package com.devlab.movieapp.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.devlab.movieapp.model.MovieModel;
import com.devlab.movieapp.request.MovieApiClient;

import java.util.List;

public class MovieRepository {

    private static MovieRepository instance;
private MovieApiClient movieApiClient;
    public static MovieRepository getInstance() {
        if (instance == null) {
            instance = new MovieRepository();
        }
        return instance;
    }

    private MovieRepository() {
        movieApiClient = MovieApiClient.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies() {
        return movieApiClient.getMovies();
    }


}
