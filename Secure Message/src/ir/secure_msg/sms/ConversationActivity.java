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

import ir.secure_msg.IO.FilePickerActivity;
import ir.secure_msg.crypt.Base64;
import ir.secure_msg.crypt.RSADecrypt;
import ir.secure_msg.crypt.ReadKey;
import ir.secure_msg.crypt.XOREncryptDecrypt;
import ir.secure_msg.database.Contacts;
import ir.secure_msg.database.Database;
import ir.secure_msg.database.Message;
import ir.secure_msg.keygeneration.ManageKeys;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import ir.secure_msg.R;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import greendroid.app.GDListActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ItemAdapter;
import greendroid.widget.QuickAction;
import greendroid.widget.QuickActionGrid;
import greendroid.widget.QuickActionWidget;
import greendroid.widget.ActionBarItem.Type;
import greendroid.widget.QuickActionWidget.OnQuickActionClickListener;
import greendroid.widget.item.DescriptionItem;
import greendroid.widget.item.Item;
import greendroid.widget.item.SeparatorItem;
import greendroid.widget.item.ThumbnailItem;

public class ConversationActivity extends GDListActivity {
	
	private static final int REQUEST_PICK_FILE = 1;
	private final String XOR_PREFIX = "%@%";
	private final String RSA_PREFIX = "$@$";
	private Contacts lis;
	private QuickActionWidget mGrid;
	private List<Item> items;
	private ItemAdapter adapter;
	private String number;
	private Contacts handler;
	private int SUCESS = 0;
	private int FAILURE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		prepareQuickActionGrid();
		addActionBarItem(Type.Edit);
		Log.d("Perfectgd", "Perfect");
		Bundle extras = getIntent().getExtras();

		Log.d("Perfectgd", "Perfect");
		lis = (Contacts) extras.getSerializable("person");
		
		number = lis.getNumber();
		Log.d("after", "ok1");
		Database tmp = new Database(this);
		List<Message> msgs = tmp.getMessage(lis);
		Log.d("after", "ok2!");
		// Collections.sort(msgs, new mycmp());
		items = new ArrayList<Item>();
		Log.d("after", "ok3");
		// items.add(new DescriptionItem(res.get(0).getBody()));
		// System.out.println(key);
		*//**
		 * get query by key
		 *//*

		// List<msg> res=new ArrayList<msg>();
		
