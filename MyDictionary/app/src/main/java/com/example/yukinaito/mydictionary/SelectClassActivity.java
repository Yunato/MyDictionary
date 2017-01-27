package com.example.yukinaito.mydictionary;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;

public class SelectClassActivity extends AppCompatActivity {
    private static final int VIEW_CODE = 1;
    private static final int ADD_CODE = 2;
    private static final int REQUEST_WRITE_STORAGE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_class);

        boolean hasPermission = (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if(!hasPermission){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
        }else
            DBAccess();

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            //単語の追加
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddEditWordActivity.class);
                startActivityForResult(intent, ADD_CODE);
            }
        });
    }

    //DBへアクセスする 表示内容の更新
    public void DBAccess(){
        SQLiteApplication sqLiteApplication = (SQLiteApplication)this.getApplication();
        ListView listView = (ListView)findViewById(R.id.listView);

        final String[] items = sqLiteApplication.getWordClass();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.rowdata, items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),SelectWordActivity.class);
                intent.putExtra("CLASS", items[position]);
                startActivityForResult(intent, VIEW_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if(requestCode == VIEW_CODE){
                if(data.getBooleanExtra("update", false))
                    DBAccess();
            }else if(requestCode == ADD_CODE)
                //AddEditWordActivityより
                if(resultCode == RESULT_OK)
                    DBAccess();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_WRITE_STORAGE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                DBAccess();
        }
    }
}
