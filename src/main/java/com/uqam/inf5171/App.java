package com.uqam.inf5171;

import com.uqam.inf5171.google.api.GoogleApi;

public class App 
{
    public static void main( String[] args )
    {
        GoogleApi api = new GoogleApi();
        
        api.getDistance("H8R2Y7", "H8N 3E4");  
    }
}
