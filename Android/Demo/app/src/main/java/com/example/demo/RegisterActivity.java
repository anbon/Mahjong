package com.example.demo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {
    private final String TAG = "RegisterActivity";
    App myapi;
    App.LoadingDialog dialog;
    Thread mythread;
    EditText username, password;
    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");
        //

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
        if(dialog!=null)
            dialog.close();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar ab = this.getActionBar();
        ab.hide();
        setContentView(R.layout.activity_register);
        username = (EditText) findViewById(R.id.signin_account);
        password = (EditText) findViewById(R.id.signin_password);
        username.setText("");
        password.setText("");
        myapi = (App) this.getApplicationContext();
        //dialog.execute();
    }

    public void login(View v){

        if(username.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
            dialog = myapi.new LoadingDialog(RegisterActivity.this, "請將資料填寫完整",true);
            dialog.execute();
        }
        else
        {
            Bundle b= new Bundle();
            b.putString("username",username.getText().toString());
            b.putString("password",password.getText().toString());
            Intent intent = new Intent();
            intent.putExtras(b);
            intent.setClass(this, FillInActivity.class);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            startActivity(intent);
        }
    }






}
