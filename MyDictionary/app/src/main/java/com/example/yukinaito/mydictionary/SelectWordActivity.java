package com.example.yukinaito.mydictionary;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class SelectWordActivity extends AppCompatActivity {
    private static final int DRAW_CODE = 1;
    private static final int ADD_CODE = 2;
    private static ArrayList<AdapterItem> items;
    //更新されたか判定 true=更新済み
    private boolean update_check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_word);

        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setFastScrollEnabled(true);
        listView.setFastScrollAlwaysVisible(true);
        //タップ時
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),DrawInfoActivity.class);
                intent.putExtra("ID", items.get(position).getId());
                startActivityForResult(intent, DRAW_CODE);
            }
        });
        //長押し時
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int delete_pos = position;
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(SelectWordActivity.this);
                builder.setTitle("削除");
                builder.setMessage("選択された単語を削除しますか？");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((SQLiteApplication)SelectWordActivity.this.getApplication()).deleteWord(items.get(delete_pos).getId());
                        update_check = true;
                        DBAccess();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                android.support.v7.app.AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });


        //要素生成
        DBAccess();

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
        //itemsの作成(セクションは抜き)
        items = sqLiteApplication.getWordName(getIntent().getStringExtra("CLASS"));
        WordNameAdapter adapter = new WordNameAdapter(this, items);
        //itemsの再作成(セクションは有)
        items = adapter.getItems();
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            //戻るボタンを押されたときの処理
            Intent intent = new Intent();
            intent.putExtra("update", update_check);
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == DRAW_CODE){
            //DrawInfoActivityより
            if(resultCode == RESULT_OK) {
                if(data.getBooleanExtra("update", false)) {
                    update_check = true;
                    DBAccess();
                }
            }
        }else if(requestCode == ADD_CODE){
            //AddEditWordActivityより
            if(resultCode == RESULT_OK){
                update_check = true;
                DBAccess();
            }
        }
    }
}
