package com.uqam.inf5171;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

public class GoogleApi {

    private final String KEY = "AIzaSyCmzFiJqWHGY66Gu8acevWwsR2Hq--hh4U";

    public GoogleApi(){}
    
    public JSONObject getDistance(String originPostalCode, String destinationPostalCode) {

        JSONObject response = new JSONObject();
        String searchInfo = "";
        String errorMsg = "";

        try{
            originPostalCode = originPostalCode.replaceAll("\\s+","");
            destinationPostalCode = destinationPostalCode.replaceAll("\\s+","");
            searchInfo = "Origin (" + originPostalCode + ") --­> Destination (" + destinationPostalCode + ")";

            String url = "https://maps.googleapis.com/maps/api/directions/json";
            url += "?origin=" + originPostalCode + "&destination=" + destinationPostalCode + "&key=" + KEY;

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");

            String dataReceived = convertResponseToString(con.getInputStream());
            response = responseAsJson(dataReceived);

        }catch(UnknownHostException ex){
            errorMsg = "Check your internet connection and try again: " + searchInfo;
        }catch(Exception ex){
            errorMsg = "An incident occurred: " + searchInfo;
        }finally{
            boolean success = (response != null) && !response.isEmpty();
            return sendResponse(response, searchInfo, errorMsg, success);
        } 
    }

    public JSONObject responseAsJson(String response) throws IOException {
        JSONObject responseToSend = new JSONObject();
        JSONParser parser = new JSONParser();

        try{
            JSONObject dataReceived = (JSONObject) parser.parse(response);
            JSONArray routes = (JSONArray) dataReceived.get("routes");
            JSONObject routesObj = (JSONObject) routes.get(0);
            JSONArray legs = (JSONArray) routesObj.get("legs");

            responseToSend = (JSONObject) legs.get(0);
        }catch(Exception ex){

        }finally{
            return responseToSend;
        }
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

    private JSONObject sendResponse(JSONObject response, String searchInfo, 
                                    String errorMsg, boolean success){
                                        
        int status = success ? 200 : 404;
        String message = success ? "Successful request": "Failed request";

        if(response == null) response = new JSONObject();
        
        response.put("statusCode", status);
        response.put("message", message);
        response.put("error", errorMsg);
        response.put("searchInfo", searchInfo);  

        return response;      
    }        
}