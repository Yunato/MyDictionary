package com.example.yukinaito.mydictionary.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.yukinaito.mydictionary.R;

import java.util.ArrayList;
import java.util.List;

public class CustomSpinner extends Spinner {
    final private Context context;
    final private List<String> items = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    public CustomSpinner(Context context){
        super(context);
        this.context = context;
        items.add("分野の選択");
        items.add("分野の追加...");
    }

    public CustomSpinner(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        this.context = context;
        items.add("分野の選択");
        items.add("分野の追加...");
    }

    public CustomSpinner(Context context, AttributeSet attributeSet, int defStyle){
        super(context, attributeSet, defStyle);
        this.context = context;
        items.add("分野の選択");
        items.add("分野の追加...");
    }

    @Override
    public void setSelection(int position, boolean animate){
        boolean sameSelected = position == getSelectedItemPosition();
        super.setSelection(position, animate);
        if(sameSelected && getOnItemSelectedListener() != null){
            getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
        }
    }

    @Override
    public void setSelection(int position){
        boolean sameSelected = position == getSelectedItemPosition();
        super.setSelection(position);
        if(sameSelected && getOnItemSelectedListener() != null){
            getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
        }
    }

    public void setAdapter(String[] addItems){
        for (String item: addItems) {
            items.add(1, item);
        }
        adapter = new ArrayAdapter<>(context, R.layout.spinner_field_item, items);
        this.setAdapter(adapter);
    }

    public int getItemSize(){
        return items.size();
    }

    public int setSelection(String str){
        for(int index = 1; index < items.size(); index++) {
            if (items.get(index).equals(str)) {
                this.setSelection(index);
                return index;
            }
        }
        return -1;
    }

    public boolean isNotMatchItem(String str){
        for(int index = 1; index < items.size(); index++) {
            if (items.get(index).equals(str)) {
                return false;
            }
        }
        return true;
    }

    public void addItem(String str){
        items.add(items.size() - 2, str);
        adapter.notifyDataSetChanged();
    }
}
