package com.example.yukinaito.mydictionary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class WordNameAdapter  extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<AdapterItem> items;

    public WordNameAdapter(Context context, ArrayList<AdapterItem> objects){
        this.inflater = LayoutInflater.from(context);
        this.items = objects;
    }

    //要素数の取得
    @Override
    public int getCount() {
        return this.items.size();
    }

    //要素の取得
    @Override
    public AdapterItem getItem(int position) {
        return this.items.get(position);
    }

    //要素IDの取得
    @Override
    public long getItemId(int position) {
        return position;
    }

    //セルのビューの生成
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AdapterItem item = this.items.get(position);

        //レイアウトの生成
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.layout_wordname_item, null);
        }

        //値の指定
        TextView name = (TextView)convertView.findViewById(R.id.textView_Name);
        TextView kana = (TextView)convertView.findViewById(R.id.textView_Kana);
        name.setText(item.getName());
        kana.setText(item.getKana());
        return convertView;
    }
}
