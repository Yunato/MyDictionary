package com.example.yukinaito.mydictionary;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public class WordNameAdapter  extends BaseAdapter implements SectionIndexer{
    private Context context;
    private ArrayList<AdapterItem> items;
    private String[] sections;

    public WordNameAdapter(Context context, ArrayList<AdapterItem> objects){
        this.context = context;
        this.items = new ArrayList<AdapterItem>();
        sectionSetting(objects);
    }

    public void sectionSetting(ArrayList<AdapterItem> objects){
        ArrayList<String> buffer = new ArrayList<String>();
        String oldLabel = null;

        Iterator<AdapterItem> it = objects.iterator();
        while(it.hasNext()){
            AdapterItem buf = it.next();
            String s = buf.getName();
            String newLabel = s.substring(0, 1).toUpperCase(Locale.ENGLISH);

            if(oldLabel == null || !oldLabel.equals(newLabel)){
                buffer.add(new String(newLabel));
                items.add(new AdapterItem("", newLabel, "", false));
            }

            items.add(new AdapterItem(buf.getId(), buf.getName(), buf.getKana(), true));
            oldLabel = newLabel;
        }

        sections = buffer.toArray(new String[] {});
    }

    public ArrayList<AdapterItem> getItems(){
        return this.items;
    }

    //要素数の取得
    @Override
    public int getCount() {
        return this.items.size();
    }

    //要素の取得
    @Override
    public AdapterItem getItem(int position) {
        return this.items.get(position);
    }

    //要素IDの取得
    @Override
    public long getItemId(int position) {
        return position;
    }

    //セルのビューの生成
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AdapterItem item = this.items.get(position);

        //レイアウトの生成
        if (convertView == null) {
            LinearLayout layout = new LinearLayout(context);
            layout.setTag("layout");
            layout.setOrientation(LinearLayout.VERTICAL);
            convertView = layout;

            TextView textView = new TextView(context);
            textView.setTag("name");
            textView.setTextSize(22.0f);
            textView.setPadding(22, 22, 22, 22);
            layout.addView(textView);
        }

        //値の指定
        TextView name = (TextView)convertView.findViewWithTag("name");
        //TextView kana = (TextView)convertView.findViewById(R.id.textView_Kana);
        name.setText(item.getName());
        //kana.setText(item.getKana());

        LinearLayout layout = (LinearLayout)convertView.findViewWithTag("layout");
        if(!item.getVisible())
            layout.setBackgroundColor(Color.parseColor("#26A69A"));
        else
            layout.setBackgroundColor(Color.parseColor("#ffffff"));
        return convertView;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return sectionIndex;
    }

    @Override
    public int getSectionForPosition(int position) {
        return position;
    }

    @Override
    public Object[] getSections() {
        return sections;
    }
}
