package com.udacity.popularmovies;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.udacity.popularmovies.model.Movie;

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Movie mSelectedMovie;

    public DetailViewModelFactory(Movie selectedMovie) {
        this.mSelectedMovie = selectedMovie;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DetailViewModel(mSelectedMovie);
    }
}
