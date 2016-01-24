package com.example.demo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends Activity {
    App myapi;
    App.LoadingDialog dialog;
    ProgressBar pb;
    SharedPreferences pref;
    ImageView start_register, start_signin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar ab = this.getActionBar();
        ab.hide();
        setContentView(R.layout.activity_start);
        myapi = (App) this.getApplicationContext();
        start_register = (ImageView) findViewById(R.id.start_register) ;
        start_signin = (ImageView) findViewById(R.id.start_signin) ;
        pref = getSharedPreferences("Account", 0);
        dialog = myapi.new LoadingDialog(this,"手機認證已成功",true);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        PushManager.startWork(getApplicationContext(),
                PushConstants.LOGIN_TYPE_API_KEY, "pGK0Dq6SVkHCpDkASiCf4sng");
        /*if(pref.getString("num","").isEmpty()){
            pb.setVisibility(View.INVISIBLE);
        }else{
            start_register.setVisibility(View.GONE);
            start_signin.setVisibility(View.GONE);
            new AsyncTaskLogin().execute();
        }*/
        //dialog.execute();
    }

    public void goRegisterActivity(View v){
        Intent intent = new Intent();
        intent.setClass(this, RegisterActivity.class);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(intent);
    }

    public void goSignInActivity(View v){
        Intent intent = new Intent();
        intent.setClass(this, SignInActivity.class);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(intent);
    }

    public class AsyncTaskLogin extends AsyncTask<Boolean, Integer, String>
    {

        protected void onPreExecute()
        {

            pb.setVisibility(View.VISIBLE);

        }

        protected String doInBackground(Boolean... state)
        {
            List<NameValuePair> params;
            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", pref.getString("username", "")));
            params.add(new BasicNameValuePair("password", pref.getString("password", "")));

            //Log.v("params",params.toString());

            return myapi.postMethod_getCode(StartActivity.this, App.Verify, params);
        }
        protected void onPostExecute(String result)
        {

            Log.v("AsyncTaskLogin", "result : " + result);
            try {
                JSONObject o = new JSONObject(result);

                if(o.getString("status").equals("1")){
                    App.Token =  o.getString("message");
                    JSONObject o_o = o.getJSONObject("data");
                    Log.v("StartActivity",o_o.toString());
                    pref.edit()
                            .putString("token", App.Token)
                            .putString("num", o_o.getString("num"))
                            .putString("name", o_o.getString("name"))
                            .putString("email", o_o.getString("email"))
                            .putString("username", o_o.getString("username"))
                            .putString("password", o_o.getString("password"))
                            .putString("gender", o_o.getString("gender"))
                            .putString("age", o_o.getString("age"))
                            .putString("photo", o_o.getString("photo"))
                            .putString("rate", o_o.getString("rate"))
                            .putString("level", o_o.getString("level")).apply();


                    Intent intent = new Intent();
                    intent.setClass(StartActivity.this, MainActivity.class);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    startActivity(intent);
                    finish();

                }
                else if(o.getString("status").equals("999")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog = myapi.new LoadingDialog(StartActivity.this, "登入請確實填寫",true);
                            dialog.execute();
                        }
                    });
                }
                else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.close();
                            dialog = myapi.new LoadingDialog(StartActivity.this,"帳號或密碼填寫錯誤",true);
                            dialog.execute();
                        }
                    });

                }
            } catch (JSONException e) {
                start_register.setVisibility(View.VISIBLE);
                start_signin.setVisibility(View.VISIBLE);
                e.printStackTrace();
            }
            pb.setVisibility(View.INVISIBLE);

        }
    }


}
