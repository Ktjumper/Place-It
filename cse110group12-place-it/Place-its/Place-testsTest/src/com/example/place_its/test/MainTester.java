package com.example.place_its.test;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import place_its.MainActivity;
import place_its.PlaceItUtils;

import com.example.place_its.R;
import com.robotium.solo.Solo;

import android.graphics.Point;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.Display;
import android.widget.EditText;


public class MainTester extends ActivityInstrumentationTestCase2<MainActivity> {
	
	private Solo solo;
	
	public MainTester() {
		super(MainActivity.class);
	}

	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	

	public void testPlaceItAndReposting() throws Exception {
		
		solo.assertCurrentActivity("Main not loaded","MainActivity");
		solo.waitForActivity("MainActivity");
		solo.assertCurrentActivity("Main not loaded","MainActivity");
		
		final float WRITERWIDTH=1280;
		final float WRITERHEIGHT=800;
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		float scalarW = width/WRITERWIDTH;
		float scalarH = height/WRITERHEIGHT;
		
		solo.clickOnButton("Show Map");
		
		solo.hideSoftKeyboard();
		
		//Map should start displaying
		solo.waitForActivity("MapActivity");
		solo.assertCurrentActivity("Map not loaded","MapActivity");

		//Wait for screen to center, 10 seconds
		TimeUnit.MILLISECONDS.sleep(4000);
		
		solo.waitForActivity("MapActivity",5);
		solo.assertCurrentActivity("Map not loaded","MapActivity");
		
		solo.clickOnScreen(900*scalarW,400*scalarH);
		solo.clickOnScreen(900*scalarW,320*scalarH);
		//Should bring up our creation dialogue
		
		solo.waitForActivity("CreateRegularPlaceit");
		solo.assertCurrentActivity("Placeit not loaded","CreateRegularPlaceit");
		
		solo.clickOnButton("Cancel");
		solo.waitForActivity("MapActivity",5);
		solo.assertCurrentActivity("Map not loaded","MapActivity");
		
		solo.scrollToSide(Solo.LEFT);
		solo.scrollToSide(Solo.LEFT);
		//solo.scrollDown();
		
		solo.clickOnScreen(400*scalarW,400*scalarH);
		solo.clickOnScreen(400*scalarW,320*scalarH);
		
		solo.waitForActivity("CreateRegularPlaceit");
		solo.assertCurrentActivity("Placeit not loaded","CreateRegularPlaceit");
		EditText title = (EditText) solo.getCurrentActivity().findViewById(0x7f050012);
		solo.clickOnView(title);
		String strInput = "Foo"; //Title
		solo.typeText(title, strInput);
		boolean actual = solo.searchEditText(strInput);
		assertEquals("Title does not match entered text",true, actual);
		strInput = "Koo"; //Desc
		EditText desc = (EditText) solo.getCurrentActivity().findViewById(0x7f050013);
		solo.clickOnView(desc);
		solo.typeText(desc,strInput);
		actual = solo.searchEditText(strInput);
		assertEquals("Desc does not match entered text",true, actual);
		solo.assertCurrentActivity("Placeit not loaded","CreateRegularPlaceit");
		
		assertTrue(solo.searchEditText("Foo"));
		assertTrue(solo.searchEditText("Koo"));
		
		solo.assertCurrentActivity("Placeit not loaded","CreateRegularPlaceit");
		
		solo.clickOnButton("OK");
		solo.waitForActivity("MapActivity");
		solo.assertCurrentActivity("Map not loaded","MapActivity");
		/* Check for Place-It, click on Place-It */
		
		display = getActivity().getWindowManager().getDefaultDisplay();
		size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
		scalarW = width/WRITERWIDTH;
		scalarH = height/WRITERHEIGHT;
		
		TimeUnit.MILLISECONDS.sleep(4000);
		solo.clickOnScreen(400*scalarW,360*scalarH);
		solo.clickOnScreen(640*scalarW,430*scalarH);
		
		//Go back to main
		TimeUnit.MILLISECONDS.sleep(5000);
		//solo.goBack();
		try{
			solo.assertCurrentActivity("MapActivity not present", "MapActivity");
			TimeUnit.MILLISECONDS.sleep(5000);
			solo.goBack();
		}catch(Exception e){}catch(Error be){}
		TimeUnit.MILLISECONDS.sleep(5000);
		solo.assertCurrentActivity("MainActivity not present", "MainActivity");
		solo.clickOnButton("View Reminders");
		solo.clickOnText("Foo");
		solo.clickLongOnView(solo.getButton("Delete"),1000);
		TimeUnit.MILLISECONDS.sleep(2000);
		/*solo.clickOnActionBarItem(0x7f060022);
		solo.clickOnActionBarItem(0x7f060024);
		solo.clickOnActionBarItem(0x7f060022);*/
		solo.clickOnText("Pulled-down"); 
		solo.clickOnText("Posted"); 
		solo.goBack();
		solo.clickOnButton("View Reminders");
		assertTrue(!solo.searchText("Foo",1,false,true));
		solo.clickOnText("Deleted"); 
		//solo.clickOnActionBarItem(0x7f060024);
		assertTrue(solo.searchText("Foo",1,false,true));
		
		//This tests the repost function
		solo.clickOnText("Foo");
		solo.clickOnButton("Repost");
		solo.clickOnText("Foo");
		solo.clickOnButton("Ok");
		//assertTrue(!solo.searchText("Foo",1,false,true));
		solo.clickOnText("Posted");
		//assertTrue(solo.searchText("Foo",1,false,true));
	}

@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
}
