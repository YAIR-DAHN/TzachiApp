package com.shahareinisim.tzachiapp.Utils;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ElevationFetcher {

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static final Handler handler = new Handler(Looper.getMainLooper());
    private static final int RESPONSE_CODE_SUCCESS = 200;
    private static final double ERROR_ELEVATION = -1.0; // Indicate error in elevation fetching

    public static void getElevation(double latitude, double longitude, ElevationCallback callback) {
        executorService.execute(() -> {
            double elevation = fetchElevation(latitude, longitude);
            handler.post(() -> callback.onElevationFetched(elevation));
        });
    }

    private static double fetchElevation(double latitude, double longitude) {
        @SuppressLint("DefaultLocale")
        String requestUrl = String.format("https://api.open-elevation.com/api/v1/lookup?locations=%f,%f", latitude, longitude);

        try {
            URL url = new URL(requestUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            int responseCode = urlConnection.getResponseCode();
            if (responseCode != RESPONSE_CODE_SUCCESS) {
                System.err.println("HTTP request failed with response code: " + responseCode);
                return ERROR_ELEVATION;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
            return jsonObject.getAsJsonArray("results")
                    .get(0).getAsJsonObject()
                    .get("elevation").getAsDouble();
        } catch (Exception e) {
            System.err.println("Failed to fetch elevation: " + e.getMessage());
            return ERROR_ELEVATION;
        }
    }

    public interface ElevationCallback {
        void onElevationFetched(double elevation);
    }
}