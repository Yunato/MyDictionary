package com.example.yukinaito.mydictionary.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.yukinaito.mydictionary.R;
import com.example.yukinaito.mydictionary.model.item.AdapterItem;

import java.util.ArrayList;
import java.util.Locale;

public class WordNameAdapter  extends BaseAdapter implements SectionIndexer{
    private final LayoutInflater inflater;
    private final ArrayList<AdapterItem> wordNameItems;
    private String[] wordLabels;

    /**
     * コンストラクタ
     * @param context context
     * @param wordList ある分野に該当する単語名リスト
     */
    public WordNameAdapter(Context context, ArrayList<AdapterItem> wordList){
        this.inflater = LayoutInflater.from(context);
        this.wordNameItems = new ArrayList<>();
        setupLabel(wordList);
    }

    /**
     * リストに差し込むラベルの生成を行う
     * @param wordList ある分野に該当する単語名リスト
     */
    private void setupLabel(ArrayList<AdapterItem> wordList){
        ArrayList<String> labelList = new ArrayList<>();
        String oldLabelName = null;
        for (AdapterItem item : wordList) {
            String str = item.getName();
            String newLabelName = str.substring(0, 1).toUpperCase(Locale.ENGLISH);
            if(oldLabelName == null || !oldLabelName.equals(newLabelName)){
                labelList.add(newLabelName);
                wordNameItems.add(new AdapterItem("", newLabelName, "", false));
                oldLabelName = newLabelName;
            }
            wordNameItems.add(item);
        }
        wordLabels = labelList.toArray(new String[] {});
    }

    private static class ViewHolder{
        TextView textName;
    }

    //要素数の取得
    @Override
    public int getCount() {
        return this.wordNameItems.size();
    }

    //要素の取得
    @Override
    public AdapterItem getItem(int position) {
        return this.wordNameItems.get(position);
    }

    //要素IDの取得
    @Override
    public long getItemId(int position) {
        return position;
    }

    //セルのビューの生成
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        AdapterItem item = this.wordNameItems.get(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.list_word_name_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textName = (TextView) convertView.findViewById(R.id.textView_Name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textName.setText(item.getName());

        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.textView_Layout);
        if (item.getVisible()) {
            layout.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            layout.setBackgroundColor(Color.parseColor("#26A69A"));

        }
        return convertView;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return sectionIndex;
    }

    @Override
    public int getSectionForPosition(int position) {
        return position;
    }

    @Override
    public Object[] getSections() {
        return wordLabels;
    }
}
