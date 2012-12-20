package ir.secure_msg.database;

import ir.secure_msg.database.Message;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.DaoConfig;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.Query;
import de.greenrobot.dao.QueryBuilder;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table MESSAGES.
*/
public class MessageDao extends AbstractDao<Message, Long> {

    public static final String TABLENAME = "MESSAGES";

    /**
     * Properties of entity message.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Body = new Property(1, String.class, "Body", false, "BODY");
        public final static Property Date = new Property(2, java.util.Date.class, "Date", false, "DATE");
        public final static Property Count = new Property(3, Long.class, "Count", false, "COUNT");
        public final static Property Mine = new Property(4, Boolean.class, "Mine", false, "MINE");
        public final static Property ContatcId = new Property(5, long.class, "contatcId", false, "CONTATC_ID");
    };

    private Query<Message> contacts_MessageListQuery;

    public MessageDao(DaoConfig config) {
        super(config);
    }
    
    public MessageDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'MESSAGES' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'BODY' TEXT," + // 1: Body
                "'DATE' INTEGER," + // 2: Date
                "'COUNT' INTEGER," + // 3: Count
                "'MINE' INTEGER," + // 4: Mine
                "'CONTATC_ID' INTEGER NOT NULL );"); // 5: contatcId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'MESSAGES'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Message entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String Body = entity.getBody();
        if (Body != null) {
            stmt.bindString(2, Body);
        }
 
        java.util.Date Date = entity.getDate();
        if (Date != null) {
            stmt.bindLong(3, Date.getTime());
        }
 
        Long Count = entity.getCount();
        if (Count != null) {
            stmt.bindLong(4, Count);
        }
 
        Boolean Mine = entity.getMine();
        if (Mine != null) {
            stmt.bindLong(5, Mine ? 1l: 0l);
        }
        stmt.bindLong(6, entity.getContatcId());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Message readEntity(Cursor cursor, int offset) {
        Message entity = new Message( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // Body
            cursor.isNull(offset + 2) ? null : new java.util.Date(cursor.getLong(offset + 2)), // Date
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3), // Count
            cursor.isNull(offset + 4) ? null : cursor.getShort(offset + 4) != 0, // Mine
            cursor.getLong(offset + 5) // contatcId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Message entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setBody(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDate(cursor.isNull(offset + 2) ? null : new java.util.Date(cursor.getLong(offset + 2)));
        entity.setCount(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
        entity.setMine(cursor.isNull(offset + 4) ? null : cursor.getShort(offset + 4) != 0);
        entity.setContatcId(cursor.getLong(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Message entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Message entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "messageList" to-many relationship of Contacts. */
    public synchronized List<Message> _queryContacts_MessageList(long contatcId) {
        if (contacts_MessageListQuery == null) {
            QueryBuilder<Message> queryBuilder = queryBuilder();
            queryBuilder.where(Properties.ContatcId.eq(contatcId));
            contacts_MessageListQuery = queryBuilder.build();
        } else {
            contacts_MessageListQuery.setParameter(0, contatcId);
        }
        return contacts_MessageListQuery.list();
    }

}
