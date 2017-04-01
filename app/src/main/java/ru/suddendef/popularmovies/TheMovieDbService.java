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
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by suddendef on 31/03/2017.
 */

public class TheMovieDbService {
    private static String BASE_URL = "https://api.themoviedb.org/3";
    private static String BASE_IMAGE_URL = "http://image.tmdb.org/t/p";

    public static String THUMBNAIL = "/w185";

    private final String apiKey;

    public class UnexpectedResponseCode extends Exception {
        private int responseCode;

        public UnexpectedResponseCode(String request, int responseCode) {
            super(request + " responded with HTTP " + String.valueOf(responseCode));
            this.responseCode = responseCode;
        }

        public int getResponseCode() {
            return responseCode;
        }
    }

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

        public String getPosterPath(String size) {
            return BASE_IMAGE_URL + size + posterPath;
        }
    }

    public TheMovieDbService(String apiKey) {
        this.apiKey = apiKey;
    }

    public ArrayList<MovieData> popularMovies() {
        ArrayList<MovieData> movies = new ArrayList<>();

        try {
            String response = getApiResponse("/movie/popular");
            appendMoviesFromJSON(response, movies);
        } catch (IOException e) {
            Log.d("TheMovieDbService", "failed to fetch popular movies", e);
        } catch (JSONException e) {
            Log.d("TheMovieDbService", "/movie/popular responded with non-JSON", e);
        }

        return movies;
    }

    public ArrayList<MovieData> topRatedMovies() {
        ArrayList<MovieData> movies = new ArrayList<>();

        try {
            String response = getApiResponse("/movie/top_rated");
            appendMoviesFromJSON(response, movies);
        } catch (IOException e) {
            Log.d("TheMovieDbService", "failed to fetch top rated movies", e);
        } catch (JSONException e) {
            Log.d("TheMovieDbService", "/movie/top_rated responded with non-JSON", e);
        }

        return movies;
    }

    protected void appendMoviesFromJSON(String jsonData, ArrayList<MovieData> movies) throws IOException, JSONException {
        JSONArray results = new JSONObject(jsonData).getJSONArray("results");

        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            movies.add(decodeMovieJSON(result));
        }
    }

    protected MovieData decodeMovieJSON(JSONObject jsonObject) throws JSONException {
        String originalTitle = jsonObject.getString("original_title");
        String posterPath = jsonObject.getString("poster_path");

        return new MovieData(originalTitle, posterPath);
    }

    protected String getApiResponse(String apiMethod) throws IOException {
        URL url = getApiUrl(apiMethod);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        String data = "";
        try {
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new UnexpectedResponseCode(apiMethod, responseCode);
            }

            InputStream in = connection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            if (scanner.hasNext()) {
                data = scanner.next();
            }
        } catch (UnexpectedResponseCode e) {
            Log.d("TheMovieDbService", "", e);
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
