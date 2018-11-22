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
import com.example.yukinaito.mydictionary.model.item.WordNameAdapterItem;

import java.util.ArrayList;
import java.util.Locale;

public class WordNameAdapter  extends BaseAdapter implements SectionIndexer{
    /** inflater  */
    private final LayoutInflater inflater;

    /** 単語名 (ラベル名含む) 格納リスト  */
    private final ArrayList<WordNameAdapterItem> wordNameItems;

    /** ラベル名格納配列  */
    private String[] wordLabels;

    /**
     * コンストラクタ
     * @param context context
     * @param wordList ある分野に該当する単語名リスト
     */
    public WordNameAdapter(Context context, ArrayList<WordNameAdapterItem> wordList){
        this.inflater = LayoutInflater.from(context);
        this.wordNameItems = new ArrayList<>();
        setupLabel(wordList);
    }

    /**
     * リストに差し込むラベルの生成を行う
     * @param wordList ある分野に該当する単語名リスト
     */
    private void setupLabel(ArrayList<WordNameAdapterItem> wordList){
        ArrayList<String> labelList = new ArrayList<>();
        String oldLabelName = null;
        for (WordNameAdapterItem item : wordList) {
            String str = item.getName();
            String newLabelName = str.substring(0, 1).toUpperCase(Locale.ENGLISH);
            if(oldLabelName == null || !oldLabelName.equals(newLabelName)){
                labelList.add(newLabelName);
                wordNameItems.add(new WordNameAdapterItem("", newLabelName, false));
                oldLabelName = newLabelName;
            }
            wordNameItems.add(item);
        }
        wordLabels = labelList.toArray(new String[] {});
    }

    /** リストビューで描画される View を保持するクラス  */
    private static class ViewHolder{
        TextView wordNaveView;
    }

    /**
     * 単語名 (ラベル名含む) リストの要素数を取得する
     * @return 単語名 (ラベル名含む) リストの要素数
     */
    @Override
    public int getCount() {
        return this.wordNameItems.size();
    }

    /**
     * 単語名 (ラベル名含む) リストから要素を取得する
     * @param position リストに対する取得したい要素のインデックス
     */
    @Override
    public WordNameAdapterItem getItem(int position) {
        return this.wordNameItems.get(position);
    }

    /**
     * リストの位置に対応するセルのインデックスを取得する
     * @param position リストの位置に対応するセルのインデックス
     * @return リストの位置に対応するセルのインデックス
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * セル (ビュー) を取得する
     * @param position リストの位置に対応するセルのインデックス
     * @param convertView セルのView情報
     * @param parent getView()メソッドで生成されるViewの親となるViewGroup
     * @return セルのView情報
     */
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        WordNameAdapterItem item = this.wordNameItems.get(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.list_word_name_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.wordNaveView = (TextView) convertView.findViewById(R.id.textView_Name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.wordNaveView.setText(item.getName());

        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.textView_Layout);
        if (item.getVisible()) {
            layout.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            layout.setBackgroundColor(Color.parseColor("#26A69A"));
        }
        return convertView;
    }

    /**
     * インデックスラベルに対応するリストビューの位置を取得する
     * @param sectionIndex インデックスラベルに対応するリストビューの位置
     * @return インデックスラベルに対応するリストビューの位置
     */
    @Override
    public int getPositionForSection(int sectionIndex) {
        return sectionIndex;
    }

    /**
     * リストの位置に対応するインデックスラベルのインデックスを取得する
     * @param position リストの位置に対応するインデックスラベルのインデックス
     * @return リストの位置に対応するインデックスラベルのインデックス
     */
    @Override
    public int getSectionForPosition(int position) {
        return position;
    }

    /**
     * ラベル名格納配列を取得する
     * @return ラベル名格納配列
     */
    @Override
    public Object[] getSections() {
        return wordLabels;
    }
}
