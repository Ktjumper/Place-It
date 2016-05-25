package com.example.place_its.test;

import java.util.concurrent.TimeUnit;

import place_its.MainActivity;

import com.robotium.solo.Solo;

import android.graphics.Point;
import android.graphics.PointF;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityUnitTestCase;
import android.view.Display;
import android.widget.EditText;


public class BirdsEyeTest extends ActivityInstrumentationTestCase2<MainActivity> {
	
	private Solo solo;
	
	public BirdsEyeTest() {
		super(MainActivity.class);
	}

	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void testALogIn() throws InterruptedException{
		solo.clickOnButton("Login");
		solo.assertCurrentActivity("Now logging in","LoginActivity");
		EditText email = (EditText) solo.getCurrentActivity().findViewById(0x7f05002a);
		EditText password = //(EditText) solo.getCurrentActivity().findViewById(0x7f050024);
		solo.getEditText("Password");
		
		String newName = ""+System.currentTimeMillis()+"@speemail.rom";
		
		String testEmail = "allenlin.ae@gmail.com";
		String testFake = "VirginiaSlims";
		String testReal = "MarlboroMan";
		solo.typeText(email, newName);
		solo.typeText(password, testFake);
		solo.clickOnButton("Register");
		
		TimeUnit.MILLISECONDS.sleep(5000);
		
		solo.assertCurrentActivity("Correctly logged in!","MainActivity");
		solo.clickOnButton("Logout");
		
		TimeUnit.MILLISECONDS.sleep(4000);
		
		solo.clickOnButton("Login");
		email = (EditText) solo.getEditText("Email");
		password = solo.getEditText("Password");
		solo.assertCurrentActivity("Now logging in","LoginActivity");
		solo.typeText(email, testEmail);
		solo.typeText(password, testFake);
		solo.clickOnButton("Sign in");
		
		TimeUnit.MILLISECONDS.sleep(3000);
		
		solo.assertCurrentActivity("Failed to assert improper status","LoginActivity");
		solo.clearEditText(password);
		solo.typeText(password,testReal);
		solo.clickOnButton("Sign in");
		solo.assertCurrentActivity("Correctly logged in!","MainActivity");
	}
	
	public void testBirdsEye(){
		//All we have to do is scroll up, scroll down, scroll left, scroll right
		//zoom in and zoom out without crashing
		solo.assertCurrentActivity("Main not loaded","MainActivity");
		solo.waitForActivity("MainActivity");
		solo.clickOnButton("Show Map");
		solo.waitForActivity("MapActivity");
		solo.assertCurrentActivity("Map not loaded","MapActivity");
		solo.hideSoftKeyboard();
		solo.scrollToSide(Solo.UP);
		solo.scrollToSide(Solo.DOWN);
		solo.scrollToSide(Solo.RIGHT);
		solo.scrollToSide(Solo.LEFT);
		solo.pinchToZoom(new PointF(200,200),new PointF(600,600),new PointF(400,400),new PointF(400,400));
		solo.pinchToZoom(new PointF(400,400),new PointF(400,400),new PointF(600,600),new PointF(200,200));
		solo.assertCurrentActivity("Correctly logged in!","MapActivity");
	}
	
	public void testMyLocationButton() throws InterruptedException{
		solo.assertCurrentActivity("Main not loaded","MainActivity");
		solo.waitForActivity("MainActivity");
		
		solo.clickOnButton("Show Map");
		solo.assertCurrentActivity("Map not loaded","MapActivity");
		solo.hideSoftKeyboard();
		TimeUnit.MILLISECONDS.sleep(4000);
		for(int a = 0; a < 10; a++){
			solo.scrollToSide(Solo.RIGHT);
		}
		
		final float WRITERWIDTH=1280;
		final float WRITERHEIGHT=800;
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		float scalarW = width/WRITERWIDTH;
		float scalarH = height/WRITERHEIGHT;
		
		solo.clickOnScreen(1235*scalarW,200*scalarH);
		TimeUnit.MILLISECONDS.sleep(10000);
	}

	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}

}
