package com.uqam.inf5171;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class GoogleApiTest extends TestCase {
    
    public GoogleApiTest(String testName) {
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
        TestSuite suite = new TestSuite(GoogleApiTest.class);
        return suite;
    }

    public void testGetDistance() {
        System.out.println("getDistance");
        String originPostalCode = "H2Y 1J1";
        String inexistingDestinationPostalCode = "H0X0K0";
        String existingDestinationPostalCode = "H1X1K1";
        GoogleApi googleApi = new GoogleApi();
 
        JSONObject result = googleApi.getDistance(originPostalCode, inexistingDestinationPostalCode);

        assertEquals((int) result.get("statusCode"), 404);
        assertEquals(result.get("message"), "Failed request");
        
        result = googleApi.getDistance(originPostalCode, existingDestinationPostalCode);
        
        assertEquals((int) result.get("statusCode"), 200);
        assertEquals(result.get("message"), "Successful request");
    }
    
}
