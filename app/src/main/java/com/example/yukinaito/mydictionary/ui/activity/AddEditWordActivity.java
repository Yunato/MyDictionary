package com.example.yukinaito.mydictionary.ui.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.yukinaito.mydictionary.ui.view.CustomSpinner;
import com.example.yukinaito.mydictionary.R;
import com.example.yukinaito.mydictionary.model.dao.SQLiteApplication;
import com.example.yukinaito.mydictionary.model.entity.Word;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class AddEditWordActivity extends AppCompatActivity {
    private static final int EDIT_CODE = 1;
    private static final int RESULT_BACK = -1;
    //"単語の意味を入力"を意味とするか判定 trueは許可
    private boolean mean_check = false;
    //単語の新規追加か修正か判定に使える nullのとき新規作成
    private Word update_word;
    //単語の言語の判定に使う
    private static final Set<Character.UnicodeBlock> japaneseUnicodeBlocks = new HashSet<Character.UnicodeBlock>() {{
        add(Character.UnicodeBlock.HIRAGANA);
        add(Character.UnicodeBlock.KATAKANA);
        add(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS);
    }};
    //spinnerで選択しているindex
    private int spinner_index = -1;
    //spinnerを一度もタップしていないか
    private boolean touch_spinner = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_word);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setElevation(0f);
        }

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            //region 前画面に戻るボタンの生成
            @SuppressLint("PrivateResource")
            final Drawable upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.abc_ic_ab_back_material, null);
            if(upArrow != null){
                upArrow.setColorFilter(ContextCompat.getColor(this, R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
            }
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }
        //endregion

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        SQLiteApplication sqLiteApplication = (SQLiteApplication)this.getApplication();

        //単語が英語から始まるか英語から始まるか確認
        ((EditText)findViewById(R.id.input_name)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(((EditText) findViewById(R.id.input_name)).getText().toString().length() != 0) {
                    if (!japaneseUnicodeBlocks.contains(Character.UnicodeBlock.of(((EditText) findViewById(R.id.input_name)).getText().toString().charAt(0)))) {
                        (findViewById(R.id.input_kana)).setEnabled(false);
                        (findViewById(R.id.input_kana)).setFocusable(false);
//                        int id = AddEditWordActivity.this.getResources().getIdentifier("underline_gray", "drawable", AddEditWordActivity.this.getPackageName());
//                        Drawable back = ResourcesCompat.getDrawable(getResources(), id, null);
//                        (findViewById(R.id.input_kana)).setBackground(back);
                        return;
                    }
                }
                (findViewById(R.id.input_kana)).setEnabled(true);
                (findViewById(R.id.input_kana)).setFocusable(true);
                (findViewById(R.id.input_kana)).setFocusableInTouchMode(true);
                int id = AddEditWordActivity.this.getResources().getIdentifier("underline_white", "drawable", AddEditWordActivity.this.getPackageName());
                Drawable back = ResourcesCompat.getDrawable(getResources(), id, null);
                (findViewById(R.id.input_kana)).setBackground(back);
            }
        });

        //スピナーの要素設定
        final CustomSpinner spinner = (CustomSpinner)findViewById(R.id.input_class);
        final String[] buf = sqLiteApplication.getWordClass();
        final String[] items = new String[buf.length + 2];
        items[0] = "分野の選択[必須]";
        System.arraycopy(buf, 0, items, 1, buf.length);
        items[buf.length + 1] = "分野の追加...";

        //スピナーに要素を登録
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.rowdata, items);
        spinner.setAdapter(adapter);
        spinner.setPrompt("分野の選択");
        spinner.setFocusable(false);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //アイテムが選択された時
            @Override
            public void onItemSelected(AdapterView<?> parent, View viw, int position, long id) {
                final CustomSpinner spinner = (CustomSpinner)findViewById(R.id.input_class);
                if(!spinner.isFocusable()) {
                    spinner.setFocusable(true);
                }else {
                    //新しい分類名を追加していない場合
                    if (spinner.getSelectedItemPosition() == items.length - 1 && spinner.getSelectedItem().toString().equals("分野の追加...")) {
                        //ダイアログ外をタップしたとき対策
                        spinner.setSelection(spinner_index);
                        adapter.notifyDataSetChanged();

                        AlertDialog.Builder builder = new AlertDialog.Builder(AddEditWordActivity.this);
                        builder.setTitle("新規分野名の入力");
                        LayoutInflater inflater = LayoutInflater.from(AddEditWordActivity.this);
                        final View view = inflater.inflate(R.layout.addclass_dialog, (ViewGroup)findViewById(R.id.dialog_root));
                        builder.setView(view);
                        builder.setPositiveButton("追加", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                items[buf.length] = ((EditText) view.findViewById(R.id.input_mean)).getText().toString();
                                spinner.setSelection(buf.length);
                                adapter.notifyDataSetChanged();
                                spinner_index = buf.length;
                                touch_spinner = false;
                            }
                        });
                        final AlertDialog dialog = builder.create();
                        dialog.show();
                        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                        ((EditText) view.findViewById(R.id.input_mean)).addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                if (!((EditText) view.findViewById(R.id.input_mean)).getText().toString().equals(""))
                                    dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                                else
                                    dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                            }
                        });
                    }
                }
            }

            //アイテムが選択されなかった
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final TextView textView = (TextView)findViewById(R.id.input_mean);
        final LinearLayout layout = (LinearLayout)findViewById(R.id.activity_add_edit_mean);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //新規作成か編集か
                String send = "";
                if(textView.getText().toString().equals("単語の意味を入力")) {
                    //正しい文字列ではない(許可していない)とき
                    if (!mean_check)
                        send = "";
                }
                else
                    send = textView.getText().toString();

                Intent intent = new Intent(getApplicationContext(),EditActivity.class);
                intent.putExtra("mean", send);
                startActivityForResult(intent, EDIT_CODE);
            }
        });

        if(getIntent().getSerializableExtra("word") != null){
            update_word = (Word)getIntent().getSerializableExtra("word");
            ((TextView)findViewById(R.id.input_name)).setText(update_word.getName());
            ((TextView)findViewById(R.id.input_kana)).setText(update_word.getKana());
            ((TextView)findViewById(R.id.input_mean)).setText(update_word.getMean());
            mean_check = true;
            for(int i = 0; i < items.length; i++)
                if(items[i].equals(update_word.getClassification())){
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            //region 前画面に戻るボタンタップ
            Intent intent = new Intent();
            setResult(RESULT_BACK, intent);
            finish();
            //endregion
        }

            //入力情報の保持
        String name = ((EditText) findViewById(R.id.input_name)).getText().toString();
        String kana = ((EditText) findViewById(R.id.input_kana)).getText().toString();
        String classification = ((Spinner) findViewById(R.id.input_class)).getSelectedItem().toString();
        String mean = ((TextView) findViewById(R.id.input_mean)).getText().toString();

        //追加する(現在の)日時を取得 年月日
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.JAPANESE);
        int date = Integer.parseInt(simpleDateFormat.format(calendar.getTime()));

        //入力内容の比較用変数
        boolean check_name, check_kana, check_class, check_mean;

        if (id == R.id.update_action || id == R.id.add_action) {
            //編集のとき、入力内容に変更があるか true 変更なし false 変更有
            if (update_word != null && update_word.getName().equals(name) && update_word.getKana().equals(kana) &&
                    update_word.getClassification().equals(classification) && update_word.getMean().equals(mean)) {
                finish();
            }else {
                //入力情報に不備がないか trueなら不備
                check_name = name.equals("");
                check_kana = (kana.equals("") && !check_name && japaneseUnicodeBlocks.contains(Character.UnicodeBlock.of(name.charAt(0))));
                check_class = ((((SQLiteApplication)this.getApplication()).getWordClass()).length == 0 &&
                        ((CustomSpinner)findViewById(R.id.input_class)).getSelectedItem().toString().equals("分野の追加...") && touch_spinner)
                        || ((CustomSpinner)findViewById(R.id.input_class)).getSelectedItem().toString().equals("分野の選択[必須]");
                check_mean = !mean_check;
                if (!(check_name ||check_kana ||check_class ||check_mean)) {
                    //追加/変更する単語情報
                    Word Word = new Word(name, kana, classification, mean, 0, -1, date);

                    if (id == R.id.update_action)
                        //DBの内容を更新
                        ((SQLiteApplication) this.getApplication()).updateWord(getIntent().getStringExtra("ID"), Word);
                    else
                        //DBに追加
                        ((SQLiteApplication) this.getApplication()).saveWord(Word);

                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    //ダイアログに表示するメッセージ生成
                    String message = "";
                    if (check_name)
                        message += "追加する単語が入力されていません。\n";
                    if (check_kana)
                        message += "単語の読み方が入力されていません。\n";
                    if (check_class)
                        message += "単語を割り振る分野が入力されていません。\n";
                    if (check_mean)
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
                    mean_check = false;
                    send = "単語の意味を入力";
                }
                else {
                    mean_check = true;
                    send = data.getStringExtra("mean");
                }

                TextView textView = (TextView) findViewById(R.id.input_mean);
                textView.setText(send);
            }
        }
    }
}
