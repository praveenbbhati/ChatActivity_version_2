package com.users.bktc.user.brahmkumaristrafficcontrol.UI;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.users.bktc.user.brahmkumaristrafficcontrol.R;
import com.users.bktc.user.brahmkumaristrafficcontrol.UI.ChatActvity;
import com.users.bktc.user.brahmkumaristrafficcontrol.UI.adapter.ChatPagerAdapter;
import com.users.bktc.user.brahmkumaristrafficcontrol.UI.fragments.ChatList;
import com.users.bktc.user.brahmkumaristrafficcontrol.Utilities.Utils;
import com.users.bktc.user.brahmkumaristrafficcontrol.databinding.ActivityMainChatBinding;

public class MainChatActivity extends AppCompatActivity {

    private ActivityMainChatBinding activityMainChatBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainChatBinding= DataBindingUtil.setContentView(this, R.layout.activity_main_chat);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 100);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.

            ChatPagerAdapter chatPagerAdapter= new ChatPagerAdapter(getSupportFragmentManager());
            chatPagerAdapter.addFragment(new ChatList(),"chat activity");
            activityMainChatBinding.viewpager.setAdapter(chatPagerAdapter);
            activityMainChatBinding.tabs.setupWithViewPager(activityMainChatBinding.viewpager);
            activityMainChatBinding.viewpager.setOffscreenPageLimit(2);



        }



    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                ChatPagerAdapter chatPagerAdapter= new ChatPagerAdapter(getSupportFragmentManager());
                chatPagerAdapter.addFragment(new ChatList(),"chat activity");
                activityMainChatBinding.viewpager.setAdapter(chatPagerAdapter);
                activityMainChatBinding.tabs.setupWithViewPager(activityMainChatBinding.viewpager);
                activityMainChatBinding.viewpager.setOffscreenPageLimit(2);
            } else {
                Utils.showSnackBar("Until you grant the permission, we canot display the names", activityMainChatBinding.parent);
            }
        }
    }
}
