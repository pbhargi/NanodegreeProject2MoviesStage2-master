package com.udacity.popularmovies.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "favorite_movies")
public class Movie implements Serializable {

    @PrimaryKey
    private int id;

    //Used in this app
    @ColumnInfo(name = "poster_path")
    private String posterPath;
    //Used in this app
    @ColumnInfo(name = "original_title")
    private String originalTitle;
    //Used in this app
    private String overview;
    //Used in this app
    @ColumnInfo(name = "vote_average")
    private int voteAverage;
    //Used in this app
    @ColumnInfo(name = "release_date")
    private String releaseDate;
    private String title;

    @Ignore
    private List<MovieReviewDetails.MovieReview> movieReviews;
    @Ignore
    private List<MovieVideoDetails.MovieVideo> movieVideos;

    public List<MovieReviewDetails.MovieReview> getMovieReviews() {
        return movieReviews;
    }

    public void setMovieReviews(List<MovieReviewDetails.MovieReview> movieReviews) {
        this.movieReviews = movieReviews;
    }

    public List<MovieVideoDetails.MovieVideo> getMovieVideos() {
        return movieVideos;
    }

    public void setMovieVideos(List<MovieVideoDetails.MovieVideo> movieVideos) {
        this.movieVideos = movieVideos;
    }

    /*
    private boolean adult;
    private List<int> genreIds = null;
    private String originalLanguage;
    private String backdropPath;
    private int popularity;
    private int voteCount;
    private boolean video;
    */

    /**
     * No args constructor for use in serialization
     */
    @Ignore
    public Movie() {
    }

    public Movie(int id, String posterPath, String originalTitle, String overview, int voteAverage, String releaseDate) {
        this.id = id;
        this.posterPath = posterPath;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    @Ignore
    public Movie(String posterPath, String originalTitle, String overview, int voteAverage, String releaseDate) {
        this.posterPath = posterPath;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public int getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(int voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString(){
        return "Id: "+id+", posterPath: "+posterPath+", originalTitle: "+originalTitle+", overview "+ overview+", voteAverage: "+voteAverage+", releaseDate: "+releaseDate;
    }
    /*
    public List<int> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<int> genreIds) {
        this.genreIds = genreIds;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }
    */
}