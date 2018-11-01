package com.example.yukinaito.mydictionary.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.yukinaito.mydictionary.R;
import com.example.yukinaito.mydictionary.model.dao.SQLiteApplication;
import com.example.yukinaito.mydictionary.ui.activity.NavigationDrawer;
import com.example.yukinaito.mydictionary.ui.adapter.WordNameAdapter;
import com.example.yukinaito.mydictionary.ui.activity.AddEditWordActivity;
import com.example.yukinaito.mydictionary.ui.activity.DrawInfoActivity;

public class SelectWordFragment extends ListFragment {
    /**
     * 要求コード
     */
    private static final int SHOW_INFO_CODE = 1;
    private static final int ADD_CODE = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_select_word, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.button_floating_action);
        fab.setOnClickListener(new View.OnClickListener() {
            //単語の追加
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(),AddEditWordActivity.class);
                startActivityForResult(intent, ADD_CODE);
            }
        });

        ListView listView = getListView();
        listView.setFastScrollEnabled(true);
        listView.setFastScrollAlwaysVisible(true);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                builder.setTitle("削除").setMessage("選択した単語を削除しますか？");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((SQLiteApplication)getActivity().getApplication()).deleteWord(((WordNameAdapter)getListView().getAdapter()).getItem(position).getId());
                        setAdapter();
                    }
                });
                builder.create().show();
                return true;
            }
        });

        setAdapter();
    }

    /**
     * DBから取得した単語名群を基にAdapterを生成し, ListViewへセットする
     */
    private void setAdapter(){
        SQLiteApplication sqLiteApplication = (SQLiteApplication)getActivity().getApplication();
        Bundle bundle = getArguments();
        if(bundle == null){
            return;
        }
        WordNameAdapter adapter = new WordNameAdapter(getActivity(), sqLiteApplication.getWordName(bundle.getString("FIELD")));
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id){
        Intent intent = new Intent(getActivity().getApplicationContext(),DrawInfoActivity.class);
        intent.putExtra("ID", (String)listView.getAdapter().getItem(position));
        startActivityForResult(intent, SHOW_INFO_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SHOW_INFO_CODE){
            if(resultCode == NavigationDrawer.RESULT_OK) {
                if(data.getBooleanExtra("UPDATE", false)) {
                    setAdapter();
                }
            }
        }else if(requestCode == ADD_CODE){
            if(resultCode == NavigationDrawer.RESULT_OK){
                setAdapter();
            }
        }
    }
}
