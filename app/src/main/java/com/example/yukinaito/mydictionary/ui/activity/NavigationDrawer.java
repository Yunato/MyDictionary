package com.example.yukinaito.mydictionary.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.yukinaito.mydictionary.R;
import com.example.yukinaito.mydictionary.ui.fragment.SelectFieldFragment;
import com.example.yukinaito.mydictionary.ui.fragment.SelectWordLastInFragment;

public class NavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    /** 要求コード  */
    private static final int REQUEST_WRITE_STORAGE = 1;

    /** 識別コード */
    public static String status;
    public static final String STATUS_MEAN = "MyDictionary_MEAN";
    private static final String STATUS_SEARCH = "MyDictionary_RESEARCH";
    private boolean fragmentSwitchFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);
        checkPermission();
        setupUIElements();
    }

    /** アクティビティ上の User Interface Elements を設定を行う */
    private void setupUIElements(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        ((NavigationView)findViewById(R.id.nav_view)).setNavigationItemSelectedListener(this);
        switchUserInterface(R.id.top_dictionary);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();
        switchUserInterface(id);
        return false;
    }

    /**
     * パラメータを基に, 次の描画に適した Activity または Fragment へ画面を切り替える
     * @param id メニューID
     */
    private void switchUserInterface(int id){
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SelectFieldFragment fragment = new SelectFieldFragment();
        transaction.replace(R.id.main_layout, fragment);
        transaction.commit();

        switch (id) {
            case R.id.top_dictionary:
                status = STATUS_MEAN;
                break;
            case  R.id.top_research:
                status = STATUS_SEARCH;
                break;
            default:
                return;
        }
        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
    }

    /** ユーザがアプリケーションに権限を付与しているか確認する */
    private void checkPermission(){
        boolean hasPermission = (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if(!hasPermission){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_convert_listflagment, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (id == R.id.action_convert) {
            if(fragmentSwitchFlag){
                fragmentSwitchFlag = false;
                Fragment fragment = new SelectWordLastInFragment();
                transaction.replace(R.id.main_layout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }else {
                fragmentSwitchFlag = true;
                getSupportFragmentManager().popBackStack();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_WRITE_STORAGE){
            if(grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                finish();
            }
        }
    }
}
