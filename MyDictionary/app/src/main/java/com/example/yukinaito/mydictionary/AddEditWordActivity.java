package com.example.yukinaito.mydictionary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class AddEditWordActivity extends AppCompatActivity {
    private static final int EDIT_CODE = 1;
    //"単語の意味を入力"を意味とするか判定 trueは許可
    private static boolean meancheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_word);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0f);

        SQLiteApplication sqLiteApplication = (SQLiteApplication)this.getApplication();

        //スピナーの要素設定
        Spinner spinner = (Spinner)findViewById(R.id.input_class);
        ArrayAdapter<String> adapter = sqLiteApplication.getWordClass(this, R.layout.rowdata);
        spinner.setAdapter(adapter);

        final TextView textView = (TextView)findViewById(R.id.input_mean);
        textView.setClickable(true);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //新規作成か編集か
                String send = new String();
                if(textView.getText().toString().equals("単語の意味を入力")) {
                    //正しい文字列ではない(許可していない)とき
                    if (!meancheck)
                        send = "";
                }
                else
                    send = textView.getText().toString();

                Intent intent = new Intent(getApplicationContext(),EditActivity.class);
                intent.putExtra("mean", send);
                startActivityForResult(intent, EDIT_CODE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EDIT_CODE){
            if(resultCode == RESULT_OK) {
                //返された文字列のチェック
                String send = new String();
                if(data.getStringExtra("mean").equals("")) {
                    //正しい文字列ではない(許可しない)
                    meancheck = false;
                    send = "単語の意味を入力";
                }
                else {
                    meancheck = true;
                    send = data.getStringExtra("mean");
                }

                TextView textView = (TextView) findViewById(R.id.input_mean);
                textView.setText(send);
            }
        }
    }
}
