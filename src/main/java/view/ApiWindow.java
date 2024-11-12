package view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class ApiWindow {
    private void callVerifyApi() {
        try {
            String url = "https://api.veryfi.com/";
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Client-Id", "vrfXfZ5Ku1K0KP8bvYnMiK5okYAmnKPGknsRAy7");
            connection.setRequestProperty("Authorization", "apikey toryn.chua:93619ea1e45fe7bd802d45a41e05bd85");

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code : " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Display response in the text area
                JSONObject jsonResponse = new JSONObject(response.toString());
                System.out.println(jsonResponse.toString(2));
            } else {
                System.out.println("API request failed with code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred: " + e.getMessage());

        }
        }
}
