package com.example.yukinaito.mydictionary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class AddEditWordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_word);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0f);

        SQLiteApplication sqLiteApplication = (SQLiteApplication)this.getApplication();

        Spinner spinner = (Spinner)findViewById(R.id.input_class);
        ArrayAdapter<String> adapter = sqLiteApplication.getWordClass(this, R.layout.rowdata);
        spinner.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
