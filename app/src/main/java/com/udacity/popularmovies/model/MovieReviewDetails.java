package com.udacity.popularmovies.model;

import java.util.List;

public class MovieReviewDetails {

    private int id;
    private List<MovieReview> movieReviews;
    private int total_pages;
    private int total_results;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<MovieReview> getMovieReviews() {
        return movieReviews;
    }

    public void setMovieReviews(List<MovieReview> movieReviews) {
        this.movieReviews = movieReviews;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public String toString(){
        return "movie reviews: "+getMovieReviews();
    }

    public static class MovieReview {
        String id;
        String author;
        String content;
        String url;
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String toString(){
            return "author: "+getAuthor()+", content: "+getContent();
        }
    }

}
