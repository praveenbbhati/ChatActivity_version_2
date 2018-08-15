package com.users.bktc.user.brahmkumaristrafficcontrol.network.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import de.measite.minidns.util.NameUtil;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABSE_VERSION=1;
    private static final String DATABASE_NAME="chats";
    private static String TABLE_CONTACTS="contacts";
    private static String KEY_ID="id";
    private static String KEY_NAME="name";
    private static String KEY_PHONE="phone";
    private static String KEY_MSG_SEND="msgsend";
    private static String KEY_TIMESTAMP="timeStamp";
    private static Context cont;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABSE_VERSION);
        cont=context;
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_CONTACT_TABLE="CREATE TABLE " + TABLE_CONTACTS+"("+KEY_ID+" INTEGER PRIMARY KEY ,"+KEY_NAME+" TEXT ,"
                +KEY_PHONE+" TEXT,"+KEY_MSG_SEND+" TEXT, "+KEY_TIMESTAMP+ " TEXT)";
        sqLiteDatabase.execSQL(CREATE_CONTACT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_CONTACTS);
        onCreate(sqLiteDatabase);
    }
    public void addContact(Contact contact){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(KEY_NAME,contact.getName());
        contentValues.put(KEY_PHONE,contact.getPhone());
        contentValues.put(KEY_MSG_SEND,contact.getMsgSend());
        contentValues.put(KEY_TIMESTAMP,contact.getTimeStamp());
        db.insert(TABLE_CONTACTS,null,contentValues);
        db.close();

    }
    Contact getcontect (int id){
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor= db.query(TABLE_CONTACTS,new String[]{KEY_ID,KEY_MSG_SEND,KEY_PHONE,KEY_NAME,KEY_TIMESTAMP},KEY_ID+"=?",
                new String[]{String.valueOf(id)},null,null,null,null);
        if(cursor !=null){
            cursor.moveToFirst();
        }

        Contact contact= new Contact(cursor.getString(0), cursor.getString(1), cursor.getString(3),cursor.getString(2),cursor.getString(4));
        return contact;
    }

    public List<Contact> getChats(String USERPHONE){
        List<Contact> contactList = new ArrayList<>();
        String SELECT_QUERY="SELECT * FROM "+TABLE_CONTACTS +" WHERE " + KEY_PHONE+" = '"+USERPHONE+"' ORDER BY " + KEY_ID;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.rawQuery(SELECT_QUERY,null);
        if(cursor.moveToFirst()){
            do{
                Contact contact=new Contact();

                contact.setId(cursor.getString(0));
                contact.setMsgSend(cursor.getString(3));
                contact.setName(cursor.getString(1));
                contact.setPhone(cursor.getString(2));
                contact.setTimeStamp(cursor.getString(4));
                contactList.add(contact);
            }
            while (cursor.moveToNext());
            }

            db.close();
            return contactList;

    }

    public int updateContact(Contact contact){
        SQLiteDatabase db= this.getReadableDatabase();
        ContentValues values= new ContentValues();
        values.put(KEY_NAME,contact.getName());
        values.put(KEY_ID,contact.getId());
        values.put(KEY_PHONE,contact.getPhone());
        values.put(KEY_MSG_SEND,contact.getMsgSend());
        values.put(KEY_TIMESTAMP,contact.getTimeStamp());
        return db.update(TABLE_CONTACTS,values,KEY_ID+"=?",new String[]{String.valueOf(contact.getId())});
    }
    public void deleteContacts(Contact contact){
        SQLiteDatabase db=this.getReadableDatabase();
        db.delete(TABLE_CONTACTS,KEY_ID+"=?",new String[]{String.valueOf(contact.getId())});
        db.close();
    }

    public int getContactCount(){
        String countQuery="SELECT * FROM "+TABLE_CONTACTS;
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor=db.rawQuery(countQuery,null);
        cursor.close();
        return cursor.getCount();
    }
    public void DeleteDatabase(){
        cont.deleteDatabase(DATABASE_NAME);
    }
}
