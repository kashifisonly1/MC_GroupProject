package com.mcgroupproject.whatsappclone.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.mcgroupproject.whatsappclone.model.Message;
import com.mcgroupproject.whatsappclone.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDB extends SQLiteOpenHelper {
    public static UserDB db;
    public static final int VERSION = 16;
    public static final String USERS = "users";
    public static final String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + USERS;
    public static final String ID = "id";
    public static final String USERNAME = "username";
    public static final String PHONE = "phone";
    public static final String URL_PROFILE = "url_profile";
    public static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + USERS + " ( " +
            ID + " TEXT NOT NULL, " +
            USERNAME + " TEXT NOT NULL," +
            PHONE + " TEXT NOT NULL," +
            URL_PROFILE + " TEXT)";

    private static final String MESSAGES = "messages";
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

    public UserDB(@Nullable Context context) {
        super(context, "whatsapp.db", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_MESSAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(CREATE_USER_TABLE);
    }
    public static void Init(Context context)
    {
        db = new UserDB(context);
    }
    public boolean AddUser(User user)
    {
        if(DoesExists(user.getUserID()))
            return false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = WriteUser(user);
        long insert = db.insert(USERS, null, cv);
        db.close();
        if(insert == -1)
            return false;
        return true;
    }
    public static boolean Add(User user)
    {
        return db.AddUser(user);
    }
    public boolean RemoveUser(User user)
    {
        if(!DoesExists(user.getUserID()))
            return false;
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = ID + "=?";
        String whereArgs[] = {user.getUserID()};
        int delete = db.delete(USERS, whereClause, whereArgs);
        db.close();
        if(delete < 1)
            return false;
        return true;
    }
    public static boolean Remove(User user)
    {
        return db.RemoveUser(user);
    }

    public boolean UpdateUser(User user)
    {
        if(!DoesExists(user.getUserID()))
            return false;
        ContentValues contentValues = WriteUser(user);
        SQLiteDatabase db = getWritableDatabase();
        String whereClause =ID + "=?";

        String whereArgs[] = {user.getUserID()};
        int update = db.update(USERS, contentValues, whereClause, whereArgs);
        db.close();

        if(update < 1)
            return false;
        return true;
    }
    public static boolean Update(User user)
    {
        return db.UpdateUser(user);
    }
    public List<User> GetUser()
    {
        return GetUserList(null,null);
    }
    public static List<User> Get()
    {
        return db.GetUser();
    }
    public static Message GetLastMessage(String uid)
    {
        return new Message();
    }
    public static boolean DoesUserExists(String id)
    {
        return db.DoesExists(id);
    }
    public boolean DoesExists(String id)
    {
        SQLiteDatabase db = getReadableDatabase();
        String Selection = ID +" =?";
        String[] SelectionArgs = new String[]{id};
        Cursor cursor = db.query(USERS,null,Selection,SelectionArgs,null,null,null);
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
    private User ReadUser(Cursor cursor)
    {
        User user = new User();
        user.setUserID(cursor.getString(cursor.getColumnIndex(ID)));
        user.setUsername(cursor.getString(cursor.getColumnIndex(USERNAME)));
        Message msg = MessageDB.GetRecentMessage(user.getUserID());
        if(msg!=null) {
            user.setLastMessage(msg.getText());
            user.setDate(Long.toString(msg.getTimeInt()));
        }
        user.setPhone(cursor.getString(cursor.getColumnIndex(PHONE)));
        user.setUrlProfile(cursor.getString(cursor.getColumnIndex(URL_PROFILE)));
        return user;
    }
    private ContentValues WriteUser(User user)
    {
        ContentValues cv = new ContentValues();
        cv.put(ID,user.getUserID());
        cv.put(USERNAME,user.getUsername());
        cv.put(PHONE,user.getPhone());
        cv.put(URL_PROFILE,user.getUrlProfile());
        return cv;
    }
    private User GetUser(String id)
    {
        SQLiteDatabase db = getReadableDatabase();
        String Selection = ID +" =?";
        String[] SelectionArgs = new String[]{id};
        Cursor cursor = db.query(USERS,null,Selection,SelectionArgs,null,null,null);
        if(cursor.moveToFirst())
        {
            User user = ReadUser(cursor);
            cursor.close();
            db.close();
            return user;
        }
        cursor.close();
        db.close();
        return null;
    }
    private List<User> GetUserList(String selection, String[] selectionArgs)
    {
        List<User> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(USERS,null,selection,selectionArgs,null,null,null);
        if(cursor.moveToFirst())
        {
            do {
                User user = ReadUser(cursor);
                list.add(user);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
    public void resetdb(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(CREATE_USER_TABLE);
        db.close();
    }
    public static void reset_db(){
        db.resetdb();
    }
}
