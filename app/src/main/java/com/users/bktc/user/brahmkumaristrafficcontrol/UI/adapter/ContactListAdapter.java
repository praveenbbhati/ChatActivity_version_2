package com.users.bktc.user.brahmkumaristrafficcontrol.UI.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.users.bktc.user.brahmkumaristrafficcontrol.R;
import com.users.bktc.user.brahmkumaristrafficcontrol.Utilities.Utils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder>{
    private List<ChatUsersListModel> chatUsersListModels;
    private List<String> phoneNumber;
    private Context context;
    private OnItemClick onItemClick;
    private static  int innerclassposition;

    public ContactListAdapter(List<ChatUsersListModel> chatUsersListModels, List<String> phoneNumber, Context context) {
        this.chatUsersListModels = chatUsersListModels;
        this.context = context;
        this.phoneNumber=phoneNumber;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


            holder.userPhone.setText(chatUsersListModels.get(position).getPhoneNumber());

        holder.lastTimeStamp.setText(Utils.getFormatDateTimeLong(chatUsersListModels.get(position).getLastTimeStamp()));
        holder.lastMessage.setText(chatUsersListModels.get(position).getLastMessage());
        String newmessage=chatUsersListModels.get(position).getNewMessageCount();

        if(Integer.parseInt(newmessage)>0){
            holder.newMessageCount.setVisibility(View.VISIBLE);
            holder.newMessageCount.setText(newmessage);
        }else{
            holder.newMessageCount.setVisibility(View.GONE);
        }

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.click(position,view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatUsersListModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView displayPicture;
        TextView userPhone;
        TextView lastTimeStamp;
        TextView lastMessage;
        TextView newMessageCount;
        RelativeLayout parent;

        public ViewHolder(View itemView) {
            super(itemView);
            displayPicture=(CircleImageView)itemView.findViewById(R.id.displayPicture);
            userPhone=(TextView) itemView.findViewById(R.id.userPhone);
            lastMessage=(TextView) itemView.findViewById(R.id.lastMessage);
            lastTimeStamp=(TextView) itemView.findViewById(R.id.lastTimeStamp);
            newMessageCount=(TextView) itemView.findViewById(R.id.NewMessageCount);
            parent=(RelativeLayout) itemView.findViewById(R.id.parent_view);
        }
    }
    public void clickItem(ContactListAdapter.OnItemClick onItemClick){

        this.onItemClick =onItemClick;
    }


    public interface OnItemClick{
        public void click(int position, View view);
    }

}
