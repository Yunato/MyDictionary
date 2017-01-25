package com.example.yukinaito.mydictionary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SelectWordActivity extends AppCompatActivity {
    private static final int DRAW_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_word);

        SQLiteApplication sqLiteApplication = (SQLiteApplication)this.getApplication();
        ListView listView = (ListView)findViewById(R.id.listView);

        //リストビューの要素生成
        final ArrayList<AdapterItem> items = sqLiteApplication.getWordName(getIntent().getStringExtra("CLASS"));
        WordNameAdapter adapter = new WordNameAdapter(this, items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),DrawInfoActivity.class);
                intent.putExtra("ID", items.get(position).getId());
                startActivityForResult(intent, DRAW_CODE);
            }
        });
    }
}
