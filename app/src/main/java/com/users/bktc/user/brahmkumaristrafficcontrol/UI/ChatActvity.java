package com.users.bktc.user.brahmkumaristrafficcontrol.UI;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.users.bktc.user.brahmkumaristrafficcontrol.R;
import com.users.bktc.user.brahmkumaristrafficcontrol.UI.adapter.ChatModel;
import com.users.bktc.user.brahmkumaristrafficcontrol.UI.adapter.ChatUsersListAdapter;
import com.users.bktc.user.brahmkumaristrafficcontrol.UI.adapter.ChatViewAdapter;
import com.users.bktc.user.brahmkumaristrafficcontrol.Utilities.Constants;
import com.users.bktc.user.brahmkumaristrafficcontrol.Utilities.HeaderHandler;
import com.users.bktc.user.brahmkumaristrafficcontrol.Utilities.KeyBoardUtils;
import com.users.bktc.user.brahmkumaristrafficcontrol.databinding.LayoutChatActvityBinding;
import com.users.bktc.user.brahmkumaristrafficcontrol.network.database.ChattingToUsers;
import com.users.bktc.user.brahmkumaristrafficcontrol.network.database.Contact;
import com.users.bktc.user.brahmkumaristrafficcontrol.network.database.DatabaseHelper;
import com.users.bktc.user.brahmkumaristrafficcontrol.network.database.DatabaseHelperChattingToUsers;
import com.users.bktc.user.brahmkumaristrafficcontrol.services.ReceivceMessageService;
import com.users.bktc.user.brahmkumaristrafficcontrol.services.SendMessage;
import com.users.bktc.user.brahmkumaristrafficcontrol.sharedpreference.AppSharedPreferences;
import com.users.bktc.user.brahmkumaristrafficcontrol.sharedpreference.PrefConstants;


import java.util.ArrayList;
import java.util.List;

public class ChatActvity extends AppCompatActivity implements HeaderHandler {
    public static boolean isInForeground ;

    private LayoutChatActvityBinding layoutChatActvityBinding;
    public  List<ChatModel> chatmsg = new ArrayList<>();
    public  ChatViewAdapter adap;
    public   ChatModel msg;
    public   LinearLayoutManager layoutmanager;
    DatabaseHelper db;
    private static String userId,userPhone;
    DatabaseHelperChattingToUsers databaseHelperChattingToUsers;
    BroadcastReceiver bd;
    IntentFilter intentFilter;
    String sendTo="";
    public static RecyclerView chatlist;
    EditText edt;
boolean st=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutChatActvityBinding = DataBindingUtil.setContentView(this, R.layout.layout_chat_actvity);
        layoutmanager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        layoutChatActvityBinding.setHeader(this);
        layoutChatActvityBinding.setChat(this);
        chatlist= (RecyclerView) findViewById(R.id.lst1);
        layoutChatActvityBinding.lst1.setLayoutManager(layoutmanager);
        edt=(EditText)findViewById(R.id.editText);
        isInForeground=true;
        db= new DatabaseHelper(this);
        try {
            String dataString = getIntent().getDataString();

            Uri uri = Uri.parse(dataString);
            String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

            Cursor cursor = getContentResolver().query(uri, projection,
                    null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    sendTo = cursor.getString(
                            cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    sendTo=sendTo+Constants.domainName;
                    Log.d("number",sendTo);

                    cursor.close();
                }
                cursor.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            sendTo = getIntent().getExtras().getString(Constants.username);
            Log.d("number",sendTo);
        }catch (Exception e){
            e.printStackTrace();
        }
        String phoneName=ChatUsersListAdapter.getContactName(sendTo,this);
        if(phoneName!=null){
            layoutChatActvityBinding.header.tvScreenName.setText(phoneName);
        }else {
            layoutChatActvityBinding.header.tvScreenName.setText(sendTo);
        }
        List<Contact> contactList = db.getChats(sendTo);

        KeyBoardUtils.addKeyboardToggleListener(this, new KeyBoardUtils.SoftKeyboardToggleListener() {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible) {
                layoutmanager.scrollToPosition(chatmsg.size()-1);

            }
        });
        for(Contact c: contactList ){
            if(c.getMsgSend().equals("false")){
                msg= new ChatModel(c.getName(),false,c.getTimeStamp());
                chatmsg.add(msg);
            }else{
                msg= new ChatModel(c.getName(),true,c.getTimeStamp());
                chatmsg.add(msg);
            }




        }
        databaseHelperChattingToUsers = new DatabaseHelperChattingToUsers(this);
        if(contactList.size()>0) {
            userId = contactList.get(0).getId();
            userPhone = contactList.get(0).getPhone();

            databaseHelperChattingToUsers.updateContact(new ChattingToUsers(userId, userPhone, contactList.get(contactList.size() - 1).getTimeStamp(), contactList.get(contactList.size() - 1).getName(), "0"));
        }
        intentFilter=new IntentFilter();
        intentFilter.addAction("YOUR_INTENT_FILTER");

