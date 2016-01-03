package com.example.demo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegisterActivity extends Activity {
    App myapi;
    App.LoadingDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar ab = this.getActionBar();
        ab.hide();
        setContentView(R.layout.activity_start);
        myapi = (App) this.getApplicationContext();
        dialog = myapi.new LoadingDialog(this,"手機認證已成功",true);
        //dialog.execute();
    }



}
