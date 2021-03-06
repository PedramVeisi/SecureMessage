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

import org.holoeverywhere.app.Activity;

import ir.secure_msg.R;
import ir.secure_msg.preferences.HandlePreferences;
import ir.secure_msg.preferences.PreferencesInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SetAppPassword extends Activity implements PreferencesInterface {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_password);
		setUpView();
	}

	private void setUpView() {

		Button setPassSend = (Button) findViewById(R.id.set_pass_send);
		Button setPassCancel = (Button) findViewById(R.id.set_pass_cancel);

		setPassCancel.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				finish();
			}
		});
		
		
		setPassSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				EditText newPassField = (EditText) findViewById(R.id.new_pass_field);
				EditText retypedPassField = (EditText) findViewById(R.id.retype_pass_field);

				String newPass = newPassField.getText().toString();
				String retypedPass = retypedPassField.getText().toString();

				SharedPreferences settings = getSharedPreferences(
						PREF_FILE_NAME, 0);
				HandlePreferences handlePrefs = new HandlePreferences(settings);

				TextView errorMsg = (TextView) findViewById(R.id.error_text);


				if (newPass.equals("")) {
					errorMsg.setText(R.string.password_is_empty);
					newPassField.setText("");
					newPassField.requestFocus();
					
				} else if (retypedPass.equals(newPass)) {

					handlePrefs.setPass(newPass);

					// finish set password activity
					finish();

				} else {
					errorMsg.setText(R.string.passwords_do_not_match);
					newPassField.setText("");
					retypedPassField.setText("");
					
					newPassField.requestFocus();
					
				}
				
			}
		});

	}

}
