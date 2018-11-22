package com.example.yukinaito.mydictionary.model.dao;

import android.app.Application;
import android.database.Cursor;

import com.example.yukinaito.mydictionary.ui.adapter.DBAdapter;
import com.example.yukinaito.mydictionary.model.item.WordNameAdapterItem;
import com.example.yukinaito.mydictionary.model.entity.Word;
import com.example.yukinaito.mydictionary.model.entity.WordComparator;

import java.util.ArrayList;
import java.util.Collections;

public class SQLiteApplication extends Application {
    /** DB アダプター */
    private DBAdapter dbAdapter;

    @Override
    public void onCreate(){
        super.onCreate();
        dbAdapter = new DBAdapter(this);
    }

    /**
     * SQLite に登録済みの単語から分野を取得する
     * @return 分野を格納した配列
     * */
    public String[] getWordFiled(){
        dbAdapter.open();

        Cursor cursor = dbAdapter.getWordClass();
        String[] filedItems = new String[cursor.getCount()];
        int index = 0;
        while(cursor.moveToNext()) {
            filedItems[index++] = cursor.getString(cursor.getColumnIndex("classification"));
        }
        cursor.close();

        dbAdapter.close();
        return filedItems;
    }

    /**
     * SQLite に登録済みの単語から単語名を取得する
     * @param wordClass 抽出したい分野名
     * @return 単語名を格納したリスト
     * */
    public ArrayList<WordNameAdapterItem> getWordNamesList(String wordClass){
        return getWordNames(wordClass, true);
    }

    /**
     * SQLite に登録済みの検索対象である単語から単語名を取得する
     * @param wordClass 抽出したい分野名
     * @return 検索対象の単語名を格納したリスト
     * */
    public ArrayList<WordNameAdapterItem> getResearchesList(String wordClass){
        return getWordNames(wordClass, false);
    }

    /**
     * SQLite に登録済みの検索対象である単語から単語名を取得する
     * @param wordClass 抽出したい分野名
     * @param flag 格納方式フラグ. true ならば登録済み単語名を, false なら検索対象単語名を抽出する
     * @return 単語名を格納したリスト
     * */
    public ArrayList<WordNameAdapterItem> getWordNames(String wordClass, boolean flag){
        dbAdapter.open();

        Cursor cursor = dbAdapter.getWordName(wordClass);
        ArrayList<WordNameAdapterItem> wordNameItems = new ArrayList<>();
        while(cursor.moveToNext()){
            if((flag && !cursor.getString(cursor.getColumnIndex("mean")).equals("")) ||
                    (!flag && cursor.getString(cursor.getColumnIndex("mean")).equals(""))) {
                WordNameAdapterItem addItem = new WordNameAdapterItem(
                        cursor.getString(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        true);
                wordNameItems.add(addItem);
            }
        }
        Collections.sort(wordNameItems, new WordComparator());
        cursor.close();

        dbAdapter.close();
        return wordNameItems;
    }

    /**
     * SQLite から ID で指定された登録済みの単語を取得する
     * @param id 取得したい単語の ID
     * @return ID に対応した登録済みの情報から生成される Word オブジェクト
     * */
    public Word getWordInfo(String id){
        dbAdapter.open();

        Cursor cursor = dbAdapter.getWordInfo(id);
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

        dbAdapter.close();
        return word;
    }

    /**
     * SQLite へ Word を保存する
     * @param word SQLite へ登録する Word オブジェクト
     * */
    public void saveWord(Word word){
        dbAdapter.open();
        dbAdapter.saveWord(word);
        dbAdapter.close();
    }

    /**
     * SQLite へ Word を更新する
     * @param wordId SQLite 内の更新するデータに対応した ID
     * @param word SQLite へ更新する Word オブジェクト
     * */
    public void updateWord(String wordId, Word word){
        dbAdapter.open();
        dbAdapter.updateWord(wordId, word);
        dbAdapter.close();
    }

    /**
     * SQLite へ Word を削除する
     * @param id SQLite 内の削除するデータに対応した ID
     * */
    public void deleteWord(String id){
        dbAdapter.open();
        dbAdapter.deleteWord(id);
        dbAdapter.close();
    }
}
