package ru.suddendef.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends AppCompatActivity {
    abstract private class FetchMoviesQuery extends AsyncTask<Void, Void, Collection<TheMovieDbService.MovieData>> {
        @Override
        protected void onPostExecute(Collection<TheMovieDbService.MovieData> movies) {
            moviesAdapter.setMovies(movies);
            super.onPostExecute(movies);
        }
    }

    private class MostPopularMoviesQuery extends FetchMoviesQuery {
        @Override
        protected Collection<TheMovieDbService.MovieData> doInBackground(Void... params) {
            return movieDb.popularMovies();
        }
    }

    private class TopRatedMoviesQuery extends FetchMoviesQuery {
        @Override
        protected Collection<TheMovieDbService.MovieData> doInBackground(Void... params) {
            return movieDb.topRatedMovies();
        }
    }

    private TheMovieDbService movieDb;
    private MovieDataAdapter moviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        moviesAdapter = new MovieDataAdapter(this, new ArrayList<TheMovieDbService.MovieData>());

        RecyclerView moviePostersView = (RecyclerView) findViewById(R.id.rv_movie_posters);
        moviePostersView.setLayoutManager(layoutManager);
        moviePostersView.setAdapter(moviesAdapter);
        moviePostersView.setHasFixedSize(true);

        movieDb = new TheMovieDbService(getString(R.string.api_key));
        new MostPopularMoviesQuery().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_most_popular:
                new MostPopularMoviesQuery().execute();
                return true;
            case R.id.action_top_rated:
                new TopRatedMoviesQuery().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
