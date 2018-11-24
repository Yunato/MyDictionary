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
import java.util.Locale;

public class EditWordInfoActivity extends AppCompatActivity {
    /** 要求コード  */
    private static final int REQUEST_EDIT_MEAN_CODE = 1;
    /** 識別子 */
    public static final String EXTRA_STRING_MEAN = "com.example.yukinaito.mydictionary.ui.activity.EXTRA_STRING_MEAN";
    public static final String EXTRA_UPDATE_REQUEST = "com.example.yukinaito.mydictionary.ui.activity.EXTRA_UPDATE_REQUEST";
    /** 更新対象である単語情報 */
    private boolean existWordFlag = false;
    private Word defaultWord;
    /** CustomSpinnerの選択インデックス */
    private int spinnerIndex = 0;

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
        setTitle(NavigationDrawer.status);

        CustomSpinner spinner = (CustomSpinner)findViewById(R.id.input_filed);
        spinner.setPrompt("分野を選択して下さい");
        spinner.setFocusable(false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CustomSpinner spinner = (CustomSpinner)findViewById(R.id.input_filed);
                if(!spinner.isFocusable()) {
                    spinner.setFocusable(true);
                }else {
                    if (position == spinner.getItemSize() - 1) {
                        createDialog(spinner);
                    }else{
                        spinnerIndex = position;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        TextView textView = (TextView)findViewById(R.id.input_mean);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditWordMeanActivity.class);
                intent.putExtra(EXTRA_STRING_MEAN, ((TextView)view).getText().toString());
                startActivityForResult(intent, REQUEST_EDIT_MEAN_CODE);
            }
        });
    }

    /** DB から取得した単語情報を基に Widgets の設定を行う */
    private void setInformation() {
        SQLiteApplication sqLiteApplication = (SQLiteApplication) this.getApplication();

        ((CustomSpinner)findViewById(R.id.input_filed)).setAdapter(sqLiteApplication.getWordFiled());

        String wordID = getIntent().getStringExtra(SelectWordFragment.EXTRA_STRING_DATA_ID);
        if(wordID != null){
            existWordFlag = true;
            defaultWord = sqLiteApplication.getWordInfo(wordID);
            ((TextView)findViewById(R.id.input_name)).setText(defaultWord.getName());
            ((TextView)findViewById(R.id.input_kana)).setText(defaultWord.getKana());
            spinnerIndex = ((CustomSpinner)findViewById(R.id.input_filed)).setSelection(defaultWord.getField());
            ((TextView)findViewById(R.id.input_mean)).setText(defaultWord.getMean());
        }
    }

    /**
     * Spinner が 「分野の追加...」 で選択された場合におけるダイアログを生成する
     * @param spinner 画面に描画されている CustomSpinner オブジェクト
     */
    private void createDialog(final CustomSpinner spinner){
        AlertDialog.Builder builder = new AlertDialog.Builder(EditWordInfoActivity.this);
        builder.setTitle("新規分野名の入力");
        LayoutInflater inflater = LayoutInflater.from(EditWordInfoActivity.this);
        final View dialogView = inflater.inflate(R.layout.dialog_add_field, (ViewGroup)findViewById(R.id.dialog_layout));
        builder.setView(dialogView);
        builder.setPositiveButton("追加", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String strInput = ((EditText) dialogView.findViewById(R.id.input_field)).getText().toString();
                spinner.addItem(strInput);
                spinnerIndex = spinner.setSelection(strInput);
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
            setResult(RESULT_CANCELED, intent);
            finish();
        }else if(id == R.id.action_add || id == R.id.action_update) {
            String name = ((EditText) findViewById(R.id.input_name)).getText().toString();
            String kana = ((EditText) findViewById(R.id.input_kana)).getText().toString();
            String field = ((Spinner) findViewById(R.id.input_filed)).getSelectedItem().toString();
            String mean = ((TextView) findViewById(R.id.input_mean)).getText().toString();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.JAPANESE);
            int date = Integer.parseInt(simpleDateFormat.format(calendar.getTime()));

            Bundle bundle = new Bundle();
            if(isCommittable(name, kana, field, mean)) {
                Word newWord = new Word(name, kana, field, mean, 0, -1, date);
                if (existWordFlag) {
                    ((SQLiteApplication) this.getApplication()).updateWord(getIntent().getStringExtra(SelectWordFragment.EXTRA_STRING_DATA_ID), newWord);
                } else {
                    ((SQLiteApplication) this.getApplication()).saveWord(newWord);
                }
                Intent intent = new Intent();
                bundle.putBoolean(EXTRA_UPDATE_REQUEST, true);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * SQLite へ入力された情報で反映させて良いか判定する
     * @param name 単語名
     * @param kana 読み方
     * @param field 分野名
     * @param mean 意味
     * @return SQLiteへの反映許可フラグ
     */
    private boolean isCommittable(String name, String kana, String field, String mean){
        boolean updatePermit = true;
        if (!existWordFlag) {
            boolean noNameChanged, noFieldChanged, noMeanChanged;
            noNameChanged = name.equals("");
            noFieldChanged = spinnerIndex == 0 || spinnerIndex == ((CustomSpinner) findViewById(R.id.input_filed)).getItemSize() - 1;
            noMeanChanged = mean.equals("");
            noMeanChanged = noMeanChanged && (NavigationDrawer.status).equals(NavigationDrawer.STATUS_MEAN);
            if (noNameChanged || noFieldChanged || noMeanChanged) {
                updatePermit = false;
                String message = "";
                if (noNameChanged) {
                    message += "追加する単語が入力されていません.\n";
                }
                if (noFieldChanged) {
                    message += "単語を割り振る分野が入力されていません.\n";
                }
                if (noMeanChanged) {
                    message += "単語の意味が入力されていません.\n";
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("警告").setMessage(message);
                builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }else{
            if (hasBlank(name, field, mean)) {
                updatePermit = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("警告").setMessage("未入力の情報があります.");
                builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            /*
            else if(isPastParam(name, kana, field, mean)) {
                updatePermit = false;
            }*/
        }
        return updatePermit;
    }

    /**
     * SQLite へ反映しようとしている情報が過去のデータから更新されたか判別する
     * @param name 単語名
     * @param kana 読み方
     * @param field 分野名
     * @param mean 意味
     * @return データ更新の有無. true ならデータ更新をしていない. false ならデータ更新がされている.
     */
    private boolean isPastParam(String name, String kana, String field, String mean){
        boolean noNameChanged, noKanaChanged, noFieldChanged, noMeanChanged;
        noNameChanged = name.equals(defaultWord.getName());
        noKanaChanged = kana.equals(defaultWord.getKana());
        noFieldChanged = field.equals(defaultWord.getField());
        noMeanChanged = mean.equals(defaultWord.getMean());
        return noNameChanged && noKanaChanged && noFieldChanged && noMeanChanged;
    }

    /**
     * SQLite へ反映しようとしている情報に未入力のデータが含まれていないか判別する
     * @param name 単語名
     * @param field 分野名
     * @param mean 意味
     * @return 未入力データの有無. true なら未入力データを保持している. false なら未入力データを保持していない.
     */
    private boolean hasBlank(String name, String field, String mean){
        boolean noNameChanged, noFieldChanged, noMeanChanged = false;
        noNameChanged = name.equals("");
        noFieldChanged = field.equals("");
        if(NavigationDrawer.status.equals(NavigationDrawer.STATUS_MEAN)) {
            noMeanChanged = mean.equals("");
        }
        return noNameChanged || noFieldChanged || noMeanChanged;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_EDIT_MEAN_CODE){
            if(resultCode == RESULT_OK) {
                String mean;
                if(data.getStringExtra(EXTRA_STRING_MEAN).equals("")) {
                    mean = "";
                }else {
                    mean = data.getStringExtra(EXTRA_STRING_MEAN);
                }
                ((TextView) findViewById(R.id.input_mean)).setText(mean);
            }
        }
    }
}
