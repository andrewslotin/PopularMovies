package ru.suddendef.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by suddendef on 31/03/2017.
 */

public class MovieDataAdapter extends RecyclerView.Adapter<MovieDataAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<TheMovieDbService.MovieData> movies;

    public MovieDataAdapter(Context context, ArrayList<TheMovieDbService.MovieData> objects) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.movies = objects;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.movie_poster, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TheMovieDbService.MovieData movie = movies.get(position);
        Picasso.with(context)
                .load(movie.getPosterPath(TheMovieDbService.THUMBNAIL))
                .fit()
                .into(holder.moviePosterView);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setMovies(Collection<TheMovieDbService.MovieData> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView moviePosterView;

        public ViewHolder(View itemView) {
            super(itemView);
            moviePosterView= (ImageView) itemView.findViewById(R.id.iv_movie_poster);
        }
    }
}
