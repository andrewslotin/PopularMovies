package ru.suddendef.popularmovies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.Locale;

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
    private TextView releaseDateView;
    private TextView userRatingView;
    private TextView overviewView;

    private final Transformation keepAspectRationTransformation = new Transformation() {
        @Override
        public Bitmap transform(Bitmap source) {
            double aspectRatio = (double) source.getHeight() / source.getWidth();
            int targetWidth = moviePosterView.getWidth();
            int targetHeight = (int) (targetWidth * aspectRatio);

            Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
            if (result != source) {
                source.recycle();
            }

            return result;
        }

        @Override
        public String key() {
            return "transformation keepAspectRatio";
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        actionBar = getSupportActionBar();
        moviePosterView = (ImageView) findViewById(R.id.iv_movie_poster_large);
        releaseDateView = (TextView) findViewById(R.id.tv_release_date);
        userRatingView = (TextView) findViewById(R.id.tv_user_rating);
        overviewView = (TextView) findViewById(R.id.tv_overview);

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

        releaseDateView.setText(movie.getReleaseDate());
        userRatingView.setText(String.format(Locale.getDefault(), getString(R.string.user_rating_format), movie.getUserRating()));
        overviewView.setText(movie.getOverview());

        Picasso.with(this)
                .load(movie.getPosterUrlString(TheMovieDbService.LARGE))
                .transform(keepAspectRationTransformation)
                .into(moviePosterView);
    }
}
