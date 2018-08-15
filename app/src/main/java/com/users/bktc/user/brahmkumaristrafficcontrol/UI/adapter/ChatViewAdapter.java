package com.users.bktc.user.brahmkumaristrafficcontrol.UI.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.users.bktc.user.brahmkumaristrafficcontrol.R;
import com.users.bktc.user.brahmkumaristrafficcontrol.Utilities.Utils;

import java.util.List;

public class ChatViewAdapter extends RecyclerView.Adapter<ChatViewAdapter.Holder> {
    private List<ChatModel> modal;
    private Context cont;
    private int MESSSAGE_RECEIVED=1;
    private int  MESSAGE_SENT=2;

    public ChatViewAdapter(List<ChatModel> modal, Context cont) {
        this.modal = modal;
        this.cont = cont;
    }

    @Override
    public int getItemViewType(int position) {
        if(modal.get(position).Send){
            return MESSAGE_SENT;
        }else{
            return MESSSAGE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=null;
        if(v==null){
            if(viewType==MESSAGE_SENT){
                v= LayoutInflater.from(parent.getContext()).inflate(R.layout.massege_send,parent,false);
            }else{
                v=LayoutInflater.from(parent.getContext()).inflate(R.layout.massege_receive,parent,false);
            }


        }
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.b.setText(modal.get(position).getMassege());
        holder.time.setText(Utils.getFormatDateTimeLong(modal.get(position).getTimeStamp()));
    }

    @Override
    public int getItemCount() {
        return modal.size();
    }

    public  static  class Holder extends RecyclerView.ViewHolder {
        TextView b ,time;
        public Holder(View itemView) {
            super(itemView);

            b=(TextView) itemView.findViewById(R.id.msgrc3v);
            time=(TextView) itemView.findViewById(R.id.timeStamp);

        }
    }
}
