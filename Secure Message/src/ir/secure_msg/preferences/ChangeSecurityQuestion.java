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

public class ChangeSecurityQuestion extends Activity implements
		PreferencesInterface {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_security_question);
		setUpViews();
	}

	private void setUpViews() {

		Button chngSecQuestionSend = (Button) findViewById(R.id.chng_sec_question_send);
		Button chngSecQuestionCancel = (Button) findViewById(R.id.chng_sec_question_cancel);

		chngSecQuestionSend.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				EditText passField = (EditText) findViewById(R.id.pass_field);
				EditText newSecQuestionField = (EditText) findViewById(R.id.new_sec_question_field);
				EditText newSecAnswerField = (EditText) findViewById(R.id.new_sec_answer_field);

				String currentPassword = passField.getText().toString();
				String newSecQuestion = newSecQuestionField.getText()
						.toString();
				String newSecAnswer = newSecAnswerField.getText().toString();

				SharedPreferences settings = getSharedPreferences(
						PREF_FILE_NAME, 0);
				HandlePreferences handlePrefs = new HandlePreferences(settings);

				String savedPassword = handlePrefs.getPass();

				TextView errorMsg = (TextView) findViewById(R.id.error_text);

				if (currentPassword.equals(savedPassword)) {

					if (newSecQuestion.equals("")) {
						errorMsg.setText(R.string.enter_sec_q);
						newSecQuestionField.requestFocus();
					} else if (newSecAnswer.equals("")) {
						errorMsg.setText(R.string.enter_sec_a);
						newSecAnswerField.requestFocus();
					} else {

						handlePrefs.setSecurityQuestion(newSecQuestion);
						handlePrefs.setSecurityAnswer(newSecAnswer);
						Toast.makeText(ChangeSecurityQuestion.this, "Security Question Updated.", Toast.LENGTH_SHORT).show();
						finish();

					}

				} else {

					errorMsg.setText(R.string.password_is_not_correct);
					passField.setText("");
					newSecQuestionField.setText("");
					newSecAnswerField.setText("");
					passField.requestFocus();

				}

			}
		});

		chngSecQuestionCancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
	}

}
