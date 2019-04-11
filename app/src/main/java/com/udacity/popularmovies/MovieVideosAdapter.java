package com.udacity.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.model.MovieListings;
import com.udacity.popularmovies.model.MovieVideoDetails;
import com.udacity.popularmovies.utils.NetworkUtils;

import java.util.List;

public class MovieVideosAdapter extends RecyclerView.Adapter<MovieVideosAdapter.MovieVideosAdapterViewHolder> {

    private static final String TAG = MovieVideosAdapter.class.getSimpleName();
    private Movie mSelectedMovie;

    private MovieVideoDetails mSelectedMovieVideoDetails;

    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final MovieVideosAdapterOnClickHandler mClickHandler;

    public MovieVideoDetails getmSelectedMovieVideoDetails() {
        return mSelectedMovieVideoDetails;
    }

    public void setmSelectedMovieVideoDetails(MovieVideoDetails mSelectedMovieVideoDetails) {
        Log.d(TAG, "in movievideosadapter, mSelectedMovieVideoDetails"+mSelectedMovieVideoDetails);
        this.mSelectedMovieVideoDetails = mSelectedMovieVideoDetails;
        notifyDataSetChanged();
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface MovieVideosAdapterOnClickHandler {
        void onClick(String selectedMovieVideo);
    }

    /**
     * Creates a ForecastAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public MovieVideosAdapter(MovieVideosAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    /**
     *
     */
    public class MovieVideosAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView mIvVideoPlay;

        public ImageView getmIvVideoPlay() {
            return mIvVideoPlay;
        }

        public TextView getmTvMovieVideoTitle() {
            return mTvMovieVideoTitle;
        }

        private final TextView mTvMovieVideoTitle;

        private Context mContext;

        public MovieVideosAdapterViewHolder(View view) {
            super(view);
            mIvVideoPlay = (ImageView) view.findViewById(R.id.iv_video_play);
            mTvMovieVideoTitle = (TextView) view.findViewById(R.id.tv_movie_video_desc);
            mContext = view.getContext();
            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            //MovieListings.Movie movieSelected = moviesListData.get(adapterPosition);
            mClickHandler.onClick(adapterPosition+"");
        }

        public Context getmContext() {
            return mContext;
        }

    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new ForecastAdapterViewHolder that holds the View for each list item
     */
    @Override
    public MovieVideosAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_videos;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieVideosAdapter.MovieVideosAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the movie
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param  movieVideosAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MovieVideosAdapterViewHolder movieVideosAdapterViewHolder, int position) {
        if(null != mSelectedMovieVideoDetails && null != mSelectedMovieVideoDetails.getMovieVideos()) {
            MovieVideoDetails.MovieVideo selectedMovieVideo = mSelectedMovieVideoDetails.getMovieVideos().get(position);
            //Picasso.with(popularMoviesAdapterViewHolder.getmContext()).load(
                    //NetworkUtils.getImageUrl(selectedMovie.getPosterPath())).into(popularMoviesAdapterViewHolder.getmMovieImageView());
            movieVideosAdapterViewHolder.getmTvMovieVideoTitle().setText(selectedMovieVideo.getName());
            //movieVideosAdapterViewHolder.getmIvVideoPlay().setText(selectedMovie.getOverview());
        }
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (null == mSelectedMovieVideoDetails || null == mSelectedMovieVideoDetails.getMovieVideos()) {
            Log.d(TAG, "in movie videos adapter, itemCount: "+itemCount);
            return itemCount;
        }
        //In this iteration of code, I have not fetched all pages from the movies database. Only the first page with 20 movies will be fetched and displayed.
        itemCount = mSelectedMovieVideoDetails.getMovieVideos().size();
        Log.d(TAG, "in movie videos adapter, itemCount: "+itemCount);
        return itemCount;
    }


    public MovieVideoDetails.MovieVideo getSelectedMovieVideo(int index) {
        MovieVideoDetails.MovieVideo movieVideo;
        if(null == mSelectedMovieVideoDetails || null == mSelectedMovieVideoDetails.getMovieVideos())
            movieVideo = null;
        else
            movieVideo = mSelectedMovieVideoDetails.getMovieVideos().get(index);
        Log.d(TAG, "in movie videos adapter, movievideo: "+movieVideo);
        return movieVideo;
    }

}
