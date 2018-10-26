package com.example.yukinaito.mydictionary.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yukinaito.mydictionary.R;
import com.example.yukinaito.mydictionary.model.item.AdapterItem;

import java.util.ArrayList;

class WordsAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater = null;
    private ArrayList<AdapterItem> items;

    public WordsAdapter(Context context){
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setItems(ArrayList<AdapterItem> items){
        this.items = items;
    }

    @Override
    public int getCount(){
        return items.size();
    }

    @Override
    public AdapterItem getItem(int position){
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.rowdata, parent, false);
        }
        ((TextView)convertView.findViewById(R.id.NameText)).setText(items.get(position).getName());
        return convertView;
    }
}
