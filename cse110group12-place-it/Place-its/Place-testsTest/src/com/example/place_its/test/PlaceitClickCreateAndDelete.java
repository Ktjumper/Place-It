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


public class PlaceitClickCreateAndDelete extends ActivityInstrumentationTestCase2<MainActivity> {
	
	private Solo solo;
	
	public PlaceitClickCreateAndDelete() {
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
		//solo.clickOnButton("No"); Replaced this method of creation
		
		//Wait for screen to center, 4 seconds
		TimeUnit.MILLISECONDS.sleep(4000);
		solo.waitForActivity("MapActivity",5);
		solo.assertCurrentActivity("Map not loaded","MapActivity");
		//solo.clickOnScreen(300*scalarW,300*scalarH);
		for(int a = 0; a < 5; a++){
			solo.scrollToSide(Solo.RIGHT);
			solo.scrollToSide(Solo.UP);
		}
		for(int a = 0; a < 10; a++){
			solo.clickOnScreen(700*scalarW,400*scalarH);
			solo.clickOnScreen(700*scalarW,320*scalarH);
			//Should bring up our creation dialogue
			
			try{
				solo.waitForActivity("CreateRegularPlaceit");
				solo.assertCurrentActivity("Placeit not loaded","CreateRegularPlaceit");
				a=11;
				break;
			}catch(Error e){}catch (Exception be){}
		}
		
		EditText mTitle = solo.getEditText("Title");
		EditText mDesc = solo.getEditText("Description");
		//solo.clickOnScreen(110, 110);
		String strInput = "Zooloo"; //Title
		solo.typeText(mTitle, strInput);
		boolean actual = solo.searchEditText(strInput);
		assertEquals("Title does not match entered text",true, actual);
		strInput = "Koo"; //Desc
		//solo.clickOnScreen(110, 190);
		solo.typeText(mDesc, strInput);
		actual = solo.searchEditText(strInput);
		assertEquals("Desc does not match entered text",true, actual);
		solo.assertCurrentActivity("Placeit not loaded","CreateRegularPlaceit");
		
		assertTrue("Zooloo".equals(mTitle.getText().toString()));
		assertTrue("Koo".equals(mDesc.getText().toString()));
		
		solo.assertCurrentActivity("Placeit not loaded","CreateRegularPlaceit");
		
		solo.clickOnButton("OK");
		solo.waitForActivity("MapActivity");
		solo.assertCurrentActivity("Map not loaded","MapActivity");
		/* Check for Place-It, click on Place-It */
		
		TimeUnit.MILLISECONDS.sleep(4000);
		display = getActivity().getWindowManager().getDefaultDisplay();
		size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
		scalarW = width/WRITERWIDTH;
		scalarH = height/WRITERHEIGHT;
		solo.clickOnScreen(700*scalarW,350*scalarH);
		solo.clickOnScreen(640*scalarW,430*scalarH);
		solo.clickOnScreen(645*scalarW,390*scalarH);
		
		solo.waitForActivity("ViewPlaceitActivity",5);
		solo.assertCurrentActivity("Placeit not loaded","ViewPlaceitActivity");
		TimeUnit.MILLISECONDS.sleep(1500);
		display = getActivity().getWindowManager().getDefaultDisplay();
		size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
		scalarW = width/WRITERWIDTH;
		scalarH = height/WRITERHEIGHT;
		solo.clickOnButton("Delete");
		TimeUnit.MILLISECONDS.sleep(1500);
		
		//Go back to main
		solo.goBack();
		solo.clickOnButton("View Reminders");
		solo.waitForActivity("ViewListsActivity",5);
		TimeUnit.MILLISECONDS.sleep(3000);
		solo.clickOnText("Pulled-down"); 
		solo.clickOnText("Posted"); 
		solo.goBack();
		solo.clickOnButton("View Reminders");
	}

@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
}
