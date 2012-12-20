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

 */

package ir.secure_msg.sms;

import ir.secure_msg.IO.FilePickerActivity;
import ir.secure_msg.database.Contacts;
import ir.secure_msg.database.DaoSession;
import ir.secure_msg.database.Database;
import ir.secure_msg.preferences.HandlePreferences;
import ir.secure_msg.preferences.PreferencesInterface;
import ir.secure_msg.preferences.SettingsActivity;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import greendroid.app.GDListActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ItemAdapter;
import greendroid.widget.ActionBarItem.Type;
import greendroid.widget.item.Item;
import greendroid.widget.item.ThumbnailItem;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class InboxActivity extends GDListActivity implements
		PreferencesInterface {
	/** Called when the activity is first created. */
	// public static final String SMS_RECEIVED =
	// "android.provider.Telephony.SMS_RECEIVED";
	public static Context contex;
	private static final int ACTION_BAR_SETTINGS = 0;
	private static final int ACTION_BAR_REFRESH=1;
	// private static final int REQUEST_PICK_FILE = 1;
	private List<Item> items;
	private Cursor cursor;
	private ThumbnailItem clickedItem;
	private static final int REQUEST_PICK_FILE = 1;
	private Intent intent;
	private Contacts handler;
	private HandlePreferences handlePrefs;
	private String appMode;
	private DaoSession dao;
	private ItemAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addActionBarItem(Type.Settings, ACTION_BAR_SETTINGS);
		addActionBarItem(Type.Refresh,ACTION_BAR_REFRESH);
		appMode = getAppMode();
		Database g2 = new Database(getApplicationContext());
		dao = g2.getDaoSession();
		cursor = g2.getAllContact(); // databaseoperation.getAllContact(contex);//
		// db.query(conDao.getTablename(),
		// conDao.getAllColumns(),null,
		// null, null, null, null);

		
		items = new ArrayList<Item>();
		Log.d("cursor", Integer.toString(cursor.getCount()));
		while (cursor.moveToNext()) {
			ThumbnailItem row;
			Contacts tm = new Contacts(cursor.getLong(0), cursor.getString(1),
					cursor.getString(2), cursor.getString(3),
					cursor.getString(4), cursor.getString(5));

			if (tm.getName().endsWith("Unknown")) {
				row = new ThumbnailItem(tm.getNumber(), tm.getLastText(),
						android.secure.message.R.drawable.icon);
			} else {
				row = new ThumbnailItem(
						tm.getName() + "," + tm.getFamiliName(),
						tm.getLastText(), android.secure.message.R.drawable.icon);
			}

			row.setTag(tm);

			items.add(row);

		}
		Log.d("handle", "ok");
		adapter = new ItemAdapter(this, items);

		setListAdapter(adapter);
		getListView().setOnItemLongClickListener(
				new AdapterView.OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						;
						clickedItem = (ThumbnailItem) getListView()
								.getAdapter().getItem(arg2);
						// TODO Auto-generated method stub
						// Toast.makeText(SecuremsgActivity.this,
						// Integer.toString(arg2), Toast.LENGTH_LONG)
						// .show();

						return false;
					}
				});
		registerForContextMenu(getListView());

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

		inflater.inflate(android.secure.message.R.menu.operation_menu, menu);
		/*
		 * Toast.makeText(InboxActivity.this, info.position, Toast.LENGTH_LONG)
		 * .show();
		 */

	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) {
		case ACTION_BAR_SETTINGS:
			 //startActivity(new Intent(this, .class));
			startActivity(new Intent(InboxActivity.this,SettingsActivity.class));
			Toast.makeText(InboxActivity.this, "check", 1000).show();
			break;
		case ACTION_BAR_REFRESH:
			break;
		default:
			return super.onHandleActionBarItemClick(item, position);
		}
		return true;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		// Toast.makeText(SecuremsgActivity.this, "First",
		// Toast.LENGTH_LONG).show();

		Log.d("view", v.toString());
		final ThumbnailItem textItem = (ThumbnailItem) l.getAdapter().getItem(
				position);
		handler = (Contacts) textItem.getTag();
		intent = new Intent(InboxActivity.this, ConversationActivity.class);
		intent.putExtra("person", handler);

		// Log.d("check",f.getKeyAddresss());
		Log.d("appMode", appMode);
		// Log.d("key",handler.getKeyAddresss());

		if (appMode.equals("ADVANCED") && handler.getKeyAddresss() == null) {
			Log.d("Khali", "Ok");
			browse();
		} else {
			Log.d("Khali", "nokay");

			startActivity(intent);
		}

		// Log.d("Perfect", "Perfect");

		// Log.d("Perfect",f.messageList.get(0).getBody() );
		//
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		final Contacts clickedrow = (Contacts) clickedItem.getTag();
		switch (item.getItemId()) {

		case android.secure.message.R.id.Reply:
			Intent newMsg = new Intent(InboxActivity.this,
					CreateEncryptedSMS.class);

			newMsg.putExtra("destNumber", clickedrow.getNumber());
			startActivity(newMsg);/*
								 * Dialog d=new Dialog(InboxActivity.this);
								 * Window window=d.getWindow();
								 * window.setFlags(WindowManager
								 * .LayoutParams.FLAG_BLUR_BEHIND,
								 * WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
								 * d.setTitle("Check");
								 * //d.setContentView(R.layout
								 * .home_page_simple);
								 */

			break;
		case android.secure.message.R.id.Open:

			break;
		case android.secure.message.R.id.Delete:
			AlertDialog alertDialog = new AlertDialog.Builder(
					InboxActivity.this).create();

			// Setting Dialog Title
			alertDialog.setTitle("Alert Dialog");

			// Setting Dialog Message
			alertDialog.setMessage("You are going to delete your whole of the conversation with the number: "+clickedrow.getNumber());

			// Setting Icon to Dialog
			alertDialog.setIcon(android.secure.message.R.drawable.remove);

			// Setting OK Button
			alertDialog.setButton("Delete", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// Write your code here to execute after dialog closed
					clickedrow.__setDaoSession(dao);
					clickedrow.delete();
					items.remove(clickedItem);
					adapter.notifyDataSetChanged();
					Toast.makeText(InboxActivity.this, "Deleted",
							Toast.LENGTH_LONG).show();
				}
			});
			
			alertDialog.setButton2("NO!",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
			// Showing Alert Message
			alertDialog.show();

			break;
		default:
			break;
		}

		return false;// .super.onContextItemSelected(item);
	}

	public static void handleincoming(String message, String number) {
		// cursor=db.query(conDao.getTablename(), conDao., selection,
		// selectionArgs, groupBy, having, orderBy)

	}

	private void browse() {

		// Create a new Intent for the file picker activity
		Intent intent = new Intent(getApplicationContext(),
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
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

					/*
					 * selectedFileTextOrError.setText(getResources().getString(
					 * R.string.selected_file) + " " + keyName);
					 */
					// selectedFileTextOrError.setTextColor(Color.GREEN);

					/*
					 * try { publicKey = readKeyFromFile(f.getPath()); } catch
					 * (RuntimeException e) { selectedFileTextOrError
					 * .setText(R.string.invalid_public_key);
					 * selectedFileTextOrError.setTextColor(Color.RED); }
					 */
					// Intent intent = new Intent(InboxActivity.this,
					// ConversationActivity.class);
					handler.setKeyAddresss(keyAddr);
					Database tmp = new Database(getApplicationContext());
					Log.d("before", "ok");
					tmp.updateContact(handler);
					startActivity(intent);

				}
			}
		}
	}

	private String getAppMode() {
		SharedPreferences settings = getSharedPreferences(PREF_FILE_NAME, 0);
		handlePrefs = new HandlePreferences(settings);

		return handlePrefs.getAppMode();
	}
	// onlong
	
}