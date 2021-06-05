package com.mcgroupproject.whatsappclone.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.mcgroupproject.whatsappclone.model.Message;

import java.util.ArrayList;
import java.util.List;
//me pankhy k aagy betha hun, awaz ni ae gi
//ye errors usi ki wja se hain
//class bh static krni hai?
public class MessageDB extends SQLiteOpenHelper {
    private static MessageDB db;
    public static final String MESSAGES = "messages";
    public static final String ID = "id";
    public static final String SENDER_ID = "sender_id";
    public static final String RECEIVER_ID = "receiver_id";
    public static final String TEXT = "text";
    public static final String STATUS = "status";
    public static final String TIME = "time";
    public static final String DATE = "date";
    public static final String REPLY_AT_ID = "reply_at_id";


    public MessageDB(@Nullable Context context) {
        super(context, "whatsapp.db", null, 1);
    }

    public static void Init(Context context)
    {
        db = new MessageDB(context);
    }
    //non static
    //ni samjha

    //this is called the first time database is accessed. There should be code in here to create a new database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + MESSAGES + " (" +
                ID + " TEXT PRIMARY KEY," +
                SENDER_ID + " TEXT NOT NULL," +
                RECEIVER_ID + " TEXT NOT NULL," +
                TEXT + " TEXT NOT NULL," +
                STATUS + " INTEGER NOT NULL," +
                TIME + " TEXT NOT NULL," +
                DATE + " TEXT NOT NULL," +
                REPLY_AT_ID + " TEXT)";
        //you love adil more than atif :(
        db.execSQL(createTableStatement);
    }

    //this is called if the database version number changes. It prevents previous user apps from breaking when you change the database design.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //to iska kya krna?
    //
    public static boolean Add(Message msg)
    {
        return db.AddMessage(msg);
    }
    //sahi ho gya
    //you are genius
    //i love you
    // :(
    //wo to me samjha sir ne tng krny k liey dia tha
    // xD
    //chalo sahi hai ho jae ga
    // baki
    //dekh li hai na kaam sahi hai?
    //wo me kr lunga
    //cos dekho
    //dekho
    ///dekho
    //pta kia scene
    //message me sender ki id hai
    //receiver ki bh hai
    //ab ye id to user ki hi hai na?
    //to user ki db me get kr lunga
    //or wo function yhan call kr lunga
    //chalo sahi hai
    //me ab krta
    //yar wo date descending kr k first entry le lunga
    //string
    //yar ek or chez bh btao
    public boolean AddMessage(Message msg)
    {
        //check if msg already exists, by id
        //ap ne bh kia hwa tha
        //i don't think k zaroorat hai
        //but still
        if(DoesMessageExist(msg.getId()))
            return false;

        //data inserted
        SQLiteDatabase db = this.getWritableDatabase();
        msg.setReplyATID("Not Null");
        ContentValues cv = WriteMessage(msg);
        long insert = db.insert(MESSAGES, null, cv);
        db.close();
        //yes
        //return bool
        //mtlb ni hwa insert
        //kahan?
        if(insert == -1)
            return false;
        return true;
        //pta kia scene
        //db ek dfa bn chuki hai
        //constraint is already set to no null
        //
    }
    public boolean Remove(Message message)
    {
        //ye phr me check hr jga hi lga dia tha
        //kesy kr skta hun?
        //btao

        if(!DoesMessageExist(message.getId()))
            return false;

        //simple delete
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = ID + "=?";
        String whereArgs[] = {message.getId()};
        db.delete(MESSAGES, whereClause, whereArgs);
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
        SQLiteDatabase db = getReadableDatabase();
        ContentValues contentValues = WriteMessage(GetMessage(id));
        String whereClause = STATUS +" >= ?";

        String whereArgs[] = {status};
        db.update(MESSAGES,contentValues,whereClause,whereArgs);
        db.close();
        //db close ni ki thi, uska to error ni?
        return true;
    }
    public List<Message> GetAllUnsendMessages()
    {
        String Selection = STATUS + "=?";
        String[] SelectionArgs = new String[]{"0"};
        //me ne getMessage list ka alg function bna lia hai
        //just where clause or arguments pass kro
        //wo us hisa
        return GetMessageList(Selection,SelectionArgs);
    }
    public List<Message> GetAllNotSeenMessages(String senderId)
    {
        //TODO: load all messages whose sender is uid and status != 4
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
    private Message ReadMessage(Cursor cursor)
    {
        //cursor me null return hwa hai, or me still read krny ki koshish kr rha hun,
        //han
        Message msg = new Message();
        msg.setId(cursor.getString(cursor.getColumnIndex(ID)));
        msg.setSenderID(cursor.getString(cursor.getColumnIndex(SENDER_ID)));
        msg.setReceiverID(cursor.getString(cursor.getColumnIndex(RECEIVER_ID)));
        msg.setText(cursor.getString(cursor.getColumnIndex(TEXT)));
        msg.setStatus(cursor.getInt(cursor.getColumnIndex(STATUS)));
        msg.setTime(cursor.getString(cursor.getColumnIndex(TIME)));
        msg.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
        msg.setReplyATID(cursor.getString(cursor.getColumnIndex(REPLY_AT_ID)));
        return msg;
    }
    private ContentValues WriteMessage(Message msg)
    {
        ContentValues cv = new ContentValues();
        cv.put(ID,msg.getId());
        cv.put(SENDER_ID,msg.getSenderID());
        cv.put(RECEIVER_ID,msg.getReceiverID());
        cv.put(TEXT,msg.getText());
        cv.put(STATUS,msg.getStatus());
        cv.put(TIME,msg.getTime());
        cv.put(DATE,msg.getDate());
        cv.put(REPLY_AT_ID,msg.getReplyATID());
        return cv;
    }
    //apka
    //is ko set kr dia tha
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
    private List<Message> GetMessageList(String selection, String[] selectionArgs)
    {
        List<Message> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(MESSAGES,null,selection,selectionArgs,null,null,null);
        if(cursor.moveToFirst())
        //nops, working
        {
            do {
                Message msg = ReadMessage(cursor);
                //yhan message me user add hoga
                list.add(msg);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
    //han
    //constructor me
    //bs apky waly ho gae set
    //chala k dekhta
}