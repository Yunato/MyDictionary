package com.example.yukinaito.mydictionary;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SelectWordActivity extends AppCompatActivity {
    private static final int DRAW_CODE = 1;
    private static final int ADD_CODE = 2;
    private static ArrayList<AdapterItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_word);

        ListView listView = (ListView)findViewById(R.id.listView);
        //要素生成
        DBAccess();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),DrawInfoActivity.class);
                intent.putExtra("ID", items.get(position).getId());
                startActivityForResult(intent, DRAW_CODE);
            }
        });

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            //単語の追加
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddEditWordActivity.class);
                startActivityForResult(intent, ADD_CODE);
            }
        });
    }

    //DBへアクセスする 表示内容の更新
    public void DBAccess(){
        SQLiteApplication sqLiteApplication = (SQLiteApplication)this.getApplication();
        ListView listView = (ListView)findViewById(R.id.listView);

        //リストビューの要素生成
        items = sqLiteApplication.getWordName(getIntent().getStringExtra("CLASS"));
        WordNameAdapter adapter = new WordNameAdapter(this, items);
        listView.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == DRAW_CODE){
            //DrawInfoActivityより
            if(resultCode == RESULT_OK) {
                if(data.getBooleanExtra("update", false)) {
                    DBAccess();
                }
            }
        }else if(requestCode == ADD_CODE){
            //AddEditWordActivityより
            if(resultCode == RESULT_OK){
                DBAccess();
            }
        }
    }
}
