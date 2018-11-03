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
    /** 要求コード  */
    public static final int REQUEST_UPDATE = 1;
    public static final int REQUEST_ADD = 2;

    /** 識別子 */
    public static final String EXTRA_STRING_FIELD = "com.example.yukinaito.mydictionary.ui.fragment.EXTRA_STRING_FIELD";

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
                startActivityForResult(intent, REQUEST_ADD);
            }
        });

        setAdapter();
    }

    /** DB から取得した分野群を基に Adapter を生成し, ListView へセットする */
    private void setAdapter(){
        SQLiteApplication sqLiteApplication = (SQLiteApplication)getActivity().getApplication();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_field_item, sqLiteApplication.getWordFiled());
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id){
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_STRING_FIELD, (String)listView.getAdapter().getItem(position));

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
            if(requestCode == REQUEST_UPDATE){
                if(data.getBooleanExtra("UPDATE", false))
                    setAdapter();
            }else if(requestCode == REQUEST_ADD)
                setAdapter();
        }
    }
}
