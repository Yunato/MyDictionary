package com.example.yukinaito.mydictionary.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.WindowManager;
import android.widget.EditText;
import android.view.MenuItem;

import com.example.yukinaito.mydictionary.R;

public class EditWordMeanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_word_mean);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        EditText editText = (EditText)findViewById(R.id.input_field);
        editText.setText(getIntent().getStringExtra("mean"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //actionbarのカスタマイズ
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit_mean, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.action_finish) {
            //入力した文字列のチェック
            String send;
            send = ((EditText)findViewById(R.id.input_field)).getText().toString();

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
