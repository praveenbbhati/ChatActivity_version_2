package com.users.bktc.user.brahmkumaristrafficcontrol.listener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.UiThread;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.users.bktc.user.brahmkumaristrafficcontrol.UI.MainActivity;
import com.users.bktc.user.brahmkumaristrafficcontrol.services.ReceivceMessageService;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;

import static com.users.bktc.user.brahmkumaristrafficcontrol.services.ReceivceMessageService.mConnection;

public class RequestListener  implements PacketListener {

    Context context;

    public RequestListener(Context context) {
        this.context = context;
    }

    @Override
    public void processStanza(Stanza packet) throws SmackException.NotConnectedException, InterruptedException {
        Presence presence= (Presence)packet;
        final Presence.Type presenceType = presence.getType();




            Log.d("from",presenceType.toString());
            if (presenceType == Presence.Type.subscribe)
            {


                Roster roster = Roster.getInstanceFor(mConnection);

                    roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
                try {
                    roster.createEntry(JidCreate.bareFrom(presence.getFrom().toString()), presence.getFrom().toString(), null);
                } catch (SmackException.NotLoggedInException e) {
                    e.printStackTrace();
                } catch (SmackException.NoResponseException e) {
                    e.printStackTrace();
                } catch (XMPPException.XMPPErrorException e) {
                    e.printStackTrace();
                } catch (XmppStringprepException e) {
                    e.printStackTrace();
                }


            }
            if(presenceType==Presence.Type.unavailable){

                    try {

                            context.startService(new Intent(context, ReceivceMessageService.class));


                    }catch (Exception e){
                        e.printStackTrace();
                    }



            }



    }






}
