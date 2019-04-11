package com.udacity.popularmovies.utils;

import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.model.MovieListings;
import com.udacity.popularmovies.model.MovieReviewDetails;
import com.udacity.popularmovies.model.MovieVideoDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {


    final static String MOVIE_LISTINGS_RESULTS = "results";
    final static String MOVIE_LISTINGS_PAGE = "page";
    final static String MOVIE_LISTINGS_TOTAL_RESULTS = "total_results";
    final static String MOVIE_LISTINGS_TOTAL_PAGES = "total_pages";

    final static String MOVIE_POSTER_PATH = "poster_path";
    final static String MOVIE_ADULT = "adult";
    final static String MOVIE_OVERVIEW = "overview";
    final static String MOVIE_RELEASE_DATE = "release_date";
    final static String MOVIE_GENRE_IDS = "genre_ids";
    final static String MOVIE_ID = "id";
    final static String MOVIE_ORIGINAL_TITLE = "original_title";
    final static String MOVIE_ORIGINAL_LANGUAGE = "original_language";
    final static String MOVIE_TITLE = "title";
    final static String MOVIE_BACKDROP_PATH = "backdrop_path";
    final static String MOVIE_POPULARITY = "popularity";
    final static String MOVIE_VOTE_COUNT = "vote_count";
    final static String MOVIE_VIDEO = "video";
    final static String MOVIE_VOTE_AVERAGE = "vote_average";

    final static String MOVIE_VIDEO_DETAIL_id = "id";
    final static String MOVIE_VIDEO_results = "results";
    final static String MOVIE_VIDEO_id = "id";
    final static String MOVIE_VIDEO_iso_6391 = "iso_639_1";
    final static String MOVIE_VIDEO_iso_3166_1 = "iso_3166_1";
    final static String MOVIE_VIDEO_key = "key";
    final static String MOVIE_VIDEO_name = "name";
    final static String MOVIE_VIDEO_site = "site";
    final static String MOVIE_VIDEO_size = "size";
    final static String MOVIE_VIDEO_type = "type";

    final static String MOVIE_REVIEW_DETAIL_id = "id";
    final static String MOVIE_REVIEW_total_pages = "total_pages";
    final static String MOVIE_REVIEW_total_results = "total_results";
    final static String MOVIE_REVIEW_results = "results";
    final static String MOVIE_REVIEW_id = "id";
    final static String MOVIE_REVIEW_author = "author";
    final static String MOVIE_REVIEW_content = "content";
    final static String MOVIE_REVIEW_url = "url";

    public static MovieListings parseMovieListingsJson(String json) {
        JSONObject movieListingsJson = null;
        MovieListings movieListings = null;
        try {
            movieListingsJson = new JSONObject(json);
            movieListings = new MovieListings();
            //JSONObject movieListingsObject = movieListingsJson.getJSONObject();
            movieListings.setPage(movieListingsJson.getInt(MOVIE_LISTINGS_PAGE));
            movieListings.setTotalResults(movieListingsJson.getInt(MOVIE_LISTINGS_TOTAL_RESULTS));
            movieListings.setTotalPages(movieListingsJson.getInt(MOVIE_LISTINGS_TOTAL_PAGES));
            movieListings.setMoviesList(getMoviesListProperty(movieListingsJson, MOVIE_LISTINGS_RESULTS));

        } catch(JSONException jsonEx){
            jsonEx.printStackTrace();
        }
        return movieListings;
    }

    private static List<Movie> getMoviesListProperty(JSONObject jsonObj, String propertyName) throws JSONException {
        JSONArray propertyArray = jsonObj.getJSONArray(propertyName);
        List<Movie> objectProperty = new ArrayList<Movie>();

        for (int i = 0; i < propertyArray.length(); i++) {
            objectProperty.add(parseMovieJson(propertyArray.getString(i)));
        }
        return objectProperty;
    }

    private static Movie parseMovieJson(String json) {
        JSONObject movieJson = null;
        Movie movie = null;
        try {
            movieJson = new JSONObject(json);
            movie = new Movie();

            movie.setPosterPath(movieJson.getString(MOVIE_POSTER_PATH));
            movie.setOverview(movieJson.getString(MOVIE_OVERVIEW));
            movie.setReleaseDate(movieJson.getString(MOVIE_RELEASE_DATE));
            movie.setId(movieJson.getInt(MOVIE_ID));
            movie.setOriginalTitle(movieJson.getString(MOVIE_ORIGINAL_TITLE));
            movie.setTitle(movieJson.getString(MOVIE_TITLE));
            movie.setVoteAverage(movieJson.getInt(MOVIE_VOTE_AVERAGE));

            /*
            movie.setGenreIds(getIntegerListProperty(movieJson, MOVIE_GENRE_IDS));
            movie.setAdult(movieJson.getBoolean(MOVIE_ADULT));
            movie.setOriginalLanguage(movieJson.getString(MOVIE_ORIGINAL_LANGUAGE));
            movie.setBackdropPath(movieJson.getString(MOVIE_BACKDROP_PATH));
            movie.setPopularity(movieJson.getInt(MOVIE_POPULARITY));
            movie.setVoteCount(movieJson.getInt(MOVIE_VOTE_COUNT));
            movie.setVideo(movieJson.getBoolean(MOVIE_VIDEO));
            */
        } catch(JSONException jsonEx){
            jsonEx.printStackTrace();
        }
        return movie;
    }

    public static MovieVideoDetails parseMovieVideoDetailsJson(String json) {
        JSONObject movieVideoDetailsJson = null;
        MovieVideoDetails movieVideoDetails = null;
        try {
            movieVideoDetailsJson = new JSONObject(json);
            movieVideoDetails = new MovieVideoDetails();
            movieVideoDetails.setMovieVideos(getMovieVideosListProperty(movieVideoDetailsJson, MOVIE_VIDEO_results));
        } catch(JSONException jsonEx){
            jsonEx.printStackTrace();
        }
        return movieVideoDetails;
    }

    private static List<MovieVideoDetails.MovieVideo> getMovieVideosListProperty(JSONObject jsonObj, String propertyName) throws JSONException {
        JSONArray propertyArray = jsonObj.getJSONArray(propertyName);
        List<MovieVideoDetails.MovieVideo> objectProperty = new ArrayList<MovieVideoDetails.MovieVideo>();

        for (int i = 0; i < propertyArray.length(); i++) {
            objectProperty.add(parseMovieVideosJson(propertyArray.getString(i)));
        }
        return objectProperty;
    }


    private static MovieVideoDetails.MovieVideo parseMovieVideosJson(String json) {
        JSONObject movieVideoJson = null;
        MovieVideoDetails.MovieVideo movieVideo = null;
        try {
            movieVideoJson = new JSONObject(json);
            movieVideo = new MovieVideoDetails.MovieVideo();

            movieVideo.setId(movieVideoJson.getString(MOVIE_VIDEO_id));
            movieVideo.setIso_6391(movieVideoJson.getString(MOVIE_VIDEO_iso_6391));
            movieVideo.setIso_3166_1(movieVideoJson.getString(MOVIE_VIDEO_iso_3166_1));
            movieVideo.setKey(movieVideoJson.getString(MOVIE_VIDEO_key));
            movieVideo.setName(movieVideoJson.getString(MOVIE_VIDEO_name));
            movieVideo.setSite(movieVideoJson.getString(MOVIE_VIDEO_site));
            movieVideo.setSize(movieVideoJson.getInt(MOVIE_VIDEO_size));
            movieVideo.setType(movieVideoJson.getString(MOVIE_VIDEO_type));
        } catch (JSONException jex){
            jex.printStackTrace();
        }
        return movieVideo;
    }

    public static MovieReviewDetails parseMovieReviewDetailsJson(String json) {
        JSONObject movieReviewDetailsJson = null;
        MovieReviewDetails movieReviewDetails = null;
        try {
            movieReviewDetailsJson = new JSONObject(json);
            movieReviewDetails = new MovieReviewDetails();
            movieReviewDetails.setMovieReviews(getMovieReviewsListProperty(movieReviewDetailsJson, MOVIE_REVIEW_results));
        } catch(JSONException jsonEx){
            jsonEx.printStackTrace();
        }
        return movieReviewDetails;
    }

    private static List<MovieReviewDetails.MovieReview> getMovieReviewsListProperty(JSONObject jsonObj, String propertyName) throws JSONException {
        JSONArray propertyArray = jsonObj.getJSONArray(propertyName);
        List<MovieReviewDetails.MovieReview> objectProperty = new ArrayList<MovieReviewDetails.MovieReview>();

        for (int i = 0; i < propertyArray.length(); i++) {
            objectProperty.add(parseMovieReviewsJson(propertyArray.getString(i)));
        }
        return objectProperty;
    }

    private static MovieReviewDetails.MovieReview parseMovieReviewsJson(String json) {
        JSONObject movieReviewJson = null;
        MovieReviewDetails.MovieReview movieReview = null;
        try {
            movieReviewJson = new JSONObject(json);
            movieReview = new MovieReviewDetails.MovieReview();

            movieReview.setId(movieReviewJson.getString(MOVIE_REVIEW_id));
            movieReview.setAuthor(movieReviewJson.getString(MOVIE_REVIEW_author));
            movieReview.setContent(movieReviewJson.getString(MOVIE_REVIEW_content));
            movieReview.setUrl(movieReviewJson.getString(MOVIE_REVIEW_url));
        } catch(JSONException jsonEx){
            jsonEx.printStackTrace();
        }
        return movieReview;
    }

    private static List<String> getStringListProperty(JSONObject jsonObj, String propertyName) throws JSONException {
        JSONArray propertyArray = jsonObj.getJSONArray(propertyName);
        List<String> objectProperty = new ArrayList<String>();

        for (int i = 0; i < propertyArray.length(); i++) {
            objectProperty.add(propertyArray.getString(i));
        }
        return objectProperty;
    }

    private static List<Integer> getIntegerListProperty(JSONObject jsonObj, String propertyName) throws JSONException {
        JSONArray propertyArray = jsonObj.getJSONArray(propertyName);
        List<Integer> objectProperty = new ArrayList<Integer>();

        for (int i = 0; i < propertyArray.length(); i++) {
            objectProperty.add(propertyArray.getInt(i));
        }
        return objectProperty;
    }

}