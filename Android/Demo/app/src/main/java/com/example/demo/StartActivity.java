package com.example.demo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class StartActivity extends Activity {
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

    public void goRegisterActivity(View v){
        /*Intent intent = new Intent();
        intent.setClass(this, RegisterActivity.class);
        overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
        startActivity(intent);*/
    }

    public void goSignInActivity(View v){
        Intent intent = new Intent();
        intent.setClass(this, SignInActivity.class);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(intent);
    }


}
