package com.udacity.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Network;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.udacity.popularmovies.database.AppDatabase;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.model.MovieListings;
import com.udacity.popularmovies.utils.JsonUtils;
import com.udacity.popularmovies.utils.NetworkUtils;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity
                                implements PopularMoviesAdapter.PopularMoviesAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    // COMPLETED (35) Add a private ForecastAdapter variable called mForecastAdapter
    private PopularMoviesAdapter mPopularMoviesAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    public static String MENU_SELECTED_KEY = "MENU_SELECTED";
    private static Integer mMenuSelected = 0;
    public static String IS_MOVIE_FAVORITE = "IS_MOVIE_FAVORITE";

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "On Create Called");
        setContentView(R.layout.movies_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);

        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        // COMPLETED (38) Create layoutManager, a LinearLayoutManager with VERTICAL orientation and shouldReverseLayout == false
        /*
         * LinearLayoutManager can support HORIZONTAL or VERTICAL orientations. The reverse layout
         * parameter is useful mostly for HORIZONTAL layouts that should reverse for right to left
         * languages.
         */
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        // COMPLETED (41) Set the layoutManager on mRecyclerView
        //mRecyclerView.setLayoutManager(layoutManager);

        // COMPLETED (42) Use setHasFixedSize(true) on mRecyclerView to designate that all items in the list will have the same size
        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mRecyclerView.setHasFixedSize(true);

        // COMPLETED (43) set mForecastAdapter equal to a new ForecastAdapter
        /*
         * The ForecastAdapter is responsible for linking our weather data with the Views that
         * will end up displaying our weather data.
         */
        mPopularMoviesAdapter = new PopularMoviesAdapter(this);

        // COMPLETED (44) Use mRecyclerView.setAdapter and pass in mForecastAdapter
        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(mPopularMoviesAdapter);

        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         *
         * Please note: This so called "ProgressBar" isn't a bar by default. It is more of a
         * circle. We didn't make the rules (or the names of Views), we just follow them.
         */
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mDb = AppDatabase.getInstance(getApplicationContext());
        //setupViewModel();

        Log.d(TAG, "In OnCreate of mainactivity, getintent : " + getIntent());
        /* Once all of our views are setup, we can load the movies data. */
        mMenuSelected = NetworkUtils.POPULAR_MOVIES;
        if(getIntent()!=null) {
            mMenuSelected = getIntent().getIntExtra(MainActivity.MENU_SELECTED_KEY, NetworkUtils.POPULAR_MOVIES);
            //if intent was indeed sent from detail activity, the value for this extra would be favorites.
            Log.d(TAG, "In OnCreate of mainactivity, getintent is not null, MENU_SELECTED_KEY: " + mMenuSelected);
        }
        if(mMenuSelected!=NetworkUtils.FAVORITE_MOVIES && savedInstanceState!=null && savedInstanceState.get(MENU_SELECTED_KEY)!=null) {
            mMenuSelected = savedInstanceState.getInt(MENU_SELECTED_KEY);
            Log.d(TAG, "In OnCreate of mainactivity savedinstance is not null, MENU_SELECTED_KEY: " + mMenuSelected);
        }

        Log.d(TAG, "In OnCreate of mainactivity, final MENU_SELECTED_KEY: " + mMenuSelected);
        loadMoviesData(mMenuSelected);
    }

    private MainViewModel getMainViewModel(){
        return ViewModelProviders.of(this).get(MainViewModel.class);
    }

    private void loadFavoriteMoviesData() {
        final MainViewModel viewModel = getMainViewModel();
        viewModel.getFavoriteMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> favoriteMovies) {
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
                Log.d(TAG, "In OnChanged method with mPopularMoviesAdapter.getmMenuSelected = "+mPopularMoviesAdapter.getmMenuSelected());
                MovieListings movieListings = new MovieListings(favoriteMovies);
                mPopularMoviesAdapter.setMoviesData(movieListings);
                //viewModel.getFavoriteMovies().removeObserver(this);
                //loadMoviesData(mPopularMoviesAdapter.getmMenuSelected());
            }
        });
    }

    /**
     * This method will get the user's preferred location for weather, and then tell some
     * background method to get the weather data in the background.
     */
    private void loadMoviesData(Integer movieListKind) {
        showMoviesDataView();
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mPopularMoviesAdapter.setmMenuSelected(movieListKind);
        if(movieListKind!=null && (NetworkUtils.POPULAR_MOVIES.equals(movieListKind) || NetworkUtils.TOP_RATED_MOVIES.equals(movieListKind))){
            String location = NetworkUtils.getMovieListKindURL(movieListKind);
            new FetchMoviesTask().execute(location);
        } else
            loadFavoriteMoviesData();
    }

    /**
     * This method is overridden by our MainActivity class in order to handle RecyclerView item
     * clicks.
     *
     * @param selectedMovieIndex The movie that was clicked
     */
    @Override
    public void onClick(String selectedMovieIndex) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        Log.d(TAG, "in onclick of mainactivity intentToStartDetailActivity: "+intentToStartDetailActivity);
        Log.d(TAG, "in onclick of mainactivity selectedMovieIndex: "+selectedMovieIndex);
        Movie selectedMovieObj = mPopularMoviesAdapter.getMovieDetails(Integer.parseInt(selectedMovieIndex));
        Log.d(TAG, "in onclick of mainactivity selectedMovieObj: "+selectedMovieObj);
        intentToStartDetailActivity.putExtra("serialize_data", selectedMovieObj);
        intentToStartDetailActivity.putExtra(IS_MOVIE_FAVORITE, getMainViewModel().isFavorite(selectedMovieObj));
        startActivity(intentToStartDetailActivity);
    }

    /**
     * This method will make the View for the weather data visible and
     * hide the error message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showMoviesDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the weather
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, MovieListings> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected MovieListings doInBackground(String... params) {

            String location = params[0];
            URL moviesRequestUrl = NetworkUtils.buildUrl(location);

            try {
                String jsonMoviesResponse = NetworkUtils
                        .getResponseFromHttpUrl(moviesRequestUrl);

                return JsonUtils.parseMovieListingsJson(jsonMoviesResponse);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(MovieListings movieListings) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieListings != null) {
                //showWeatherDataView();
                mPopularMoviesAdapter.setMoviesData(movieListings);
            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.movies_listtype, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_popular) {
            mPopularMoviesAdapter.setMoviesData(null);
            mMenuSelected = NetworkUtils.POPULAR_MOVIES;
            loadMoviesData(NetworkUtils.POPULAR_MOVIES);
            return true;
        }

        if (id == R.id.action_top_rated) {
            mPopularMoviesAdapter.setMoviesData(null);
            mMenuSelected = NetworkUtils.TOP_RATED_MOVIES;
            loadMoviesData(NetworkUtils.TOP_RATED_MOVIES);
            return true;
        }

        if (id == R.id.action_favorites) {
            mPopularMoviesAdapter.setMoviesData(null);
            mMenuSelected = NetworkUtils.FAVORITE_MOVIES;
            loadFavoriteMoviesData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putInt(MENU_SELECTED_KEY, mMenuSelected);
        //savedInstanceState.putInt(STATE_LEVEL, currentLevel);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
}
