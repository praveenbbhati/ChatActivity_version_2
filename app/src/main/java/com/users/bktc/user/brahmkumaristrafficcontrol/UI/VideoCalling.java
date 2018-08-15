package com.users.bktc.user.brahmkumaristrafficcontrol.UI;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.oney.WebRTCModule.WebRTCModulePackage;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.users.bktc.user.brahmkumaristrafficcontrol.R;
import com.users.bktc.user.brahmkumaristrafficcontrol.Utilities.Utils;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class VideoCalling extends AppCompatActivity {
    private JitsiMeetView view;
    private WebView mWebview ;
    ProgressDialog pd;

    @Override
    public void onBackPressed() {
//        if (!JitsiMeetView.onBackPressed()) {
//            // Invoke the default handler if it wasn't handled by React.
//            super.onBackPressed();
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//
//        view = new JitsiMeetView(this);
//
//        Bundle config = new Bundle();
//        config.putBoolean("startWithAudioMuted", true);
//        config.putBoolean("startWithVideoMuted", false);
//        Bundle urlObject = new Bundle();
//        urlObject.putBundle("config", config);
//        urlObject.putString("url", "https://iserv.tech:7443/ofmeet/sourabh");
//        view.loadURL(null);

        mWebview  = new WebView(this);

        mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript
       mWebview.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebview.getSettings().setBuiltInZoomControls(true);
        mWebview.getSettings().setUseWideViewPort(true);
        mWebview.getSettings().setDomStorageEnabled(true);
        mWebview.getSettings().setLoadWithOverviewMode(true);
        String newUA= "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
        mWebview.getSettings().setUserAgentString(newUA);
        final Activity activity = this;
        pd= new ProgressDialog(this);
        pd.setMessage("Loading...");
        mWebview.setWebChromeClient(new WebChromeClient());
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pd.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pd.dismiss();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }
        });

        mWebview .loadUrl("https://iserv.tech:7443/ofmeet/sourabh");
        setContentView(mWebview );

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        view.dispose();
//        view = null;
//
//        JitsiMeetView.onHostDestroy(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        JitsiMeetView.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

//        JitsiMeetView.onHostResume(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

//        JitsiMeetView.onHostPause(this);
    }
}