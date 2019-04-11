package com.udacity.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.database.AppDatabase;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.model.MovieReviewDetails;
import com.udacity.popularmovies.model.MovieVideoDetails;
import com.udacity.popularmovies.utils.JsonUtils;
import com.udacity.popularmovies.utils.NetworkUtils;

import java.net.URL;

public class DetailActivity extends AppCompatActivity
        implements MovieVideosAdapter.MovieVideosAdapterOnClickHandler {

    private static final String TAG = DetailActivity.class.getSimpleName();

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private RecyclerView mVideosRecyclerView;
    private MovieVideosAdapter mMovieVideosAdapter;
    private RecyclerView mReviewsRecyclerView;
    private MovieReviewsAdapter mMovieReviewsAdapter;

    Movie mSelectedMovieObj;
    int mSelectedMovieID;
    boolean mIsMovieFavorite = false;
    Button mButton;

    private TextView mVideoErrorMessageDisplay;
    private ProgressBar mVideoLoadingIndicator;

    private TextView mReviewErrorMessageDisplay;
    private ProgressBar mReviewLoadingIndicator;

    // Member variable for the Database
    private AppDatabase mDb;
    private static final int DEFAULT_MOVIE_ID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        mVideoErrorMessageDisplay = (TextView) findViewById(R.id.tv_video_error_message_display);
        mVideoLoadingIndicator = (ProgressBar) findViewById(R.id.pb_video_loading_indicator);

        mReviewErrorMessageDisplay = (TextView) findViewById(R.id.tv_review_error_message_display);
        mReviewLoadingIndicator = (ProgressBar) findViewById(R.id.pb_review_loading_indicator);

        setupVideoDetailRecycleView();
        setupReviewDetailRecycleView();

        Intent intent = getIntent();

        if (savedInstanceState != null && savedInstanceState.getSerializable(SELECTED_MOVIE)!=null){
            mSelectedMovieObj = (Movie) savedInstanceState.getSerializable(SELECTED_MOVIE);
            Log.d(TAG, "in detailactivity from savedinstancestate mSelectedMovieObj: "+mSelectedMovieObj);
            mIsMovieFavorite = savedInstanceState.getBoolean(MainActivity.IS_MOVIE_FAVORITE);
            Log.d(TAG, "in detailactivity from savedinstancestate mIsMovieFavorite: "+mIsMovieFavorite);
            savedInstanceState.clear();
        } else if (intent != null) {
            mSelectedMovieObj = (Movie) getIntent().getSerializableExtra("serialize_data");
            Log.d(TAG, "in detailactivity from intent mSelectedMovieObj: "+mSelectedMovieObj);
            mIsMovieFavorite = getIntent().getBooleanExtra(MainActivity.IS_MOVIE_FAVORITE, false);
            Log.d(TAG, "in detailactivity from intent mIsMovieFavorite: "+mIsMovieFavorite);
        } else {
            Log.d(TAG, "in detailactivity both savesinstancestate and intent are null closeOnError");
            closeOnError();
        }

        if (mSelectedMovieObj == null) {
            // Movie data unavailable
            closeOnError();
            return;
        }

        mSelectedMovieID = mSelectedMovieObj.getId();
        Log.d(TAG, "in detailactivity onCreate mSelectedMovieObj: "+mSelectedMovieObj);
        Log.d(TAG, "in detailactivity onCreate selectedMovieID: "+mSelectedMovieID);
        Log.d(TAG, "in detailactivity onCreate mIsMovieFavorite: "+mIsMovieFavorite);

        mDb = AppDatabase.getInstance(getApplicationContext());
        Log.d(TAG, "in detailactivity selectedMovieID after getting mDb: "+mSelectedMovieID);

        loadMovieVideosData(mSelectedMovieID);
        loadMovieReviewsData(mSelectedMovieID);

        /*DetailViewModelFactory factory = new DetailViewModelFactory(mSelectedMovieObj);
        final DetailViewModel viewModel
                = ViewModelProviders.of(this, factory).get(DetailViewModel.class);
        mSelectedMovieObj = viewModel.getSelectedMovie();
        mMovieVideosAdapter.setMovieData(mSelectedMovieObj);*/

        // COMPLETED (12) Observe the LiveData object in the ViewModel. Use it also when removing the observer
        /*viewModel.getTask().observe(this, new Observer<TaskEntry>() {
            @Override
            public void onChanged(@Nullable TaskEntry taskEntry) {
                viewModel.getTask().removeObserver(this);
                populateUI(taskEntry);
            }
        });*/

        initMarkAsFavoriteFn();
        populateUI(mSelectedMovieObj);

    }

    private void setupVideoDetailRecycleView(){
        mVideosRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_videos);
        /*
         * LinearLayoutManager can support HORIZONTAL or VERTICAL orientations. The reverse layout
         * parameter is useful mostly for HORIZONTAL layouts that should reverse for right to left
         * languages.
         */
        mVideosRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //new GridLayoutManager(this, 1));
        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mVideosRecyclerView.setHasFixedSize(true);
        /*
         * The ForecastAdapter is responsible for linking our weather data with the Views that
         * will end up displaying our weather data.
         */
        mMovieVideosAdapter = new MovieVideosAdapter(this);
        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mVideosRecyclerView.setAdapter(mMovieVideosAdapter);
    }

    private void setupReviewDetailRecycleView(){
        mReviewsRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_reviews);
        /*
         * LinearLayoutManager can support HORIZONTAL or VERTICAL orientations. The reverse layout
         * parameter is useful mostly for HORIZONTAL layouts that should reverse for right to left
         * languages.
         */
        mReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mReviewsRecyclerView.setHasFixedSize(true);

        mMovieReviewsAdapter = new MovieReviewsAdapter();
        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mReviewsRecyclerView.setAdapter(mMovieReviewsAdapter);
    }

    /**
     * initViews is called from onCreate to init the member variable views
     */
    private void initMarkAsFavoriteFn() {
        mButton = findViewById(R.id.bt_mark_as_favorite);
        prepareFavoriteUIComponents(mIsMovieFavorite);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsMovieFavorite = !mIsMovieFavorite;
                prepareFavoriteUIComponents(mIsMovieFavorite);
                onMarkAsFavoriteButtonClicked();
                /*Class destinationClass = MainActivity.class;
                Intent intentToStartMainActivity = new Intent(getApplicationContext(), destinationClass);
                Log.d(TAG, "in detail activity intentToStartMainActivity:"+intentToStartMainActivity);
                intentToStartMainActivity.putExtra(MainActivity.MENU_SELECTED_KEY, NetworkUtils.FAVORITE_MOVIES);
                startActivity(intentToStartMainActivity);*/
            }
        });
    }

    private void prepareFavoriteUIComponents(boolean isMovieFavorite){
        ImageView favoriteStar = findViewById(R.id.iv_favorite_star);
        if(isMovieFavorite){
            mButton.setText(getString (R.string.remove_from_favorites));
            favoriteStar.setVisibility(View.VISIBLE);
        } else{
            mButton.setText(getString (R.string.mark_as_favorite));
            favoriteStar.setVisibility(View.GONE);
        }

    }
    private void onMarkAsFavoriteButtonClicked(){
        //final Movie movie = new Movie(description, priority, date);
        if(mSelectedMovieObj!=null) {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if(mIsMovieFavorite) {
                        mDb.favoriteMovieDao().insertFavoriteMovie(mSelectedMovieObj);
                        Log.d(TAG, "Calling insert favorite movie with movie: " + mSelectedMovieObj);
                    } else {
                        Log.d(TAG, "Calling delete favorite movie with movie: "+mSelectedMovieObj);
                        mDb.favoriteMovieDao().deleteMovieFromFavorites(mSelectedMovieObj);
                    }
                    //finish();
                }
            });
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Movie selectedMovie) {

        ImageView moviePosterIV = findViewById(R.id.iv_movie_poster);
        Picasso.with(this.getApplicationContext()).load(
                NetworkUtils.getImageUrl(selectedMovie.getPosterPath())).into(moviePosterIV);

        TextView originalTitleTV = findViewById(R.id.tv_original_title);
        originalTitleTV.setText(selectedMovie.getOriginalTitle());

        TextView movieOverviewTV = findViewById(R.id.tv_movie_overview);
        movieOverviewTV.setText(selectedMovie.getOverview());

        TextView userRatingTV = findViewById(R.id.tv_user_rating);
        userRatingTV.setText(String.valueOf(selectedMovie.getVoteAverage())+"/10");

        TextView releaseDateTV = findViewById(R.id.tv_release_date);
        releaseDateTV.setText(selectedMovie.getReleaseDate());

    }

    @Override
    public void onClick(String selectedMovieVideo) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartMovieVideoActivity = new Intent(context, destinationClass);
        MovieVideoDetails.MovieVideo selectedMovieVideoObj = mMovieVideosAdapter.getSelectedMovieVideo(Integer.parseInt(selectedMovieVideo));
        Log.d(TAG,"Call content provider with selected movie video: "+selectedMovieVideoObj);
        launchVideoOnYoutubeOrBrowser(selectedMovieVideoObj.getKey());
        //intentToStartDetailActivity.putExtra("serialize_data", selectedMovieObj);
        //startActivity(intentToStartDetailActivity);
    }

    private void launchVideoOnYoutubeOrBrowser(String videoKey){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NetworkUtils.YOUTUBE_APP_URI + videoKey));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NetworkUtils.YOUTUBE_BROWSER_URI + videoKey));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    private void loadMovieVideosData(int selectedMovieId){
        showMovieVideosDataView();
        new MovieVideosAsyncTask().execute(mSelectedMovieObj.getId());
    }

    private void showMovieVideosDataView() {
        /* First, make sure the error is invisible */
        mVideoErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the data is visible */
        mVideosRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showVideosErrorMessage() {
        /* First, hide the currently visible data */
        mVideosRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mVideoErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class MovieVideosAsyncTask extends AsyncTask<Integer,Void,MovieVideoDetails> {
        private final String TAG = this.getClass().getName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mVideoLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected MovieVideoDetails doInBackground(Integer... params) {
            Integer selectedMovieIdParam = params[0];
            Log.d(TAG, "inside doInBackground of detailactivity->LiveData->loadData, selectedMovieID: "+selectedMovieIdParam);
            URL movieVideosRequestUrl = NetworkUtils.buildUrl(NetworkUtils.getMovieVideosUrl(selectedMovieIdParam));

            Log.d(TAG, "inside doInBackground of detailactivity->LiveData->loadData, movieVideosRequestUrl: "+movieVideosRequestUrl);
            try {
                String jsonMovieVideosResponse = NetworkUtils.getResponseFromHttpUrl(movieVideosRequestUrl);
                MovieVideoDetails movieVideoDetails = JsonUtils.parseMovieVideoDetailsJson(jsonMovieVideosResponse);
                Log.d(TAG, "inside doInBackground of detailactivity->LiveData->loadData, movieVideoDetails.getMovieVideos(): "+movieVideoDetails.getMovieVideos());
                return movieVideoDetails;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(MovieVideoDetails movieVideoDetails) {
            mVideoLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieVideoDetails != null) {
                //showWeatherDataView();
                mMovieVideosAdapter.setmSelectedMovieVideoDetails(movieVideoDetails);
            } else {
                showVideosErrorMessage();
            }
        }
    }

    private void loadMovieReviewsData(int selectedMovieId){
        showMovieReviewsDataView();
        new MovieReviewsAsyncTask().execute(mSelectedMovieObj.getId());
    }

    private void showMovieReviewsDataView() {
        /* First, make sure the error is invisible */
        mReviewErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the data is visible */
        mReviewsRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showReviewErrorMessage() {
        /* First, hide the currently visible data */
        mReviewsRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mReviewErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class MovieReviewsAsyncTask extends AsyncTask<Integer,Void,MovieReviewDetails> {
        private final String TAG = this.getClass().getName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mReviewLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected MovieReviewDetails doInBackground(Integer... params) {
            Integer selectedMovieIdParam = params[0];
            Log.d(TAG, "inside doInBackground of MovieReviewsAsyncTask->LiveData->loadData, selectedMovieID: "+selectedMovieIdParam);
            URL movieReviewsRequestUrl = NetworkUtils.buildUrl(NetworkUtils.getMovieReviewsUrl(selectedMovieIdParam));

            Log.d(TAG, "inside doInBackground of MovieReviewsAsyncTask->LiveData->loadData, movieReviewsRequestUrl: "+movieReviewsRequestUrl);
            try {
                String jsonMovieReviewsResponse = NetworkUtils.getResponseFromHttpUrl(movieReviewsRequestUrl);
                MovieReviewDetails movieReviewDetails = JsonUtils.parseMovieReviewDetailsJson(jsonMovieReviewsResponse);
                Log.d(TAG, "inside doInBackground of jsonMovieReviewsResponse ->LiveData->loadData, movieReviewDetails.getMovieReviews(): "+movieReviewDetails.getMovieReviews());
                return movieReviewDetails;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(MovieReviewDetails movieReviewDetails) {
            mReviewLoadingIndicator.setVisibility(View.INVISIBLE);
            Log.d(TAG, "onPostExecute movieReviewDetails: "+movieReviewDetails);
            if (movieReviewDetails != null) {
                mMovieReviewsAdapter.setSelectedMovieReviewDetails(movieReviewDetails);
            } else {
                showReviewErrorMessage();
            }
        }
    }

    public static String SELECTED_MOVIE = "SELECTED_MOVIE";

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current state
        savedInstanceState.putSerializable(SELECTED_MOVIE, mSelectedMovieObj);
        savedInstanceState.putBoolean(MainActivity.IS_MOVIE_FAVORITE, mIsMovieFavorite);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
}