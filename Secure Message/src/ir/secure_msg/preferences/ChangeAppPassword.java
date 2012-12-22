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
import ir.secure_msg.R;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChangeAppPassword extends Activity implements PreferencesInterface {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_password);
		setUpViews();
	}

	private void setUpViews() {
		Button chngPassSend = (Button) findViewById(R.id.chng_pass_send);
		Button chngPassCancel = (Button) findViewById(R.id.chng_pass_cancel);

		chngPassSend.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				EditText currentPassField = (EditText) findViewById(R.id.current_pass_field);
				EditText newPassField = (EditText) findViewById(R.id.new_pass_field);
				EditText retypedPassField = (EditText) findViewById(R.id.retype_pass_field);

				String currentPassword = currentPassField.getText().toString();
				String newPass = newPassField.getText().toString();
				String retypedPass = retypedPassField.getText().toString();

				SharedPreferences settings = getSharedPreferences(
						PREF_FILE_NAME, 0);
				HandlePreferences handlePrefs = new HandlePreferences(settings);

				String savedPassword = handlePrefs.getPass();

				TextView errorMsg = (TextView) findViewById(R.id.error_text);

				if (!currentPassword.equals(savedPassword)) {
					errorMsg.setText(R.string.current_password_is_not_correct);
					currentPassField.setText("");
					newPassField.setText("");
					retypedPassField.setText("");
					currentPassField.requestFocus();

					
				} else {

					if (newPass.equals("")) {
						errorMsg.setText(R.string.password_is_empty);
						currentPassField.setText("");
						currentPassField.requestFocus();
						
					} else if (retypedPass.equals(newPass)) {

						handlePrefs.setPass(newPass);
						
						Toast.makeText(ChangeAppPassword.this, R.string.password_successfully_updated, Toast.LENGTH_SHORT).show();

						// finish set password activity
						finish();

					} else {
						errorMsg.setText(R.string.passwords_do_not_match);
						currentPassField.setText("");
						newPassField.setText("");
						retypedPassField.setText("");
						
						currentPassField.requestFocus();
						
					}
				}

			}
		});

		chngPassCancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
	}

}
