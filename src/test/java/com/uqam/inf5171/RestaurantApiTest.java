package com.uqam.inf5171;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class RestaurantApiTest extends TestCase {
    
    public RestaurantApiTest(String testName) {
        super(testName);
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(RestaurantApiTest.class);
        return suite;
    }
    
    public void testGetTheNearestBySequential() {
        System.out.println("getTheNearestBySequential");
        
        long numberOfRestaurant = 50;
        MockRestaurantApi mock = new MockRestaurantApi(new GoogleApi(), numberOfRestaurant);
        
        String origin = "H8R2Y7";
        int begin = 0;
        int end = mock.getListOfRestaurants().size() - 1;
        
        JSONObject result = mock.getTheNearestBySequential(origin, begin, end);
        
        assertEquals(result.get("name"), "Restaurant_No_3");
        assertEquals(result.get("start_address"), "Lasalle, QC H8R 2Y7, Canada");
        assertEquals(result.get("end_address"), "Hampstead, QC H3X 3K3, Canada");
        
        JSONObject duration = (JSONObject) result.get("duration");
        
        assertEquals(duration.get("text"), "19 mins");
        assertEquals((long) duration.get("value"), 1163);
        
        origin = "H2L 2C4";        
        result = mock.getTheNearestBySequential(origin, begin, end);
        
        assertEquals(result.get("name"), "Restaurant_No_2");
        assertEquals(result.get("start_address"), "Montreal, QC H2L 2C4, Canada");
        assertEquals(result.get("end_address"), "Montreal, QC H2X 2K2, Canada");
        
        duration = (JSONObject) result.get("duration");
        
        assertEquals(duration.get("text"), "5 mins");
        assertEquals((long) duration.get("value"), 320);       
    }

    public void testGetTheNearestByParallel() {
        System.out.println("getTheNearestByParallel");
        
        long numberOfRestaurant = 50;
        MockRestaurantApi mock = new MockRestaurantApi(new GoogleApi(), numberOfRestaurant);
        
        String origin = "H8R2Y7";
        int begin = 0;
        int end = mock.getListOfRestaurants().size() - 1;
        int grainsize = 1;
        
        JSONObject sequentialRes = mock.getTheNearestBySequential(origin, begin, end);
        JSONObject parallelRes = mock.getTheNearestByParallel(origin, begin, end, grainsize);
        
        assertEquals(sequentialRes.get("name"), parallelRes.get("name"));
    }

    @org.junit.Test
    public void testReadRestaurants() {
        System.out.println("readRestaurants");
        MockRestaurantApi mock = new MockRestaurantApi(new GoogleApi(), 10);
        JSONArray expResult = mock.getListOfRestaurants();
        JSONArray result = mock.readRestaurants();
        
        assertEquals(expResult.size(), result.size());
        assertEquals(expResult, result);
    }
}
