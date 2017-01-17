package com.example.yukinaito.mydictionary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;

public class SelectClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_class);

        SQLiteApplication sqLiteApplication = (SQLiteApplication)this.getApplication();
        ListView listView = (ListView)findViewById(R.id.listView);

        ArrayAdapter<String> adapter = sqLiteApplication.getWordClass(this, R.layout.rowdata);
        listView.setAdapter(adapter);
    }
}
