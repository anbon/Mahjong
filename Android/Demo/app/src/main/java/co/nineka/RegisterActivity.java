package co.nineka;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class RegisterActivity extends Activity {
    private final String TAG = "RegisterActivity";
    App myapi;
    App.LoadingDialog dialog;
    Thread mythread;
    EditText username, password, signin_number;
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
        signin_number = (EditText) findViewById(R.id.signin_number);
        username.setText("");
        password.setText("");
        myapi = (App) this.getApplicationContext();
        //dialog.execute();
    }

    /*
    if (!Linkify.addLinks(senderEmail.getText(), Linkify.EMAIL_ADDRESSES) && !senderEmail.getText().toString().equals(""))
		{
			emailcorrect = false;
		}

		if (!Linkify.addLinks(senderPhoneNumber.getText(), Linkify.PHONE_NUMBERS) && !senderPhoneNumber.getText().toString().equals(""))
		{
			numbercorrect = false;
		}
     */

    public void login(View v){

        if(username.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
            dialog = myapi.new LoadingDialog(RegisterActivity.this, "請將資料填寫完整",true);
            dialog.execute();
        }else if(!Linkify.addLinks(username.getText(), Linkify.EMAIL_ADDRESSES)){
            dialog = myapi.new LoadingDialog(RegisterActivity.this, "請檢查您的信箱格式是否正確",true);
            dialog.execute();
        }else if(!Linkify.addLinks(signin_number.getText(), Linkify.PHONE_NUMBERS) ){
            dialog = myapi.new LoadingDialog(RegisterActivity.this, "請檢查您的手機格式是否正確",true);
            dialog.execute();
        }
        else
        {
            mythread = new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog = myapi.new LoadingDialog(RegisterActivity.this, "請稍後...", false);
                            dialog.execute();
                        }
                    });
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("registerUser", username.getText().toString()));
                    params.add(new BasicNameValuePair("registerPass", password.getText().toString()));
                    params.add(new BasicNameValuePair("registerPhone", signin_number.getText().toString()));

                    String result = myapi.postMethod_getCode(RegisterActivity.this, App.register, params);
                    Log.v("register_phone", result);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.close();
                        }
                    });
                    try {
                        final JSONObject o = new JSONObject(result);

                        if(o.getString("status").equals("1")){
                            final String user_ID = o.getString("message");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Bundle b= new Bundle();
                                    b.putString("username",username.getText().toString());
                                    b.putString("password",password.getText().toString());
                                    Intent intent = new Intent();
                                    intent.putExtra("user_ID",user_ID);
                                    intent.putExtras(b);
                                    intent.setClass(RegisterActivity.this, VerifyActivity.class);
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        dialog = myapi.new LoadingDialog(RegisterActivity.this, o.getString("message"), true);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    dialog.execute();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog = myapi.new LoadingDialog(RegisterActivity.this, "伺服器發生錯誤！", true);
                                dialog.execute();
                            }
                        });

                    }

                }
            });
            mythread.start();

        }
    }






}
