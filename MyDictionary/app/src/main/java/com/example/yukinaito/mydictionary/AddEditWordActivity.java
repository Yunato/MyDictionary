package com.example.yukinaito.mydictionary;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class AddEditWordActivity extends AppCompatActivity {
    private static final int EDIT_CODE = 1;
    //"単語の意味を入力"を意味とするか判定 trueは許可
    private boolean meancheck = false;
    //単語の新規追加か修正か判定に使える nullのとき新規作成
    private Word update_word;
    //spinnerで選択しているindex
    private int spinner_index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_word);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0f);

        SQLiteApplication sqLiteApplication = (SQLiteApplication)this.getApplication();

        //スピナーの要素設定
        final Spinner spinner = (Spinner)findViewById(R.id.input_class);
        final String[] buf = sqLiteApplication.getWordClass();
        final String[] items = new String[buf.length + 1];
        System.arraycopy(buf, 0, items, 0, buf.length);
        items[buf.length] = "分類の追加...";

        //スピナーに要素を登録
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.rowdata, items);
        spinner.setAdapter(adapter);
        spinner.setPrompt("分類の選択");

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //アイテムが選択された時
            public void onItemSelected(AdapterView<?> parent, View viw, int arg2, long arg3) {
                final Spinner spinner = (Spinner) parent;

                //新しい分類名を追加していない場合
                if (spinner.getSelectedItemPosition() == buf.length && spinner.getSelectedItem().toString().equals("分類の追加...")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddEditWordActivity.this);
                    builder.setTitle("新規分類名の入力");
                    LayoutInflater inflater = LayoutInflater.from(AddEditWordActivity.this);
                    final View view = inflater.inflate(R.layout.addclass_dialog, null);
                    builder.setView(view);
                    builder.setPositiveButton("追加", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            items[buf.length] = ((EditText)view.findViewById(R.id.edittext)).getText().toString();
                            adapter.notifyDataSetChanged();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }

            //アイテムが選択されなかった
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final TextView textView = (TextView)findViewById(R.id.input_mean);
        textView.setClickable(true);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //新規作成か編集か
                String send = new String();
                if(textView.getText().toString().equals("単語の意味を入力")) {
                    //正しい文字列ではない(許可していない)とき
                    if (!meancheck)
                        send = "";
                }
                else
                    send = textView.getText().toString();

                Intent intent = new Intent(getApplicationContext(),EditActivity.class);
                intent.putExtra("mean", send);
                startActivityForResult(intent, EDIT_CODE);
            }
        });

        if((Word)getIntent().getSerializableExtra("word") != null){
            update_word = (Word)getIntent().getSerializableExtra("word");
            ((TextView)findViewById(R.id.input_name)).setText(update_word.getName());
            ((TextView)findViewById(R.id.input_kana)).setText(update_word.getKana());
            ((TextView)findViewById(R.id.input_mean)).setText(update_word.getMean());
            for(int i = 0; i < items.length; i++)
                if(items[i] == update_word.getClassification()){
                    spinner.setSelection(i);
                    spinner_index = i;
                    break;
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        final MenuInflater inflater = getMenuInflater();
        if(update_word != null)
            inflater.inflate(R.menu.edit_menu, menu);
        else
            inflater.inflate(R.menu.add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        //入力情報の保持
        String name = ((EditText)findViewById(R.id.input_name)).getText().toString();
        String kana = ((EditText)findViewById(R.id.input_kana)).getText().toString();
        String classification = ((Spinner)findViewById(R.id.input_class)).getSelectedItem().toString();
        String mean = ((TextView)findViewById(R.id.input_mean)).getText().toString();

        if(id == R.id.update_action) {
            //編集のとき、入力内容に変更があったかどうか
            if(!(update_word.getName().equals(name) && update_word.getKana().equals(kana) &&
                    update_word.getClassification().equals(classification) && update_word.getMean().equals(mean))) {
                //更新後の単語情報
                Word newWord = new Word(
                        ((EditText)findViewById(R.id.input_name)).getText().toString(),
                        ((EditText)findViewById(R.id.input_kana)).getText().toString(),
                        ((Spinner)findViewById(R.id.input_class)).getSelectedItem().toString(),
                        ((TextView)findViewById(R.id.input_mean)).getText().toString(), 0, 0);
                //DBの内容を更新
                ((SQLiteApplication) this.getApplication()).updateWord(getIntent().getStringExtra("ID"), newWord);

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
            }
            finish();
            return true;
        }else if(id == R.id.add_action){
            //単語の新規追加の際、入力情報に不備がないか
            if(!(name.equals("") || kana.equals("") || !meancheck)){
                //追加する単語情報
                Word newWord = new Word(
                        ((EditText)findViewById(R.id.input_name)).getText().toString(),
                        ((EditText)findViewById(R.id.input_kana)).getText().toString(),
                        ((Spinner)findViewById(R.id.input_class)).getSelectedItem().toString(),
                        ((TextView)findViewById(R.id.input_mean)).getText().toString(), 0, 0);
                //DBに追加
                ((SQLiteApplication) this.getApplication()).saveWord(newWord);

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }else{
                //ダイアログに表示するメッセージ生成
                String message = "";
                if(name.equals(""))
                    message += "追加する単語が記入されていません。\n";
                if(kana.equals(""))
                    message += "単語の読み方が記入されていません。\n";
                if(!meancheck)
                    message += "単語の意味が入力されていません\n";

                //ダイアログの表示
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("警告");
                builder.setMessage(message);
                builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EDIT_CODE){
            if(resultCode == RESULT_OK) {
                //返された文字列のチェック
                String send;
                if(data.getStringExtra("mean").equals("")) {
                    //正しい文字列ではない(許可しない)
                    meancheck = false;
                    send = "単語の意味を入力";
                }
                else {
                    meancheck = true;
                    send = data.getStringExtra("mean");
                }

                TextView textView = (TextView) findViewById(R.id.input_mean);
                textView.setText(send);
            }
        }
    }
}
