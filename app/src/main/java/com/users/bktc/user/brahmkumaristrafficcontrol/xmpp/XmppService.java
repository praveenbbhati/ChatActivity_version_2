package com.users.bktc.user.brahmkumaristrafficcontrol.xmpp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.packet.Stanza;

public class XmppService extends Service implements Handler.Callback,ChatManagerListener,MessageListener,PacketListener,ConnectionListener{

    public static void setup(final Context context,String domain,String ip,String username,String password){

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean handleMessage(Message message) {
        return false;
    }

    @Override
    public void connected(XMPPConnection connection) {

    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {

    }

    @Override
    public void connectionClosed() {

    }

    @Override
    public void connectionClosedOnError(Exception e) {

    }

    @Override
    public void reconnectionSuccessful() {

    }

    @Override
    public void reconnectingIn(int seconds) {

    }

    @Override
    public void reconnectionFailed(Exception e) {

    }

    @Override
    public void processMessage(org.jivesoftware.smack.packet.Message message) {

    }

    @Override
    public void processStanza(Stanza packet) throws SmackException.NotConnectedException, InterruptedException {

    }

    @Override
    public void chatCreated(Chat chat, boolean createdLocally) {

    }
}
