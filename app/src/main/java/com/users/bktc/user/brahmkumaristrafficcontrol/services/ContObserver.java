package com.users.bktc.user.brahmkumaristrafficcontrol.services;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.users.bktc.user.brahmkumaristrafficcontrol.R;
import com.users.bktc.user.brahmkumaristrafficcontrol.Utilities.Constants;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.xdata.Form;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static com.users.bktc.user.brahmkumaristrafficcontrol.services.ReceivceMessageService.mConnection;

public class ContObserver extends ContentObserver {

    Context context;
    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        UserSearchManager search = new UserSearchManager(mConnection);
        Form searchForm = null;

        if (!selfChange) {
            try {
                if (ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.READ_CONTACTS)
                        == PackageManager.PERMISSION_GRANTED) {
                    ContentResolver cr = context.getContentResolver();
                    Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                    if (cursor != null && cursor.getCount() > 0) {
                        //moving cursor to last position
                        //to get last element added
                        cursor.moveToLast();
                        String contactName = null, photo = null, contactNumber = null;
                        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                        if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                            Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                            if (pCur != null) {
                                while (pCur.moveToNext()) {
                                    contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    if (contactNumber != null && contactNumber.length() > 0) {
                                        contactNumber = contactNumber.replace(" ", "");
                                    }
                                    contactName = pCur.getString(pCur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                                    //Displaying result

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

                                    if(!LinkedContact.contains(contactNumber)){

                                        //Add Number to ArrayList
                                        try {
                                            searchForm = search
                                                    .getSearchForm(JidCreate.domainBareFrom("search.iserv.tech"));
                                            Form answerForm = searchForm.createAnswerForm();
                                            answerForm.setAnswer("Username", true);
                                            answerForm.setAnswer("search", contactNumber);
                                            ReportedData data = search
                                                    .getSearchResults(answerForm, JidCreate.domainBareFrom("search.iserv.tech"));


                                            if (data.getRows().size() > 0) {

                                                //if this condition is true then user exits on the server
                                                addContact(contactName,contactNumber,contactNumber);


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
                                pCur.close();
                            }
                        }
                        cursor.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void addContact(String name1, String username1, String number1)
    {
        String MIMETYPE = "vnd.android.cursor.item/vnd.com.users.bktc.user.brahmkumaristrafficcontrol";
        ContentResolver resolver = context.getContentResolver();
        //resolver.delete(ContactsContract.RawContacts.CONTENT_URI,  null, null);

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        ops.add(ContentProviderOperation.newInsert(addCallerIsSyncAdapterParameter(ContactsContract.RawContacts.CONTENT_URI, true))
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, Constants.SyncAccountname)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, Constants.SyncAccountType)
                //.withValue(RawContacts.SOURCE_ID, 12345)
                //.withValue(RawContacts.AGGREGATION_MODE, RawContacts.AGGREGATION_MODE_DISABLED)
                .build());
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
                android.R.drawable.ic_dialog_email);
        byte[] bytes=null;
        if (bmp != null ) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            bytes = stream.toByteArray();
        }
        ops.add(ContentProviderOperation.newInsert(addCallerIsSyncAdapterParameter(ContactsContract.Settings.CONTENT_URI, true))
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, MIMETYPE)
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
                .withValue(ContactsContract.Data.DATA1, number1)
                .withValue(ContactsContract.Data.DATA2, number1)
                .withValue(ContactsContract.Data.DATA3, "Message " + number1)

                    .withValue(ContactsContract.Data.DATA4,bytes)

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
    public ContObserver(Handler handler,Context context) {
        super(handler);
        this.context = context;    }
}
