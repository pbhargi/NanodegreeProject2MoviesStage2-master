/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.udacity.popularmovies.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the weather servers.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_MOVIES_URL = "https://api.themoviedb.org/3/movie/";
    private static final String POPULAR_MOVIES_URL =
            BASE_MOVIES_URL+"popular";

    private static final String TOP_RATED_MOVIES_URL =
            BASE_MOVIES_URL+"top_rated";

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE = "w185";

    private static final String MOVIES_BASE_URL = POPULAR_MOVIES_URL;

    /* The format we want our API to return */
    private static final String format = "json";
    /* The units we want our API to return */
    private static final String units = "metric";
    /* The number of days we want our API to return */
    private static final int numDays = 14;

    final static String API_KEY_PARAM = "api_key";
    //REMOVE THIS BEFORE SUBMISSION
    final static String API_KEY_PARAM_VALUE = "190e6cfbb906244152cfc643f3bb8217";
    final static String LANGUAGE_PARAM = "language";
    final static String LANGUAGE_PARAM_EN_US_VALUE = "en-US";
    final static String PAGE_PARAM = "page";
    final static String PAGE_PARAM_ONE_VALUE = "1";

    public static final Integer POPULAR_MOVIES = 1;
    public static final Integer TOP_RATED_MOVIES = 2;
    public static final Integer FAVORITE_MOVIES = 3;

    public static final String YOUTUBE_APP_URI = "vnd.youtube:";
    public static final String YOUTUBE_BROWSER_URI = "http://www.youtube.com/watch?v=";

    final static Map<Integer, String> kindOfMovieListMap = new HashMap<Integer, String>();

    static {
        kindOfMovieListMap.put(POPULAR_MOVIES, POPULAR_MOVIES_URL);
        kindOfMovieListMap.put(TOP_RATED_MOVIES, TOP_RATED_MOVIES_URL);
        kindOfMovieListMap.put(FAVORITE_MOVIES, null);
    }

    public static String getMovieListKindURL(Integer kindOfMoviesList) {
        String movieListURL = kindOfMovieListMap.get(kindOfMoviesList);
        if (movieListURL != null)
            return movieListURL;
        else
            return MOVIES_BASE_URL;
    }

    public static String getImageUrl(String posterPathReturned) {
        return IMAGE_BASE_URL + IMAGE_SIZE + posterPathReturned;

    }

    public static String getMovieVideosUrl(int movieID) {
        //"/videos?api_key=<<api key>>&language=en-US
        return BASE_MOVIES_URL + movieID + "/videos";
    }

    public static String getMovieReviewsUrl(int movieID) {
        //https://api.themoviedb.org/3/movie/{movie_id}/reviews?api_key=<<api_key>>&language=en-US&page=1
        return BASE_MOVIES_URL + movieID + "/reviews";
    }

    /**
     * @param
     * @return
     */
    public static URL buildUrl(String location) {
        Uri builtUri = Uri.parse(location).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY_PARAM_VALUE)
                .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE_PARAM_EN_US_VALUE)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        if(isConnected()) {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = urlConnection.getInputStream();

                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    return scanner.next();
                } else {
                    return null;
                }
            } finally {
                urlConnection.disconnect();
            }
        } else return null;
    }

    public static boolean isConnected() {
        try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
            sock.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}