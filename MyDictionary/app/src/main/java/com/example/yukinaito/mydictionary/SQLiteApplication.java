package com.example.yukinaito.mydictionary;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class SQLiteApplication extends Application {
    static DBAdapter dbAdapter;

    @Override
    public void onCreate(){
        super.onCreate();
        dbAdapter = new DBAdapter(this);
    }

    //分類選択画面Listの要素作成
    public ArrayAdapter getWordClass(Context context, int resource){
        dbAdapter.open();
        Cursor cursor = dbAdapter.getWordClass();

        String[] data = new String[cursor.getCount()];
        int i = 0;
        while(cursor.moveToNext())
            data[i++] = cursor.getString(cursor.getColumnIndex("classification"));
        cursor.close();
        dbAdapter.close();
        return new ArrayAdapter<String>(context, resource, data);
    }

    //テスト用(分類のみ出力) 成功
    public void getWordClass(){
        dbAdapter.open();
        Cursor cursor = dbAdapter.getWordClass();
        Log.d("Size","Size " + Integer.toString(cursor.getCount()));
        while(cursor.moveToNext()){
            Log.d("TEST",cursor.getString(cursor.getColumnIndex("classification")));
        }
        cursor.close();
        dbAdapter.close();
    }

    public ArrayList<AdapterItem> getWordName(){
        ArrayList<AdapterItem> items = new ArrayList<AdapterItem>();
        return items;
    }

    /*
    public Word getWordInfo(String name, String kana, String classification){
        Word word = new Word();
        return word;
    }
    */

    public void saveWord(Word word){
        dbAdapter.open();
        dbAdapter.saveWord(word);
        dbAdapter.close();
    }
}
