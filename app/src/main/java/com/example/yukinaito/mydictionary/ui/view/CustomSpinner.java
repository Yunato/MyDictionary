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

    /**
     * コンストラクタ
     * @param context context
     */
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

    /**
     * Spinner の選択されているアイテムを設定する
     * @param position インデックス
     * @param animate アイテムの選択方法
     */
    @Override
    public void setSelection(int position, boolean animate){
        boolean sameSelected = position == getSelectedItemPosition();
        super.setSelection(position, animate);
        if(sameSelected && getOnItemSelectedListener() != null){
            getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
        }
    }

    /**
     * Spinner の選択されているアイテムを設定する
     * @param position インデックス
     */
    @Override
    public void setSelection(int position){
        boolean sameSelected = position == getSelectedItemPosition();
        super.setSelection(position);
        if(sameSelected && getOnItemSelectedListener() != null){
            getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
        }
    }

    /**
     * Spinner にアイテム配列で生成した Adapter を設定する
     * @param addItems アイテム配列
     */
    public void setAdapter(String[] addItems){
        for (String item: addItems) {
            items.add(1, item);
        }
        adapter = new ArrayAdapter<>(context, R.layout.spinner_field_item, items);
        this.setAdapter(adapter);
    }

    /**
     * Spinner のアイテム数を取得する
     * @return アイテム数
     */
    public int getItemSize(){
        return items.size();
    }

    /**
     * Spinner が str を選択している状態にし, そのインデックスを返す
     * @param str 選択対象文字列
     * @return  str のアイテムリストの該当インデックス. str がアイテムリストに存在しない場合, -1 を返す.
     */
    public int setSelection(String str){
        for(int index = 1; index < items.size(); index++) {
            if (items.get(index).equals(str)) {
                this.setSelection(index);
                return index;
            }
        }
        return -1;
    }

    /**
     * Spinner の持つアイテム群に str が含まれていないかを得る
     * @param str 選択対象文字列
     * @return 含まれていなければ true. 含まれていれば false.
     */
    public boolean isNotMatchItem(String str){
        for(int index = 1; index < items.size(); index++) {
            if (items.get(index).equals(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Spinner のアイテム群に str を追加する
     * @param str 選択対象文字列
     */
    public void addItem(String str){
        items.add(items.size() - 2, str);
        adapter.notifyDataSetChanged();
    }
}
