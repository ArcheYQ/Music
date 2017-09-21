package com.music.activity;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.music.R;

import butterknife.ButterKnife;


/**
 * Created by 雅倩宝宝 on 2017/9/7.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public Activity mActivity;
    private Dialog progressDialog;
    public Toolbar mToolbar;
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        mActivity = this;
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ButterKnife.bind(this);
        mActivity = this;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                super.onBackPressed();//返回
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    public void showProgressDialog(){
        if (progressDialog==null){
            progressDialog = new Dialog(this,R.style.Pro);
            progressDialog.setContentView(R.layout.dialog_loading);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
            msg.setText("(σ'・д・)σ");
            progressDialog.show();
        }
        progressDialog.show();
    }

    public void dissmiss(){
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }
    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        ButterKnife.bind(this);
        mActivity = this;

    }
    public void setToolBar(int toolId){
        Toolbar toolbar = (Toolbar) findViewById(toolId);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            this.mToolbar=toolbar;
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }
    public void initHome(){
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.black_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
    }
    public void initWhiteHome(){
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.mipmap.white_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
    }
}
