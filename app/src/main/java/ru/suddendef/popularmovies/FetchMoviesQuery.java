package ru.suddendef.popularmovies;

import android.os.AsyncTask;

import java.util.Collection;

/**
 * Created by suddendef on 04/04/2017.
 */
abstract public class FetchMoviesQuery extends AsyncTask<Void, Void, Collection<TheMovieDbService.MovieData>> {
    protected FetchDataCompleteListener listener;

    public FetchMoviesQuery(FetchDataCompleteListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPostExecute(Collection<TheMovieDbService.MovieData> movies) {
        this.listener.onFetchDataTaskComplete(movies);
        super.onPostExecute(movies);
    }
}
