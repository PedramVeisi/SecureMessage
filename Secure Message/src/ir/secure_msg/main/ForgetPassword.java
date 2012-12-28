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

import ir.secure_msg.firstrun.SetAppPassword;
import ir.secure_msg.preferences.HandlePreferences;
import ir.secure_msg.preferences.PreferencesInterface;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import ir.secure_msg.R;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ForgetPassword extends Activity implements PreferencesInterface {
	
	private String savedSecurityAnswer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget_password);

		setUpView();
	}

	private void setUpView() {
		
		String securityQuestion;

		TextView secQuestionText = (TextView) findViewById(R.id.ask_security_question_text);
		Button secAnswerSend = (Button) findViewById(R.id.sec_answer_send);
		Button secAnswerCancel = (Button) findViewById(R.id.sec_answer_cancel);
		
		// Getting shared preferences to read password.
		SharedPreferences settings = getSharedPreferences(PREF_FILE_NAME, 0);
		HandlePreferences handlePrefs = new HandlePreferences(settings);
		
		securityQuestion = handlePrefs.getSecurityQuestion();
		savedSecurityAnswer = handlePrefs.getSecurityAnswer();
		
		secQuestionText.setText(securityQuestion);
		
		
		
		secAnswerSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				TextView wrongSecAnswer = (TextView) findViewById(R.id.wrong_security_answer_text);
				EditText securityAnswerField = (EditText) findViewById(R.id.sec_answer_field);
				String securityAnswer = securityAnswerField.getText().toString();
				
				if (securityAnswer.equals(savedSecurityAnswer)) {
					Intent intent = new Intent(ForgetPassword.this, SetAppPassword.class);
					startActivity(intent);
					finish();
				}else{
					wrongSecAnswer.setText(R.string.security_answer_is_incorrect);
					securityAnswerField.setText("");
					securityAnswerField.requestFocus();
				}				
				
			}
		});		

		secAnswerCancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
		
	}

}
