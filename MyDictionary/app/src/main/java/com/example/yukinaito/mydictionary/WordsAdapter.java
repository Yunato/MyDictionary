package com.example.yukinaito.mydictionary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class WordsAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<AdapterItem> items;

    public WordsAdapter(Context context){
        this.context = context;
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
        convertView = layoutInflater.inflate(R.layout.rowdata,parent,false);

        ((TextView)convertView.findViewById(R.id.NameText)).setText(items.get(position).getName());
        return convertView;
    }
}
