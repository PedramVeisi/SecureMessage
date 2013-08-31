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

package ir.secure_msg.sms;

import ir.secure_msg.contacts.ContactsManager;
import ir.secure_msg.crypt.RSAEncrypt;
import ir.secure_msg.crypt.ReadKey;
import ir.secure_msg.crypt.XOREncryptDecrypt;
import ir.secure_msg.io.FilePickerActivity;
import ir.secure_msg.preferences.HandlePreferences;
import ir.secure_msg.preferences.PreferencesInterface;

import java.io.File;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.holoeverywhere.app.Activity;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import ir.secure_msg.R;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import org.holoeverywhere.widget.MultiAutoCompleteTextView;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.EditText;
import android.widget.SimpleAdapter;
import org.holoeverywhere.widget.TextView;
import org.holoeverywhere.widget.Toast;

public class CreateEncryptedSMS extends Activity implements
		PreferencesInterface {

	private static final int REQUEST_PICK_FILE = 1;

	private TextView selectedFileTextOrError;
	private Button browseButton;
	private Button encryptButton;
	private Button sendSMSButton;
	private EditText plainTextField;
	private Button clearEncryptedTextButton;
	private MultiAutoCompleteTextView receiverMultiAutoCompleteTextView;
	private String multipleInputContact = "";
	private String appMode;
	private ArrayList<String> receiverNumbers = new ArrayList<String>();

	private PublicKey publicKey = null;

	private boolean encryptedFlag = false;

	private String XOR_PREFIX = "%@%";
	private String RSA_PREFIX = "$@$";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences settings = getSharedPreferences(PREF_FILE_NAME, 0);
		final HandlePreferences handlePrefs = new HandlePreferences(settings);

		appMode = handlePrefs.getAppMode();

		if (appMode.equals("SIMPLE")) {
			setContentView(R.layout.simple_mode_compose);
		} else if (appMode.equals("ADVANCED")) {
			setContentView(R.layout.advanced_mode_compose);
			setUpAdvanced();
		}

		setUpViews();
		
		Bundle extras = getIntent().getExtras();
		Log.d("enter", "ok!");
		String tmp="";
		if (extras!=null && extras.containsKey("destNumber")) {
			Log.d("enter", "ok!");
			tmp= extras.getString("destNumber");
		}
		
		receiverMultiAutoCompleteTextView = (MultiAutoCompleteTextView) findViewById(R.id.sms_receiver_autocomplete_textview);
		receiverMultiAutoCompleteTextView.setText(tmp);
	}

	private void setUpAdvanced() {
		selectedFileTextOrError = (TextView) findViewById(R.id.selected_file_text);
		browseButton = (Button) findViewById(R.id.browse_button);

		browseButton.setOnClickListener(new OnClickListener() {

			// TODO ask in a dialog to selecte from sdcard or internal
			public void onClick(View v) {
				// Create a new Intent for the file picker activity
				Intent intent = new Intent(CreateEncryptedSMS.this,
						FilePickerActivity.class);

				// Set the initial directory to be the sdcard
				// intent.putExtra(FilePickerActivity.EXTRA_FILE_PATH,
				// Environment.getExternalStorageDirectory());

				// Show hidden files
				// intent.putExtra(FilePickerActivity.EXTRA_SHOW_HIDDEN_FILES,
				// true);

				// Only make .png files visible
				// ArrayList<String> extensions = new ArrayList<String>();
				// extensions.add(".png");
				// intent.putExtra(FilePickerActivity.EXTRA_ACCEPTED_FILE_EXTENSIONS,
				// extensions);

				// Start the activity
				startActivityForResult(intent, REQUEST_PICK_FILE);

			}
		});

	}

	private void setUpViews() {

		getSupportActionBar().setHomeButtonEnabled(true);
		
		plainTextField = (EditText) findViewById(R.id.plain_text_field);
		encryptButton = (Button) findViewById(R.id.encrypt_text_button);
		clearEncryptedTextButton = (Button) findViewById(R.id.clear_encrypted_text_button);
		sendSMSButton = (Button) findViewById(R.id.send_sms_button);

		receiverMultiAutoCompleteTextView = (MultiAutoCompleteTextView) findViewById(R.id.sms_receiver_autocomplete_textview);
	
		manageReceiverAutocomplete();

		encryptButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				String cipher = null;
				String message = plainTextField.getText().toString();

				if (message.length() != 0) {
					if (appMode.equals("ADVANCED")) {
						if (publicKey == null) {
							selectedFileTextOrError
									.setText(R.string.select_public_key);
						} else {
							RSAEncrypt rsaEncrypt = new RSAEncrypt();
							try {
								cipher = RSA_PREFIX
										+ rsaEncrypt.encrypt(
												message.getBytes(), publicKey);

								encryptedFlag = true;

							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					} else if (appMode.equals("SIMPLE")) {
						cipher = XOR_PREFIX
								+ XOREncryptDecrypt.encrypt(message);
						encryptedFlag = true;
					}

					if (encryptedFlag) {
						Toast.makeText(
								CreateEncryptedSMS.this,
								getResources()
										.getString(R.string.cipher_length)
										+ " " + cipher.length(),
								Toast.LENGTH_SHORT).show();

						plainTextField.setText(cipher);

						plainTextField.setClickable(false);
						plainTextField.setFocusable(false);

						sendSMSButton.setTextColor(Color.GREEN);
						clearEncryptedTextButton.setTextColor(Color.RED);
					}
				} else {
					Toast.makeText(CreateEncryptedSMS.this, R.string.fill_text_field,
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		clearEncryptedTextButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

				if (!plainTextField.getText().toString().equals("")) {

					AlertDialog.Builder alert = new AlertDialog.Builder(
							CreateEncryptedSMS.this);

					alert.setTitle(R.string.are_you_sure);
					alert.setMessage(R.string.clear_text_field_description);

					alert.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									plainTextField.setText("");
									plainTextField.setFocusable(true);
									plainTextField.setClickable(true);
									plainTextField
											.setFocusableInTouchMode(true);

								}
							});
					alert.setNegativeButton(R.string.no,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// Canceled.
								}
							});

					alert.show();
				}
			}
		});

		sendSMSButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				prepareToSendSMS();
			}
		});
	}

	private void manageManualNumbers() {
		// Read Manual numbers

		receiverMultiAutoCompleteTextView = (MultiAutoCompleteTextView) findViewById(R.id.sms_receiver_autocomplete_textview);

		// TODO Show user a toast with this number
		@SuppressWarnings("unused")
		int unParsableNumbers = 0;

		String contactNumber = receiverMultiAutoCompleteTextView.getText()
				.toString();
		String cleanContactNumber = contactNumber.replaceAll("\\+", "00");

		StringTokenizer st = new StringTokenizer(cleanContactNumber, ",");
		String token = null;

		while (st.hasMoreTokens()) {
			try {
				token = st.nextToken().trim(); // Get string without beginning
												// whitespace(if any)
				Integer.parseInt(token);
				receiverNumbers.add(token);
			} catch (NumberFormatException e) {
				unParsableNumbers++;
			}
		}
	}

	private void prepareToSendSMS() {

		manageManualNumbers();

		String smsText = plainTextField.getText().toString();

		if (smsText.length() == 0 || receiverNumbers.isEmpty()) {
			Toast.makeText(CreateEncryptedSMS.this,
					"Fill both \"To\" and \"Text\" fields", Toast.LENGTH_SHORT)
					.show();
		} else {

			if (!encryptedFlag) {

				AlertDialog.Builder alert = new AlertDialog.Builder(
						CreateEncryptedSMS.this);

				alert.setTitle(R.string.warning);
				alert.setMessage("Text is not encrypted.Continue?");

				alert.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								sendSMS();
							}

						});

				alert.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Canceled.
							}
						});

				alert.show();

			} else {
				sendSMS();
			}
		}

	}

	private void sendSMS() {

		// TODO Move to strings.xml
		String sent = "SMS SENT";
		String delivered = "SMS_DELIVERED";

		PendingIntent sentPendingIntent = PendingIntent.getBroadcast(this, 0,
				new Intent(sent), 0);

		PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast(this,
				0, new Intent(delivered), 0);

		// TODO Change Toasts to status in database and outbox
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "SMS sent",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(getBaseContext(), "Generic failure",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(getBaseContext(), "No service",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(getBaseContext(), "Radio off",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(sent));

		// Receive delivery report
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "SMS delivered",
							Toast.LENGTH_SHORT).show();
					break;
				case Activity.RESULT_CANCELED:
					Toast.makeText(getBaseContext(), "Delivery failed",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(delivered));

		String smsText = plainTextField.getText().toString();

		for (int i = 0; i < receiverNumbers.size(); i++) {
			SmsManager sm = SmsManager.getDefault();
			sm.sendTextMessage(receiverNumbers.get(i), null, smsText,
					sentPendingIntent, deliveredPendingIntent);
		}

	}

	private void manageReceiverAutocomplete() {

		ContentResolver contentResolver = getContentResolver();
		ArrayList<HashMap<String, String>> autoCompleteList = ContactsManager
				.getAutoCompleteArrayList(contentResolver);

		SimpleAdapter autoCompleteAdapter = new SimpleAdapter(this,
				autoCompleteList, R.layout.sms_receiver_list_item,
				new String[] { "name", "phone_type", "number" }, new int[] {
						R.id.contact_name, R.id.contact_phone_type,
						R.id.contact_number });
		receiverMultiAutoCompleteTextView.setAdapter(autoCompleteAdapter);
		receiverMultiAutoCompleteTextView
				.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

		receiverMultiAutoCompleteTextView
				.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						String contactName = ((TextView) arg1
								.findViewById(R.id.contact_name)).getText()
								.toString();

						String contactNumber = ((TextView) arg1
								.findViewById(R.id.contact_number)).getText()
								.toString();

						String cleanContactNumber = contactNumber.replaceAll(
								"-", "");

						receiverNumbers.add(cleanContactNumber);

						multipleInputContact = multipleInputContact
								+ contactName + " <" + cleanContactNumber
								+ " >, ";

						receiverMultiAutoCompleteTextView
								.setText(multipleInputContact);
						// receiverMultiAutoCompleteTextView.setSelection(receiverMultiAutoCompleteTextView.length());
						plainTextField.requestFocus();

					}
				});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_PICK_FILE:
				if (data.hasExtra(FilePickerActivity.EXTRA_FILE_PATH)) {
					// Get the file path
					File f = new File(
							data.getStringExtra(FilePickerActivity.EXTRA_FILE_PATH));

					// Set the file path text view

					String keyAddr = f.getPath();
					int i = keyAddr.lastIndexOf('/');
					String keyName = keyAddr.substring(i + 1);

					selectedFileTextOrError.setText(getResources().getString(
							R.string.selected_file)
							+ " " + keyName);
					selectedFileTextOrError.setTextColor(Color.GREEN);

					try {
						publicKey = ReadKey.readPublicKeyFromFile(f.getPath());
					} catch (RuntimeException e) {
						selectedFileTextOrError
								.setText(R.string.invalid_public_key);
						selectedFileTextOrError.setTextColor(Color.RED);
					}
				}
			}
		}
	}

//	private String setCorrectNumber() {
//		Bundle extras = getIntent().getExtras();
//		if (extras.containsKey("destNumber")) {
//			return extras.getString("destNumber");
//		}
//		return " ";
//	}
}