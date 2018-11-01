package com.example.yukinaito.mydictionary.ui.fragment;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.yukinaito.mydictionary.R;
import com.example.yukinaito.mydictionary.model.dao.SQLiteApplication;
import com.example.yukinaito.mydictionary.ui.activity.AddEditWordActivity;
import com.example.yukinaito.mydictionary.ui.activity.NavigationDrawer;

public class SelectFieldFragment extends ListFragment {
    /**
     * 要求コード
     */
    private static final int UPDATE_CODE = 1;
    private static final int ADD_CODE = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_select_field, container, false);
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

        setAdapter();
    }

    /**
     * DBから取得した分野群を基にAdapterを生成し, ListViewへセットする
     */
    private void setAdapter(){
        SQLiteApplication sqLiteApplication = (SQLiteApplication)getActivity().getApplication();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, sqLiteApplication.getWordFiled());
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id){
        Bundle bundle = new Bundle();
        bundle.putString("FIELD", (String)listView.getAdapter().getItem(position));

        Fragment fragment = new SelectWordFragment();
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == NavigationDrawer.RESULT_OK) {
            if(requestCode == UPDATE_CODE){
                if(data.getBooleanExtra("UPDATE", false))
                    setAdapter();
            }else if(requestCode == ADD_CODE)
                setAdapter();
        }
    }
}
