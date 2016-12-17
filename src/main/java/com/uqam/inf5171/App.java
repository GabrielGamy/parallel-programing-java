package com.uqam.inf5171;

import com.uqam.inf5171.benchmarksimulator.BenchmarkSimulator;
import org.json.simple.JSONObject;

public class App {

    private static RestaurantApi restauApi;
    private static BenchmarkSimulator benchmarkSimulator;

    public static void main(String[] args) {
        restauApi = new RestaurantApi(new GoogleApi());
        benchmarkSimulator = new BenchmarkSimulator();
        benchmarkSimulator.warmup();
        
        run();
    }
    
    private static void run(){
        System.out.println("\n----------------------------------");
        System.out.println("----- Mesure de temps -----");
        System.out.println("----------------------------------\n");

        System.out.format("\n%s%16s%16s\n\n", "NB_RESTAURANTS", "NB_THREADS", "TEMPS(sec)");
        
        for(int i = 0; i < restauApi.restaurantsSize(); i++){
            runAlgorithme(i);
        }    
    }
    
    public static void runAlgorithme(int grainsize) {
        String postal_codes[] = {"H3B 4G5", "H8N 1X1", "H2L 2C4", "K1N 6N5"};
         
        benchmarkSimulator.start();
        for (int i = 0; i < postal_codes.length; ++i) {
            JSONObject response = restauApi.getTheNearestRestaurant(postal_codes[i], grainsize);
        }
        benchmarkSimulator.end();
        
        String time = String.format( "%.2f", benchmarkSimulator.getTotalTime() )  + " sec";
        int nb_restaurants = restauApi.restaurantsSize();
        int nb_threads = (grainsize <= 0) ? 1 : (nb_restaurants / grainsize);
        
        System.out.format("%s%20s%24s\n", nb_restaurants, nb_threads, time);
    }
}
