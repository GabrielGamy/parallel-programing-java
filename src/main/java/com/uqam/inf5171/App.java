package com.uqam.inf5171;

import com.uqam.inf5171.google.api.GoogleApi;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class App 
{
    public static void main( String[] args )
    {
        GoogleApi api = new GoogleApi();
        
        JSONObject result = api.getDistance("H8R2Y7", "H8N 3E4"); 

        System.out.println("statusCode: " + result.get("statusCode"));
        System.out.println("message: " + result.get("message"));
        System.out.println("search: " + result.get("searchInfo"));
        System.out.println("error: " + result.get("error"));

        if((Integer) result.get("statusCode") == 200) {
            System.out.println("duration: " + result.get("duration"));
        }
    }
}
