package com.example.yukinaito.mydictionary.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.yukinaito.mydictionary.R;
import com.example.yukinaito.mydictionary.model.dao.SQLiteApplication;
import com.example.yukinaito.mydictionary.model.entity.Word;
import com.example.yukinaito.mydictionary.ui.fragment.SelectWordFragment;

public class DrawWordInfoActivity extends AppCompatActivity {
    /** 要求コード  */
    private static final int REQUEST_EDIT = 1;

    /** 変更有無フラグ  */
    private boolean updateFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_word_info);
        setupUIElements();
        setInformation();
    }

    /** アクティビティ上の User Interface Elements を設定を行う */
    private void setupUIElements(){
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setElevation(0f);
            actionBar.setDisplayHomeAsUpEnabled(true);

            @SuppressLint("PrivateResource")
            final Drawable upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.abc_ic_ab_back_material, null);
            if(upArrow != null) {
                upArrow.setColorFilter(ContextCompat.getColor(this, R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
                actionBar.setHomeAsUpIndicator(upArrow);
            }
        }
    }

    /** DB から取得した単語情報を基に Widgets の設定を行う */
    private void setInformation() {
        SQLiteApplication sqLiteApplication = (SQLiteApplication) this.getApplication();
        final Word word = sqLiteApplication.getWordInfo(getIntent().getStringExtra(SelectWordFragment.EXTRA_STRING_DATA_ID));

        ((TextView) findViewById(R.id.draw_name)).setText(word.getName());
        ((TextView) findViewById(R.id.draw_field)).setText(word.getField());
        ((TextView) findViewById(R.id.draw_mean)).setText(word.getMean());
        if (!word.getKana().equals("")) {
            ((TextView) findViewById(R.id.draw_kana)).setText(word.getKana());
        }else{
            int id = DrawWordInfoActivity.this.getResources().getIdentifier("view_underline_gray", "drawable", DrawWordInfoActivity.this.getPackageName());
            Drawable back = ResourcesCompat.getDrawable(getResources(), id, null);
            (findViewById(R.id.draw_kana)).setBackground(back);
        }
    }

    /** 呼び出し元へ渡す単語情報の変更の有無情報を添え, Activity を終了する */
    private void closeActivity(){
        Intent intent = new Intent();
        intent.putExtra(SelectWordFragment.EXTRA_BOOLEAN_UPDATE, updateFlag);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_draw_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home) {
            closeActivity();
        }else if(id == R.id.edit_action) {
            Intent intent = new Intent(getApplicationContext(), EditWordInfoActivity.class);
            intent.putExtra(SelectWordFragment.EXTRA_STRING_DATA_ID, getIntent().getStringExtra(SelectWordFragment.EXTRA_STRING_DATA_ID));
            startActivityForResult(intent, REQUEST_EDIT);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            closeActivity();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_EDIT && resultCode == RESULT_OK){
            updateFlag = true;
            setInformation();
        }
    }
}
