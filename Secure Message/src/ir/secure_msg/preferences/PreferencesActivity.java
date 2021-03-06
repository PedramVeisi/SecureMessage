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

package ir.secure_msg.preferences;

import org.holoeverywhere.preference.PreferenceActivity;

import com.actionbarsherlock.view.MenuItem;

import ir.secure_msg.main.HomePage;
import android.content.Intent;
import android.os.Bundle;
import ir.secure_msg.R;

public class PreferencesActivity extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO Remove deprecated method
		// http://developer.android.com/reference/android/preference/PreferenceActivity.html

		addPreferencesFromResource(R.xml.preferences);
		setUpViews();
	}

	private void setUpViews() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.settings);

	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {

	    case android.R.id.home:
	    	onBackPressed();

	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	

	@Override
	public void onBackPressed() {

		Intent intent = new Intent(PreferencesActivity.this, HomePage.class);
		startActivity(intent);

		super.onBackPressed();
	}

}