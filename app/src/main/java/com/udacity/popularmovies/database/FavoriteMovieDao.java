package com.udacity.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.udacity.popularmovies.model.Movie;

import java.util.List;

@Dao
@SuppressWarnings("unchecked")
public interface FavoriteMovieDao {

    @Query("SELECT * FROM favorite_movies ORDER BY original_title")
    LiveData<List<Movie>> loadAllFavoriteMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavoriteMovie(Movie favoriteMovie);

    //@Update(onConflict = OnConflictStrategy.REPLACE)
    //void updateFavoriteMovie(Movie favoriteMovie);

    @Delete
    void deleteMovieFromFavorites(Movie favoriteMovie);

    //@Query("SELECT * FROM favorite_movies WHERE id = :id")
    //LiveData<Movie> loadFavoriteMovieById(int id);
}
