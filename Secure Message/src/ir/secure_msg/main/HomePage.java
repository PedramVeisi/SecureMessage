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
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import ir.secure_msg.documents.AboutUs;
import ir.secure_msg.keygeneration.ManageKeys;
import ir.secure_msg.preferences.HandlePreferences;
import ir.secure_msg.preferences.PreferencesInterface;
import ir.secure_msg.preferences.PreferencesActivity;
import ir.secure_msg.sms.CreateEncryptedSMS;
import android.content.Intent;
//import ir.secure_msg.sms.InboxActivity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import ir.secure_msg.R;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class HomePage extends Activity implements PreferencesInterface {
	
	private String appMode;

	private ActionBarDrawerToggle actionBarDrawerToggle;
	private DrawerLayout mDrawerLayout;
	private RelativeLayout drawerRelativeLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences settings = getSharedPreferences(PREF_FILE_NAME, 0);
		final HandlePreferences handlePrefs = new HandlePreferences(settings);

		appMode = handlePrefs.getAppMode();

		if (appMode.equals("SIMPLE")) {
			setContentView(R.layout.home_page_simple);
		} else if (appMode.equals("ADVANCED")) {
			// TODO Edit Advanced layout
			setContentView(R.layout.home_page_advanced);
		}

		setUpViews();

	}

	private void setUpViews() {
		// ImageButton viewInboxButton= (ImageButton)
		// findViewById(R.id.view_inbox_button);

		Button settingsButton = (Button) findViewById(R.id.settings_button);
		Button aboutButton = (Button) findViewById(R.id.about_button);
		Button helpButton = (Button) findViewById(R.id.help_button);
		Button homeButton = (Button) findViewById(R.id.home_button);
		Button generateButton = (Button) findViewById(R.id.drawer_gen_keys_button);

		Typeface journalFont = Typeface.createFromAsset(getAssets(),
				"journal.ttf");

		homeButton.setTypeface(journalFont);
		settingsButton.setTypeface(journalFont);		
		aboutButton.setTypeface(journalFont);		
		helpButton.setTypeface(journalFont);
		generateButton.setTypeface(journalFont);
		
		getSupportActionBar().setTitle("Conversations");

		mDrawerLayout = (DrawerLayout) findViewById(R.id.home_drawer_layout);
		drawerRelativeLayout = (RelativeLayout) findViewById(R.id.drawer_relative_layout);

		actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {
			@Override
			public void onDrawerOpened(View drawerView) {
				// TODO Auto-generated method stub
				super.onDrawerOpened(drawerView);
			}
		};

		mDrawerLayout.setDrawerListener(actionBarDrawerToggle);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		settingsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(HomePage.this,
						PreferencesActivity.class);
				startActivity(intent);
				finish();

			}
		});

		aboutButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(HomePage.this, "Call About Us Activity",
						Toast.LENGTH_SHORT).show();

			}
		});

		helpButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(HomePage.this, "Call Help Activity",
						Toast.LENGTH_SHORT).show();

			}
		});
		
		
		/*
		 * if (appMode.equals("ADVANCED")) { TextView genKeysText = (TextView)
		 * findViewById(R.id.home_gen_keys_text); ImageButton genKeysButton =
		 * (ImageButton) findViewById(R.id.home_gen_keys_button);
		 * genKeysText.setTypeface(journalFont);
		 * 
		 * genKeysButton.setOnClickListener(new OnClickListener() { public void
		 * onClick(View arg0) { Intent intent = new Intent(HomePage.this,
		 * ManageKeys.class); startActivity(intent); } });
		 * 
		 * }
		 */

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		actionBarDrawerToggle.syncState();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.home_action_bar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {

			if (mDrawerLayout.isDrawerOpen(drawerRelativeLayout)) {
				mDrawerLayout.closeDrawer(drawerRelativeLayout);
			} else {
				mDrawerLayout.openDrawer(drawerRelativeLayout);
			}
		}

		switch (item.getItemId()) {
		case R.id.action_send_msg:
			Intent intent = new Intent(HomePage.this, CreateEncryptedSMS.class);
			startActivity(intent);
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

}
