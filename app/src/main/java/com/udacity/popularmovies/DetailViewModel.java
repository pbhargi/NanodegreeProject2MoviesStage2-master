package com.udacity.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.model.MovieListings;
import com.udacity.popularmovies.model.MovieReviewDetails;
import com.udacity.popularmovies.model.MovieVideoDetails;
import com.udacity.popularmovies.utils.JsonUtils;
import com.udacity.popularmovies.utils.NetworkUtils;

import java.net.URL;
import java.util.List;

public class DetailViewModel extends ViewModel {

    // Constant for logging
    private static final String TAG = DetailViewModel.class.getSimpleName();

    private MovieVideoDetails mMovieVideoDetails;
    private Movie mSelectedMovie;

    public DetailViewModel(Movie selectedMovie) {
        this.mSelectedMovie = selectedMovie;
        //movieVideosLiveData = new MovieVideosLiveData(mSelectedMovie.getId());
        //Log.d(TAG, "In detailviewmodel, movieVideosLiveData: "+movieVideosLiveData);
        //Log.d(TAG, "In detailviewmodel, movieVideosLiveData.getValue(): "+movieVideosLiveData.getValue());
        Log.d(TAG, "In detailviewmodel, selectedMovie: "+selectedMovie);
        if(selectedMovie!=null) {
            loadData(selectedMovie.getId());
            mSelectedMovie.setMovieVideos(mMovieVideoDetails.getMovieVideos());
            Log.d(TAG, "In detailviewmodel, movie videos list: "+mSelectedMovie.getMovieVideos());
        }
        //loadMovieVideos();
        //loadMovieReviews();
    }


    public Movie getSelectedMovie() {
        return mSelectedMovie;
    }

    public void setSelectedMovie(Movie mSelectedMovie) {
        this.mSelectedMovie = mSelectedMovie;
    }


    private void loadData(int selectedMovieID) {
            new AsyncTask<Integer,Void,MovieVideoDetails>() {
                private final String TAG = this.getClass().getName();
                @Override
                protected MovieVideoDetails doInBackground(Integer... params) {
                    Integer selectedMovieIdParam = params[0];
                    Log.d(TAG, "inside doInBackground of DataViewModel->LiveData->loadData, selectedMovieID: "+selectedMovieIdParam);
                    URL movieReviewsRequestUrl = NetworkUtils.buildUrl(NetworkUtils.getMovieVideosUrl(selectedMovieIdParam));

                    Log.d(TAG, "inside doInBackground of DataViewModel->LiveData->loadData, movieReviewsRequestUrl: "+movieReviewsRequestUrl);
                    try {
                        String jsonMovieVideosResponse = NetworkUtils.getResponseFromHttpUrl(movieReviewsRequestUrl);
                        MovieVideoDetails movieVideoDetails = JsonUtils.parseMovieVideoDetailsJson(jsonMovieVideosResponse);
                        Log.d(TAG, "inside doInBackground of DataViewModel->LiveData->loadData, movieVideoDetails.getMovieVideos(): "+movieVideoDetails.getMovieVideos());
                        return movieVideoDetails;

                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
                @Override
                protected void onPostExecute(MovieVideoDetails movieVideoDetails) {
                    if (movieVideoDetails != null) {
                        //showWeatherDataView();
                        mMovieVideoDetails = movieVideoDetails;
                    }
                }
            }.execute(selectedMovieID);


    }


    /*private void loadMovieVideos(){
        if(mSelectedMovie==null || mSelectedMovie.getId()==null) return;

        new FetchMovieVideosTask().execute(mSelectedMovie.getId());

        try {
            String jsonMovieVideosResponse = NetworkUtils.getResponseFromHttpUrl(movieVideosRequestUrl);
            MovieVideoDetails movieVideoDetails = JsonUtils.parseMovieVideoDetailsJson(jsonMovieVideosResponse);
            mSelectedMovie.setMovieVideos(movieVideoDetails.getMovieVideos());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void loadMovieReviews(){
        if(mSelectedMovie==null || mSelectedMovie.getId()==null) return;

        URL movieReviewsRequestUrl = NetworkUtils.buildUrl(NetworkUtils.getMovieReviewsUrl(mSelectedMovie.getId()));

        try {
            String jsonMovieVideosResponse = NetworkUtils.getResponseFromHttpUrl(movieReviewsRequestUrl);
            MovieReviewDetails movieReviewDetails = JsonUtils.parseMovieReviewDetailsJson(jsonMovieVideosResponse);
            mSelectedMovie.setMovieReviews(movieReviewDetails.getMovieReviews());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }*/
}
