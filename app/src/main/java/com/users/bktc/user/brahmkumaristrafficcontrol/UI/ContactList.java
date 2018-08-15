package com.users.bktc.user.brahmkumaristrafficcontrol.UI;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.users.bktc.user.brahmkumaristrafficcontrol.R;
import com.users.bktc.user.brahmkumaristrafficcontrol.UI.adapter.ChatUsersListModel;
import com.users.bktc.user.brahmkumaristrafficcontrol.UI.adapter.ContactListAdapter;
import com.users.bktc.user.brahmkumaristrafficcontrol.Utilities.Constants;
import com.users.bktc.user.brahmkumaristrafficcontrol.Utilities.HeaderHandler;
import com.users.bktc.user.brahmkumaristrafficcontrol.Utilities.Utils;
import com.users.bktc.user.brahmkumaristrafficcontrol.databinding.ActivityContactListBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContactList extends AppCompatActivity implements HeaderHandler {
    ActivityContactListBinding contactListBinding;
    View view;
    List<String> fechedPhoneNumbers;
    ContactListAdapter contactListAdapter;
    BroadcastReceiver bd;
    String TAG="logs";

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    List<ChatUsersListModel> chatUsersListModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactListBinding = DataBindingUtil.setContentView(this, R.layout.activity_contact_list);
        contactListBinding.setHeader(this);
        contactListBinding.header.tvScreenName.setText("Select Contacts");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        contactListBinding.chatlist.setLayoutManager(layoutManager);

        chatUsersListModel = new ArrayList<>();
        fechedPhoneNumbers= new ArrayList<>();
        contactListAdapter = new ContactListAdapter(chatUsersListModel, fechedPhoneNumbers,getApplicationContext());

        bd=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String error=intent.getStringExtra(Constants.ERROR);

                if(error!=null){



                    new AlertDialog.Builder(ContactList.this).setCancelable(false).setMessage("Your phone number is no longer registered on this phone. This is likely because you registered your phone number with app on different phone").setPositiveButton("Verify", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent in= new Intent(ContactList.this, MainActivity.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(in);
                        }
                    }).show();

                }
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
          getContactList();

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
               getContactList();
            } else {
               Utils.showSnackBar("Until you grant the permission, we canot display the names", contactListBinding.parentView);
            }
        }
    }

    @Override
    public void backClick(View view) {
        startActivity(new Intent(this, MainChatActivity.class));
    }

    public class BackgroundGetContact extends AsyncTask<String, String ,String >{


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
          }

    @Override
    protected String doInBackground(String... strings) {
        getContactList();
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);


    }

}
    private void getContactList() {
        Utils.displayProgressDialog("Getting Contacts",ContactList.this,"");

        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(
                ContactsContract.RawContacts.CONTENT_URI,
                null,
                ContactsContract.RawContacts.ACCOUNT_TYPE + "= '"+Constants.SyncAccountType+"'",
                null,
                null);

        if (cur != null) {
            if (cur.getCount() > 0) {
                if (cur.moveToFirst()) {
                    do {
                        //whatsappContactId for get Number,Name,Id ect... from  ContactsContract.CommonDataKinds.Phone
                        String whatsappContactId =cur.getString(cur.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID));

                        if (whatsappContactId != null) {
                            //Get Data from ContactsContract.CommonDataKinds.Phone of Specific CONTACT_ID
                            Cursor whatsAppContactCursor = cr.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                                            ContactsContract.CommonDataKinds.Phone.NUMBER,
                                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{whatsappContactId}, null);

                            if (whatsAppContactCursor != null) {
                                whatsAppContactCursor.moveToFirst();
                                String id = whatsAppContactCursor.getString(whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                                String name = whatsAppContactCursor.getString(whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                String number = whatsAppContactCursor.getString(whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                whatsAppContactCursor.close();

                                //Add Number to ArrayList
                                chatUsersListModel.add(new ChatUsersListModel(name,number,"MONINJ","0"));

fechedPhoneNumbers.add(number);
                            }
                        }
                    } while (cur.moveToNext());
                    cur.close();
                }
            }
        }

                    Collections.sort(chatUsersListModel,new SortModelList());

        contactListAdapter = new ContactListAdapter(chatUsersListModel, fechedPhoneNumbers,ContactList.this);
        contactListBinding.chatlist.setAdapter(contactListAdapter);

        contactListAdapter.clickItem(new ContactListAdapter.OnItemClick() {
            @Override
            public void click(int position, View view) {
                Intent intent= new Intent(ContactList.this, ChatActvity.class);
                intent.putExtra(Constants.username,chatUsersListModel.get(position).getLastMessage()+Constants.domainName);
                startActivity(intent);
            }
        });
        contactListBinding.header.contactCount.setText(chatUsersListModel.size()+" contacts");
        contactListBinding.header.contactCount.setVisibility(View.VISIBLE);
        Utils.hideProgressDialog();

    }
    class SortModelList implements Comparator<ChatUsersListModel> {

        @Override
        public int compare(ChatUsersListModel chatUsersListModel, ChatUsersListModel t1) {
           return  chatUsersListModel.getPhoneNumber().compareTo(t1.getPhoneNumber());

        }
    }
    public void GetContacts() {
    }
}
