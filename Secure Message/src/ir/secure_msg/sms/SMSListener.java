/*
	Copyright 2012 Mohammad Moein

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

 

package ir.secure_msg.sms;


import ir.secure_msg.database.Database;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;


public class SMSListener extends BroadcastReceiver {
	private SharedPreferences preferences;
	private final String XOR_PREFIX = "%@%";
	private final String RSA_PREFIC="$@$";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction()
				.equals("android.provider.Telephony.SMS_RECEIVED")) {
			Bundle bundle = intent.getExtras(); // ---get the SMS message passed
			// in---
		//	getApplicationContext();
			
			SmsMessage[] msgs = null;
			String msg_from;
			if (bundle != null) {
				// ---retrieve the SMS message received---
				try {
					Object[] pdus = (Object[]) bundle.get("pdus");
					msgs = new SmsMessage[pdus.length];
					for (int i = 0; i < msgs.length; i++) {
						msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
						// msg_from = msgs[i].getOriginatingAddress();
						// String msgBody = msgs[i].getMessageBody();
					}
					for (SmsMessage mes : msgs) {
						String msg = mes.getMessageBody();
						String To = mes.getOriginatingAddress();
						
						if (msg.toLowerCase().startsWith(XOR_PREFIX)||msg.toLowerCase().startsWith(RSA_PREFIC)) {
							String body = msg;
							Database g=new Database(context);
							g.insertNewMessage(body, To);
							abortBroadcast();
						}

					}
				//	SecuremsgActivity.data();
					// abortBroadcast();
					//Log.d("notAbort", "s");
				} catch (Exception e) {
					Log.d("Exception caught",e.getMessage());
				}
			}
		}
	}

}
*/