package com.example.yukinaito.mydictionary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.EditText;
import android.view.MenuItem;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        EditText editText = (EditText)findViewById(R.id.input_mean);
        editText.setText(getIntent().getStringExtra("mean"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //actionbarのカスタマイズ
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editmean_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.finish_action) {
            //入力した文字列のチェック
            String send = new String();
            send = ((EditText)findViewById(R.id.input_mean)).getText().toString();

            //戻り値の生成 mean=単語の意味
            Intent intent = new Intent();
            intent.putExtra("mean", send);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
