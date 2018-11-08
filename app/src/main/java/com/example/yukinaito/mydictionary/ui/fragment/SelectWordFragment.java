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
import com.example.yukinaito.mydictionary.model.item.AdapterItem;
import com.example.yukinaito.mydictionary.ui.activity.NavigationDrawer;
import com.example.yukinaito.mydictionary.ui.adapter.WordNameAdapter;
import com.example.yukinaito.mydictionary.ui.activity.EditWordInfoActivity;
import com.example.yukinaito.mydictionary.ui.activity.DrawWordInfoActivity;

public class SelectWordFragment extends ListFragment {
    /** 要求コード  */
    private static final int REQUEST_DRAW_INFO = 1;
    private static final int REQUEST_ADD = 2;

    /** 識別子 */
    public static final String EXTRA_STRING_DATA_ID = "com.example.yukinaito.mydictionary.ui.fragment.EXTRA_STRING_DATA_ID";
    public static final String EXTRA_BOOLEAN_UPDATE = "com.example.yukinaito.mydictionary.ui.fragment.EXTRA_BOOLEAN_UPDATE";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_select_word, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        setupUIElements(view);
        setAdapter();
    }

    /**
     * フラグメント上の User Interface Elements を設定を行う
     * @param view onCreateView() メソッドにより生成された View
     */
    private void setupUIElements(View view){
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.button_floating_action);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(),EditWordInfoActivity.class);
                startActivityForResult(intent, REQUEST_ADD);
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
                        if(getListAdapter().getCount() == 0){
                            getFragmentManager().popBackStack();
                        }
                    }
                });
                builder.setNegativeButton("CANCEL", null);
                builder.create().show();
                return true;
            }
        });
    }

    /** DB から取得した単語名群を基に Adapter を生成し, ListView へセットする */
    private void setAdapter(){
        SQLiteApplication sqLiteApplication = (SQLiteApplication)getActivity().getApplication();
        Bundle bundle = getArguments();
        if(bundle == null){
            return;
        }
        WordNameAdapter adapter = new WordNameAdapter(getActivity(), sqLiteApplication.getWordName(bundle.getString(SelectFieldFragment.EXTRA_STRING_FIELD)));
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id){
        AdapterItem item = (AdapterItem)listView.getAdapter().getItem(position);
        if(!item.getVisible()){
            return;
        }
        Intent intent = new Intent(getActivity().getApplicationContext(),DrawWordInfoActivity.class);
        intent.putExtra(EXTRA_STRING_DATA_ID, item.getId());
        startActivityForResult(intent, REQUEST_DRAW_INFO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_DRAW_INFO){
            if(resultCode == NavigationDrawer.RESULT_OK) {
                if(data.getBooleanExtra(EXTRA_BOOLEAN_UPDATE, false)) {
                    setAdapter();
                }
            }
        }else if(requestCode == REQUEST_ADD){
            if(resultCode == NavigationDrawer.RESULT_OK){
                setAdapter();
            }
        }
    }
}
