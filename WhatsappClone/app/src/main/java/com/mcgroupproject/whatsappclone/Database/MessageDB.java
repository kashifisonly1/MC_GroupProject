package com.mcgroupproject.whatsappclone.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.mcgroupproject.whatsappclone.Model.Message;

import java.util.ArrayList;
import java.util.List;
public class MessageDB extends SQLiteOpenHelper {
    public static final int VERSION = 16;
    private static MessageDB db;
    private static final String MESSAGES = "messages";
    private static final String ID = "id";
    private static final String SENDER_ID = "sender_id";
    private static final String RECEIVER_ID = "receiver_id";
    private static final String PUSH_ID = "push_id";
    private static final String ALTER_MESSAGE_TABLE = "ALTER TABLE " + MESSAGES + " ADD COLUMN " + PUSH_ID + " TEXT NOT NULL";
    private static final String TEXT = "text";
    private static final String STATUS = "status";
    private static final String TIME = "time";
    private static final String REPLY_AT_ID = "reply_at_id";
    private static final String CREATE_MESSAGE_TABLE = "CREATE TABLE IF NOT EXISTS " + MESSAGES + " (" +
            ID + " TEXT PRIMARY KEY," +
            SENDER_ID + " TEXT NOT NULL," +
            RECEIVER_ID + " TEXT NOT NULL," +
            PUSH_ID + " TEXT NOT NULL," +
            TEXT + " TEXT NOT NULL," +
            STATUS + " INTEGER NOT NULL," +
            TIME + " TEXT NOT NULL," +
            REPLY_AT_ID + " TEXT)";
    private static final String DROP_MESSAGE_TABLE = "DROP TABLE IF EXISTS "  + MESSAGES;
    public static final String USERS = "users";
    public static final String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + USERS;
    public static final String USERNAME = "username";
    public static final String PHONE = "phone";
    public static final String URL_PROFILE = "url_profile";
    public static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + USERS + " ( " +
            ID + " TEXT NOT NULL, " +
            USERNAME + " TEXT NOT NULL," +
            PHONE + " TEXT NOT NULL," +
            URL_PROFILE + " TEXT)";

    public MessageDB(@Nullable Context context) {
        super(context, "whatsapp.db", null, VERSION);
    }

    public static void Init(Context context)
    {
        db = new MessageDB(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MESSAGE_TABLE);
        db.execSQL(CREATE_USER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_MESSAGE_TABLE);
        db.execSQL(CREATE_MESSAGE_TABLE);
    }

    public static boolean Add(Message msg)
    {
        return db.AddMessage(msg);
    }
    public boolean AddMessage(Message msg)
    {
        if(DoesMessageExist(msg.getId()))
            return false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = WriteMessage(msg);
        long insert = db.insert(MESSAGES, null, cv);
        db.close();
        if(insert == -1)
            return false;
        return true;
    }
    public static boolean rm(Message message){
        return db.Remove(message);
    }
    public boolean Remove(Message message)
    {
        if(!DoesMessageExist(message.getId()))
            return false;

        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = ID + "=?";
        String whereArgs[] = {message.getId()};
        int delete = db.delete(MESSAGES, whereClause, whereArgs);
        db.close();
        if(delete < 1)
            return false;
        return true;
    }
    public static boolean Update(String id, String status)
    {
        return db.UpdateMessage(id,status);
    }
    public boolean UpdateMessage(String id, String status)
    {
        if(!DoesMessageExist(id))
            return false;
        Message m = GetMessage(id);
        m.setStatus(Integer.parseInt(status));
        ContentValues contentValues = WriteMessage(m);
        SQLiteDatabase db = getWritableDatabase();
        String whereClause =ID + "=?" + " AND " + STATUS +" <= ?";

        String whereArgs[] = {id,status};
        int update = db.update(MESSAGES, contentValues, whereClause, whereArgs);
        db.close();

        if(update < 1)
            return false;
        return true;
    }
    public static List<Message> unseenMsg(){
        return db.GetAllUnsendMessages();
    }
    public static List<Message> unreadMsg(String uid){
        return db.GetAllNotSeenMessages(uid);
    }
    public static List<Message> undeliveredMsg(String uid) {return db.GetAllUndeliveredMessage(uid);}
    public List<Message> GetAllUndeliveredMessage(String uid) {
        String Selection = RECEIVER_ID + "=?" + " AND " + STATUS + "<?";
        String[] SelectionArgs = new String[]{uid, "3"};
        return GetMessageList(Selection,SelectionArgs);
    }
    public List<Message> GetAllUnsendMessages()
    {
        String Selection = STATUS + "=?";
        String[] SelectionArgs = new String[]{"1"};
        return GetMessageList(Selection,SelectionArgs);
    }
    public List<Message> GetAllNotSeenMessages(String senderId)
    {
        String Selection = SENDER_ID + "=?" + " AND " + STATUS + "!=?";
        String[] SelectionArgs = new String[]{senderId,"4"};
        return GetMessageList(Selection,SelectionArgs);
    }

