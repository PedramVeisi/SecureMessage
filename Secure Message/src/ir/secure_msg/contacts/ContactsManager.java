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


package ir.secure_msg.contacts;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;

public class ContactsManager {

	public static ArrayList<HashMap<String, String>> getAutoCompleteArrayList(ContentResolver contentResolver) {
	
		ArrayList<HashMap<String, String>> autoCompleteList = new ArrayList<HashMap<String, String>>();

		Cursor cursor = contentResolver.query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				String ContactId = cursor.getString(cursor
						.getColumnIndex(ContactsContract.Contacts._ID));

				String contactName = cursor
						.getString(cursor
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				
				if (Integer
						.parseInt(cursor.getString(cursor
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

					Cursor phonesCursor = contentResolver.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = ?", new String[] { ContactId }, null);

					String phoneType = null;

					while (phonesCursor.moveToNext()) {

						HashMap<String, String> autoCompleteListItemMap = new HashMap<String, String>();
						autoCompleteListItemMap.put("name", contactName);

						switch (phonesCursor.getInt(phonesCursor
								.getColumnIndex(Phone.TYPE))) {

						case Phone.TYPE_MOBILE:
							phoneType = "Mobile";
							break;
						case Phone.TYPE_HOME:
							phoneType = "Home";
							break;
						case Phone.TYPE_WORK:
							phoneType = "Work";
							break;
						case Phone.TYPE_OTHER:
							phoneType = "Other";
							break;

						}

						autoCompleteListItemMap.put("phone_type", phoneType);

						String contactPhoneNumber = phonesCursor
								.getString(phonesCursor
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

						autoCompleteListItemMap.put("number", contactPhoneNumber);

						autoCompleteList.add(autoCompleteListItemMap);

					}
					phonesCursor.close();
				}

			}
		}
		
		return autoCompleteList;
	
	}
	
}
