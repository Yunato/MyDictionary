package com.example.yukinaito.mydictionary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

public class DrawInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_info);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0f);

        SQLiteApplication sqLiteApplication = (SQLiteApplication)this.getApplication();
        Word word = sqLiteApplication.getWordInfo(getIntent().getStringExtra("ID"));

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
}
