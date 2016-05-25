package com.example.place_its.test;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import place_its.MainActivity;

import com.robotium.solo.Solo;

import android.graphics.Point;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.Display;
import android.widget.EditText;


public class PlaceitMinorTest extends ActivityInstrumentationTestCase2<MainActivity> {
	
	private Solo solo;
	
	public PlaceitMinorTest() {
		super(MainActivity.class);
	}

	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void testPlaceIt() throws Exception {
		
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
		solo.clickOnScreen(400*scalarW,400*scalarH);
		
		solo.clickOnScreen(900*scalarW,400*scalarH);
		solo.clickOnScreen(900*scalarW,320*scalarH);
		//Should bring up our creation dialogue
		
		solo.waitForActivity("CreateRegularPlaceit");
		solo.assertCurrentActivity("Placeit not loaded","CreateRegularPlaceit");
		solo.clickOnButton("Cancel");
		solo.waitForActivity("MapActivity",5);
		solo.assertCurrentActivity("Map not loaded","MapActivity");
		
		solo.clickOnScreen(400*scalarW,400*scalarH);
		solo.clickOnScreen(400*scalarW,310*scalarH);
		
		solo.waitForActivity("CreateRegularPlaceit");
		solo.assertCurrentActivity("Placeit not loaded","CreateRegularPlaceit");
		EditText mTitle = solo.getEditText("Title");
		EditText mDesc = solo.getEditText("Description");
		solo.clickOnView(mTitle);
		solo.clearEditText(mTitle);
		String strInput = "ZimbabweZimbabweZimbabweZimbabweZimbabweZimbabweZimbabweZimbabweZimbabweZimbabweZimbabweZimbabwe"; //Title
		solo.typeText(mTitle, strInput);
		boolean actual = solo.searchEditText(strInput);
		assertEquals("Title does not match entered text",true, actual);
		strInput = "Koo"; //Desc
		solo.clickOnView(mDesc);
		solo.clearEditText(mDesc);
		solo.enterText(mDesc, strInput);
		actual = solo.searchEditText(strInput);
		assertEquals("Desc does not match entered text",true, actual);
		solo.assertCurrentActivity("Placeit not loaded","CreateRegularPlaceit");
		
		assertTrue("ZimbabweZimbabweZimbabweZimbabweZimbabweZimbabweZimbabweZimbabweZimbabweZimbabweZimbabweZimbabwe".equals(mTitle.getText().toString()));
		assertTrue("Koo".equals(mDesc.getText().toString()));
		
		solo.hideSoftKeyboard();
		
		solo.clickOnButton("OK");
		solo.waitForActivity("MapActivity");
		solo.assertCurrentActivity("Map not loaded","MapActivity");
		solo.goBack();
		solo.clickOnButton("View Reminders");
		solo.clickOnText("Zimbabwe");
		try{
			solo.clickOnText("ZimbabweZimbabweZimbabweZimbabweZimbabweZimbabweZimbabweZimbabweZimbabweZimbabweZimbabweZimbabwe",1,false);
			fail();
		}catch(Exception e){}catch(Error e){}
		solo.assertCurrentActivity("Map not loaded","ViewPlaceitActivity");
		solo.clickOnButton("Delete");
		/* Check for Place-It, click on Place-It */
	}
	
	public void testAddressSearch() throws InterruptedException{
		solo.waitForActivity("MainActivity");
		final float WRITERWIDTH=1280;
		final float WRITERHEIGHT=800;
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		solo.assertCurrentActivity("Main not loaded","MainActivity");
		solo.clickOnButton("Show Map");
		float scalarW = width/WRITERWIDTH;
		float scalarH = height/WRITERHEIGHT;
		solo.assertCurrentActivity("Map not loaded","MapActivity");
		solo.clickOnEditText(0);
		solo.clearEditText(0);
		for(int a = 0; a < 4; a++){
			solo.scrollToSide(Solo.LEFT);
		}
		EditText searchAddr = solo.getEditText("Enter address");
		solo.typeText(searchAddr,"1515 Ruscitto Lane");
		solo.hideSoftKeyboard();
		TimeUnit.MILLISECONDS.sleep(1000);
		solo.clickOnButton("Search");
		solo.clickOnButton("Search");
		TimeUnit.MILLISECONDS.sleep(4000);
		solo.clickOnScreen(640*scalarW,430*scalarH);
	}
	
	public void testTearDown(){
		solo.waitForActivity("MainActivity");
		solo.clickOnButton("Logout");
	}

@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
}
//createPlaceItIntent.putExtra(PlaceItUtils.PLACE_IT_LOCATION, bundle);
//startActivityForResult(createPlaceItIntent, CREATE_PLACE_IT_REQUEST);