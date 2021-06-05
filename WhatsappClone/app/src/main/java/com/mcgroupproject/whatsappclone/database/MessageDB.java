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

public class MessageDB {
    public class MessageDB extends SQLiteOpenHelper {
        public static final List<Message> list = new ArrayList<>();
        public static void Add(Message i)
        {
            for(int a = 0; a<list.size(); a++)
            {
                if(list.get(a).getId().equals(i.getId()))
                    return;
            }
            list.add(i);
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

            //this is called the first time database is accessed. There should be code in here to create a new database
            @Override
            public void onCreate(SQLiteDatabase db) {
            String createTableStatement = "CREATE TABLE " + MESSAGES + " (" +
                    ID + " INTEGER PRIMARY KEY," +
                    SENDER_ID + " TEXT NOT NULL," +
                    RECEIVER_ID + " TEXT NOT NULL," +
                    TEXT + " TEXT NOT NULL," +
                    STATUS + " INTEGER NOT NULL," +
                    TIME + " TEXT NOT NULL," +
                    DATE + " TEXT NOT NULL," +
                    REPLY_AT_ID + " TEXT NOT NULL)";
            db.execSQL(createTableStatement);
        }
            public static void Remove(Message i)

            //this is called if the database version number changes. It prevents previous user apps from breaking when you change the database design.
            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

            public boolean Add(Message msg)
            {
                list.remove(i);
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
            public static void Update(String id, String status)
            public boolean Remove(Message message)
            {
                //TODO: greater ka check
                for(int a =0; a<list.size(); a++)
                {
                    if(list.get(a).getId().equals(id))
                    {
                        if((list.get(a).getStatus())>=Integer.parseInt(status))
                            return;
                        list.get(a).setStatus(Integer.parseInt(status));
                        return;
                    }
                }
                if(!DoesMessageExist(message.getId()))
                    return false;
                SQLiteDatabase db = this.getWritableDatabase();
                String whereClause = ID + "=?";
                String whereArgs[] = {message.getId()};
                db.delete("Items", whereClause, whereArgs);
                return true;
            }
            public boolean Update(String id, String status)
            {
                if(!DoesMessageExist(id))
                    return false;
                SQLiteDatabase db = getReadableDatabase();
                ContentValues contentValues = WriteMessage(GetMessage(id));
                String whereClause = STATUS +" >= ?";
                String whereArgs[] = {status};
                db.update(MESSAGES,contentValues,whereClause,whereArgs);
                return true;
            }

            
            public static List<Message> GetAllUnsendMessages()
            public List<Message> GetAllUnsendMessages()
            {
                return null;
                String Selection = STATUS + "=?";
                String[] SelectionArgs = new String[]{"0"};
                return GetMessageList(Selection,SelectionArgs);
            }
            public static List<Message> GetAllNotSeenMessages(String senderId)
            public List<Message> GetAllNotSeenMessages(String senderId)
            {
                //TODO: load all messages whose sender is uid and status != 4
                String Selection = SENDER_ID + "=?" + " AND " + STATUS + "!=?";
                String[] SelectionArgs = new String[]{senderId,"4"};
                return GetMessageList(Selection,SelectionArgs);
            }
            public List<Message> Get(String uid)
            {
                String Selection = SENDER_ID +" =?" + " OR " + RECEIVER_ID + "=?";
                String[] SelectionArgs = new String[]{uid,uid};
                return GetMessageList(Selection,SelectionArgs);
            }
            private Message ReadMessage(Cursor cursor)
            {
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
            private boolean DoesMessageExist(String id)
            {
                SQLiteDatabase db = getReadableDatabase();
                String Selection = ID +" =?";
                String[] SelectionArgs = new String[]{id};
                Cursor cursor = db.query(MESSAGES,null,Selection,SelectionArgs,null,null,null);
                if(cursor != null)
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
                if(cursor != null)
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
            public static List<Message> Get(String uid)
            private List<Message> GetMessageList(String selection, String[] selectionArgs)
            {
                List<Message> l = new ArrayList<>();
                for(int a =0; a<list.size(); a++)
                    List<Message> list = new ArrayList<>();
                SQLiteDatabase db = this.getReadableDatabase();
                Cursor cursor = db.query(MESSAGES,null,selection,selectionArgs,null,null,null);
                if(cursor.moveToFirst())
                {
                    Message m = list.get(a);
                    if(m.getSenderID().equals(uid) || m.getReceiverID().equals(uid))
                    {
                        l.add(m);
                    }
                    do {
                        Message msg = ReadMessage(cursor);
                        list.add(msg);
                    }while (cursor.moveToNext());
                }
                return l;
                cursor.close();
                db.close();
                return list;
            }
        }
