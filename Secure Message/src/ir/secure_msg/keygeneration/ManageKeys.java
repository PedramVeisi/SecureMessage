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


package ir.secure_msg.keygeneration;

import ir.secure_msg.io.CopyFile;
import ir.secure_msg.io.CreateAppFolder;
import ir.secure_msg.io.FileExistsException;
import ir.secure_msg.uicomponents.IconContextMenu;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;

import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.app.Dialog;
import org.holoeverywhere.app.ListActivity;

import com.actionbarsherlock.view.MenuItem;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import ir.secure_msg.R;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ManageKeys extends ListActivity {

	private int SUCESS = 0;
	private int FAILURE = 1;
	//private int COMMON_WARNING = 2;

	private final int CONTEXT_MENU_ID = 1;
	private IconContextMenu iconContextMenu = null;

	private final int MENU_ITEM_1_EXPORT_PUBLIC = 1;
	private final int MENU_ITEM_2_EXPORT_PRIVATE = 2;
	private final int MENU_ITEM_3_REMOVE = 3;
	
	private String APP_NAME = "SecureMessage";
	private final String KEYS_FOLDER_NAME = "keys";
	
	String listSelectedItemName;

	// It's here so the Thread inner class can see it
	private ProgressDialog processingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generate_key);

		setUpViews();
	}

	private void setUpViews() {

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.manage_keys);
		
		setUpList();

		Resources res = getResources();

		// init the menu
		iconContextMenu = new IconContextMenu(this, CONTEXT_MENU_ID);
		
		iconContextMenu.addItem(res, R.string.export_public_key,
				R.drawable.export, MENU_ITEM_1_EXPORT_PUBLIC);
		iconContextMenu.addItem(res, R.string.export_private_key,
				R.drawable.export, MENU_ITEM_2_EXPORT_PRIVATE);
		iconContextMenu.addItem(res, R.string.remove, R.drawable.remove,
				MENU_ITEM_3_REMOVE);

		// set onclick listener for context menu
		iconContextMenu
				.setOnClickListener(new IconContextMenu.IconContextMenuOnClickListener() {
					public void onClick(int menuId) {
						

						AlertDialog.Builder alert = new AlertDialog.Builder(
								ManageKeys.this);
						
						final EditText input = new EditText(ManageKeys.this);

						
						switch (menuId) {
						case MENU_ITEM_1_EXPORT_PUBLIC:
							
							new CreateAppFolder(APP_NAME);
							
							alert.setTitle(R.string.export_name);
							alert.setMessage(R.string.enter_name_for_exported_key);

							// Set an EditText view to get user input
							alert.setView(input);

							alert.setPositiveButton(R.string.ok,
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
												int whichButton) {
											String exportedKeyName = input.getText().toString();
											// Pair Name is entered

											String sourceKeyAddr = getFilesDir() + "/" + KEYS_FOLDER_NAME + "/" + listSelectedItemName + "/" + "public.key";
											String destinationKeyAddr = Environment.getExternalStorageDirectory() + "/" + APP_NAME + "/" + exportedKeyName + "_public.key";
											
											File sourceKeyFile = new File(sourceKeyAddr);
											File destinationKeyFile = new File(destinationKeyAddr);
																						
											try {
												
												CopyFile.copyFile(sourceKeyFile, destinationKeyFile);
												
												Toast.makeText(getApplicationContext(), R.string.exported_to + destinationKeyAddr, Toast.LENGTH_LONG).show();
												
												
											} catch (IOException e) {

													showAlertDialog(FAILURE, getResources().getString(R.string.error), getResources().getString(R.string.something_went_wrong));

											} catch (FileExistsException e) {
												showAlertDialog(FAILURE, getResources().getString(R.string.already_exists), getResources().getString(R.string.pair_already_exists_description));
											}
										}
									});

							alert.setNegativeButton(R.string.cancel,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											// Canceled.
										}
									});

							alert.show();
							
							
							break;
						case MENU_ITEM_2_EXPORT_PRIVATE:

							new CreateAppFolder(APP_NAME);
							
							alert = new AlertDialog.Builder(ManageKeys.this);

							alert.setTitle(R.string.warning);
							alert.setMessage( R.string.private_key_export_warning);

							alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									
									AlertDialog.Builder alert = new AlertDialog.Builder(ManageKeys.this);
									
									alert.setTitle(R.string.export_name);
									alert.setMessage(R.string.enter_name_for_exported_key);

									// Set an EditText view to get user input
									alert.setView(input);

									alert.setPositiveButton(R.string.ok,
											new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog,
														int whichButton) {
													String exportedKeyName = input.getText().toString();
													// Pair Name is entered

													String sourceKeyAddr = getFilesDir() + "/" + KEYS_FOLDER_NAME + "/" + listSelectedItemName + "/" + "private.key";
													String destinationKeyAddr = Environment.getExternalStorageDirectory() + "/" + APP_NAME + "/" + exportedKeyName + "_private.key";
													
													File sourceKeyFile = new File(sourceKeyAddr);
													File destinationKeyFile = new File(destinationKeyAddr);
																								
													try {
														
														CopyFile.copyFile(sourceKeyFile, destinationKeyFile);
														
														Toast.makeText(getApplicationContext(), R.string.exported_to + destinationKeyAddr, Toast.LENGTH_LONG).show();
														
														
													} catch (IOException e) {

															showAlertDialog(FAILURE, getResources().getString(R.string.error), getResources().getString(R.string.something_went_wrong));

													} catch (FileExistsException e) {
														showAlertDialog(FAILURE, getResources().getString(R.string.already_exists), getResources().getString(R.string.pair_already_exists_description));
													}
												}
											});

									alert.setNegativeButton(R.string.cancel,
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int whichButton) {
													// Canceled.
												}
											});

									alert.show();

								}
							});

							alert.show();							
							
							break;
						case MENU_ITEM_3_REMOVE:

							/*Verification to remove using alert dialog*/
							alert.setTitle(R.string.are_you_sure);
							alert.setMessage(R.string.remove_pair_dialog_description);

							alert.setPositiveButton(R.string.ok,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											/*OK button function*/
											boolean status = deletePairFolder(listSelectedItemName);
											if (status) {
												Toast.makeText(
														getApplicationContext(),
														R.string.item_removed, Toast.LENGTH_SHORT)
														.show();
												setUpList();
											} else
												showAlertDialog(FAILURE,
														getString(R.string.failure),
														getString(R.string.deletion_failed));
											/*OK button function*/
											
										}
									});

							alert.setNegativeButton(R.string.cancel,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											// Canceled.
										}
									});

							alert.show();

							break;

						}
					}
				});

		Button generateButton = (Button) findViewById(R.id.generate_keys_button);

		generateButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

				AlertDialog.Builder alert = new AlertDialog.Builder(
						ManageKeys.this);

				alert.setTitle(R.string.keypair_name);
				alert.setMessage(R.string.enter_keypair_name);

				// Set an EditText view to get user input
				final EditText input = new EditText(ManageKeys.this);
				alert.setView(input);

				alert.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								String pairName = input.getText().toString();
								// Pair Name is entered
								startKeyGeneration(pairName);
								setUpList();
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

			}
		});

	}

	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {

	    case android.R.id.home:
	         super.onBackPressed();   

	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	private void setUpList() {
		
		ArrayList<String> folderNames = getCurrentPairsNames();
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, folderNames));
				
		getListView().setOnItemClickListener(itemClickHandler);

	}

	/**
	 * list item long click handler used to show the context menu
	 */
	private OnItemClickListener itemClickHandler = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {

			listSelectedItemName = (String) getListView().getAdapter().getItem(
					position);

			showDialog(CONTEXT_MENU_ID);
		}
	};

	/**
	 * create context menu
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == CONTEXT_MENU_ID) {
			return iconContextMenu.createMenu(getString(R.string.manage_keys));
		}
		return (Dialog) super.onCreateDialog(id);
	}

	private void showAlertDialog(int status, String title, String message) {

		// TODO setup status for success and failure.

		AlertDialog.Builder alert = new AlertDialog.Builder(ManageKeys.this);

		alert.setTitle(title);
		alert.setMessage(message);

		alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}
		});

		alert.show();

	}

	private boolean deletePairFolder(String folder) {

		//TODO 3245jm,. Creates all this dialogs
		
		if (folder == null)
			showAlertDialog(FAILURE, getString(R.string.error), getString(R.string.something_went_wrong));

		File pairDirectory = new File(getFilesDir() + "/" + KEYS_FOLDER_NAME + "/" + folder);

		if (!pairDirectory.exists())
			showAlertDialog(FAILURE, getString(R.string.error), getString(R.string.no_such_folder));

		if (!pairDirectory.isDirectory())
			showAlertDialog(FAILURE, getString(R.string.error), getString(R.string.something_went_wrong));

		// Recursively delete contents of a folder
		String[] insideFiles = pairDirectory.list();

		if (insideFiles != null) {
			for (int i = 0; i < insideFiles.length; i++) {
				File entry = new File(pairDirectory, insideFiles[i]);

				// System.out.println("\tremoving entry " + entry);

				if (entry.isDirectory()) {
					if (!deletePairFolder(entry.toString()))
						return false;
				} else {
					if (!entry.delete())
						return false;
				}
			}
		}

		// Deletes main file
		return pairDirectory.delete();
	}

	private ArrayList<String> getCurrentPairsNames() {

		ArrayList<String> folderNames = new ArrayList<String>();
		
		File dir = new File(getFilesDir() + "/" + KEYS_FOLDER_NAME);

		File[] files = dir.listFiles();

		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				folderNames.add(files[i].getName());
			}
		}

		return folderNames;
	}

	private void startKeyGeneration(final String pairName) {

		// If folder created successfully (Means it's not duplicate) create new
		// thread and run RSA generate
		if (createPairFolder(pairName) == 0) {

			processingDialog = ProgressDialog.show(ManageKeys.this, "",
					getString(R.string.creating_keypair_wait), true);

			new Thread() {
				public void run() {

					generateRSAKeys(pairName);
					processingDialog.dismiss();
				}
			}.start();

			// TODO Make this work.MultiThreading needed.
			// Toast.makeText(getApplicationContext(),
			// "KeyPair Creation was successful.", 1000)
			// .show();

		}

		// Toast.makeText(getApplicationContext(),
		// "Key Pair created.", 1000).show();

	}

	private int createPairFolder(String pairName) {

		File newDir = null;
		newDir = new File(getFilesDir() + "/" + KEYS_FOLDER_NAME, pairName);
		if (!newDir.exists()) {
			newDir.mkdirs();
			return SUCESS;
		} else {
			showAlertDialog(FAILURE, getString(R.string.already_exists),
					getString(R.string.pair_already_exists_description));
			return FAILURE;
		}
	}

	private void generateRSAKeys(String pairName) {

		try {
			// Generate a 1024-bit RSA key pair
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(512);
			KeyPair keypair = keyGen.genKeyPair();

			KeyFactory fact = KeyFactory.getInstance("RSA");
			RSAPublicKeySpec publicKey = fact.getKeySpec(keypair.getPublic(),
					RSAPublicKeySpec.class);
			RSAPrivateKeySpec privateKey = fact.getKeySpec(
					keypair.getPrivate(), RSAPrivateKeySpec.class);

			saveToFile(pairName, "public.key", publicKey.getModulus(),
					publicKey.getPublicExponent());
			saveToFile(pairName, "private.key", privateKey.getModulus(),
					privateKey.getPrivateExponent());

		} catch (java.security.NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO create alert dialog
			e.printStackTrace();
		}

	}

	private void saveToFile(String pairName, String fileName,
			BigInteger modulus, BigInteger exponent) throws IOException {

		String pairFolderAddr = getFilesDir().toString() + "/" + KEYS_FOLDER_NAME + "/" + pairName + "/";

		ObjectOutputStream oout = new ObjectOutputStream(
				new BufferedOutputStream(new FileOutputStream(pairFolderAddr
						+ fileName)));

		try {
			oout.writeObject(modulus);
			System.out.println(modulus);
			oout.writeObject(exponent);
			System.err.println(exponent);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			oout.close();
		}
	}

}