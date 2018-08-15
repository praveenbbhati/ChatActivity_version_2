package com.users.bktc.user.brahmkumaristrafficcontrol.services;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import com.users.bktc.user.brahmkumaristrafficcontrol.Utilities.Constants;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.xdata.Form;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.util.ArrayList;
import java.util.List;

import static com.users.bktc.user.brahmkumaristrafficcontrol.services.ReceivceMessageService.mConnection;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    public static final String TAG = "SyncAdapter";
    private  AccountManager accountManager;
    ContentResolver itsContentResolver=null;
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        accountManager=AccountManager.get(context);
    }

//    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs, AccountManager accountManager) {
//        super(context, autoInitialize, allowParallelSyncs);
//        this.accountManager = accountManager;
//    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        UserSearchManager search = new UserSearchManager(mConnection);
        Form searchForm = null;
        ContentResolver cr = getContext().getContentResolver();

        Cursor cur = cr.query(
                ContactsContract.RawContacts.CONTENT_URI,
                null,
                ContactsContract.RawContacts.ACCOUNT_TYPE + " <> ?",
                new String[]{Constants.SyncAccountType},
                null);

        List<String> LinkedContact= new ArrayList<>();

        Cursor cur2 = cr.query(
                ContactsContract.RawContacts.CONTENT_URI,
                null,
                ContactsContract.RawContacts.ACCOUNT_TYPE + " = ?",
                new String[]{Constants.SyncAccountType},
                null);
        if (cur2 != null) {
            if (cur2.getCount() > 0) {
                if (cur2.moveToFirst()) {
                    do {
//                        //whatsappContactId for get Number,Name,Id ect... from  ContactsContract.CommonDataKinds.Phone
                        String whatsappContactId2 = cur2.getString(cur2.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID));

                        if (whatsappContactId2 != null) {
                            //Get Data from ContactsContract.CommonDataKinds.Phone of Specific CONTACT_ID
                            Cursor whatsAppContactCursor2 = cr.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                                            ContactsContract.CommonDataKinds.Phone.NUMBER,
                                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{whatsappContactId2}, null);

                            if (whatsAppContactCursor2 != null && whatsAppContactCursor2.getCount()>0) {
                                whatsAppContactCursor2.moveToFirst();
                                // String id = whatsAppContactCursor.getString(whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                                String name = whatsAppContactCursor2.getString(whatsAppContactCursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                String number = whatsAppContactCursor2.getString(whatsAppContactCursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                whatsAppContactCursor2.close();

                                LinkedContact.add(number);
                            }



                        }

                    }while (cur2.moveToNext());
                    cur2.close();
                }
            }
        }

        if (cur != null) {
            if (cur.getCount() > 0) {
                if (cur.moveToFirst()) {
                    do {
//                        //whatsappContactId for get Number,Name,Id ect... from  ContactsContract.CommonDataKinds.Phone
                        String whatsappContactId = cur.getString(cur.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID));

                        if (whatsappContactId != null) {
                            //Get Data from ContactsContract.CommonDataKinds.Phone of Specific CONTACT_ID
                            Cursor whatsAppContactCursor = cr.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                                            ContactsContract.CommonDataKinds.Phone.NUMBER,
                                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{whatsappContactId}, null);

                            if (whatsAppContactCursor != null && whatsAppContactCursor.getCount()>0) {
                                whatsAppContactCursor.moveToFirst();
                                // String id = whatsAppContactCursor.getString(whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                                String name = whatsAppContactCursor.getString(whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                String number = whatsAppContactCursor.getString(whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                whatsAppContactCursor.close();
if(!LinkedContact.contains(number)) {
                                //Add Number to ArrayList
                                try {
                                    if(mConnection.isAuthenticated()&& mConnection.isConnected()) {
                                        searchForm = search
                                                .getSearchForm(JidCreate.domainBareFrom("search.iserv.tech"));
                                        Form answerForm = searchForm.createAnswerForm();
                                        answerForm.setAnswer("Username", true);
                                        answerForm.setAnswer("search", number);
                                        ReportedData data = search
                                                .getSearchResults(answerForm, JidCreate.domainBareFrom("search.iserv.tech"));


                                        if (data.getRows().size() > 0) {

                                            //if this condition is true then user exits on the server
                                            addContact(name, number, number);
                                            Log.d("search", number);

                                        }
                                    }
                                } catch (SmackException.NoResponseException e) {
                                    e.printStackTrace();
                                } catch (XMPPException.XMPPErrorException e) {
                                    e.printStackTrace();
                                } catch (SmackException.NotConnectedException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (XmppStringprepException e) {
                                    e.printStackTrace();
                                }

}



                                        }



                        }

                    }while (cur.moveToNext());
                    cur.close();
                }
            }
        }
    }
    public void addContact( String name1, String username1, String number1)
    {
         String MIMETYPE = "vnd.android.cursor.item/com.company.demo.account.profile";
        ContentResolver resolver = getContext().getContentResolver();
        //resolver.delete(ContactsContract.RawContacts.CONTENT_URI,  null, null);

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        ops.add(ContentProviderOperation.newInsert(addCallerIsSyncAdapterParameter(ContactsContract.RawContacts.CONTENT_URI, true))
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, Constants.SyncAccountname)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, Constants.SyncAccountType)
                //.withValue(RawContacts.SOURCE_ID, 12345)
                //.withValue(RawContacts.AGGREGATION_MODE, RawContacts.AGGREGATION_MODE_DISABLED)
                .build());

        ops.add(ContentProviderOperation.newInsert(addCallerIsSyncAdapterParameter(ContactsContract.Settings.CONTENT_URI, true))
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, Constants.SyncAccountname)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, Constants.SyncAccountType)
                .build());

        ops.add(ContentProviderOperation.newInsert(addCallerIsSyncAdapterParameter(ContactsContract.Data.CONTENT_URI, true))
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name1)
                .build());

        ops.add(ContentProviderOperation.newInsert(addCallerIsSyncAdapterParameter(ContactsContract.Data.CONTENT_URI, true))
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,number1)
                .build());


        ops.add(ContentProviderOperation.newInsert(addCallerIsSyncAdapterParameter(ContactsContract.Data.CONTENT_URI, true))
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .build());


        ops.add(ContentProviderOperation.newInsert(addCallerIsSyncAdapterParameter(ContactsContract.Data.CONTENT_URI, true))
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, MIMETYPE)
                .withValue(ContactsContract.Data.DATA1, name1)
                .withValue(ContactsContract.Data.DATA2, number1)
                .withValue(ContactsContract.Data.DATA3, "Message " + number1)
                .build());
        try {
            ContentProviderResult[] results = resolver.applyBatch(ContactsContract.AUTHORITY, ops);
            if (results.length == 0)
                ;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static Uri addCallerIsSyncAdapterParameter(Uri uri, boolean isSyncOperation) {
        if (isSyncOperation) {
            return uri.buildUpon()
                    .appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true")
                    .build();
        }
        return uri;
    }
}
