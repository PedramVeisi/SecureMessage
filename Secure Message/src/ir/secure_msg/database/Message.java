package ir.secure_msg.database;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table MESSAGES.
 */
public class Message {

    private Long id;
    private String Body;
    private java.util.Date Date;
    private Long Count;
    private Boolean Mine;
    private long contatcId;

    public Message() {
    }

    public Message(Long id) {
        this.id = id;
    }

    public Message(Long id, String Body, java.util.Date Date, Long Count, Boolean Mine, long contatcId) {
        this.id = id;
        this.Body = Body;
        this.Date = Date;
        this.Count = Count;
        this.Mine = Mine;
        this.contatcId = contatcId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String Body) {
        this.Body = Body;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public void setDate(java.util.Date Date) {
        this.Date = Date;
    }

    public Long getCount() {
        return Count;
    }

    public void setCount(Long Count) {
        this.Count = Count;
    }

    public Boolean getMine() {
        return Mine;
    }

    public void setMine(Boolean Mine) {
        this.Mine = Mine;
    }

    public long getContatcId() {
        return contatcId;
    }

    public void setContatcId(long contatcId) {
        this.contatcId = contatcId;
    }

}