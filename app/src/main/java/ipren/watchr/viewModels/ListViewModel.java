package ipren.watchr.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.Arrays;
import java.util.List;

import ipren.watchr.dataHolders.Genre;
import ipren.watchr.dataHolders.Movie;
import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.IMovieRepository;
import ipren.watchr.repository.IUserDataRepository;

import static java.lang.Integer.parseInt;

/**
 * The view model for the movies lists, handling the data conversion
 */
public class ListViewModel extends ViewModel {

    // Repositories
    private IMovieRepository movieRepository;
    private IUserDataRepository userRepository;

    // Live data from user repo
    private LiveData<User> user;
    private LiveData<String[]> favoritesIds;
    private LiveData<String[]> watchLaterIds;
    private LiveData<String[]> watchedIds;

    // Live data from movie repo
    private LiveData<List<Movie>> movies;

    public ListViewModel() {
        userRepository = IUserDataRepository.getInstance();
        // TODO: @johan Fixa detta!!
        movieRepository = IMovieRepository.getInstance();
        user = userRepository.getUserLiveData();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public LiveData<User> getUser() {
        return user;
    }

    public void getMoviesFromQuery(String query) {
        movies = movieRepository.Search(query, 1, false);
    }

    /**
     * Converts an array of integer strings to an array of ints
     */
    private int[] convertStringArrayToIntArray(String[] strArr) {
        int[] intArr = new int[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            intArr[i] = parseInt(strArr[i]);
        }
        return intArr;
    }

    public void initBrowse() {
        movies = movieRepository.getMovieList(IMovieRepository.TRENDING_LIST, 1, false);
    }

    public void initRecommended() {
        movies = movieRepository.getDiscoverList(new int[]{27}, 1, false);
    }

    public void initMovieIdLists() {
        String userId = user.getValue().getUID();
        watchedIds = userRepository.getMovieList(IUserDataRepository.WATCHED_LIST, userId);
        watchLaterIds = userRepository.getMovieList(IUserDataRepository.WATCH_LATER_LIST, userId);
        favoritesIds = userRepository.getMovieList(IUserDataRepository.FAVORITES_LIST, userId);
    }

    public LiveData<String[]> getWatchedIds() {
        return watchedIds;
    }

    public LiveData<String[]> getWatchLaterIds() {
        return watchLaterIds;
    }

    public LiveData<String[]> getFavoritesIds() {
        return favoritesIds;
    }

    public void initUserMovieList(String[] movieIds) {
        int[] movieIdsInt = convertStringArrayToIntArray(movieIds);
        movies = movieRepository.getMoviesByID(movieIdsInt);
    }

    public LiveData<List<Genre>> getGenres(int movieId) {
        return movieRepository.getGenresFromMovie(movieId);
    }

    public void removeMovieFromList(String movieId, String listType) {
        userRepository.removeMovieFromList(listType, movieId, user.getValue().getUID(), v -> {});
    }

    public void addMovieToList(String movieId, String listType) {
        userRepository.addMovieToList(listType, movieId, user.getValue().getUID(), v -> {});
    }
}
