package com.devlab.movieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.devlab.movieapp.model.MovieModel;
import com.devlab.movieapp.request.Servicey;
import com.devlab.movieapp.response.MovieSearchResponse;
import com.devlab.movieapp.utils.Credential;
import com.devlab.movieapp.utils.MovieApi;
import com.devlab.movieapp.viewmodels.MovieListViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends AppCompatActivity {
    Button btn;
private MovieListViewModel movieListViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn=findViewById(R.id.btn);

        movieListViewModel=new ViewModelProvider(this).get(MovieListViewModel.class);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRetrofitResponse();
            }
        });
    }

    private void ObserveAnyChange(){
        movieListViewModel.getMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {

            }
        });
    }


    private void getRetrofitResponse() {
        MovieApi movieApi = Servicey.getMovieApi();

        Call<MovieSearchResponse> responseCall = movieApi
                .searchMovie(Credential.API_KEY,
                        "Jack Reacher",
                        1);

        responseCall.enqueue(new Callback<MovieSearchResponse>() {
            @Override
            public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {
                if (response.code() == 20) {
                    Log.v("Tag", "the response " + response.body().toString());
                    List<MovieModel> movies = new ArrayList<>(response.body().getMovies());
                    for (MovieModel movie : movies) {
                        Log.v("Tag", "The release date " + movie.getRelease_date());

                    }
                }
                else {
                    try {
                        Log.v("Tag","Error"+response.errorBody().toString());
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieSearchResponse> call, Throwable t) {

            }
        });
    }

    private void getRetrofitResponseById(){
        MovieApi movieApi=Servicey.getMovieApi();
        Call<MovieModel> responseCall=movieApi.getMovie(550,
                Credential.API_KEY);

        responseCall.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if (response.code()==200){
                    MovieModel movie=response.body();
                    Log.v("Tag","The Response "+movie.getTitle());
                }
                else {
                    try {
                        Log.v("Tag","Error "+response.errorBody().string());
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {

            }
        });
    }

}