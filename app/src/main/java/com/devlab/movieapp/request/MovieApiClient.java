package com.devlab.movieapp.request;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.devlab.movieapp.AppExecutors;
import com.devlab.movieapp.model.MovieModel;
import com.devlab.movieapp.response.MovieSearchResponse;
import com.devlab.movieapp.utils.Credential;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {

    private MutableLiveData<List<MovieModel>> mMovies;
    private static MovieApiClient instance;
    //making global runnable
    private RetrieveMoviesRunnable retrieveMoviesRunnable;

    public static MovieApiClient getInstance() {
        if (instance == null) {
            instance = new MovieApiClient();
        }
        return instance;
    }

    private MovieApiClient() {
        mMovies = new MutableLiveData<>();
    }

    public LiveData<List<MovieModel>> getMovies() {
        return mMovies;
    }

    public void searchMovieApi(String query,int pageNumber) {
        if (retrieveMoviesRunnable!=null){
            retrieveMoviesRunnable=null;
        }
        retrieveMoviesRunnable=new RetrieveMoviesRunnable(query,pageNumber);
        final Future myHandler = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnable);
        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //cancel retrofit call
                myHandler.cancel(true);
            }
        }, 5000, TimeUnit.MILLISECONDS);
    }

    //retreiving data from restapi by runnable class
    private class RetrieveMoviesRunnable implements Runnable {
        private String query;
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            this.cancelRequest = false;
        }

        @Override
        public void run() {
            //Getting the response object

            try {
                Response response=getMovies(query,pageNumber).execute();
                if (cancelRequest){
                    return;
                }
                if (response.code()==200){
                    List<MovieModel> list=new ArrayList<>(((MovieSearchResponse)response.body()).getMovies());
                    if (pageNumber==1){
                        //sending data to live data
                        //postvalue: used for background thread
                        //setvalue: not for background thread
                        mMovies.postValue(list);
                    }
                    else {
                        List<MovieModel> currentMovies=mMovies.getValue();
                        currentMovies.addAll(list);
                        mMovies.postValue(currentMovies);
                    }
                }
                else {
                    String error=response.errorBody().string();
                    Log.v("Tag","Error "+error);
                    mMovies.postValue(null);
                }
            }
            catch (Exception e){
                e.printStackTrace();
                mMovies.postValue(null);
            }

            if (cancelRequest) {
                return;
            }

        }

        // Search Method/query

        private Call<MovieSearchResponse> getMovies(String query, int pageNumber){
            return Servicey.getMovieApi().searchMovie(
                    Credential.API_KEY,
                    query,
                    pageNumber);
        }

        private void cancelRequest(){
            Log.v("Tag","Cancelling Search Request");
            cancelRequest=true;
        }
    }
}
