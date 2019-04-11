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
import com.udacity.popularmovies.model.MovieReviewDetails;
import com.udacity.popularmovies.model.MovieReviewDetails;
import com.udacity.popularmovies.utils.NetworkUtils;

import java.util.List;

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.MovieReviewsAdapterViewHolder> {

    private static final String TAG = MovieReviewsAdapter.class.getSimpleName();
    private Movie mSelectedMovie;

    public MovieReviewDetails getmSelectedMovieReviewDetails() {
        return mSelectedMovieReviewDetails;
    }

    public void setSelectedMovieReviewDetails(MovieReviewDetails selectedMovieReviewDetails) {
        this.mSelectedMovieReviewDetails = selectedMovieReviewDetails;
        Log.d(TAG, "in moviereviewsadapter, this.mSelectedMovieReviewDetails"+this.mSelectedMovieReviewDetails);
        //Log.d(TAG, "in moviereviewsadapter, this.mSelectedMovieReviewDetails getItemCount():"+getItemCount());

        notifyDataSetChanged();
    }

    private MovieReviewDetails mSelectedMovieReviewDetails;

    /**
     *
     */
    public class MovieReviewsAdapterViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTvMovieReviewAuthor;
        private final TextView mTvMovieReviewContent;
        private Context mContext;

        public TextView getTvMovieReviewAuthor() {
            return mTvMovieReviewAuthor;
        }

        public TextView getTvMovieReviewContent() {
            return mTvMovieReviewContent;
        }

        public MovieReviewsAdapterViewHolder(View view) {
            super(view);
            mTvMovieReviewAuthor = (TextView) view.findViewById(R.id.tv_movie_review_author);
            mTvMovieReviewContent = (TextView) view.findViewById(R.id.tv_movie_review_content);
            mContext = view.getContext();
            //view.setOnClickListener(this);
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
    public MovieReviewsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_reviews;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieReviewsAdapter.MovieReviewsAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the movie
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param  movieReviewsAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MovieReviewsAdapterViewHolder movieReviewsAdapterViewHolder, int position) {
        if(null != mSelectedMovieReviewDetails && null != mSelectedMovieReviewDetails.getMovieReviews()) {
            MovieReviewDetails.MovieReview selectedMovieReview = mSelectedMovieReviewDetails.getMovieReviews().get(position);
            //Picasso.with(popularMoviesAdapterViewHolder.getmContext()).load(
            //NetworkUtils.getImageUrl(selectedMovie.getPosterPath())).into(popularMoviesAdapterViewHolder.getmMovieImageView());
            movieReviewsAdapterViewHolder.getTvMovieReviewAuthor().setText(selectedMovieReview.getAuthor());
            movieReviewsAdapterViewHolder.getTvMovieReviewContent().setText(selectedMovieReview.getContent());
            //movieReviewsAdapterViewHolder.getmIvReviewPlay().setText(selectedMovie.getOverview());
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
        if (null == mSelectedMovieReviewDetails || null == mSelectedMovieReviewDetails.getMovieReviews()) {
            Log.d(TAG, "in movie reviews adapter, itemCount inside null: "+itemCount);
            return itemCount;
        }
        //In this iteration of code, I have not fetched all pages from the movies database. Only the first page with 20 movies will be fetched and displayed.
        itemCount = mSelectedMovieReviewDetails.getMovieReviews().size();
        Log.d(TAG, "in movie reviews adapter, itemCount: "+itemCount);
        return itemCount;
    }


    public MovieReviewDetails.MovieReview getSelectedMovieReview(int index) {
        MovieReviewDetails.MovieReview movieReview;
        if(null == mSelectedMovieReviewDetails || null == mSelectedMovieReviewDetails.getMovieReviews())
            movieReview = null;
        else
            movieReview = mSelectedMovieReviewDetails.getMovieReviews().get(index);
        Log.d(TAG, "in movie reviews adapter, moviereview: "+movieReview);
        return movieReview;
    }

}
