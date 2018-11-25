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
    /** 識別子 */
    public static final String EXTRA_STRING_MEAN = "com.example.yukinaito.mydictionary.ui.activity.EXTRA_STRING_MEAN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_word_mean);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        setupUIElements();
    }

    /** アクティビティ上の User Interface Elements を設定を行う */
    private void setupUIElements(){
        EditText editText = (EditText)findViewById(R.id.input_mean);
        editText.setText(getIntent().getStringExtra(EditWordInfoActivity.EXTRA_STRING_MEAN));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit_mean, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.action_finish) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_STRING_MEAN, ((EditText)findViewById(R.id.input_mean)).getText().toString());
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
