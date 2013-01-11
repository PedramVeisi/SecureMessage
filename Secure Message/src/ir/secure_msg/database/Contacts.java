package ir.secure_msg.database;

import ir.secure_msg.database.DaoSession;

import java.io.Serializable;
import java.util.List;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table CONTACTS.
 */
@SuppressWarnings("serial")
public class Contacts implements Serializable {

    private Long id;
    private String Name;
    private String FamiliName;
    private String Number;
    private String LastText;
    private String keyAddresss;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    public transient ContactsDao myDao;
    public transient MessageDao msgDao;

    private List<Message> messageList;

    public Contacts() {
    }

    public Contacts(Long id) {
        this.id = id;
    }

    public Contacts(Long id, String Name, String FamiliName, String Number, String LastText, String keyAddresss) {
        this.id = id;
        this.Name = Name;
        this.FamiliName = FamiliName;
        this.Number = Number;
        this.LastText = LastText;
        this.keyAddresss = keyAddresss;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getContactsDao() : null;
        msgDao = daoSession != null ? daoSession.getMessageDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getFamiliName() {
        return FamiliName;
    }

    public void setFamiliName(String FamiliName) {
        this.FamiliName = FamiliName;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String Number) {
        this.Number = Number;
    }

    public String getLastText() {
        return LastText;
    }

    public void setLastText(String LastText) {
        this.LastText = LastText;
    }

    public String getKeyAddresss() {
        return keyAddresss;
    }

    public void setKeyAddresss(String keyAddresss) {
        this.keyAddresss = keyAddresss;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public synchronized List<Message> getMessageList() {
        if (messageList == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MessageDao targetDao = daoSession.getMessageDao();
            messageList = targetDao._queryContacts_MessageList(id);
        }
        return messageList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetMessageList() {
        messageList = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        } 
        for(int i=0;i<this.getMessageList().size();i++){
        	msgDao.delete(this.getMessageList().get(i));
        }
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#d(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}
