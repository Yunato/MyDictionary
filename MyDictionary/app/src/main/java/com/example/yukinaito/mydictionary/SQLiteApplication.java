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
    public String[] getWordClass(){
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

    //テスト用(分類のみ出力) 成功
    public String[] Test_getWordClass(){
        dbAdapter.open();
        Cursor cursor = dbAdapter.getWordClass();
        Log.d("SQLApp getWordClass()","Size " + Integer.toString(cursor.getCount()));

        String[] data = new String[cursor.getCount()];
        int i = 0;
        while(cursor.moveToNext()){
            Log.d("SQLApp getWordClass()",cursor.getString(cursor.getColumnIndex("classification")));
            data[i++] = cursor.getString(cursor.getColumnIndex("classification"));
        }
        cursor.close();
        dbAdapter.close();
        return data;
    }

    //単語選択画面Listの要素作成
    public ArrayList<AdapterItem> getWordName(String wordclass){
        ArrayList<AdapterItem> items = new ArrayList<AdapterItem>();
        dbAdapter.open();
        Cursor cursor = dbAdapter.getWordName(wordclass);

        int i = 0;
        while(cursor.moveToNext()){
            Log.d("SQLApp getWordName()",cursor.getString(cursor.getColumnIndex("name")) + " " + cursor.getString(cursor.getColumnIndex("kana")));
            AdapterItem buf = new AdapterItem(cursor.getString(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("kana")));
            items.add(buf);
        }
        cursor.close();
        dbAdapter.close();
        return items;
    }

    //テスト用(名前と読み方を出力) 成功
    public AdapterItem[] Test_getWordName(String wordclass){
        dbAdapter.open();
        Cursor cursor = dbAdapter.getWordName(wordclass);
        Log.d("SQLApp getWordClass()","Size " + Integer.toString(cursor.getCount()));

        AdapterItem[] data = new AdapterItem[cursor.getCount()];
        int i = 0;
        while(cursor.moveToNext()){
            Log.d("SQLApp getWordName()",cursor.getString(cursor.getColumnIndex("name")) + " " + cursor.getString(cursor.getColumnIndex("kana")));
            AdapterItem buf = new AdapterItem(cursor.getString(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("kana")));
            data[i++] = buf;
        }
        cursor.close();
        dbAdapter.close();
        return data;
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
                cursor.getInt(cursor.getColumnIndex("leastaccesscount")));
        cursor.close();
        dbAdapter.close();
        return word;
    }

    //テスト(単語の情報を出力) 成功
    public void Test_getWordInfo(String id){
        dbAdapter.open();
        Cursor cursor = dbAdapter.getWordInfo(id);

        cursor.moveToNext();
        Log.d("SQLApp getWordName()",cursor.getString(cursor.getColumnIndex("name")) + " " +
                cursor.getString(cursor.getColumnIndex("kana")) + " " +
                cursor.getString(cursor.getColumnIndex("classification")) + " " +
                cursor.getString(cursor.getColumnIndex("mean")) + " " +
                cursor.getInt(cursor.getColumnIndex("accesscount")) + " " +
                cursor.getInt(cursor.getColumnIndex("leastaccesscount")) );
        cursor.close();
        dbAdapter.close();
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
    public void deleteWord(int id){
        dbAdapter.open();
        dbAdapter.deleteWord(id);
        dbAdapter.close();
    }
}
