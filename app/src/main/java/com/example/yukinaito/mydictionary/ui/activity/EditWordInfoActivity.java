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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.yukinaito.mydictionary.ui.fragment.SelectWordFragment;
import com.example.yukinaito.mydictionary.ui.view.CustomSpinner;
import com.example.yukinaito.mydictionary.R;
import com.example.yukinaito.mydictionary.model.dao.SQLiteApplication;
import com.example.yukinaito.mydictionary.model.entity.Word;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class EditWordInfoActivity extends AppCompatActivity {
    /** 要求コード  */
    private static final int REQUEST_EDIT_CODE = 1;
    /** 識別子 */
    private static final String EXTRA_STRING_MEAN = "com.example.yukinaito.mydictionary.ui.activity.EXTRA_STRING_MEAN";

    private static final int RESULT_BACK = -1;
    private boolean meanUpdateFlag = false;
    private static final Set<Character.UnicodeBlock> japaneseUnicodeBlocks = new HashSet<Character.UnicodeBlock>() {{
        add(Character.UnicodeBlock.HIRAGANA);
        add(Character.UnicodeBlock.KATAKANA);
        add(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS);
    }};
    private int spinnerIndex = -1;
    private boolean touch_spinner = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_word_info);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setupUIElements();
        setInformation();
    }

    /** アクティビティ上の User Interface Elements を設定を行う */
    private void setupUIElements(){
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setElevation(0f);
            actionBar.setDisplayHomeAsUpEnabled(true);

            @SuppressLint("PrivateResource")
            final Drawable upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.abc_ic_ab_back_material, null);
            if(upArrow != null) {
                upArrow.setColorFilter(ContextCompat.getColor(this, R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
                actionBar.setHomeAsUpIndicator(upArrow);
            }
        }

        TextView textView = (TextView)findViewById(R.id.input_field);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String send = "";
                if (meanUpdateFlag){
                    send = ((TextView)view).getText().toString();
                }

                Intent intent = new Intent(getApplicationContext(),EditWordMeanActivity.class);
                intent.putExtra(EXTRA_STRING_MEAN, send);
                startActivityForResult(intent, REQUEST_EDIT_CODE);
            }
        });
    }

    /** DB から取得した単語情報を基に Widgets の設定を行う */
    private void setInformation() {
        SQLiteApplication sqLiteApplication = (SQLiteApplication) this.getApplication();

        CustomSpinner spinner = (CustomSpinner)findViewById(R.id.input_filed);
        spinner.setAdapter(sqLiteApplication.getWordFiled());
        spinner.setPrompt("分野を選択して下さい");
        spinner.setFocusable(false);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CustomSpinner spinner = (CustomSpinner)findViewById(R.id.input_filed);
                if(!spinner.isFocusable()) {
                    spinner.setFocusable(true);
                }else {
                    if (spinner.getSelectedItemPosition() == spinner.getItemSize() - 1) {
                        spinner.setSelection(spinnerIndex);
                        createDialog(spinner);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        String wordID = getIntent().getStringExtra(SelectWordFragment.EXTRA_STRING_DATA_ID);
        if(wordID != null){
            Word word = sqLiteApplication.getWordInfo(wordID);
            ((TextView)findViewById(R.id.input_name)).setText(word.getName());
            ((TextView)findViewById(R.id.input_kana)).setText(word.getKana());
            ((TextView)findViewById(R.id.input_field)).setText(word.getMean());
            meanUpdateFlag = false;
            spinnerIndex = spinner.setSelection(word.getClassification());
        }
    }

    private void createDialog(final CustomSpinner spinner){
        AlertDialog.Builder builder = new AlertDialog.Builder(EditWordInfoActivity.this);
        builder.setTitle("新規分野名の入力");
        LayoutInflater inflater = LayoutInflater.from(EditWordInfoActivity.this);
        final View dialogView = inflater.inflate(R.layout.dialog_add_field, (ViewGroup)findViewById(R.id.dialog_root));
        builder.setView(dialogView);
        builder.setPositiveButton("追加", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String strInput = ((EditText) dialogView.findViewById(R.id.input_field)).getText().toString();
                spinner.addItem(strInput);
                spinnerIndex = spinner.setSelection(strInput);
                touch_spinner = false;
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        ((EditText) dialogView.findViewById(R.id.input_field)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String strInput = ((EditText) dialogView.findViewById(R.id.input_field)).getText().toString();
                //TODO: 不適切な理由を表示 (既に分野が存在していることを警告する. 空欄に対する警告は必要ない.)
                if (!strInput.equals("") && spinner.isNotMatchItem(strInput)) {
                    dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }else {
                    dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        final MenuInflater inflater = getMenuInflater();
        String wordID = getIntent().getStringExtra(SelectWordFragment.EXTRA_STRING_DATA_ID);
        if(wordID != null) {
            inflater.inflate(R.menu.menu_edit, menu);
        }else {
            inflater.inflate(R.menu.menu_add, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent intent = new Intent();
            setResult(RESULT_BACK, intent);
            finish();
        }

        String name = ((EditText) findViewById(R.id.input_name)).getText().toString();
        String kana = ((EditText) findViewById(R.id.input_kana)).getText().toString();
        String classification = ((Spinner) findViewById(R.id.input_filed)).getSelectedItem().toString();
        String mean = ((TextView) findViewById(R.id.input_field)).getText().toString();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.JAPANESE);
        int date = Integer.parseInt(simpleDateFormat.format(calendar.getTime()));

        boolean check_name, check_kana, check_class, check_mean;

        SQLiteApplication sqLiteApplication = (SQLiteApplication) this.getApplication();
        String wordID = getIntent().getStringExtra(SelectWordFragment.EXTRA_STRING_DATA_ID);
        Word word = sqLiteApplication.getWordInfo(wordID);

        if (id == R.id.update_action || id == R.id.add_action) {
            if (word != null && word.getName().equals(name) && word.getKana().equals(kana) &&
                    word.getClassification().equals(classification) && word.getMean().equals(mean)) {
                finish();
            }else {
                check_name = name.equals("");
                check_kana = (kana.equals("") && !check_name && japaneseUnicodeBlocks.contains(Character.UnicodeBlock.of(name.charAt(0))));
                check_class = ((((SQLiteApplication)this.getApplication()).getWordFiled()).length == 0 &&
                        ((CustomSpinner)findViewById(R.id.input_filed)).getSelectedItem().toString().equals("分野の追加...") && touch_spinner)
                        || ((CustomSpinner)findViewById(R.id.input_filed)).getSelectedItem().toString().equals("分野の選択[必須]");
                check_mean = !meanUpdateFlag;
                if (!(check_name ||check_kana ||check_class ||check_mean)) {
                    Word Word = new Word(name, kana, classification, mean, 0, -1, date);

                    if (id == R.id.update_action)
                        ((SQLiteApplication) this.getApplication()).updateWord(getIntent().getStringExtra("ID"), Word);
                    else
                        ((SQLiteApplication) this.getApplication()).saveWord(Word);

                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    String message = "";
                    if (check_name)
                        message += "追加する単語が入力されていません。\n";
                    if (check_kana)
                        message += "単語の読み方が入力されていません。\n";
                    if (check_class)
                        message += "単語を割り振る分野が入力されていません。\n";
                    if (check_mean)
                        message += "単語の意味が入力されていません\n";

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
        if(requestCode == REQUEST_EDIT_CODE){
            if(resultCode == RESULT_OK) {
                String send;
                if(data.getStringExtra(EXTRA_STRING_MEAN).equals("")) {
                    meanUpdateFlag = false;
                    send = "単語の意味を入力";
                }else {
                    meanUpdateFlag = true;
                    send = data.getStringExtra(EXTRA_STRING_MEAN);
                }
                TextView textView = (TextView) findViewById(R.id.input_field);
                textView.setText(send);
            }
        }
    }
}
