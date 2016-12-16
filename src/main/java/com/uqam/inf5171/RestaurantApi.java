package com.uqam.inf5171;

import java.io.*;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

public class RestaurantApi {

    private final static String PATH = "src/resources/restaurants.json";

    protected final GoogleApi googleApi;
    protected JSONArray listOfRestaurants;
    protected ForkJoinPool threadsPool;
    
    public RestaurantApi(GoogleApi googleApi){
        this.googleApi = googleApi;
        this.threadsPool = new ForkJoinPool();
        listOfRestaurants = readRestaurants();
    }

    public JSONObject getTheNearestRestaurant(String origin, int grainsize){
        int begin = 0;
        int end = listOfRestaurants.size() - 1;
        boolean modeSequential =  (grainsize <= 0);
        
        JSONObject results = new JSONObject();
        
        if(modeSequential){
            results = getTheNearestBySequential(origin, begin, end);
        }else{
            threadsPool = new ForkJoinPool(listOfRestaurants.size() / 2);
            results = getTheNearestByParallel(origin, begin, end, grainsize);
            threadsPool.shutdown();
        }
        return results;
    }


    public JSONObject getTheNearestBySequential(String origin, int begin, int end){
        JSONObject theRestaurant = null;

        try{
            for(int i = begin; i <= end; ++i){
                JSONObject restaurant = (JSONObject) listOfRestaurants.get(i);
                JSONObject response = googleApi.getDistance(origin, (String) restaurant.get("postal_code"));

                boolean isNearest = (Integer) response.get("statusCode") == 200 
                                     && isTheNewRestaurantNearest(theRestaurant, response);
                
                if(isNearest) {
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

    public JSONObject getTheNearestByParallel(String origin, int begin, int end, int grainsize){
        if(end - begin + 1 <= grainsize){
            return getTheNearestBySequential(origin, begin, end);
        }else{
            int mid = (begin + end) / 2;

            Future leftPromise = threadsPool.submit(() -> getTheNearestByParallel(origin, begin, mid, grainsize)
            );
            JSONObject right = getTheNearestByParallel(origin, mid + 1, end, grainsize);
            JSONObject left = new JSONObject();
            try{ 
                left = (JSONObject) leftPromise.get();
            }catch(Exception e){}

            return isTheNewRestaurantNearest(left, right) ? right : left; 
        }
    }

    public JSONArray getListOfRestaurants() {
        return listOfRestaurants;
    }
    
        public int restaurantsSize(){
        return (listOfRestaurants == null) ? 0 : listOfRestaurants.size();
    }
    
    public void setListOfRestaurants(JSONArray listOfRestaurants) {
        if(listOfRestaurants != null){
            this.listOfRestaurants = listOfRestaurants;
        }
    }

    private boolean isTheNewRestaurantNearest(JSONObject oldRestaurant, JSONObject newRestaurant){

        if(newRestaurant == null) return false;
        if(oldRestaurant == null) return true;

        JSONObject oldDuration = (JSONObject) oldRestaurant.get("duration");
        JSONObject newDuration = (JSONObject) newRestaurant.get("duration");   

        if(oldDuration == null || newDuration == null) return false;

        if((Long) oldDuration.get("value") <= (Long) newDuration.get("value")) {
            return false;
        }

        return true;             
    }
    
    protected JSONArray readRestaurants(){
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

    protected void displayListOfRestaurants(JSONArray theListOfRestaurants){
        Iterator<JSONObject> iterator = theListOfRestaurants.iterator();
        while (iterator.hasNext()) {
            JSONObject restaurant = iterator.next(); 
            System.out.println("Name:" + restaurant.get("name") 
                          + " - Postal Code:" + restaurant.get("postal_code"));
        }
    }       
}