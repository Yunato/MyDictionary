package com.example.yukinaito.mydictionary.model.dao;

import android.app.Application;
import android.database.Cursor;

import com.example.yukinaito.mydictionary.ui.adapter.WordDBAdapter;
import com.example.yukinaito.mydictionary.model.item.WordNameAdapterItem;
import com.example.yukinaito.mydictionary.model.entity.Word;
import com.example.yukinaito.mydictionary.model.entity.WordComparator;

import java.util.ArrayList;
import java.util.Collections;

public class SQLiteApplication extends Application {
    /** DB アダプター */
    private WordDBAdapter wordDbAdapter;

    @Override
    public void onCreate(){
        super.onCreate();
        wordDbAdapter = new WordDBAdapter(this);
    }

    /**
     * SQLite に登録済みの単語から分野を取得する
     * @return 分野を格納した配列
     * */
    public String[] getWordFiled(){
        wordDbAdapter.open();

        Cursor cursor = wordDbAdapter.getWordClass();
        String[] filedItems = new String[cursor.getCount()];
        int index = 0;
        while(cursor.moveToNext()) {
            filedItems[index++] = cursor.getString(cursor.getColumnIndex("classification"));
        }
        cursor.close();

        wordDbAdapter.close();
        return filedItems;
    }

    /**
     * SQLite に登録済みの単語から単語名を取得する
     * @return 単語名を格納したリスト
     * */
    public ArrayList<WordNameAdapterItem> getWordName(String wordClass){
        wordDbAdapter.open();

        Cursor cursor = wordDbAdapter.getWordName(wordClass);
        ArrayList<WordNameAdapterItem> wordNameItems = new ArrayList<>();
        while(cursor.moveToNext()){
            WordNameAdapterItem addItem = new WordNameAdapterItem(
                    cursor.getString(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    true);
            wordNameItems.add(addItem);
        }
        Collections.sort(wordNameItems, new WordComparator());
        cursor.close();

        wordDbAdapter.close();
        return wordNameItems;
    }

    /**
     * SQLite から ID で指定された登録済みの単語を取得する
     * @param id 取得したい単語の ID
     * @return ID に対応した登録済みの情報から生成される Word オブジェクト
     * */
    public Word getWordInfo(String id){
        wordDbAdapter.open();

        Cursor cursor = wordDbAdapter.getWordInfo(id);
        cursor.moveToNext();
        Word word = new Word(
                cursor.getString(cursor.getColumnIndex("name")),
                cursor.getString(cursor.getColumnIndex("kana")),
                cursor.getString(cursor.getColumnIndex("classification")),
                cursor.getString(cursor.getColumnIndex("mean")),
                cursor.getInt(cursor.getColumnIndex("accesscount")),
                cursor.getInt(cursor.getColumnIndex("leastaccesscount")),
                cursor.getInt(cursor.getColumnIndex("adddate")));
        cursor.close();

        wordDbAdapter.close();
        return word;
    }

    /**
     * SQLite へ Word を保存する
     * @param word SQLite へ登録する Word オブジェクト
     * */
    public void saveWord(Word word){
        wordDbAdapter.open();
        wordDbAdapter.saveWord(word);
        wordDbAdapter.close();
    }

    /**
     * SQLite へ Word を更新する
     * @param wordId SQLite 内の更新するデータに対応した ID
     * @param word SQLite へ更新する Word オブジェクト
     * */
    public void updateWord(String wordId, Word word){
        wordDbAdapter.open();
        wordDbAdapter.updateWord(wordId, word);
        wordDbAdapter.close();
    }

    /**
     * SQLite へ Word を削除する
     * @param id SQLite 内の削除するデータに対応した ID
     * */
    public void deleteWord(String id){
        wordDbAdapter.open();
        wordDbAdapter.deleteWord(id);
        wordDbAdapter.close();
    }
}
