package com.example.yukinaito.mydictionary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class TestActivity extends AppCompatActivity {
    private SQLiteApplication sqLiteApplication;
    private String[] data;
    private AdapterItem[] words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        sqLiteApplication = (SQLiteApplication)this.getApplication();

        //テスト 分類名の取得
        ((Button)findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                data = sqLiteApplication.Test_getWordClass();
                for(int i = 0; i < data.length; i ++)
                    Log.d("TEST getWordClass()出力結果", data[i]);
            }
        });

        //テスト 分類名を送信し、単語の名前と読み方を取得
        ((Button)findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                for(int j = 0 ; j < data.length; j++) {
                    Log.d("TEST getWordName()送信", data[j]);
                    words = sqLiteApplication.Test_getWordName(data[j]);
                    for (int i = 0; i < words.length; i++)
                        Log.d("TEST getWordName()出力結果", words[i].getName() + " " + words[i].getKana());
                }
            }
        });

        //テスト 単語の名前と読み方を送信し、単語の情報を取得
        ((Button)findViewById(R.id.button4)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });

        //テスト 新規単語の挿入
        ((Button)findViewById(R.id.button6)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //単語の名前、読み方、分類、意味、アクセス数、どのくらいアクセスされていないか
                Word word = new Word("", "" , "", "", 0, 0);
                sqLiteApplication.saveWord(word);
            }
        });

        //テスト 新規単語の挿入
        ((Button)findViewById(R.id.button7)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });
    }
}
