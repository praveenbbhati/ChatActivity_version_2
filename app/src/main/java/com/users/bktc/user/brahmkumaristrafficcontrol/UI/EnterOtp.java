package com.users.bktc.user.brahmkumaristrafficcontrol.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.msg91.sendotp.library.SendOtpVerification;
import com.msg91.sendotp.library.Verification;
import com.msg91.sendotp.library.VerificationListener;
import com.users.bktc.user.brahmkumaristrafficcontrol.Utilities.Constants;
import com.users.bktc.user.brahmkumaristrafficcontrol.R;
import com.users.bktc.user.brahmkumaristrafficcontrol.Utilities.HeaderHandler;
import com.users.bktc.user.brahmkumaristrafficcontrol.Utilities.Utils;
import com.users.bktc.user.brahmkumaristrafficcontrol.databinding.ActivityEnterOtpBinding;
import com.users.bktc.user.brahmkumaristrafficcontrol.listener.RequestListener;
import com.users.bktc.user.brahmkumaristrafficcontrol.services.ReceivceMessageService;
import com.users.bktc.user.brahmkumaristrafficcontrol.sharedpreference.AppSharedPreferences;
import com.users.bktc.user.brahmkumaristrafficcontrol.sharedpreference.PrefConstants;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import static com.users.bktc.user.brahmkumaristrafficcontrol.services.ReceivceMessageService.mConnection;

public class EnterOtp extends AppCompatActivity implements VerificationListener,HeaderHandler{

    ActivityEnterOtpBinding activityEnterOtpBinding;
    ProgressDialog pd;
    String phonenumber="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       activityEnterOtpBinding= DataBindingUtil.setContentView(this, R.layout.activity_enter_otp);
       pd= new ProgressDialog(this);
       pd.setMessage(getString(R.string.please_wait));
        activityEnterOtpBinding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String entederdOtp= activityEnterOtpBinding.pinView.getText().toString();
                phonenumber=getIntent().getExtras().getString(Constants.phone);
                //SendOtp(entederdOtp,phonenumber);
                Register(phonenumber,phonenumber);

            }
        });
    }
    public void SendOtp(String otp,String phone) {

        pd.show();
        Verification mVerifivation = SendOtpVerification.createSmsVerification(
                SendOtpVerification
                        .config("+91"+phone)
                        .otp(otp)
                        .context(this)
                        .autoVerification(true)
                        .build(),this);
        mVerifivation.verify(otp);
    }

    @Override
    public void onInitiated(String response) {

    }

    @Override
    public void onInitiationFailed(Exception paramException) {

    }

    @Override
    public void onVerified(String response) {

        if (!Pattern.matches("[a-zA-Z]+", response) ) {

            Register(phonenumber,phonenumber);

          // Utils.showSnackBar("sucess",activityEnterOtpBinding.parentView);
        }else{
            Utils.showSnackBar(response,activityEnterOtpBinding.parentView);
        }

    }

    @Override
    public void onVerificationFailed(Exception paramException) {

        pd.dismiss();
        Utils.showSnackBar(paramException.getMessage(),activityEnterOtpBinding.parentView);
    }
    public void Register(final String phone, final String password){

        // Creating a connection
        new Thread(){
            @Override
            public void run() {
                XMPPTCPConnectionConfiguration connConfig =
                        null;
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
                try {

                    connConfig = XMPPTCPConnectionConfiguration.builder()
                            .setHost(Constants.Host)  // Name of your Host
                            .setHostAddress(addr)
                            .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                            .setPort(5222)          // Your Port for accepting c2s connection
                            .setDebuggerEnabled(true)
                            .setHostnameVerifier(verifier)
                            .setServiceName( JidCreate.domainBareFrom(phone+Constants.domainName))
                            .build();
                } catch (Throwable e) {
                   Log.d("exeption",e.getMessage());
                }
                AbstractXMPPConnection connection = new XMPPTCPConnection(connConfig);

                try {
                    // connecting...

                    connection.connect();

                    Log.i("TAG", "Connected to " + connection.getHost());

                    // Registering the user
                    AccountManager accountManager = AccountManager.getInstance(connection);
                    accountManager.sensitiveOperationOverInsecureConnection(true);
                    accountManager.createAccount(Localpart.from(phone), password);   // Skipping optional fields like email, first name, last name, etc..
                    AppSharedPreferences.getInstance().writeString(PrefConstants.KEY_PHONENUMBER,phone);
                    startService(new Intent(getBaseContext(),ReceivceMessageService.class));
                    Intent inte=new Intent(EnterOtp.this, MainChatActivity.class);
                    inte.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(inte);
                } catch ( Throwable e) {
                    Log.d("exeption",e.getMessage());
                    if(e.getMessage().toLowerCase().contains("conflict - cancel")) {
                        //perform action when account already exists
                        AppSharedPreferences.getInstance().writeString(PrefConstants.KEY_PHONENUMBER,phone);
                        startService(new Intent(getBaseContext(),ReceivceMessageService.class));
                        Intent inte=new Intent(EnterOtp.this, MainChatActivity.class);
                        inte.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(inte);
                    }

                }
            }
        }.start();

    }



    @Override
    public void backClick(View view) {
        finish();
    }


}
