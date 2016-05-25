package com.example.place_its.test;

import android.test.InstrumentationTestSuite;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllPlaceitTests extends TestSuite{

	public static Test suite() {
		TestSuite suite = new TestSuite(AllPlaceitTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(BirdsEyeTest.class);
		suite.addTestSuite(CategoricalTest.class);
		suite.addTestSuite(MainTester.class);
		suite.addTestSuite(PlaceitClickCreateAndDelete.class);
		suite.addTestSuite(PlaceitMinorTest.class);
		//$JUnit-END$
		return suite;
	}

}