       adap = new ChatViewAdapter(chatmsg, getApplicationContext());
        layoutChatActvityBinding.lst1.setAdapter(adap);
        layoutmanager.scrollToPosition(chatmsg.size()-1);

        bd=new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {

                String text = intent.getStringExtra("msg");
                String time = intent.getStringExtra("time");
                String num= intent.getStringExtra("number");

                String error=intent.getStringExtra(Constants.ERROR);

                if(error!=null){



                    new AlertDialog.Builder(ChatActvity.this).setCancelable(false).setMessage("Your phone number is no longer registered on this phone. This is likely because you registered your phone number with app on different phone").setPositiveButton("Verify", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent in= new Intent(ChatActvity.this, MainActivity.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(in);
                        }
                    }).show();

                }
                final NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

                if(text!=null && num!=null && num.equals(sendTo)) {
                    msg = new ChatModel(text, false, time);
                    chatmsg.add(msg);
                    adap = new ChatViewAdapter(chatmsg, getApplicationContext());
                    layoutChatActvityBinding.lst1.setAdapter(adap);
                    layoutmanager.scrollToPosition(chatmsg.size() - 1);
                }else if(text!=null && num!=null){
                    String CHANNEL_ID = "my_channel_01";
                    inboxStyle.setBigContentTitle("Enter Content Text");
                    inboxStyle.addLine(text);
                    Intent targetIntent = new Intent(getApplicationContext(), ChatActvity.class);
                    targetIntent.putExtra(Constants.username, num);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                    Notification builder2 = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                            .setSmallIcon(android.R.drawable.ic_dialog_email)
                            .setContentTitle("message")
                            .setContentText(text)
                            .setContentIntent(pendingIntent)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                            .build();


                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(1, builder2);
                }


            }
        };
        registerReceiver(bd,intentFilter);
//            ChatManager chatManager = ChatManager.getInstanceFor(mConnection);
//            chatManager.addIncomingListener(new IncomingChatMessageListener() {
//                @Override
//                public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
//
//                    ChatModel msg = new ChatModel(message.getBody(), false);
//                    chatmsg.add(msg);
//                    db.addContact(new Contact(message.getBody(),"false",from.toString()));
//
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            ChatAdapter adap = new ChatAdapter(chatmsg, getApplicationContext());
//                            layoutChatActvityBinding.lst1.setAdapter(adap);
//                        }
//                    });
//
//                }
//
//            });



        layoutChatActvityBinding.fab6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (layoutChatActvityBinding.editText.getText().toString().length() > 0) {

                    SendMessage.sendMessage(layoutChatActvityBinding.editText.getText().toString(), sendTo);
                    String text = layoutChatActvityBinding.editText.getText().toString();

                    String timeStamp= String.valueOf(System.currentTimeMillis());
                    msg = new ChatModel(text, true,timeStamp);
                    chatmsg.add(msg);
                    db.addContact(new Contact(text,"true", sendTo,timeStamp));
                    String getuserId=databaseHelperChattingToUsers.getUserExists(sendTo);
                    if(getuserId!=null) {
                        databaseHelperChattingToUsers.updateContact(new ChattingToUsers(userId, sendTo, timeStamp, text, "0"));
                    }else{
                        databaseHelperChattingToUsers.addContact(new ChattingToUsers(sendTo,timeStamp,text,"0"));
                    }
                    layoutChatActvityBinding.editText.setText("");
                   adap = new ChatViewAdapter(chatmsg, getApplicationContext());
                    layoutChatActvityBinding.lst1.setAdapter(adap);
                    layoutmanager.scrollToPosition(chatmsg.size()-1);
                   
                }


            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent inter=new Intent(ChatActvity.this,MainChatActivity.class);
        inter.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(inter);
    }

    @Override
    protected void onResume() {
        isInForeground=true;
        registerReceiver(bd,intentFilter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        isInForeground=false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        isInForeground=false;
        unregisterReceiver(bd);
        super.onDestroy();
    }

    @Override
    public void backClick(View view) {
        startActivity(new Intent(ChatActvity.this,MainChatActivity.class));
    }
}
