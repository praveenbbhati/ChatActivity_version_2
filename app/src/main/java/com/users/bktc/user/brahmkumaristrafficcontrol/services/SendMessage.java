package com.users.bktc.user.brahmkumaristrafficcontrol.services;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import static com.users.bktc.user.brahmkumaristrafficcontrol.services.ReceivceMessageService.mConnection;

public class SendMessage {

    public static void sendMessage(String messageSend,String entitytId){
        EntityBareJid jid=null;
        try {
            jid = JidCreate.entityBareFrom(entitytId);
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }

            ChatManager chatManager= ChatManager.getInstanceFor(mConnection);
            Chat chat = chatManager.chatWith(jid);
            Message newMessage = new Message();
            newMessage.setBody(messageSend);

            try {
                chat.send(newMessage);

            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

    }
}
