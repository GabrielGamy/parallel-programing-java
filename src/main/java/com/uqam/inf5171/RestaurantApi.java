package com.uqam.inf5171;

import java.io.*;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

public class RestaurantApi {

    private final static String PATH = "src/resources/restaurants.json";

    private GoogleApi googleApi;
    private JSONArray listOfRestaurants;
    
    public RestaurantApi(GoogleApi googleApi){
        this.googleApi = googleApi;
        listOfRestaurants = readRestaurants();
    }

    public JSONObject getTheNearestRestaurant(String origin, boolean modeSequential){
        if(modeSequential){
            return getTheNearestBySequential(origin, getListOfRestaurants());
        }else{
            return getTheNearestByParallel(origin, getListOfRestaurants());
        }
    }


    public JSONObject getTheNearestBySequential(String origin, JSONArray listOfRestaurants){
        JSONObject theRestaurant = null;

        try{
            Iterator<JSONObject> iterator = listOfRestaurants.iterator();
            while (iterator.hasNext()) {
                JSONObject restaurant = iterator.next();
                JSONObject response = googleApi.getDistance(origin, (String) restaurant.get("postal_code"));

                boolean isNewRestaurantNearest = (Integer) response.get("statusCode") == 200 
                                                 && isTheNewRestaurantNearest(theRestaurant, response);

                if(isNewRestaurantNearest) {
                    theRestaurant = response;
                    theRestaurant.put("name", restaurant.get("name"));
                }          
            }
        }catch(Exception ex){
            System.out.println("Unable to get the distance : " + ex.getMessage());
        }finally{
            theRestaurant = (theRestaurant != null) ? theRestaurant : new JSONObject();
            return theRestaurant;
        }
    }


    public JSONObject getTheNearestByParallel(String origin, JSONArray listOfRestaurants){
        return new JSONObject();
    }
    
    private boolean isTheNewRestaurantNearest(JSONObject oldRestaurant, JSONObject newRestaurant){

        if(newRestaurant == null) return false;

        if(oldRestaurant == null) return true;

        JSONObject oldDuration = (JSONObject) oldRestaurant.get("duration");
        JSONObject newDuration = (JSONObject) newRestaurant.get("duration");   

        if(oldDuration == null || newDuration == null) return false;

        if((Long) oldDuration.get("value") < (Long) newDuration.get("value")) {
            return false;
        }

        return true;             
    }

    private JSONArray getListOfRestaurants() {
        return listOfRestaurants;
    }

    private void setListOfRestaurants(JSONArray listOfRestaurants) {
        if(listOfRestaurants != null){
            this.listOfRestaurants = listOfRestaurants;
        }
    }

    private JSONArray readRestaurants(){
        JSONParser parser = new JSONParser();
        JSONArray restaurants = new JSONArray();

        try {

            Object obj = parser.parse(new FileReader(
                new File(PATH).getAbsolutePath()
            ));
 
            JSONObject data = (JSONObject) obj;
            restaurants = (JSONArray) data.get("restaurants");
 
        } catch (Exception e) {
            System.out.println("Unable to load restaurant list : " + e.getMessage());
        }finally{
            restaurants = (restaurants != null) ? restaurants : new JSONArray();
            return restaurants;
        }
    }

    private void displayListOfRestaurants(JSONArray theListOfRestaurants){
        Iterator<JSONObject> iterator = theListOfRestaurants.iterator();
        while (iterator.hasNext()) {
            JSONObject restaurant = iterator.next(); 
            System.out.println(restaurant.get("name"));
        }
    }       
}