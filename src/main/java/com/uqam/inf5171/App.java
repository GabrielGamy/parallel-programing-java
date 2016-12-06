package com.uqam.inf5171;

import org.json.simple.JSONObject;

public class App 
{
    public static void main( String[] args )
    {

        RestaurantApi restauApi = new RestaurantApi(new GoogleApi()); 
        JSONObject restaurant = restauApi.getTheNearestRestaurant("H8R2Y7", true); 

        System.out.println("name : " + restaurant.get("name"));
        System.out.println("duration : " + restaurant.get("duration"));      
    }
}
