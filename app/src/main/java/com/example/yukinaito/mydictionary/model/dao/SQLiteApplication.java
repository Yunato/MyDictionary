package com.example.yukinaito.mydictionary.model.dao;

import android.app.Application;
import android.database.Cursor;
import android.util.Log;

import com.example.yukinaito.mydictionary.ui.adapter.DBAdapter;
import com.example.yukinaito.mydictionary.model.item.WordNameAdapterItem;
import com.example.yukinaito.mydictionary.model.entity.Word;
import com.example.yukinaito.mydictionary.model.entity.WordComparator;

import java.util.ArrayList;
import java.util.Collections;

public class SQLiteApplication extends Application {
    private DBAdapter dbAdapter;

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d("Test","作成");
        dbAdapter = new DBAdapter(this);
    }

    //分類選択画面Listの要素作成
    public String[] getWordFiled(){
        dbAdapter.open();
        Cursor cursor = dbAdapter.getWordClass();

        String[] data = new String[cursor.getCount()];
        int i = 0;
        while(cursor.moveToNext())
            data[i++] = cursor.getString(cursor.getColumnIndex("classification"));
        cursor.close();
        dbAdapter.close();
        return data;
    }

    //単語選択画面Listの要素作成
    public ArrayList<WordNameAdapterItem> getWordName(String wordClass){
        ArrayList<WordNameAdapterItem> items = new ArrayList<>();
        dbAdapter.open();
        Cursor cursor = dbAdapter.getWordName(wordClass);

        while(cursor.moveToNext()){
            WordNameAdapterItem buf = new WordNameAdapterItem(cursor.getString(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    true);
            items.add(buf);
        }
        cursor.close();
        dbAdapter.close();

        Collections.sort(items, new WordComparator());
        return items;
    }

    //単語詳細画面の表示情報作成
    public Word getWordInfo(String id){
        dbAdapter.open();
        Cursor cursor = dbAdapter.getWordInfo(id);

        cursor.moveToNext();
        Word word = new Word(cursor.getString(cursor.getColumnIndex("name")),
                cursor.getString(cursor.getColumnIndex("kana")),
                cursor.getString(cursor.getColumnIndex("classification")),
                cursor.getString(cursor.getColumnIndex("mean")),
                cursor.getInt(cursor.getColumnIndex("accesscount")),
                cursor.getInt(cursor.getColumnIndex("leastaccesscount")),
                cursor.getInt(cursor.getColumnIndex("adddate")));
        cursor.close();
        dbAdapter.close();
        return word;
    }

    public void saveWord(Word word){
        dbAdapter.open();
        dbAdapter.saveWord(word);
        dbAdapter.close();
    }

    public void updateWord(String wordId, Word word){
        dbAdapter.open();
        dbAdapter.updateWord(wordId, word);
        dbAdapter.close();
    }

    //テスト(単語情報をDBから削除)
    public void deleteWord(String id){
        dbAdapter.open();
        dbAdapter.deleteWord(id);
        dbAdapter.close();
    }
}
