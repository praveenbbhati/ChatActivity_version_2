package com.users.bktc.user.brahmkumaristrafficcontrol.network.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelperChattingToUsers extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="ChattingToUsers";
    private static String TABLE_CHATTING_TO_USERS="Users";
    private static String KEY_ID="id";
    private static String KEY_PHONE="phone";
    private static String KEY_LAST_TIMESTAMP="lastTimeStamp";
    private static String KEY_LAST_MESSAGE="lastMessage";
    private static String KEY_NEWMESSAGE_COUNT="newMessageCount";
    private static Context cont;

    public DatabaseHelperChattingToUsers(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        cont=context;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CONTACT_TABLE="CREATE TABLE " + TABLE_CHATTING_TO_USERS+"("+KEY_ID+" INTEGER PRIMARY KEY ,"+KEY_PHONE+" TEXT ,"
                +KEY_LAST_MESSAGE+" TEXT,"+KEY_LAST_TIMESTAMP+" TEXT,"+KEY_NEWMESSAGE_COUNT+" TEXT)";
        sqLiteDatabase.execSQL(CREATE_CONTACT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_CHATTING_TO_USERS);
        onCreate(sqLiteDatabase);
    }
    public void addContact(ChattingToUsers contact){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(KEY_PHONE,contact.getPhone());
        contentValues.put(KEY_LAST_MESSAGE,contact.getLastMessage());
        contentValues.put(KEY_LAST_TIMESTAMP,contact.getLastTimeStamp());
        contentValues.put(KEY_NEWMESSAGE_COUNT,contact.getNewMessageCout());
        db.insert(TABLE_CHATTING_TO_USERS,null,contentValues);
        db.close();

    }
    ChattingToUsers getcontect (int id){
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor= db.query(TABLE_CHATTING_TO_USERS,new String[]{KEY_ID,KEY_PHONE,KEY_LAST_MESSAGE,KEY_LAST_TIMESTAMP,KEY_NEWMESSAGE_COUNT},KEY_ID+"=?",
                new String[]{String.valueOf(id)},null,null,null,null);
        if(cursor !=null){
            cursor.moveToFirst();
        }

        ChattingToUsers contact= new ChattingToUsers(cursor.getString(0), cursor.getString(1), cursor.getString(3),cursor.getString(2),cursor.getString(4));
        return contact;
    }
    public List<ChattingToUsers> getAllContacts(){
        List<ChattingToUsers> contactList = new ArrayList<>();
        String SELECT_QUERY="SELECT * FROM "+TABLE_CHATTING_TO_USERS+" ORDER BY " + KEY_LAST_TIMESTAMP +" DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.rawQuery(SELECT_QUERY,null);
        if(cursor.moveToFirst()){
            do{
                ChattingToUsers contact=new ChattingToUsers();

                contact.setId(cursor.getString(0));

                contact.setPhone(cursor.getString(1));
                contact.setLastMessage(cursor.getString(2));
                contact.setLastTimeStamp(cursor.getString(3));
                contact.setNewMessageCout(cursor.getString(4));
                contactList.add(contact);
            }
            while (cursor.moveToNext());
        }

        db.close();
        return contactList;

    }



    public int updateContact(ChattingToUsers contact){
        SQLiteDatabase db= this.getReadableDatabase();
        ContentValues values= new ContentValues();
        values.put(KEY_PHONE,contact.getPhone());
        values.put(KEY_ID,contact.getId());
        values.put(KEY_LAST_MESSAGE,contact.getLastMessage());
        values.put(KEY_LAST_TIMESTAMP,contact.getLastTimeStamp());
        values.put(KEY_NEWMESSAGE_COUNT,contact.getNewMessageCout());
        return db.update(TABLE_CHATTING_TO_USERS,values,KEY_ID+"=?",new String[]{String.valueOf(contact.getId())});
    }

    public void deleteContacts(ChattingToUsers contact){
        SQLiteDatabase db=this.getReadableDatabase();
        db.delete(TABLE_CHATTING_TO_USERS,KEY_ID+"=?",new String[]{String.valueOf(contact.getId())});
        db.close();
    }

    public int getContactCount(){
        String countQuery="SELECT * FROM "+TABLE_CHATTING_TO_USERS;
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor=db.rawQuery(countQuery,null);
        cursor.close();
        return cursor.getCount();
    }
    public String getUserExists(String USERS_PHONE){
        List<ChattingToUsers> contactList = new ArrayList<>();
        String SELECT_QUERY="SELECT * FROM "+TABLE_CHATTING_TO_USERS+" WHERE " + KEY_PHONE+" = '"+USERS_PHONE+"'" ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.rawQuery(SELECT_QUERY,null);
        if(cursor.moveToFirst()){
            do{
                ChattingToUsers contact=new ChattingToUsers();

                contact.setId(cursor.getString(0));

                contact.setPhone(cursor.getString(3));
                contact.setLastMessage(cursor.getString(2));
                contact.setLastTimeStamp(cursor.getString(1));
                contact.setNewMessageCout(cursor.getString(4));
                contactList.add(contact);
            }
            while (cursor.moveToNext());
        }

        db.close();
        if(contactList.size()>0){
            return contactList.get(0).getId() + "/"+contactList.get(0).getNewMessageCout();
        }else{
            return  null;
        }


    }
    public int getNewMessageCount(){
        String countQuery="SELECT * FROM "+TABLE_CHATTING_TO_USERS +" WHERE " + KEY_NEWMESSAGE_COUNT +" != '0'";
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor=db.rawQuery(countQuery,null);
        int count=0;
        if(cursor.moveToFirst()){
            do{
                count++;
            }
            while (cursor.moveToNext());
        }
        return count;
    }
    public void DeleteDatabase(){
        cont.deleteDatabase(DATABASE_NAME);
    }
}
