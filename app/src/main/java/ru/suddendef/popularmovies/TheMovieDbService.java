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
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p";

    public static final String THUMBNAIL = "/w185";
    public static final String LARGE = "/w500";

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
        private int id;
        private String originalTitle;
        private String posterPath;
        private double userRating;
        private String releaseDate;
        private String overview;

        public MovieData(int id, String originalTitle, String releaseDate, double userRating, String posterPath, String overview) {
            this.id = id;
            this.originalTitle = originalTitle;
            this.releaseDate = releaseDate;
            this.userRating = userRating;
            this.posterPath = posterPath;
            this.overview = overview;
        }

        public int getId() {
            return id;
        }

        public String getOriginalTitle() {
            return originalTitle;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public String getPosterPath() {
            return posterPath;
        }

        public double getUserRating() {
            return userRating;
        }

        public String getOverview() {
            return overview;
        }

        public String getPosterUrlString(String size) {
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

    public MovieData getMovieById(int movieId) {
        MovieData movie = null;
        String movieIdString = String.valueOf(movieId);

        try {
            String response = getApiResponse("/movie/" + movieIdString);
            movie = decodeMovieJSON(new JSONObject(response));
        } catch (IOException e) {
            Log.d("TheMovieDbService", "failed to fetch movie id:" + movieIdString, e);
        } catch (JSONException e) {
            Log.d("TheMovieDbService", "/movie/" + movieIdString + " responded with non-JSON", e);
        }

        return movie;
    }

    protected void appendMoviesFromJSON(String jsonData, ArrayList<MovieData> movies) throws IOException, JSONException {
        JSONArray results = new JSONObject(jsonData).getJSONArray("results");

        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            movies.add(decodeMovieJSON(result));
        }
    }

    protected MovieData decodeMovieJSON(JSONObject jsonObject) throws JSONException {
        int movieId = jsonObject.getInt("id");
        String originalTitle = jsonObject.getString("original_title");
        double userRating = jsonObject.getDouble("vote_average");
        String posterPath = jsonObject.getString("poster_path");
        String releaseDate = jsonObject.getString("release_date");
        String overview = jsonObject.getString("overview");

        return new MovieData(movieId, originalTitle, releaseDate, userRating, posterPath, overview);
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
