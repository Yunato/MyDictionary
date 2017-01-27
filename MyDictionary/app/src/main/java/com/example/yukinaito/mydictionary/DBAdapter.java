package com.example.yukinaito.mydictionary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class DBAdapter {
    static final String DATABASE_NAME = Environment.getExternalStorageDirectory() + "/sample.db";
    static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "words";
    public static final String WORD_ID = "_id";
    public static final String WORD_NAME = "name";
    public static final String WORD_KANA = "kana";
    public static final String WORD_CLASS = "classification";
    public static final String WORD_MEAN = "mean";
    public static final String WORD_COUNT = "accesscount";
    public static final String WORD_LEASTCOUNT = "leastaccesscount";
    public static final String WORD_DATE = "adddate";

    protected final Context context;
    protected DatabaseHelper dbHelper;
    protected SQLiteDatabase db;

    public DBAdapter(Context context){
        this.context = context;
        dbHelper = new DatabaseHelper(this.context);
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
    public DBAdapter open(){
        db = dbHelper.getWritableDatabase();
        return this;
    }

    //データベースのクローズ
    public void close(){
        dbHelper.close();
    }

    //分類名の取得
    public Cursor getWordClass(){
        return db.query(true, TABLE_NAME, new String[]{WORD_CLASS}, null, null, null, null, "_id ASC", null);
    }

    //単語名の取得
    public Cursor getWordName(String wordClass){
        return db.query(TABLE_NAME, new String[]{WORD_ID, WORD_NAME, WORD_KANA}, WORD_CLASS + " = ?", new String[]{wordClass}, null, null, "_id ASC");
    }

    //単語情報の取得
    public Cursor getWordInfo(String wordId){
        return db.query(TABLE_NAME, new String[]{WORD_NAME, WORD_KANA, WORD_CLASS, WORD_MEAN, WORD_COUNT, WORD_LEASTCOUNT, WORD_DATE}, WORD_ID + " = ?", new String[]{wordId}, null, null, "_id ASC");
    }

    //行の削除
    public boolean deleteWord(String id){
        return db.delete(TABLE_NAME, WORD_ID + "=" + id, null) > 0;
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
