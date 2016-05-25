/**
 * Created by CSE 110 Team 12.
 * User: Team 12 - Kristiyan Dzhamalov
 * 				 - Richard Tran
 * 				 - Kenneth Tran
 * 				 - Monica Cheung
 * 				 - Heather Lee
 * 				 - Allen Lin
 * Date: 3/15/14
 */
package place_its;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import login.LoginUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.place_its.R;


public class LoginActivity extends Activity {

	public static final String TAG = "LoginActivity";
	
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;
	private UserRegisterTask mRegTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	
	public Boolean foundUser = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
						
		setContentView(R.layout.activity_login);
		// Set up the login form.
		mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		/* 
		 * Listener for the sign in button 
		 * If the user has internet connection, attempt login
		 * Otherwise, prompt the user to connect to the internet
		 */
		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						ConnectionDetector cd = new ConnectionDetector(getBaseContext());
					    if (cd.isConnectingToInternet()) {
					    	attemptLogin();
					    }
					    else {
					    	Toast.makeText(getBaseContext(),
									"Please connect to internet to sign in!",
									Toast.LENGTH_SHORT).show();
					    }
						
					}
				});

		/* 
		 * Listener for the register button
		 * If the user has internet connection, attempt registration process
		 * Otherwise, prompt the user to connect to the internet
		 */
		findViewById(R.id.register_button).setOnClickListener(
				new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						ConnectionDetector cd = new ConnectionDetector(getBaseContext());
					    if (cd.isConnectingToInternet()) {
					    	attemptRegister();
					    }
					    else {
					    	Toast.makeText(getBaseContext(),
									"Please connect to internet to register!",
									Toast.LENGTH_SHORT).show();
					    }

						
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	/*
	 * Attempt register method which takes in user input for the user name
	 * and password fields and validate input. If input is valid, register the
	 * user.
	 */
	public void attemptRegister() {
		if (mRegTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText("Registering...");
			showProgress(true);
			mRegTask = new UserRegisterTask();
			mRegTask.execute((Void) null);
		}
		
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.


			/* get list of local username/passwords */
			ArrayList<String> credentials = LoginUtils.getLogins();

			foundUser = false;
			for (String credential : credentials) {
				String[] pieces = credential.split(":");
				if (pieces[0].equals(mEmail.toLowerCase())) {
					foundUser = true;

					// Account exists, return true if the password matches.
					return pieces[1].equals(mPassword);
				}
				Log.d(String.valueOf(foundUser), "FOUNDUSER SEARCH ACCOUNTS");
			}

			/* user entered nonexist email */

			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				/* remember the user that logged in*/
				String user = mEmailView.getText().toString() + ":"+mPasswordView.getText().toString();
				LoginUtils.login(getApplicationContext(),
						user,
						getUserKey(user));
				
				finish();
			}
			else {
				Log.d(String.valueOf(foundUser), "FOUNDUSER POSTEXECUTE");

				/* user doesn't exist*/
				if(foundUser==false){
					mEmailView
					.setError("Account doesn't exist");
					mEmailView.requestFocus();
					
				}
				else {
					/* wrong password */
					mPasswordView
					.setError(getString(R.string.error_incorrect_password));
					mPasswordView.requestFocus();
				}
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
	
	
	
	public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {

			try {
				// Simulate network access.
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return false;
			}

			/* get list of local username/passwords */
			ArrayList<String> credentials = LoginUtils.getLogins();

			for (String credential : credentials) {
				String[] pieces = credential.split(":");
				if (pieces[0].equals(mEmail.toLowerCase())) {
					/* return true of username already exists in datastore*/
					return true;
				}
			}

			/* return false if ok to create account */

			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mRegTask = null;
			showProgress(false);

			if (success) {
				
				mEmailView
				.setError("An account with that email already exists");
				mEmailView.requestFocus();
			}
			else {
				
				/* register account*/
				String newAccount = mEmail + ":" + mPassword;
				/* remember user */
				LoginUtils.login(getApplicationContext(),
						newAccount,
						LoginUtils.getNewKey());
				
				postdata(); //add user to datastore
				DatabaseHandler db = new DatabaseHandler(getBaseContext());
				db.deleteDatabase();
				
				db.close();
				
				finish();


			}
		}

		@Override
		protected void onCancelled() {
			mRegTask = null;
			showProgress(false);
		}
	}
	
    private String getUserKey (String user) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		String TAG = "loginactivity";
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(MainActivity.PRODUCT_URI);
		String key ="";
		try {
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			String data = EntityUtils.toString(entity);
			JSONObject myjson;

			try {
				myjson = new JSONObject(data);
				JSONArray array = myjson.getJSONArray("data");
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					if(user.toLowerCase().equals(obj.get("description").toString().toLowerCase())) {
						key = obj.get("name").toString();
					}
				}
			} catch (JSONException e) {
				Log.d(TAG, "Error in parsing JSON");
			}

		} catch (ClientProtocolException e) {
			Log.d(TAG,
					"ClientProtocolException thrown while trying to Connect to GAE");
		} catch (IOException e) {
			Log.d(TAG, "IOException thrown while trying to Connect to GAE");
		}
		return key;
	}
    
	
	
	
	/* Create new user: stores username/password to datastore */
	private void postdata() {

		Thread t = new Thread() {

			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(MainActivity.PRODUCT_URI);

			    try {
			      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			      nameValuePairs.add(new BasicNameValuePair("name",
			    		  LoginUtils.getNewKey()));
			      nameValuePairs.add(new BasicNameValuePair("description",
			    		  mEmailView.getText().toString().toLowerCase() + ":"+mPasswordView.getText().toString()));
			      nameValuePairs.add(new BasicNameValuePair("action",
				          "put"));
			      post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			 
			      HttpResponse response = client.execute(post);
			      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			      String line = "";
			      while ((line = rd.readLine()) != null) {
			        Log.d(TAG, line);
			      }

			    } catch (IOException e) {
			    	Log.d(TAG, "IOException while trying to conect to GAE");
			    }
			}
		};

		t.start();
	}
	
}
