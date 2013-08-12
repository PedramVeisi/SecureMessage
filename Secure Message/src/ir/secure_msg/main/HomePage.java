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

import ir.secure_msg.documents.AboutUs;
import ir.secure_msg.keygeneration.ManageKeys;
import ir.secure_msg.preferences.HandlePreferences;
import ir.secure_msg.preferences.PreferencesInterface;
import ir.secure_msg.preferences.PreferencesActivity;
import ir.secure_msg.sms.CreateEncryptedSMS;
//import ir.secure_msg.sms.InboxActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import ir.secure_msg.R;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class HomePage extends Activity implements PreferencesInterface {

	private String appMode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
      setContentView(R.layout.home_page_advanced);
		
		
        SharedPreferences settings = getSharedPreferences(PREF_FILE_NAME, 0);
		final HandlePreferences handlePrefs = new HandlePreferences(settings);

		appMode = handlePrefs.getAppMode();

		if (appMode.equals("SIMPLE")) {
			setContentView(R.layout.home_page_simple);
		} else if (appMode.equals("ADVANCED")) {
			setContentView(R.layout.home_page_advanced);
		}
        
        
		setUpViews();
		
		
	}

	private void setUpViews() {
		
		TextView sendSMSText = (TextView) findViewById(R.id.send_sms_text);
		TextView viewInboxText = (TextView) findViewById(R.id.view_inbox_text);
		TextView settingText = (TextView) findViewById(R.id.settings_text);
        //Button donationButton = (Button) findViewById(R.id.donation_button);
        //ImageButton viewInboxButton= (ImageButton) findViewById(R.id.view_inbox_button);
        ImageButton sendSMSButton = (ImageButton) findViewById(R.id.send_sms_button);
        ImageButton settingsButton = (ImageButton) findViewById(R.id.settings_button);
        Typeface journalFont = Typeface.createFromAsset(getAssets(), "journal.ttf");
        
        sendSMSText.setTypeface(journalFont);
        viewInboxText.setTypeface(journalFont);
        settingText.setTypeface(journalFont);
        
        
        if (appMode.equals("ADVANCED")) {
        	TextView genKeysText = (TextView) findViewById(R.id.home_gen_keys_text);
        	ImageButton genKeysButton = (ImageButton) findViewById(R.id.home_gen_keys_button);
        	genKeysText.setTypeface(journalFont);
        	
        	genKeysButton.setOnClickListener(new OnClickListener() {
    			public void onClick(View arg0) {
    				Intent intent = new Intent(HomePage.this, ManageKeys.class);
    				startActivity(intent);
    			}
    		});
        	
        }
        
        //TODO Setup donation using donation button

        sendSMSButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(HomePage.this, CreateEncryptedSMS.class);
				startActivity(intent);
			}
		});
        
        settingsButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				Intent intent = new Intent(HomePage.this, PreferencesActivity.class);
				startActivity(intent);
				finish();

			}
		});
        
        
        /*viewInboxButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomePage.this, InboxActivity.class);
				startActivity(intent);
				
			}
		});*/
        
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.homepage_option_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.option_menu_faq:
	        	//TODO Create activity
	        	Toast.makeText(this, "FAQ", Toast.LENGTH_SHORT).show();
	            break;
	        case R.id.option_menu_about_us:     

	        	Intent intent = new Intent(HomePage.this, AboutUs.class);
	        	startActivity(intent);

	            break;
	    }
	    return true;
	}
}
