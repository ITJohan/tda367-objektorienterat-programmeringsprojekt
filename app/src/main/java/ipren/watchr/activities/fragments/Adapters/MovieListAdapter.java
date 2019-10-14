package ipren.watchr.activities.fragments.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ipren.watchr.R;
import ipren.watchr.activities.fragments.MovieListFragmentDirections;
import ipren.watchr.activities.fragments.listeners.MovieClickListener;
import ipren.watchr.dataHolders.Movie;
import ipren.watchr.databinding.ItemMovieBinding;

/**
 * Class for handling the creation and updating of movie cards in the recycler view
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> implements MovieClickListener, Filterable {

    private List<Movie> movieList;
    private List<Movie> movieListFull;

    /**
     * Creates a movie adapter with a list of movies
     */
    public MovieListAdapter(List<Movie> movieList) {
        this.movieList = movieList;
        // Create a copy of the list so we can filter the other
        movieListFull = new ArrayList<>(movieList);
    }

    /**
     * Clears and updates the list and layouts with new content
     */
    public void updateMovieList(List<Movie> newMovieList) {
        movieList.clear();
        movieList.addAll(newMovieList);
        notifyDataSetChanged();
    }

    /**
     * Inflates a movie card layout and returns it in a view holder
     */
    @NonNull
    @Override
    public MovieListAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemMovieBinding view = DataBindingUtil.inflate(inflater, R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    /**
     * Gets called automatically to update a view holder when scrolling
     */
    @Override
    public void onBindViewHolder(@NonNull MovieListAdapter.MovieViewHolder holder, int position) {
        holder.itemView.setMovie(movieList.get(position));
        holder.itemView.setListener(this);
    }

    /**
     * Navigates to the detail screen when card is clicked
     */
    @Override
    public void onMovieClicked(View v) {
        int id = getMovieId(v);

        MovieListFragmentDirections.ActionDetail action = MovieListFragmentDirections.actionDetail();
        action.setMovieId(id);
        Navigation.findNavController(v).navigate(action);
    }

    /**
     * Adds/removes the movie to the favorite list
     */
    @Override
    public void onFavoriteClicked(View v) {
        ConstraintLayout parent = (ConstraintLayout) v.getParent();
        int id = getMovieId(parent);
        // TODO: Fix multiple buttons being highlighted because ViewHolders is being reused
        changeButtonColor((ImageButton) v, R.color.colorAccent);

        Toast.makeText(v.getContext(), "Added id " + id + " to favorites", Toast.LENGTH_SHORT).show();
    }

    /**
     * Adds/removes the movie to the watch later list
     */
    @Override
    public void onWatchLaterClicked(View v) {
        ConstraintLayout parent = (ConstraintLayout) v.getParent();
        int id = getMovieId(parent);

        changeButtonColor((ImageButton) v, R.color.colorAccent);

        Toast.makeText(v.getContext(), "Added id " + id + " to watch later", Toast.LENGTH_SHORT).show();
    }

    /**
     * Adds/removes the movie to the watched list
     */
    @Override
    public void onWatchedClicked(View v) {
        ConstraintLayout parent = (ConstraintLayout) v.getParent();
        int id = getMovieId(parent);

        changeButtonColor((ImageButton) v, R.color.colorAccent);

        Toast.makeText(v.getContext(), "Added id " + id + " to watched", Toast.LENGTH_SHORT).show();
    }

    /**
     * Returns the current movie id from context
     */
    private int getMovieId(View v) {
        String idString = ((TextView) v.findViewById(R.id.movieId)).getText().toString();
        return Integer.valueOf(idString);
    }

    /**
     * Changes the color of the button
     */
    private void changeButtonColor(ImageButton v, int color) {
        ImageButton btn = v;
        btn.setColorFilter(btn.getContext().getResources().getColor(color));
    }

    /**
     * Returns the size of the movie array
     */
    @Override
    public int getItemCount() {
        return movieList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            return null;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

        }
    };

    /**
     * Class for holding a movie view layout
     */
    class MovieViewHolder extends RecyclerView.ViewHolder {

        public ItemMovieBinding itemView;

        public MovieViewHolder(@NonNull ItemMovieBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }
    }
}
