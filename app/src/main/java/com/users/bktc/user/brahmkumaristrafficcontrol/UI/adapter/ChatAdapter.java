package com.users.bktc.user.brahmkumaristrafficcontrol.UI.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.users.bktc.user.brahmkumaristrafficcontrol.R;

import java.util.List;

/**
 * Created by sourabh on 8/22/2017.
 */

public class ChatAdapter extends BaseAdapter {
    private List<ChatModel> modal;
    private Context cont;
    private LayoutInflater lay;

    public ChatAdapter(List<ChatModel> modal, Context cont) {
        this.modal = modal;
        this.cont = cont;
        lay=(LayoutInflater)cont.getSystemService(cont.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return modal.size();
    }

    @Override
    public Object getItem(int i) {
        return modal.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v= view;
        if(v==null){
            if(modal.get(i).Send){
                v=lay.inflate(R.layout.massege_send,null);
            }else{
                v=lay.inflate(R.layout.massege_receive,null);
            }
        }
        return v;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
    }

}
