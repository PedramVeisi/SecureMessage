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

package ir.secure_msg.database;

import ir.secure_msg.database.ContactsDao.Properties;
import ir.secure_msg.database.DaoMaster.DevOpenHelper;

import java.util.Date;
import java.util.List;

import de.greenrobot.dao.QueryBuilder;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import greendroid.app.GDActivity;



public class Database extends GDActivity {
	private  SQLiteDatabase db;
	private  DaoMaster daoMaster;
	private  DaoSession daoSession;
	public DaoSession getDaoSession() {
		return daoSession;
	}

	public Database(Context contex){
		
		Log.d("1", "ok");
		
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(contex, "sm",
				null);
		Log.d("2", "ok");
		db = helper.getWritableDatabase();
		Log.d("3", "ok");
		daoMaster = new DaoMaster(db);
		Log.d("4", "ok");
		daoSession = daoMaster.newSession();
		Log.d("5", "ok");
	}

	public  Cursor getAllContact(){
	
		ContactsDao conDao = daoSession.getContactsDao();
		return db.query(conDao.getTablename(), conDao.getAllColumns(), null,
				null, null, null, null);
	}
	public  void insertNewMessage(String msgbody,String number){
		ContactsDao conDao = this.daoSession.getContactsDao();
		QueryBuilder<Contacts> conts=conDao.queryBuilder();
		conts.where(Properties.Number.eq(number));
		List<Contacts> t=conts.list();
		MessageDao msgdao=this.daoSession.getMessageDao();
		Message msg;
		Log.d("DbProcess", "ok!");
		if(t.size()!=0){
			 msg=new Message(null, msgbody, new Date(), null, false, t.get(0).getId());
			 Log.d("Include", "hast");
			
			
		}
		else{
			Log.d("Error","OOps");
			Contacts tmp=new Contacts(null, "Unknown", "Unnamed", number, msgbody.substring(0, 3),null);
			
			conDao.insert(tmp);
			msg=new Message(null, msgbody, new Date(), null, false, conDao.getKey(tmp));
			
		}
		msgdao.insert(msg);
	}
	public List<Message> getMessage(Contacts con){
		ContactsDao conDao = this.daoSession.getContactsDao();
		return conDao.load(con.getId()).getMessageList();
	}
	public void updateContact(Contacts contact){
		ContactsDao conDao = this.daoSession.getContactsDao();
		QueryBuilder<Contacts> conts=conDao.queryBuilder();
		conts.where(Properties.Number.eq(contact.getNumber()));
		List<Contacts> t=conts.list();
		Log.d("successful","lk");
		t.add(0, contact);
		t.get(0).myDao=conDao;
		t.get(0).update();
	}
	

}
