package ru.suddendef.popularmovies;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;

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

    public TheMovieDbService(String apiKey) {
        this.apiKey = apiKey;
    }

    public String popularMovies() {
        String response = "";
        try {
            response = getApiResponse("/movie/popular");
        } catch (IOException e) {
            Log.d("TheMovieDbService", "failed to fetch popular movies", e);
        }

        return response;
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
