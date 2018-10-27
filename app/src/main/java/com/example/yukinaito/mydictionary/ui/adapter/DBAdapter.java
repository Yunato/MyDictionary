package com.example.yukinaito.mydictionary.ui.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.example.yukinaito.mydictionary.model.entity.Word;

public class DBAdapter {
    //]static final String DATABASE_NAME = "/storage/53EA-840B/MyDictionary.db";
    private static final String DATABASE_NAME = Environment.getExternalStorageDirectory() + "/MyDictionary.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "words";
    private static final String WORD_ID = "_id";
    private static final String WORD_NAME = "name";
    private static final String WORD_KANA = "kana";
    private static final String WORD_CLASS = "classification";
    private static final String WORD_MEAN = "mean";
    private static final String WORD_COUNT = "accesscount";
    private static final String WORD_LEASTCOUNT = "leastaccesscount";
    private static final String WORD_DATE = "adddate";

    private final DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context context){
        dbHelper = new DatabaseHelper(context);
    }

    public class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
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

    //データベースのオープン
    public void open(){
        db = dbHelper.getWritableDatabase();
    }

    //データベースのクローズ
    public void close(){
        dbHelper.close();
    }

    //分類名の取得
    public Cursor getWordClass(){
        return db.query(true, TABLE_NAME, new String[]{WORD_CLASS}, null, null, null, null, "_id ASC", null);
    }

    public Cursor getWordName(String wordClass){
        return db.query(TABLE_NAME, new String[]{WORD_ID, WORD_NAME, WORD_KANA}, WORD_CLASS + " = ?", new String[]{wordClass}, null, null, "_id ASC");
    }

    //単語情報の取得
    public Cursor getWordInfo(String wordId){
        return db.query(TABLE_NAME, new String[]{WORD_NAME, WORD_KANA, WORD_CLASS, WORD_MEAN, WORD_COUNT, WORD_LEASTCOUNT, WORD_DATE}, WORD_ID + " = ?", new String[]{wordId}, null, null, "_id ASC");
    }

    //行の削除
    public void deleteWord(String id){
        db.delete(TABLE_NAME, WORD_ID + "=" + id, null);
    }

    //行の挿入
    public void saveWord(Word word){
        ContentValues values = new ContentValues();
        values.put(WORD_NAME, word.getName());
        values.put(WORD_KANA, word.getKana());
        values.put(WORD_CLASS, word.getClassification());
        values.put(WORD_MEAN, word.getMean());
        values.put(WORD_COUNT, word.getAccesscount());
        values.put(WORD_LEASTCOUNT, word.getLeastaccesscount());
        values.put(WORD_DATE, word.getDate());
        db.insertOrThrow(TABLE_NAME, null, values);
    }

    //行の更新
    public void updateWord(String wordId, Word word){
        ContentValues values = new ContentValues();
        values.put(WORD_NAME, word.getName());
        values.put(WORD_KANA, word.getKana());
        values.put(WORD_CLASS, word.getClassification());
        values.put(WORD_MEAN, word.getMean());
        values.put(WORD_COUNT, word.getAccesscount());
        values.put(WORD_LEASTCOUNT, word.getLeastaccesscount());
        values.put(WORD_DATE, word.getDate());
        db.update(TABLE_NAME, values, WORD_ID + " = " + wordId, null);
    }

}
