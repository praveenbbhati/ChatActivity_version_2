package com.users.bktc.user.brahmkumaristrafficcontrol.services;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.users.bktc.user.brahmkumaristrafficcontrol.R;
import com.users.bktc.user.brahmkumaristrafficcontrol.UI.ChatActvity;
import com.users.bktc.user.brahmkumaristrafficcontrol.UI.MainActivity;
import com.users.bktc.user.brahmkumaristrafficcontrol.Utilities.Constants;
import com.users.bktc.user.brahmkumaristrafficcontrol.listener.RequestListener;
import com.users.bktc.user.brahmkumaristrafficcontrol.network.database.ChattingToUsers;
import com.users.bktc.user.brahmkumaristrafficcontrol.network.database.Contact;
import com.users.bktc.user.brahmkumaristrafficcontrol.network.database.DatabaseHelper;
import com.users.bktc.user.brahmkumaristrafficcontrol.network.database.DatabaseHelperChattingToUsers;
import com.users.bktc.user.brahmkumaristrafficcontrol.sharedpreference.AppSharedPreferences;
import com.users.bktc.user.brahmkumaristrafficcontrol.sharedpreference.PrefConstants;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.ping.PingFailedListener;
import org.jivesoftware.smackx.ping.PingManager;
import org.jivesoftware.smackx.ping.packet.Ping;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.xdata.Form;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import static com.users.bktc.user.brahmkumaristrafficcontrol.UI.ChatActvity.isInForeground;

