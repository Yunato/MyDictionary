package com.example.yukinaito.mydictionary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;

public class SelectClassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_class);

        SQLiteApplication sqLiteApplication = (SQLiteApplication)this.getApplication();
        ListView listView = (ListView)findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.rowdata, sqLiteApplication.getWordClass());
        listView.setAdapter(adapter);
    }
}