    public static List<Message> Get(String uid)
    {
        return db.GetMsg(uid);
    }
    public List<Message> GetMsg(String uid)
    {
        String Selection = SENDER_ID +" =?" + " OR " + RECEIVER_ID + "=?";
        String[] SelectionArgs = new String[]{uid,uid};
        return GetMessageList(Selection,SelectionArgs);
    }
    public static String GetLastPushID(String user_id){
        return db.GetPushID(user_id);
    }
    public String GetPushID(String user_id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(MESSAGES,null,PUSH_ID+" not null AND "+RECEIVER_ID+" = ?",new String[]{user_id},null,null,PUSH_ID+" DESC", "1");
        if(cursor.moveToFirst())
        {
            Message msg = ReadMessage(cursor);
            cursor.close();
            db.close();
            return msg.getPushId();
        }
        cursor.close();
        db.close();
        return "";
    }

    private Message ReadMessage(Cursor cursor)
    {
        Message msg = new Message();
        msg.setId(cursor.getString(cursor.getColumnIndex(ID)));
        msg.setSenderID(cursor.getString(cursor.getColumnIndex(SENDER_ID)));
        msg.setReceiverID(cursor.getString(cursor.getColumnIndex(RECEIVER_ID)));
        msg.setPushId(cursor.getString(cursor.getColumnIndex(PUSH_ID)));
        msg.setText(cursor.getString(cursor.getColumnIndex(TEXT)));
        msg.setStatus(cursor.getInt(cursor.getColumnIndex(STATUS)));
        msg.setTimeInt(cursor.getLong(cursor.getColumnIndex(TIME)));
        System.out.println(msg.getTimeInt());
        msg.setReplyATID(cursor.getString(cursor.getColumnIndex(REPLY_AT_ID)));
        msg.setDateTime();
        return msg;
    }
    private ContentValues WriteMessage(Message msg)
    {
        msg.setReplyATID("Not Null");
        ContentValues cv = new ContentValues();
        cv.put(ID,msg.getId());
        cv.put(SENDER_ID,msg.getSenderID());
        cv.put(RECEIVER_ID,msg.getReceiverID());
        cv.put(PUSH_ID,msg.getPushId());
        cv.put(TEXT,msg.getText());
        cv.put(STATUS,msg.getStatus());
        cv.put(TIME,msg.getTimeInt());
        cv.put(REPLY_AT_ID,msg.getReplyATID());
        return cv;
    }
    private boolean DoesMessageExist(String id)
    {
        return db.DoesExist(id);
    }
    private boolean DoesExist(String id)
    {
        SQLiteDatabase db = getReadableDatabase();
        String Selection = ID +" =?";
        String[] SelectionArgs = new String[]{id};
        Cursor cursor = db.query(MESSAGES,null,Selection,SelectionArgs,null,null,null);
        if(cursor.moveToFirst())
        {
            cursor.close();
            db.close();
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }
    private Message GetMessage(String id)
    {
        SQLiteDatabase db = getReadableDatabase();
        String Selection = ID +" =?";
        String[] SelectionArgs = new String[]{id};
        Cursor cursor = db.query(MESSAGES,null,Selection,SelectionArgs,null,null,null);
        if(cursor.moveToFirst())
        {
            Message msg = ReadMessage(cursor);
            cursor.close();
            db.close();
            return msg;
        }
        cursor.close();
        db.close();
        return null;
    }
    public Message GetLastMessage(String uid){
        List<Message> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = SENDER_ID+" = ? OR "+RECEIVER_ID+" = ?";
        String[] selectionArgs = {uid, uid};
        Cursor cursor = db.query(MESSAGES,null,selection,selectionArgs,null,null,TIME+" DESC", "1");
        Message msg = null;
        if(cursor.moveToFirst())
        {
            msg = ReadMessage(cursor);
        }
        cursor.close();
        db.close();
        return msg;
    }
    public static Message GetRecentMessage(String uid){
        return db.GetLastMessage(uid);
    }
    private List<Message> GetMessageList(String selection, String[] selectionArgs)
    {
        List<Message> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(MESSAGES,null,selection,selectionArgs,null,null,TIME+" ASC");
        if(cursor.moveToFirst())
        {
            do {
                Message msg = ReadMessage(cursor);
                list.add(msg);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
    public void resetdb(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(DROP_MESSAGE_TABLE);
        db.execSQL(CREATE_MESSAGE_TABLE);
        db.close();
    }
    public static void reset_db(){
        db.resetdb();
    }
}