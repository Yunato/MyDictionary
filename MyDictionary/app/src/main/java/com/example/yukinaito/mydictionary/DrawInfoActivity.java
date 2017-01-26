package com.example.yukinaito.mydictionary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class DrawInfoActivity extends AppCompatActivity {
    private static final int EDIT_CODE = 1;
    private Word word;
    //更新されたか判定 true=更新済み
    private boolean update_check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_info);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0f);

        DBAccess();
    }

    //DBへアクセスする 表示内容の更新
    public void DBAccess(){
        SQLiteApplication sqLiteApplication = (SQLiteApplication)this.getApplication();
        word = sqLiteApplication.getWordInfo(getIntent().getStringExtra("ID"));

        ((TextView)findViewById(R.id.output_name)).setText(word.getName());
        ((TextView)findViewById(R.id.output_kana)).setText(word.getKana());
        ((TextView)findViewById(R.id.output_class)).setText(word.getClassification());
        ((TextView)findViewById(R.id.output_mean)).setText(word.getMean());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.info_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.edit_action) {
            //値を渡す
            Intent intent = new Intent(getApplicationContext(),AddEditWordActivity.class);
            intent.putExtra("word", word);
            intent.putExtra("ID", getIntent().getStringExtra("ID"));
            startActivityForResult(intent, EDIT_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        if(requestCode == EDIT_CODE){
            if(resultCode == RESULT_OK) {
                update_check = true;
                DBAccess();
            }
        }
    }
}
