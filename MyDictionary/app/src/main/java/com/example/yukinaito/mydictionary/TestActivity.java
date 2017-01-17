package com.example.yukinaito.mydictionary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TestActivity extends AppCompatActivity {
    private SQLiteApplication sqLiteApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        sqLiteApplication = (SQLiteApplication)this.getApplication();

        //テスト 新規単語の挿入
        ((Button)findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Word word = new Word("", "", "" , "", 0, 0);
                sqLiteApplication.saveWord(word);
            }
        });

        //テスト 分類名の取得
        ((Button)findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sqLiteApplication.getWordClass();
            }
        });
    }
}
