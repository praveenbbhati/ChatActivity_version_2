package com.users.bktc.user.brahmkumaristrafficcontrol.UI.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.users.bktc.user.brahmkumaristrafficcontrol.R;
import com.users.bktc.user.brahmkumaristrafficcontrol.UI.ChatActvity;
import com.users.bktc.user.brahmkumaristrafficcontrol.UI.ContactList;
import com.users.bktc.user.brahmkumaristrafficcontrol.UI.MainActivity;
import com.users.bktc.user.brahmkumaristrafficcontrol.UI.adapter.ChatUsersListAdapter;
import com.users.bktc.user.brahmkumaristrafficcontrol.UI.adapter.ChatUsersListModel;
import com.users.bktc.user.brahmkumaristrafficcontrol.Utilities.Constants;
import com.users.bktc.user.brahmkumaristrafficcontrol.databinding.ActivityChatListBinding;
import com.users.bktc.user.brahmkumaristrafficcontrol.network.database.ChattingToUsers;
import com.users.bktc.user.brahmkumaristrafficcontrol.network.database.DatabaseHelperChattingToUsers;
import com.users.bktc.user.brahmkumaristrafficcontrol.sharedpreference.AppSharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChatList extends android.support.v4.app.Fragment {

    private ActivityChatListBinding activityChatListBinding;
    private DatabaseHelperChattingToUsers databaseHelperChattingToUsers;
    private View view;
    private List<ChattingToUsers> chatUsers;
    BroadcastReceiver bd;
    public static boolean errorocccured=false;
    List<String> userPhone;
    private List<ChatUsersListModel> chatUsersModel;
    ChatUsersListAdapter chatUsersListAdapter;
    private ChatUsersListModel chatUsersListModel;
    IntentFilter intentFilter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, final Bundle savedInstanceState) {
        activityChatListBinding = DataBindingUtil.inflate(inflater, R.layout.activity_chat_list, container, false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        activityChatListBinding.chatlist.setLayoutManager(layoutManager);

        userPhone = new ArrayList<>();
        chatUsersListAdapter = new ChatUsersListAdapter(chatUsersModel, getContext());
        FetchList();
        intentFilter = new IntentFilter();
        intentFilter.addAction("YOUR_INTENT_FILTER");
        activityChatListBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),ContactList.class));
            }
        });
        bd = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                String message = intent.getStringExtra("usersmessage");
                String phonenummber = intent.getStringExtra("usersphone");
                String usernewmessage = intent.getStringExtra("usersnewmessage");
                String usertimestamp = intent.getStringExtra("userstime");
                String error=intent.getStringExtra(Constants.ERROR);

                if(error!=null){

                    errorocccured=true;

                    new AlertDialog.Builder(getActivity()).setCancelable(false).setMessage("Your phone number is no longer registered on this phone. This is likely because you registered your phone number with app on different phone").setPositiveButton("Verify", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent in= new Intent(getContext(), MainActivity.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(in);
                        }
                    }).show();

                }
                if (message != null) {



                  int position=GetIndexOf(phonenummber);

                    if (position >= 0) {
                        ChatUsersListModel chatUsersListModel = new ChatUsersListModel(phonenummber, message, usertimestamp, usernewmessage);
                        UpdateList(position, chatUsersListModel);
                    } else {
                        ChatUsersListModel chatUsersListModel = new ChatUsersListModel(phonenummber, message, usertimestamp, usernewmessage);

                        AddElemetInList(chatUsersListModel);
                    }

                }

            }
        };
        getActivity().registerReceiver(bd, intentFilter);
        chatUsersListAdapter.clickItem(new ChatUsersListAdapter.OnItemClick() {
            @Override
            public void click(int position, View view) {
              Intent inten= new Intent(getActivity(), ChatActvity.class);
              inten.putExtra(Constants.username,chatUsersModel.get(position).getPhoneNumber());

              startActivity(inten);
            }
        });
        view = activityChatListBinding.getRoot();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(bd);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(bd, intentFilter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void FetchList() {
        databaseHelperChattingToUsers = new DatabaseHelperChattingToUsers(getContext());
        chatUsers = databaseHelperChattingToUsers.getAllContacts();
        chatUsersModel = new ArrayList<>();

        userPhone = new ArrayList<>();
        for (ChattingToUsers c : chatUsers) {
            chatUsersListModel = new ChatUsersListModel(c.getPhone(), c.getLastMessage(), c.getLastTimeStamp(), c.getNewMessageCout());
            chatUsersModel.add(chatUsersListModel);
            userPhone.add(c.getPhone());


        }

        chatUsersListAdapter = new ChatUsersListAdapter(chatUsersModel, getContext());
        activityChatListBinding.chatlist.setAdapter(chatUsersListAdapter);

    }

    public void UpdateList(int position, ChatUsersListModel model) {

        chatUsersModel.set(position, model);
        Collections.sort(chatUsersModel, new SortModelList());
        chatUsersListAdapter.notifyDataSetChanged();

    }

    public void AddElemetInList( ChatUsersListModel model) {
        chatUsersModel.add(model);
        Collections.sort(chatUsersModel, new SortModelList());
        chatUsersListAdapter.notifyDataSetChanged();
    }

    class SortModelList implements Comparator<ChatUsersListModel> {

        @Override
        public int compare(ChatUsersListModel chatUsersListModel, ChatUsersListModel t1) {
            Long time1= Long.parseLong(chatUsersListModel.getLastTimeStamp());
            Long time2= Long.parseLong(t1.getLastTimeStamp());
            int compparison =time1.compareTo(time2);

            if (compparison> 0) {
                return -1;
            } else {
                return 1;
            }

        }
    }
    public int GetIndexOf(String phone){
        for(int i=0;i<chatUsersModel.size();i++){
            if(chatUsersModel.get(i).getPhoneNumber().equals(phone)){

                return i;
            }
        }
        return -1;
    }

}
