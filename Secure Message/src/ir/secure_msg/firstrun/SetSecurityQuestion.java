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

import ir.secure_msg.preferences.HandlePreferences;
import ir.secure_msg.preferences.PreferencesInterface;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.secure.message.R;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SetSecurityQuestion extends Activity implements PreferencesInterface {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_security_question);
		setUpView();
	}

	private void setUpView() {

		Button setSecqSend = (Button) findViewById(R.id.set_secq_send);
		Button setSecqCancel = (Button) findViewById(R.id.set_secq_cancel);

		setSecqCancel.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				finish();
			}
		});

		setSecqSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				EditText secQuestionField = (EditText) findViewById(R.id.sec_question_field);
				EditText secAnswerField = (EditText) findViewById(R.id.sec_answer_field);

				String secQuestion = secQuestionField.getText().toString();
				String secAnswer = secAnswerField.getText().toString();

				SharedPreferences settings = getSharedPreferences(
						PREF_FILE_NAME, 0);
				HandlePreferences handlePrefs = new HandlePreferences(settings);

				TextView errorMsg = (TextView) findViewById(R.id.error_text);

				if (secQuestion.equals("")) {
					errorMsg.setText(R.string.enter_sec_q);
					secQuestionField.setText("");
					secQuestionField.requestFocus();

				} else if(secAnswer.equals("")){
					errorMsg.setText(R.string.enter_sec_a);
					secAnswerField.setText("");
					secAnswerField.requestFocus();
				}
				else {
					
					handlePrefs.setSecurityQuestion(secQuestion);
					handlePrefs.setSecurityAnswer(secAnswer);
					
					// finish set password activity
					finish();

				}

			}
		});

	}

}