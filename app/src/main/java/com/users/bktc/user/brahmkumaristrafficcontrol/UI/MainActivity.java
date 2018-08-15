package com.users.bktc.user.brahmkumaristrafficcontrol.UI;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.databinding.DataBindingUtil;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.msg91.sendotp.library.SendOtpVerification;
import com.msg91.sendotp.library.Verification;
import com.msg91.sendotp.library.VerificationListener;
import com.users.bktc.user.brahmkumaristrafficcontrol.ApiEnumeration.ApiEnumeration;
import com.users.bktc.user.brahmkumaristrafficcontrol.ApiEnumeration.VolleyResponse;
import com.users.bktc.user.brahmkumaristrafficcontrol.Utilities.Constants;
import com.users.bktc.user.brahmkumaristrafficcontrol.R;
import com.users.bktc.user.brahmkumaristrafficcontrol.Utilities.Utils;
import com.users.bktc.user.brahmkumaristrafficcontrol.databinding.ActivityMainBinding;
import com.users.bktc.user.brahmkumaristrafficcontrol.logger.MLogger;
import com.users.bktc.user.brahmkumaristrafficcontrol.network.NetworkRequestVolley;
import com.users.bktc.user.brahmkumaristrafficcontrol.network.request.RequestModel;
import com.users.bktc.user.brahmkumaristrafficcontrol.network.response.ResponseModel;
import com.users.bktc.user.brahmkumaristrafficcontrol.services.ContactWatchService;
import com.users.bktc.user.brahmkumaristrafficcontrol.services.ReceivceMessageService;
import com.users.bktc.user.brahmkumaristrafficcontrol.sharedpreference.AppSharedPreferences;
import com.users.bktc.user.brahmkumaristrafficcontrol.sharedpreference.PrefConstants;

import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.packet.Presence;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static com.users.bktc.user.brahmkumaristrafficcontrol.Utilities.Constants.username;

public class MainActivity extends AppCompatActivity implements VerificationListener {
 private ActivityMainBinding activityMainBinding;
    boolean rst= false;
    ContentResolver itsContentResolver=null;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       activityMainBinding= DataBindingUtil.setContentView(this, R.layout.activity_main);
       activityMainBinding.setActmain(this);
       pd=new ProgressDialog(this);
        pd.setMessage(getString(R.string.please_wait));
        startService(new Intent(getBaseContext(), ContactWatchService.class));

        String phoneNumber= AppSharedPreferences.getInstance().readString(PrefConstants.KEY_PHONENUMBER,null);
        if(phoneNumber!=null ){

                if(!Utils.isSyncAccountExists(this)) {

                    try {
                        Utils.createSyncAccount(this, "message", "meesage", "msg@msg.com");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(!isMyServiceRunning(ReceivceMessageService.class)) {
                    startService(new Intent(getBaseContext(), ReceivceMessageService.class));
                }
            Intent inte=new Intent(MainActivity.this, MainChatActivity.class);
            inte.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(inte);
        }
        activityMainBinding.button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

       // SendOtp(activityMainBinding.pass.getText().toString());
        Intent intent= new Intent(MainActivity.this,EnterOtp.class);
        intent.putExtra(Constants.phone,activityMainBinding.pass.getText().toString());
        startActivity(intent);
       // Register(activityMainBinding.userName.getText().toString(),activityMainBinding.pass.getText().toString());
    }
});
    }
    public void addContact(Account account,String name1, String username1, String number1)
    {
        ArrayList<ContentProviderOperation> operationList = new ArrayList<ContentProviderOperation>();

        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI);
        builder.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, account.name);
        builder.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, account.type);
        builder.withValue(ContactsContract.RawContacts.SYNC1, username1);
        operationList.add(builder.build());

        builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
        builder.withValueBackReference(ContactsContract.CommonDataKinds.StructuredName.RAW_CONTACT_ID, 0);
        builder.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        builder.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name1);
        operationList.add(builder.build());

        builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
        builder.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0);
        builder.withValue(ContactsContract.Data.MIMETYPE, "vnd.android.cursor.item/vnd.com.company.demo.account.profile");
        builder.withValue(ContactsContract.Data.DATA1, username1);
        builder.withValue(ContactsContract.Data.DATA2, number1);
        operationList.add(builder.build());

        try
        {
            itsContentResolver.applyBatch(ContactsContract.AUTHORITY, operationList);
        }
        catch (OperationApplicationException e)
        {
            e.printStackTrace();
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    public void HitApi(){
        RequestModel requestModel= new RequestModel();
        requestModel.setUsername(activityMainBinding.userName.getText().toString());
        requestModel.setEmail(activityMainBinding.email.getText().toString());
        requestModel.setPassword(activityMainBinding.pass.getText().toString());
        try {
            new NetworkRequestVolley(new VolleyResponse<ResponseModel>() {
                @Override
                public void onResult(ResponseModel resultModel, ApiEnumeration apiEnumeration) {


                    Utils.showSnackBar(resultModel.getMessage(),activityMainBinding.parentView);

                }

                @Override
                public void onFailure(String errorMessage, ApiEnumeration apiEnumeration) {
                    Utils.showSnackBar(errorMessage,activityMainBinding.parentView);
                }
            }, this).sendRequest(ApiEnumeration.RU_SEND_DATA,requestModel ,
                    true, getString(R.string.please_wait),"sending data");
        } catch (JSONException e) {
            MLogger.error("JSON", e);
        }
    }


    public void SendOtp(String phone) {

        pd.show();
        Verification mVerifivation =SendOtpVerification.createSmsVerification(
                SendOtpVerification
                .config("+91"+phone)
                        .message("Your verification code is  ##OTP##")
                        .expiry("10")
                        .senderId("ISERVS")
                        .otplength("4")
                .context(this)
                .build(),MainActivity.this);
        mVerifivation.initiate();


    }

    @Override
    public void onInitiated(String response) {
pd.dismiss();
        if (!Pattern.matches("[a-zA-Z]+", response) ) {
           Intent intent= new Intent(MainActivity.this,EnterOtp.class);
           intent.putExtra(Constants.phone,activityMainBinding.pass.getText().toString());
           startActivity(intent);
        }else{
            Utils.showSnackBar(response,activityMainBinding.parentView);
        }


    }

    @Override
    public void onInitiationFailed(Exception paramException) {

        pd.dismiss();
        Utils.showSnackBar(paramException.getMessage(),activityMainBinding.parentView);
    }

    @Override
    public void onVerified(String response) {

    }

    @Override
    public void onVerificationFailed(Exception paramException) {

    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
