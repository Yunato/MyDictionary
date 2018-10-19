package com.example.yukinaito.mydictionary.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.yukinaito.mydictionary.R;
import com.example.yukinaito.mydictionary.model.dao.SQLiteApplication;
import com.example.yukinaito.mydictionary.ui.adapter.WordNameAdapter;
import com.example.yukinaito.mydictionary.ui.activity.AddEditWordActivity;
import com.example.yukinaito.mydictionary.ui.activity.DrawInfoActivity;
import com.example.yukinaito.mydictionary.model.item.AdapterItem;

import java.util.ArrayList;

public class SelectWordFragment extends ListFragment {
    private static final int DRAW_CODE = 1;
    private static final int ADD_CODE = 2;
    private static ArrayList<AdapterItem> items;
    //更新されたか判定 true=更新済み
    private boolean update_check = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_select_word, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        ListView listView = getListView();
        listView.setFastScrollEnabled(true);
        listView.setFastScrollAlwaysVisible(true);

        //タップ時
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Drawable backColor = view.getBackground();
                if(backColor instanceof ColorDrawable && (((ColorDrawable)backColor).getColor() != Color.parseColor("#ffffff")))
                    return;
                Intent intent = new Intent(getActivity().getApplicationContext(),DrawInfoActivity.class);
                intent.putExtra("ID", items.get(position).getId());
                startActivityForResult(intent, DRAW_CODE);
            }
        });
        //長押し時
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int delete_pos = position;
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                builder.setTitle("削除");
                builder.setMessage("選択された単語を削除しますか？");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((SQLiteApplication)getActivity().getApplication()).deleteWord(items.get(delete_pos).getId());
                        update_check = true;
                        DBAccess();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                android.support.v7.app.AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });

        //要素生成
        DBAccess();

        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            //単語の追加
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(),AddEditWordActivity.class);
                startActivityForResult(intent, ADD_CODE);
            }
        });
    }


    //DBへアクセスする 表示内容の更新
    public void DBAccess(){
        SQLiteApplication sqLiteApplication = (SQLiteApplication)getActivity().getApplication();
        ListView listView = getListView();

        //リストビューの要素生成
        //itemsの作成(セクションは抜き)
        items = sqLiteApplication.getWordName(getActivity().getIntent().getStringExtra("CLASS"));
        WordNameAdapter adapter = new WordNameAdapter(getActivity(), items);
        //itemsの再作成(セクションは有)
        items = adapter.getItems();
        listView.setAdapter(adapter);
    }
/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            //戻るボタンを押されたときの処理
            Intent intent = new Intent();
            intent.putExtra("update", update_check);
            setResult(getActivity().RESULT_OK, intent);
            getActivity().finish();
        }
        return super.onKeyDown(keyCode, event);
    }
*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == DRAW_CODE){
            //DrawInfoActivityより
            if(resultCode == getActivity().RESULT_OK) {
                if(data.getBooleanExtra("update", false)) {
                    update_check = true;
                    DBAccess();
                }
            }
        }else if(requestCode == ADD_CODE){
            //AddEditWordActivityより
            if(resultCode == getActivity().RESULT_OK){
                update_check = true;
                DBAccess();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //region 前画面に戻るボタンタップ
            getActivity().finish();
            //endregion
        }
        return super.onOptionsItemSelected(item);
    }
}
