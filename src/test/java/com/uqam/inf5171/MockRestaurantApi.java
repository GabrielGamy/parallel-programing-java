package com.uqam.inf5171;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class MockRestaurantApi extends RestaurantApi {
    
    private long numberOfRestaurant;
    
    public MockRestaurantApi(GoogleApi googleApi, long numberOfRestaurant) {
        super(googleApi);
        this.numberOfRestaurant = numberOfRestaurant;
        this.listOfRestaurants = readRestaurants();
    }
    
    @Override
    public JSONArray readRestaurants(){
        JSONArray restaurants = new JSONArray();
        
        for(long i = 0; i < numberOfRestaurant; ++i){
            JSONObject aRestaurant = new JSONObject();
            String name = "Restaurant_No_" + i;
            String postal_code = "H" + (i % 10) + "X" + (i % 10) + "K" + (i % 10);
            
            aRestaurant.put("name", name);
            aRestaurant.put("postal_code",postal_code);
            
            restaurants.add(aRestaurant);
        }
        
        return restaurants;
    }
    
}
