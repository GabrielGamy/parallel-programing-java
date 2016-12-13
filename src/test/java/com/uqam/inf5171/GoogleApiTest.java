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
        String originPostalCode = "";
        String destinationPostalCode = "";
        GoogleApi instance = new GoogleApi();
        JSONObject expResult = null;
        JSONObject result = instance.getDistance(originPostalCode, destinationPostalCode);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
