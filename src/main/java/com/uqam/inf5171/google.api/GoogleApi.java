package com.uqam.inf5171.google.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

public class GoogleApi {

    private final String key = "AIzaSyCmzFiJqWHGY66Gu8acevWwsR2Hq--hh4U";

    public GoogleApi(){}
    
    public void getDistance(String originPostalCode, String destinationPostalCode) {
        try{
            originPostalCode = originPostalCode.replaceAll("\\s+","");
            destinationPostalCode = destinationPostalCode.replaceAll("\\s+","");

            String url = "https://maps.googleapis.com/maps/api/directions/json";
            url += "?origin=" + originPostalCode + "&destination=" + destinationPostalCode + "&key=" + key;

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            System.out.println("Response Code : " + responseCode);

            String response = convertResponseToString(con.getInputStream());
            System.out.println(response);        
        }catch(UnknownHostException ex){
            System.out.println("Error: Check your internet connection and try again ...");
        }catch(Exception ex){
            System.out.println("Error: An incident occurred. Please try again !");
        }
        
        // JSONObject jsonObj = new JSONObject("{\"phonetype\":\"N95\",\"cat\":\"WP\"}");
    }

    private String convertResponseToString(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();

        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }
}