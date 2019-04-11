package com.udacity.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.udacity.popularmovies.database.AppDatabase;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.model.MovieListings;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private MovieListings mMovieListings;
    private LiveData<List<Movie>> mFavoriteMovies;

    public MainViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        mFavoriteMovies = database.favoriteMovieDao().loadAllFavoriteMovies();
    }

    public boolean isFavorite(Movie selectedMovie) {
        if(selectedMovie==null || mFavoriteMovies==null || mFavoriteMovies.getValue()==null) return false;
        List<Movie> favoriteMoviesList = mFavoriteMovies.getValue();
        int size = favoriteMoviesList.size();
        for (Movie movie : favoriteMoviesList) {
            if(selectedMovie.getId()==movie.getId())
                return true;
        }
        return false;
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        return mFavoriteMovies;
    }
}
