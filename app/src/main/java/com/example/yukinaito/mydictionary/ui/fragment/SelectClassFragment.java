package com.example.yukinaito.mydictionary.ui.fragment;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.yukinaito.mydictionary.R;
import com.example.yukinaito.mydictionary.model.dao.SQLiteApplication;
import com.example.yukinaito.mydictionary.ui.activity.AddEditWordActivity;
import com.example.yukinaito.mydictionary.ui.activity.NavigationDrawer;

public class SelectClassFragment extends ListFragment {
    private static final int VIEW_CODE = 1;
    private static final int ADD_CODE = 2;
    //Activityの状態を示す。 true = 分野選択 false = 追加順
    //private boolean CONDITION = true;
    private String[] items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_select_class, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
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
    private void DBAccess(){
        SQLiteApplication sqLiteApplication = (SQLiteApplication)getActivity().getApplication();
        ListView listView = getListView();

        items = sqLiteApplication.getWordClass();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.rowdata, items);
        setListAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                SelectWordFragment fragment = new SelectWordFragment();
                transaction.replace(R.id.main_layout, fragment);
                transaction.commit();
            }
        });
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id){
        Intent intent = new Intent(getActivity().getApplicationContext(), SelectWordFragment.class);
        intent.putExtra("CLASS", items[position]);
        startActivityForResult(intent, VIEW_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == NavigationDrawer.RESULT_OK) {
            if(requestCode == VIEW_CODE){
                if(data.getBooleanExtra("update", false))
                    DBAccess();
            }else if(requestCode == ADD_CODE)
                //AddEditWordActivityより
                DBAccess();
        }
    }
}
