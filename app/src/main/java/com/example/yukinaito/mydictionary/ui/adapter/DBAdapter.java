package com.example.yukinaito.mydictionary.ui.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.example.yukinaito.mydictionary.model.entity.Word;

public class DBAdapter {
    /** SQLite 保存先 */
    private static final String DATABASE_PATH = Environment.getExternalStorageDirectory()
            + "/Android/data/com.example.yukinaito.mydictionary/MyDictionary.db";

    /** SQLite バージョン */
    private static final int DATABASE_VERSION = 1;

    /** SQLite カラム名 */
    private static final String TABLE_NAME = "words";
    private static final String WORD_ID = "_id";
    private static final String WORD_NAME = "name";
    private static final String WORD_KANA = "kana";
    private static final String WORD_CLASS = "classification";
    private static final String WORD_MEAN = "mean";
    private static final String WORD_COUNT = "accesscount";
    private static final String WORD_LEASTCOUNT = "leastaccesscount";
    private static final String WORD_DATE = "adddate";

    /** ヘルパークラス */
    private final DatabaseHelper dbHelper;

    /** SQLiteDB オブジェクト */
    private SQLiteDatabase db;

    /**
     * コンストラクタ
     * @param context context
     */
    public DBAdapter(Context context){
        dbHelper = new DatabaseHelper(context);
    }

    /** ヘルパークラス */
    private class DatabaseHelper extends SQLiteOpenHelper {
        /**
         * コンストラクタ
         * @param context context
         */
        private DatabaseHelper(Context context){
            super(context, DATABASE_PATH, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(
                    "CREATE TABLE " + TABLE_NAME + " ("
                            + WORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + WORD_NAME + " TEXT NOT NULL,"
                            + WORD_KANA + " ,"
                            + WORD_CLASS + " TEXT NOT NULL,"
                            + WORD_MEAN + " TEXT NOT NULL,"
                            + WORD_COUNT + " ,"
                            + WORD_LEASTCOUNT + " ,"
                            + WORD_DATE + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    /**
     * SQLiteDB をオープンする
     * */
    public void open(){
        db = dbHelper.getWritableDatabase();
    }

    /**
     * SQLiteDB をクローズする
     * */
    public void close(){
        dbHelper.close();
    }

    /**
     * SQLiteDB から分野名を検索する
     * @return 検索結果のCursor
     * */
    public Cursor getWordField(){
        return db.query(true, TABLE_NAME, new String[]{WORD_CLASS}, null, null, null, null, "_id ASC", null);
    }

    /**
     * SQLiteDB から単語名を取得する
     * @return 検索結果のCursor
     * */
    public Cursor getWordName(){
        return db.query(TABLE_NAME,new String[]{WORD_ID, WORD_NAME, WORD_KANA},null,null,null,null,"_id ASC", null);
    }

    /**
     * SQLiteDB から単語名を検索する
     * @param wordClass 検索対象分野名
     * @return 検索結果のCursor
     * */
    public Cursor getWordName(String wordClass){
        return db.query(TABLE_NAME, new String[]{WORD_ID, WORD_NAME, WORD_KANA, WORD_MEAN}, WORD_CLASS + " = ?", new String[]{wordClass}, null, null, "_id ASC");
    }

    /**
     * SQLiteDB から単語情報を検索する
     * @param wordId 検索対象単語 ID
     * @return 検索結果のCursor
     * */
    public Cursor getWordInfo(String wordId){
        return db.query(TABLE_NAME, new String[]{WORD_NAME, WORD_KANA, WORD_CLASS, WORD_MEAN, WORD_COUNT, WORD_LEASTCOUNT, WORD_DATE}, WORD_ID + " = ?", new String[]{wordId}, null, null, "_id ASC");
    }

    /**
     * SQLiteDB から該当する ID の単語を削除する
     * @param id 削除対象単語 ID
     * */
    public void deleteWord(String id){
        db.delete(TABLE_NAME, WORD_ID + "=" + id, null);
    }

    /**
     * SQLiteDB へ単語を挿入する
     * @param word 挿入対象単語オブジェクト
     * */
    public void saveWord(Word word){
        ContentValues values = new ContentValues();
        values.put(WORD_NAME, word.getName());
        values.put(WORD_KANA, word.getKana());
        values.put(WORD_CLASS, word.getField());
        values.put(WORD_MEAN, word.getMean());
        values.put(WORD_COUNT, word.getAccesscount());
        values.put(WORD_LEASTCOUNT, word.getLeastaccesscount());
        values.put(WORD_DATE, word.getDate());
        db.insertOrThrow(TABLE_NAME, null, values);
    }

    /**
     * SQLiteDB の単語情報を更新する
     * @param wordId 更新対象単語 ID
     * @param word 更新対象単語オブジェクト
     * */
    public void updateWord(String wordId, Word word){
        ContentValues values = new ContentValues();
        values.put(WORD_NAME, word.getName());
        values.put(WORD_KANA, word.getKana());
        values.put(WORD_CLASS, word.getField());
        values.put(WORD_MEAN, word.getMean());
        values.put(WORD_COUNT, word.getAccesscount());
        values.put(WORD_LEASTCOUNT, word.getLeastaccesscount());
        values.put(WORD_DATE, word.getDate());
        db.update(TABLE_NAME, values, WORD_ID + " = " + wordId, null);
    }

}
