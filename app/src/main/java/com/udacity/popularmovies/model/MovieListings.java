package com.udacity.popularmovies.model;

import java.io.Serializable;
import java.util.List;

public class MovieListings implements Serializable {

    private int page;
    private List<Movie> moviesList = null;
    private int totalResults;
    private int totalPages;

    public MovieListings(){}

    public MovieListings(List<Movie> moviesList) {
        this.moviesList = moviesList;
    }

    public MovieListings(int page, List<Movie> moviesList, int totalResults, int totalPages) {
        this.page = page;
        this.moviesList = moviesList;
        this.totalResults = totalResults;
        this.totalPages = totalPages;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Movie> getMoviesList() {
        return moviesList;
    }

    public void setMoviesList(List<Movie> moviesList) {
        this.moviesList = moviesList;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

}