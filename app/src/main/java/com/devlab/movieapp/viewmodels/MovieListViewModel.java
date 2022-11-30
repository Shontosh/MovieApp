package com.devlab.movieapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.devlab.movieapp.model.MovieModel;
import com.devlab.movieapp.repositories.MovieRepository;

import java.util.List;

public class MovieListViewModel extends ViewModel {

    private MovieRepository movieRepository;

    public MovieListViewModel() {
        movieRepository = MovieRepository.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies() {
        return movieRepository.getMovies();
    }

    //calling the  methode in repository

    public void searchMovieApi(String query,int pageNumber){
        movieRepository.searchMovieApi(query,pageNumber);
    }

}
