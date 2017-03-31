package ru.suddendef.popularmovies;

gimport android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
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
    }
}
