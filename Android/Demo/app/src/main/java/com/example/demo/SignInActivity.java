package com.example.demo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SignInActivity extends Activity {
    private final String TAG = "SignInActivity";
    App myapi;
    App.LoadingDialog dialog;
    Thread mythread;
    EditText username, password;
    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");
        //
        if (mythread != null) {
            mythread.interrupt();
            mythread = null;

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("LifeCycle", "SignInActivity onResume");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("LifeCycle", "SignInActivity onDestroy");
        dialog.close();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar ab = this.getActionBar();
        ab.hide();
        setContentView(R.layout.activity_signin);
        username = (EditText) findViewById(R.id.signin_account);
        password = (EditText) findViewById(R.id.signin_password);
        username.setText("x51811danny");
        password.setText("as1597530");
        myapi = (App) this.getApplicationContext();


        //dialog.execute();
    }


    public void forgetPassword(View v){

    }
    @SuppressWarnings("deprecation")
    public void login(View v){
        mythread = new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog = myapi.new LoadingDialog(SignInActivity.this, "請稍後...", false);
                        dialog.execute();
                    }
                });

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username.getText().toString()));
                params.add(new BasicNameValuePair("password", password.getText().toString()));
                String result = myapi.postMethod_getCode(SignInActivity.this, App.Verify, params);

                parseResult(result);


            }
        });
        if(username.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
            Toast.makeText(SignInActivity.this, "請將資料填寫完整", Toast.LENGTH_SHORT).show();
        }
        else
            mythread.start();

    }

    @SuppressWarnings("deprecation")
    private void parseResult(String result) {
        Log.v("Verify", result);

        try {
            JSONObject o = new JSONObject(result);


            if(o.getString("status").equals("1")) {
                Log.v(TAG, "Token = " + o.getString("message"));
                App.Token =  o.getString("message");





                JSONObject o_o = o.getJSONObject("data");
                Log.v(TAG,o_o.toString());
                SharedPreferences pref = getSharedPreferences("Account", 0);
                pref.edit()
                        .putString("token", App.Token)
                        .putString("num", o_o.getString("num"))
                        .putString("name", o_o.getString("name"))
                        .putString("email", o_o.getString("email"))
                        .putString("username", o_o.getString("username"))
                        .putString("password", o_o.getString("password"))
                        .putString("gender", o_o.getString("gender"))
                        .putString("age", o_o.getString("age"))
                        //.putString("location_x", o_o.getString("location_x"))
                        //.putString("location_y", o_o.getString("location_y"))
                        .putString("photo", o_o.getString("photo"))
                        .putString("rate", o_o.getString("rate"))
                        //.putString("ratingpeople", o_o.getString("ratingpeople"))
                        .putString("level", o_o.getString("level")).apply();


                Intent intent = new Intent();
                intent.setClass(this, MainActivity.class);
                overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
                startActivity(intent);
                finish();

            }
            else if(o.getString("status").equals("999")){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog = myapi.new LoadingDialog(SignInActivity.this, "登入請確實填寫",true);
                        dialog.execute();
                    }
                });
            }
            else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.close();
                        dialog = myapi.new LoadingDialog(SignInActivity.this,"帳號或密碼填寫錯誤",true);
                        dialog.execute();
                    }
                });
            }
        } catch (Exception e) {
            dialog.close();
            /*runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog = myapi.new LoadingDialog(SignInActivity.this,"發生未預期的錯誤!",true);
                    dialog.execute();
                }
            });*/
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.close();
            }
        });
    }


}
