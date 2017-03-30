package ru.suddendef.popularmovies;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by suddendef on 31/03/2017.
 */

public class TheMovieDbService {
    private static String BASE_URL = "https://api.themoviedb.org/3";
    private final String apiKey;

    public class MovieData {
        private String originalTitle;
        private String posterPath;

        public MovieData(String originalTitle, String posterPath) {
            this.originalTitle = originalTitle;
            this.posterPath = posterPath;
        }

        public String getOriginalTitle() {
            return originalTitle;
        }

        public String getPosterPath() {
            return posterPath;
        }
    }

    public TheMovieDbService(String apiKey) {
        this.apiKey = apiKey;
    }

    public MovieData[] popularMovies() {
        MovieData[] movies = null;

        try {
            JSONArray results = new JSONObject(getApiResponse("/movie/popular")).getJSONArray("results");
            movies = new MovieData[results.length()];

            for (int i = 0; i < results.length(); i++) {

                JSONObject result = null;
                result = results.getJSONObject(i);

                movies[i] = new MovieData(result.getString("original_title"), result.getString("poster_path"));
            }
        } catch (IOException e) {
            Log.d("TheMovieDbService", "failed to fetch popular movies", e);
        } catch (JSONException e) {
            Log.d("TheMovieDbService", "unexpected response format", e);
        }

        return movies;
    }

    protected String getApiResponse(String apiMethod) throws IOException {
        URL url = getApiUrl(apiMethod);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        String data = "";
        try {
            InputStream in = connection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            if (scanner.hasNext()) {
                data = scanner.next();
            }
        } finally {
            connection.disconnect();
        }

        return data;
    }

    private URL getApiUrl(String apiMethod) throws MalformedURLException {
        Uri uri = Uri.parse(BASE_URL + apiMethod).buildUpon()
                .appendQueryParameter("api_key", apiKey)
                .build();

        return new URL(uri.toString());
    }
}
