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

package ir.secure_msg.firstrun;


import java.io.File;

import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.app.AlertDialog.Builder;

import com.actionbarsherlock.app.SherlockPreferenceActivity;

import ir.secure_msg.main.HomePage;
import ir.secure_msg.preferences.HandlePreferences;
import ir.secure_msg.preferences.PreferencesInterface;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import ir.secure_msg.R;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SetFirstTimePrefs extends SherlockPreferenceActivity implements PreferencesInterface {

	private final String KEYS_FOLDER_NAME = "keys";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_time_prefs);
		addPreferencesFromResource(R.xml.first_time_preferences);
		
		//Create keys folder in the internal file folder
		boolean success = (new File(getFilesDir() + "/" + KEYS_FOLDER_NAME)).mkdirs();

		setUpViews();
		
	}

	private void setUpViews() {
		
		SharedPreferences settings = getSharedPreferences(PREF_FILE_NAME, 0);
		final HandlePreferences handlePrefs = new HandlePreferences(settings);
		
		Button startApp = (Button) findViewById(R.id.start_app_button);
		
		startApp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				String appPass = handlePrefs.getPass();
				String secQuestion = handlePrefs.getSecurityQuestion();
				int appMode = handlePrefs.getAppModeId();
				
				if(appPass.equals("")){
					showWarning(getString(R.string.set_app_password));
				}else if (secQuestion.equals("")) {
					showWarning(getString(R.string.set_security_question));
				}else if (appMode == 0) {
					showWarning(getString(R.string.set_app_mode));
				}
				else{
					
					Intent intent = new Intent(SetFirstTimePrefs.this, HomePage.class);
					startActivity(intent);
					finish();
					
				}
				
			}
		});
		
	}
	
	
	void showWarning(String message){
		Builder alertDialogBuilder = new AlertDialog.Builder(SetFirstTimePrefs.this);
		alertDialogBuilder.setTitle(getString(R.string.warning))
		.setMessage(message)
		.setCancelable(false)		
		.setNegativeButton(R.string.ok,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            } });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
	}
	
}