		for (int i = 0; i < msgs.size(); i++) {
			Log.d("checkpoin1", "ok!");
			String body = msgs.get(i).getBody();
			Log.d("checkpoin2", "ok!");
			if (msgs.get(i).getMine()) {// belomgs to owner
				items.add(new SeparatorItem("Me"));
				ThumbnailItem g = new ThumbnailItem(msgs.get(i).getDate()
						.toLocaleString(), "More Info", R.drawable.icon);
				g.setTag(lis);
				items.add(g);
				items.add(new DescriptionItem(body));
			} else {// belongs to others
				items.add(new SeparatorItem(lis.getName() + ","
						+ lis.getFamiliName()));
				ThumbnailItem g = new ThumbnailItem(msgs.get(i).getDate()
						.toLocaleString(), "More Info", R.drawable.icon);
				g.setTag(lis);
				items.add(g);
				items.add(new DescriptionItem(body));
			}
			// items.add(new SeparatorItem(Integer.toString(key)));
			// ThumbnailItem g = new ThumbnailItem(Integer.toString(key),
			// "aka paramotoring", R.drawable.icon);
			// g.setTag(Integer.toString(key));
			// items.add(g);

		}
		adapter = new ItemAdapter(this, items);
		setListAdapter(adapter);
		
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		// super.onListItemClick(l, v, position, id);
		final ThumbnailItem textItem = (ThumbnailItem) l.getAdapter().getItem(
				position);
		Contacts handler = (Contacts) textItem.getTag();
		Toast.makeText(this, handler.getNumber(), 1000).show();
	}

	private String realText(String cipher) {
		String tmp = "";
		if (cipher.startsWith(XOR_PREFIX)) {
			tmp = XOREncryptDecrypt.decrypt(cipher.substring(XOR_PREFIX
					.length()));
		} else if (cipher.startsWith(RSA_PREFIX)) {
			Log.d("checkpoin3", "ok!");
			String add = lis.getKeyAddresss();
			Log.d("checkpoin4", lis.getKeyAddresss());
			PrivateKey privateKey = null;
			try {
				privateKey = ReadKey.readPrivateKeyFromFile(add);
			} catch (InvalidKeyException e1) {
				showAlertDialog(FAILURE, getString(R.string.failure),
						getString(R.string.invalid_key));
				e1.printStackTrace();
			}
			Log.d("checkpoin5", "ok!");
			try {
				try {
					tmp = RSADecrypt.decrypt(Base64.decode(cipher
							.substring(RSA_PREFIX.length())), privateKey);
				} catch (InvalidKeyException e) {
					showAlertDialog(FAILURE, getString(R.string.failure),
							getString(R.string.invalid_key));
				}
				Log.d("checkpoin6", "ok!");
			} catch (IOException e) {
				Log.d("checkpoin7", "ok!");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return tmp;
	}

	public void onShowGrid(View v) {
		mGrid.show(v);
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {

		switch (position) {
		case 0:
			onShowGrid(item.getItemView());
			break;

		default:
			return super.onHandleActionBarItemClick(item, position);
		}

		return true;
	}

	private void prepareQuickActionGrid() {
		mGrid = new QuickActionGrid(this);
		mGrid.addQuickAction(new MyQuickAction(this,
				R.drawable.gd_action_bar_compose, R.string.gd_compose));
		mGrid.addQuickAction(new MyQuickAction(this,
				R.drawable.gd_action_bar_search, ir.secure_msg.R.string.decrypt)); // TODO
																					// Check
		mGrid.addQuickAction(new MyQuickAction(this,
				R.drawable.gd_action_bar_edit, ir.secure_msg.R.string.change));																		// this

		mGrid.setOnQuickActionClickListener(mActionListener);
	}

	private OnQuickActionClickListener mActionListener = new OnQuickActionClickListener() {
		public void onQuickActionClicked(QuickActionWidget widget, int position) {
			switch (position) {
			case 0:
				Intent newMsg = new Intent(ConversationActivity.this,
						CreateEncryptedSMS.class);

				newMsg.putExtra("destNumber", number);
				startActivity(newMsg);
				break;
			case 1:
				Toast.makeText(ConversationActivity.this,
						"Decryption is Starting....", Toast.LENGTH_SHORT)
						.show();
				changeValue(items);
				adapter.notifyDataSetChanged();
				break;
			case 2:
				browse();
			default:
				break;
			}

		}
	};

	private static class MyQuickAction extends QuickAction {

		private static final ColorFilter BLACK_CF = new LightingColorFilter(
				Color.BLUE, Color.BLACK);

		public MyQuickAction(Context ctx, int drawableId, int titleId) {
			super(ctx, buildDrawable(ctx, drawableId), titleId);
		}

		private static Drawable buildDrawable(Context ctx, int drawableId) {
			Drawable d = ctx.getResources().getDrawable(drawableId);
			d.setColorFilter(BLACK_CF);
			return d;
		}

	}

	private void changeValue(List<Item> item) {
		for (int i = 2; i < item.size(); i += 3) {
			DescriptionItem tmp = (DescriptionItem) item.get(i);
			tmp.text = realText(tmp.text);

		}

	}

	private void showAlertDialog(int status, String title, String message) {

		// TODO setup status for success and failure.

		AlertDialog.Builder alert = new AlertDialog.Builder(
				ConversationActivity.this);

		alert.setTitle(title);
		alert.setMessage(message);

		alert.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				});

		alert.show();

	}

	private void browse() {

		// Create a new Intent for the file picker activity
		Intent intent = new Intent(getApplicationContext(),
				FilePickerActivity.class);

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

					
					lis.setKeyAddresss(keyAddr);
					Database tmp = new Database(getApplicationContext());
					Log.d("check before", "ok");
					tmp.updateContact(lis);
					Toast.makeText(ConversationActivity.this, lis.getKeyAddresss(), 10000).show();
					
					

				}
			}
		}
	}

}
*/