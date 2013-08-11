/*
 	Copyright 2012 Pedram Veisi 

    This file is part of Secure Message.

    Secure Message is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Secure Message is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Secure Message.  If not, see <http://www.gnu.org/licenses/>.

 */


package ir.secure_msg.main;


import org.holoeverywhere.app.Activity;

import ir.secure_msg.firstrun.SetFirstTimePrefs;
import ir.secure_msg.preferences.HandlePreferences;
import ir.secure_msg.preferences.PreferencesInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import ir.secure_msg.R;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AppStart extends Activity implements PreferencesInterface {

	// TODO
	// Store and retrieve password using MD5

	// TODO
	// Add verification for Cancel button

	private String appPass;
	private String securityQuestion;
	private int appMode;
	HandlePreferences handlePrefs;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Getting shared preferences to read password.
		SharedPreferences settings = getSharedPreferences(PREF_FILE_NAME, 0);
		handlePrefs = new HandlePreferences(settings);

		appPass = handlePrefs.getPass();
		securityQuestion = handlePrefs.getSecurityQuestion();
		appMode = handlePrefs.getAppModeId();

		handleLayout();

	}

	// Method to set proper layout based on password
	private void handleLayout() {

		/*
		 * If read password(from SharedPreferences) is empty call set password
		 * /* activity.Empty password means firstrun, since app won't allow
		 * empty /* password on set password dialog.
		 */

		if (appPass.equals("") || securityQuestion.equals("") || appMode == 0) {
			// TODO problem in style or activity
			setContentView(R.layout.first_run);
			setUpViews();

		} else {
			setContentView(R.layout.check_password);
			checkPassword();
			handleForgetPassword();
		}
	}

	private void setUpViews() {

		WebView firstRunWebView = (WebView) findViewById(R.id.first_run_web_view);
		Button nextButton = (Button) findViewById(R.id.next_button);
		Button cancelButton = (Button) findViewById(R.id.cancel_button);

		firstRunWebView.loadUrl("file:///android_asset/HTML/first_run.html");

		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AppStart.this,
						SetFirstTimePrefs.class);
				startActivity(intent);
				finish();
			}
		});

	}

	private void checkPassword() {

		Button passSend = (Button) findViewById(R.id.pass_send);
		Button passCancel = (Button) findViewById(R.id.pass_cancel);

		passSend.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				EditText passField = (EditText) findViewById(R.id.pass_field);
				TextView wrongPassword = (TextView) findViewById(R.id.wrong_password);
				String enteredPassword = passField.getText().toString();

				if (enteredPassword.equals(appPass)) {
					Intent intent = new Intent(AppStart.this, HomePage.class);
					startActivity(intent);

					// finish password input activity
					finish();

				} else {
					// TODO Limit number of tries
					wrongPassword.setText(R.string.wrong_password);
					// Clears Password field on wrong attempt.
					passField.setText("");
				}
			}
		});

		passCancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});

	}
	
	private void handleForgetPassword() {
		TextView forgetPasswordText = (TextView) findViewById(R.id.forget_password_textview);
		
		forgetPasswordText.setMovementMethod(LinkMovementMethod.getInstance());
		
		forgetPasswordText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(AppStart.this, ForgetPassword.class);
				startActivity(intent);
				finish();
			}
		});
		
	}

	
}