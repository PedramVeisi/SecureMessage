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

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.secure.message.R;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class AppMode extends Activity implements PreferencesInterface {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_mode);
		setUpViews();
	}

	private void setUpViews() {

		SharedPreferences settings = getSharedPreferences(PREF_FILE_NAME, 0);
		final HandlePreferences handlePrefs = new HandlePreferences(settings);

		final RadioGroup appModeGroup = (RadioGroup) findViewById(R.id.mode_radio_group);
		Button modeSend = (Button) findViewById(R.id.app_mode_done);
		
		final RadioButton simpleModeRButton = (RadioButton) findViewById(R.id.simple_mode_radiob);
		final RadioButton advancedModeRButton = (RadioButton) findViewById(R.id.advanced_mode_radiob);
		
		int prevCheckedId = handlePrefs.getAppModeId();
		
		if (prevCheckedId == 0){
			appModeGroup.check(simpleModeRButton.getId());
		}
		else{
			appModeGroup.check(prevCheckedId);
		}
		
		modeSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				int selectedId = appModeGroup.getCheckedRadioButtonId();
				RadioButton SelectedRadioButton = (RadioButton) findViewById(selectedId);

				String selectedMode = (String) SelectedRadioButton.getText();

				if (selectedMode.equals("Simple Mode")) {
					handlePrefs.setAppModeId(simpleModeRButton.getId());
					handlePrefs.setAppMode("SIMPLE");
				}
				else if (selectedMode.equals("Advanced Mode")) {
					handlePrefs.setAppModeId(advancedModeRButton.getId());
					handlePrefs.setAppMode("ADVANCED");
				}

				finish();

			}
		});

	}

}
