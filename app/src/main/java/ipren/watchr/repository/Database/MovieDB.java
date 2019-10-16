package ipren.watchr.repository.Database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import ipren.watchr.Helpers.DateConverter;
import ipren.watchr.dataHolders.Actor;
import ipren.watchr.dataHolders.Comment;
import ipren.watchr.dataHolders.Genre;
import ipren.watchr.dataHolders.GenreList;
import ipren.watchr.dataHolders.Movie;
import ipren.watchr.dataHolders.MovieGenreJoin;
import ipren.watchr.dataHolders.MovieList;
import ipren.watchr.repository.API.IMovieApi;
import ipren.watchr.repository.API.MovieApi;
import retrofit2.Call;
import retrofit2.Response;

@Database(entities = {Movie.class, Actor.class, Comment.class, Genre.class, MovieGenreJoin.class, MovieList.class}, version = 12)
@TypeConverters(DateConverter.class)
public abstract class MovieDB extends RoomDatabase {
    private static MovieDB INSTANCE; // Database instance
    private static String DB_NAME = "Ipren-Database";
    // This callback is called when the DB is created.
    private static Callback dbCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    // Singleton
    public static synchronized MovieDB getInstance(Context context) {
        if (INSTANCE != null)
            return INSTANCE;

        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                MovieDB.class, DB_NAME)
                .fallbackToDestructiveMigration()
                .addCallback(dbCallback)
                .build();
        return INSTANCE;

    }

    public abstract ActorDao actorDao();

    public abstract MovieDao movieDao();

    public abstract CommentDao commentDao();

    public abstract GenreDao genreDao();

    public abstract MovieGenreJoinDao movieGenreJoinDao();

    public abstract MovieListDao movieListDao();

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final GenreDao genreDao;
        private final IMovieApi movieApi;

        PopulateDbAsync(MovieDB db) {
            genreDao = db.genreDao();
            movieApi = new MovieApi();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            insertGenres();
            return null;
        }

        private void insertGenres() {
            // Download the full genrelist
            movieApi.getAllGenres().enqueue(new retrofit2.Callback<GenreList>() {
                @Override
                public void onResponse(Call<GenreList> call, Response<GenreList> response) {
                    if (!response.isSuccessful()) {
                        Log.d("MOVIE", "Error getting genrelist!");
                        return;
                    }
                    new Thread(() -> {
                        genreDao.insert(response.body().getGenres());
                    }).start();
                }

                @Override
                public void onFailure(Call<GenreList> call, Throwable t) {

                }
            });
        }
    }
}