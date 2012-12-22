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

package ir.secure_msg.documents;

import ir.secure_msg.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutUs extends Activity {

	private String APP_NAME = getString(R.string.app_name);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_us);
		
		setUpViews();
		
	}

	private void setUpViews() {

		TextView appDescription = (TextView) findViewById(R.id.app_description_text_view);
		
		appDescription.setText(APP_NAME  + " is a GPLv3 liscenced app which will reamin free for ever.");
		
	}
	
}
