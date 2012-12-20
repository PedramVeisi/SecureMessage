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

import android.content.SharedPreferences;

public class HandlePreferences {

	private SharedPreferences settings;

	public HandlePreferences(SharedPreferences settings) {
		this.settings = settings;
	}

	private String APP_PASSWORD;
	private String APP_MODE;
	private int APP_MODE_ID;
	private String SECURIT_QUESTION;
	private String SECURIT_ANSWER;

	SharedPreferences.Editor settingsEditor;

	public String getPass() {
		APP_PASSWORD = settings.getString("appPassword", "");
		return APP_PASSWORD;
	}

	public void setPass(String newPass) {
		settingsEditor = settings.edit();

		APP_PASSWORD = settings.getString("appPassword", "");

		// TODO Put Hash instead of plain text
		settingsEditor.putString("appPassword", newPass);
		settingsEditor.commit();

	}

	public int getAppModeId() {
		APP_MODE_ID = settings.getInt("appModeId", 0);
		return APP_MODE_ID;
	}

	public void setAppModeId(int mode) {
		settingsEditor = settings.edit();
		settingsEditor.putInt("appModeId", mode);
		settingsEditor.commit();
	}

	public String getAppMode() {
		APP_MODE = settings.getString("appMode", "UNSET");
		return APP_MODE;
	}

	public void setAppMode(String mode) {
		settingsEditor = settings.edit();
		settingsEditor.putString("appMode", mode);
		settingsEditor.commit();
	}

	public String getSecurityQuestion() {
		SECURIT_QUESTION = settings.getString("secQuestion", "");
		return SECURIT_QUESTION;
	}

	public void setSecurityQuestion(String securityQestion) {
		settingsEditor = settings.edit();
		settingsEditor.putString("secQuestion", securityQestion);
		settingsEditor.commit();
	}

	public String getSecurityAnswer() {
		SECURIT_ANSWER = settings.getString("secAnswer", "");
		return SECURIT_ANSWER;
	}

	public void setSecurityAnswer(String securityAnswer) {
		settingsEditor = settings.edit();
		settingsEditor.putString("secAnswer", securityAnswer);
		settingsEditor.commit();
	}

}