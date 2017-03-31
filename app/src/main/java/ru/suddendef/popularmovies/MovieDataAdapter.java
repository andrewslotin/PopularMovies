package ru.suddendef.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        holder.originalTitleView.setText(movie.getOriginalTitle());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView originalTitleView;

        public ViewHolder(View itemView) {
            super(itemView);
            originalTitleView = (TextView) itemView.findViewById(R.id.tv_movie_title);
        }
    }
}
