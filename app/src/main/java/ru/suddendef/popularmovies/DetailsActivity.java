package ru.suddendef.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE_ID = "ru.suddendef.popularmovies.MOVIE_ID";

    private class FetchMovieQuery extends AsyncTask<Integer, Void, TheMovieDbService.MovieData> {
        @Override
        protected TheMovieDbService.MovieData doInBackground(Integer... params) {
            return movieDb.getMovieById(params[0]);
        }

        @Override
        protected void onPostExecute(TheMovieDbService.MovieData movieData) {
            setMovieData(movieData);
            super.onPostExecute(movieData);
        }
    }

    private TheMovieDbService movieDb;
    private ActionBar actionBar;
    private ImageView moviePosterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        actionBar = getSupportActionBar();
        moviePosterView = (ImageView) findViewById(R.id.iv_movie_poster_large);
        movieDb = new TheMovieDbService(getString(R.string.api_key));

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_MOVIE_ID)) {
            int movieId = intent.getIntExtra(EXTRA_MOVIE_ID, 0);
            if (movieId > 0) {
                new FetchMovieQuery().execute(movieId);
            }
        }
    }

    protected void setMovieData(TheMovieDbService.MovieData movie) {
        actionBar.setTitle(movie.getOriginalTitle());
        Picasso.with(this)
                .load(movie.getPosterUrlString(TheMovieDbService.THUMBNAIL))
                .fit()
                .into(moviePosterView);
    }
}
