package co.nineka;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class VerifyActivity extends Activity {
    private final String TAG = "VerifyActivity";
    App myapi;
    App.LoadingDialog dialog;
    Thread mythread;
    EditText number;
    TextView verify_remessage;
    Boolean hasPhone;
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
        setContentView(R.layout.activity_verify);
        number = (EditText) findViewById(R.id.verify_number_panel);
        verify_remessage = (TextView) findViewById(R.id.verify_remessage);

        number.setText("");

        myapi = (App) this.getApplicationContext();
        //dialog.execute();
    }



    public void Verify(View v){

        if(number.getText().toString().trim().isEmpty()){
            dialog = myapi.new LoadingDialog(VerifyActivity.this, "請將資料填寫完整",true);
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
                            dialog = myapi.new LoadingDialog(VerifyActivity.this, "請稍後...", false);
                            dialog.execute();

                        }
                    });

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("registerUser", getIntent().getExtras().getString("username")));
                    params.add(new BasicNameValuePair("registerVerify", number.getText().toString()));
                    String result = myapi.postMethod_getCode(VerifyActivity.this, App.register_verify, params);
                    Log.v("register_verify", result);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.close();
                        }
                    });
                    try {
                        final JSONObject o = new JSONObject(result);

                        if(o.getString("status").equals("1")){

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Intent intent = new Intent();
                                    intent.putExtra("username",getIntent().getExtras().getString("username"));
                                    intent.putExtra("password",getIntent().getExtras().getString("password"));
                                    intent.putExtra("user_ID",getIntent().getExtras().getString("user_ID"));
                                    intent.setClass(VerifyActivity.this, FillInActivity.class);
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                    startActivity(intent);
                                    VerifyActivity.this.finish();

                                }
                            });
                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    verify_remessage.setVisibility(View.VISIBLE);
                                    try {
                                        dialog = myapi.new LoadingDialog(VerifyActivity.this, o.getString("message"), true);
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

                                dialog = myapi.new LoadingDialog(VerifyActivity.this, "伺服器發生錯誤！", true);
                                dialog.execute();
                            }
                        });

                    }

                }
            });
            mythread.start();

        }
    }
    public void ReMessage(View v){
        mythread = new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog = myapi.new LoadingDialog(VerifyActivity.this, "請稍後...", false);
                        dialog.execute();
                    }
                });
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("registerUser", getIntent().getExtras().getString("username")));
                String result = myapi.postMethod_getCode(VerifyActivity.this, App.ReMessage, params);
                Log.v("ReMessage", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.close();
                    }
                });
                try {
                    final JSONObject o = new JSONObject(result);

                    if(o.getString("status").equals("1")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog = myapi.new LoadingDialog(VerifyActivity.this, "發送成功！", true);
                                dialog.execute();
                            }
                        });

                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    dialog = myapi.new LoadingDialog(VerifyActivity.this, o.getString("message"), true);
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
                            dialog = myapi.new LoadingDialog(VerifyActivity.this, "伺服器發生錯誤！", true);
                            dialog.execute();
                        }
                    });

                }

            }
        });
        mythread.start();
    }






}
