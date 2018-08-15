package com.users.bktc.user.brahmkumaristrafficcontrol.UI;

import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog.Builder;
import android.os.Bundle;

import com.users.bktc.user.brahmkumaristrafficcontrol.R;

public class AlertDialogs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_dialog);
        new AlertDialog.Builder(this).setTitle("sdiofjoisdj").show();
    }
}