public class ReceivceMessageService extends IntentService {
    public static AbstractXMPPConnection mConnection;
    static boolean isNetworkConnected= false;
    private DatabaseHelper db;
    private static boolean called=false;
    String TAG="roster";
    AlertDialog.Builder builder;
    Roster roster;
    private DatabaseHelperChattingToUsers databaseHelperChattingToUsers;
    public ReceivceMessageService() {
        super(null);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Bundle extras = intent.getExtras();
        isNetworkConnected = extras.getBoolean("isNetworkConnected");
        if(isNetworkConnected){
            setConnection();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


            isNetworkConnected = true;
        setConnection();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if(mConnection.isConnected() && mConnection.isAuthenticated()) {
//
//            Presence presence = new Presence(org.jivesoftware.smack.packet.Presence.Type.unavailable);
//
//            try {
//                mConnection.disconnect(presence);
//                Log.d("logs", "disconnected");
//            } catch (SmackException.NotConnectedException e) {
//                Log.d("logs", e.toString());
//            }
//
//        }else{
//            Log.d("else","notconnected");
//        }
    }


    public  void CheckConnection() {


                while(true) {



                    try {

                        UserSearchManager search = new UserSearchManager(mConnection);
                        Form searchForm = null;
                        searchForm = search
                                .getSearchForm(JidCreate.domainBareFrom("search.iserv.tech"));
                    } catch (SmackException.NoResponseException e) {
                        e.printStackTrace();
                        setConnection();
                    } catch (XMPPException.XMPPErrorException e) {
                        e.printStackTrace();
                        setConnection();
                    } catch (SmackException.NotConnectedException e) {
                        e.printStackTrace();
                        setConnection();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        setConnection();
                    } catch (XmppStringprepException e) {
                        e.printStackTrace();
                        setConnection();
                    }

                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }


    }
    public void setConnection() {
        //notification setting
        db = new DatabaseHelper(this);

        databaseHelperChattingToUsers = new DatabaseHelperChattingToUsers(this);
        builder = new AlertDialog.Builder(this);


        final String username = AppSharedPreferences.getInstance().readString(PrefConstants.KEY_PHONENUMBER);
        final String password = AppSharedPreferences.getInstance().readString(PrefConstants.KEY_PHONENUMBER);

        // Create the configuration for this new connection
        if (username != null && password != null) {
            new Thread() {
                @Override
                public void run() {


                    InetAddress addr = null;
                    try {
                        addr = InetAddress.getByName(Constants.Host);
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    HostnameVerifier verifier = new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return false;
                        }
                    };
                    DomainBareJid serviceName = null;
                    try {
                        serviceName = JidCreate.domainBareFrom(Constants.Host);
                    } catch (XmppStringprepException e) {
                        e.printStackTrace();
                    }
                    XMPPTCPConnectionConfiguration config = null;
                    try {
                        config = XMPPTCPConnectionConfiguration.builder()

                                .setUsernameAndPassword(username, password)
                                .setPort(5222)
                                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                                .setXmppDomain(serviceName)
                                .setHostnameVerifier(verifier)
                                .setHostAddress(addr)
                                .setConnectTimeout(100000)
                                .setDebuggerEnabled(true)
                                .setResource(Resourcepart.from("Mobile"))
                                .build();
                    } catch (XmppStringprepException e) {
                        e.printStackTrace();
                    }

                    mConnection = new XMPPTCPConnection(config);
                    try {
                        if (isNetworkConnected && !mConnection.isConnected()) {
                            mConnection.setPacketReplyTimeout(100000);
                            mConnection.setReplyTimeout(100000);

                            mConnection.addConnectionListener(new ConnectionListener() {
                                @Override
                                public void connected(XMPPConnection connection) {
                                    Log.d("fff", connection.toString());

                                        login();

                                }

                                @Override
                                public void authenticated(XMPPConnection connection, boolean resumed) {
                                    Log.d("fff", connection.toString());
                                    login();
                                }

                                @Override
                                public void connectionClosed() {
                                    Log.d("f", "connection closed");
                                   setConnection();
                                }

                                @Override
                                public void connectionClosedOnError(Exception e) {
                                    if (e.getMessage().contains("conflict")&& isNetworkAvailable()) {
                                        AppSharedPreferences.getInstance().clearAll();
                                        db.DeleteDatabase();
                                        databaseHelperChattingToUsers.DeleteDatabase();
                                        Intent intent = new Intent();
                                        intent.setAction("YOUR_INTENT_FILTER");
                                        intent.putExtra(Constants.ERROR, "Session Ends Here");
                                        sendBroadcast(intent);

                                    } else {
                                        setConnection();
                                    }
                                }

                                @Override
                                public void reconnectionSuccessful() {
                                    Log.d("rrrr", "reconnecting");
                                }

                                @Override
                                public void reconnectingIn(int seconds) {
                                    Log.d("rrrr", "reconnecting in  " + seconds);

                                }

                                @Override
                                public void reconnectionFailed(Exception e) {
                                    Log.d("fff", e.getMessage());
                                }
                            });

                            mConnection.connect();



                        }

                        if (mConnection.isAuthenticated() && mConnection.isConnected() && isNetworkConnected) {
                            ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(mConnection);
                            reconnectionManager.enableAutomaticReconnection();


                            if(called==false) {
                                called=true;
                                new Thread(){
                                    @Override
                                    public void run() {
                                        super.run();
                                        CheckConnection();
                                    }
                                };

                            }

                            mConnection.addPacketListener(new RequestListener(getApplicationContext()),new PacketTypeFilter(Presence.class));




                            final NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                            ChatManager chatManager = ChatManager.getInstanceFor(mConnection);
                            chatManager.addIncomingListener(new IncomingChatMessageListener() {
                                @SuppressLint({"WrongConstant", "NewApi"})
                                @Override
                                public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                                    //Do something on message received
                                    if (!isInForeground) {
                                        String CHANNEL_ID = "my_channel_01";
                                        inboxStyle.setBigContentTitle("Enter Content Text");
                                        inboxStyle.addLine(message.getBody());
                                        Intent targetIntent = new Intent(ReceivceMessageService.this, ChatActvity.class);
                                        targetIntent.putExtra(Constants.username, from.toString());
                                        PendingIntent pendingIntent = PendingIntent.getActivity(ReceivceMessageService.this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                                        Notification builder2 = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                                .setSmallIcon(android.R.drawable.ic_dialog_email)
                                                .setContentTitle("message")
                                                .setContentText(message.getBody())
                                                .setContentIntent(pendingIntent)
                                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                                .build();


                                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                        notificationManager.notify(1, builder2);
                                    }
                                    String timeStamp = String.valueOf(System.currentTimeMillis());
                                    Intent intent = new Intent();
                                    intent.setAction("YOUR_INTENT_FILTER");
                                    intent.putExtra("msg", message.getBody());
                                    intent.putExtra("time", timeStamp);
                                    intent.putExtra("number",from.toString());

                                    db.addContact(new Contact(message.getBody(), "false", from.toString(), String.valueOf(System.currentTimeMillis())));
                                    String userId = databaseHelperChattingToUsers.getUserExists(from.toString());
                                    if (userId == null) {
                                        String phone = "";
                                        try {
                                            phone = JidCreate.entityBareFrom(from.toString()).toString();
                                        } catch (XmppStringprepException e) {

                                            e.printStackTrace();
                                        }
                                        if (!isInForeground) {
                                            databaseHelperChattingToUsers.addContact(new ChattingToUsers(phone, timeStamp, message.getBody(), "1"));
                                            intent.putExtra("usersmessage", message.getBody());
                                            intent.putExtra("usersphone", phone);
                                            intent.putExtra("userstime", timeStamp);
                                            intent.putExtra("usersnewmessage", "1");
                                        } else {
                                            databaseHelperChattingToUsers.updateContact(new ChattingToUsers(phone, timeStamp, message.getBody(), "0"));
                                            intent.putExtra("usersmessage", message.getBody());
                                            intent.putExtra("usersphone", phone);
                                            intent.putExtra("userstime", timeStamp);
                                            intent.putExtra("usersnewmessage", "0");
                                        }


                                    } else {
                                        String phone = "";
                                        String splits[] = userId.split("/");

                                        try {
                                            phone = JidCreate.entityBareFrom(from.toString()).toString();
                                        } catch (XmppStringprepException e) {

                                            e.printStackTrace();
                                        }
                                        if (!isInForeground) {
                                            databaseHelperChattingToUsers.updateContact(new ChattingToUsers(splits[0], phone, timeStamp, message.getBody(), String.valueOf(Integer.parseInt(splits[1]) + 1)));
                                            intent.putExtra("usersmessage", message.getBody());
                                            intent.putExtra("usersphone", phone);
                                            intent.putExtra("userstime", timeStamp);
                                            intent.putExtra("usersnewmessage", String.valueOf(Integer.parseInt(splits[1]) + 1));
                                        } else {
                                            databaseHelperChattingToUsers.updateContact(new ChattingToUsers(splits[0], phone, timeStamp, message.getBody(), "0"));
                                            intent.putExtra("usersmessage", message.getBody());
                                            intent.putExtra("usersphone", phone);
                                            intent.putExtra("userstime", timeStamp);
                                            intent.putExtra("usersnewmessage", "0");

                                        }


                                    }


                                    sendBroadcast(intent);
//                                    ChatModel chatModel= new ChatModel(message.getBody(),false);
//                                    ChatActvity.chatmsg.add(chatModel);
//
//                                    ChatActvity.adap.notifyDataSetChanged();
//                                    //ChatActvity.layoutmanager.scrollToPosition();
//                                    ChatActvity.chatlist.scrollToPosition(ChatActvity.chatmsg.size()-1);


                                }

                            });


                        }


                    } catch (SmackException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (XMPPException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }.start();
        }
    }
public void login(){
    if(!mConnection.isAuthenticated() ){
        try {
            mConnection.login();

        } catch (XMPPException e) {
            e.printStackTrace();
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Presence p = new Presence(Presence.Type.available, "Online", 42, Presence.Mode.available);
        try {
            mConnection.sendStanza(p);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onCreate() {

        super.onCreate();
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
