package co.nineka;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import co.nineka.util.CycleWheelView;

public class FillInActivity extends Activity {
    App myapi;
    App.LoadingDialog dialog;
    Thread mythread;
    int wheel_position;
    SharedPreferences pref;
    ActionBar ab;
    View customView;;
    RelativeLayout relative_gender, relative_nickname, relative_age;
    TextView back, titletextView, about_gender, about_age;
    EditText about_nickname;
    //ImageView about_photo;
    List<String> class_name;
    //File tmpFile;
    @Override
    public void onPause() {
        super.onPause();
        //isAlive = false;
        if (mythread != null) {
            mythread.interrupt();
            mythread = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //undo(getView());
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        InputMethodManager imm = (InputMethodManager) relative_age.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_fill_in);
        setTitle("");
        ab = getActionBar();
        ab.setDisplayShowCustomEnabled(true);
        View homeIcon = findViewById(android.R.id.home);
        ((View) homeIcon.getParent()).setVisibility(View.GONE);
        pref = getSharedPreferences("Account",0);
        pref.edit().putString("username", getIntent().getExtras().getString("username"))
                .putString("password", getIntent().getExtras().getString("password"))
                .putString("num", getIntent().getExtras().getString("user_ID")).apply();
        LayoutInflater li = LayoutInflater.from(this);
        customView = li.inflate(R.layout.drawerlayout, null);

        titletextView = (TextView) customView.findViewById(R.id.titletextView);
        titletextView.setText("個人資料");

        TextView back = (TextView) customView.findViewById(R.id.back);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ab.setCustomView(customView);
        myapi = (App) this.getApplicationContext();
        relative_gender = (RelativeLayout) findViewById(R.id.relative_gender);
        relative_nickname = (RelativeLayout) findViewById(R.id.relative_nickname);
        relative_age = (RelativeLayout) findViewById(R.id.relative_age);
        about_nickname = (EditText) findViewById(R.id.about_nickname);
        about_gender = (TextView) findViewById(R.id.about_gender);
        about_age = (TextView) findViewById(R.id.about_age);

        relative_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GenderListAlertDialog(v);
            }
        });
        relative_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                about_nickname.requestFocus();
                InputMethodManager imm = (InputMethodManager) about_nickname.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }
        });
        class_name = new ArrayList<String>();
        for(int i = 0 ; i<100 ; i++) {
            class_name.add((i+1)+"");
        }

        relative_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog=new Dialog(FillInActivity.this, R.style.age_Dialog);
                dialog.setContentView(R.layout.item_age_dialog);
                TextView cancel = (TextView)dialog.findViewById(R.id.textView1);
                TextView done = (TextView)dialog.findViewById(R.id.textView3);
                final String temp_age = about_age.getText().toString();
                cancel.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        about_age.setText(temp_age);
                        dialog.dismiss();
                    }
                });
                final CycleWheelView cycleWheelView1 = (CycleWheelView)dialog.findViewById(R.id.cycleWheelView1);
                /*CycleWheelView cycleWheelView2 = (CycleWheelView)dialog.findViewById(R.id.cycleWheelView2);
                cycleWheelView2.setVisibility(View.GONE);*/
                cycleWheelView1.setLabels(class_name);
                if(Integer.parseInt(temp_age)!=0)
                    cycleWheelView1.setSelection(Integer.parseInt(temp_age)-1);
                cycleWheelView1.setAlphaGradual(0.5f);
                cycleWheelView1.setDivider(ContextCompat.getColor(FillInActivity.this, R.color.lighter_gray), (int) convertDpToPixel(1, FillInActivity.this));
                cycleWheelView1.setSolid(ContextCompat.getColor(FillInActivity.this, android.R.color.white), ContextCompat.getColor(FillInActivity.this, android.R.color.white));
                cycleWheelView1.setLabelColor(ContextCompat.getColor(FillInActivity.this, R.color.text_gray));
                cycleWheelView1.setLabelSelectColor(ContextCompat.getColor(FillInActivity.this, R.color.darker_gray));
                try {
                    cycleWheelView1.setWheelSize(7);
                } catch (CycleWheelView.CycleWheelViewException e) {
                    e.printStackTrace();
                }
                cycleWheelView1.setOnWheelItemSelectedListener(new CycleWheelView.WheelItemSelectedListener() {
                    @Override
                    public void onItemSelected(int position, String label) {
                        about_age.setText(label);
                    }
                });

                done.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        wheel_position=cycleWheelView1.getSelection();
                        about_age.setText(cycleWheelView1.getSelectLabel());
                        dialog.dismiss();
                    }
                });

                Window dialogWindow = dialog.getWindow();
                dialogWindow.setGravity(Gravity.BOTTOM );
                dialog.show();

            }
        });


    }
    public void Confirm(View v){
        relative_nickname.clearFocus();
        InputMethodManager imm = (InputMethodManager) relative_age.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //if(imm.isActive())
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

        if(about_nickname.getText().toString().isEmpty()){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog = myapi.new LoadingDialog(FillInActivity.this, "暱稱不得為空！", true);
                    dialog.execute();
                }
            });
            return;
        }

        mythread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog = myapi.new LoadingDialog(FillInActivity.this, "請稍後...", false);
                            dialog.execute();
                        }
                    });

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    //params.add(new BasicNameValuePair("filename", f.getName()));
                    params.add(new BasicNameValuePair("user_ID", pref.getString("num","")));
                    params.add(new BasicNameValuePair("gender", gender));
                    params.add(new BasicNameValuePair("name", about_nickname.getText().toString()));
                    params.add(new BasicNameValuePair("age", about_age.getText().toString()));
                    //TODO 串 上傳帳戶資訊API
                    final String result = myapi.postMethod_getCode(FillInActivity.this, App.User_Info, params);
                    dialog.close();
                    try {
                        JSONObject o = new JSONObject(result);
                        if(o.getString("status").equals("1")){

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(FillInActivity.this, "更改個人資料成功！", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(FillInActivity.this, StartActivity.class);
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog = myapi.new LoadingDialog(FillInActivity.this, "更改個人資料失敗！", true);
                                    dialog.execute();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(mActivity, result, Toast.LENGTH_LONG).show();
                        }
                    });

                }catch (NullPointerException ne){
                    ne.printStackTrace();
                }

            }
        });
        mythread.start();

    }
    String gender = "0";
    String[] GenderListStr = {"女", "男"};
    public void GenderListAlertDialog(View v) {

        AlertDialog.Builder TimeListAlertDialog = new AlertDialog.Builder(FillInActivity.this);
        TimeListAlertDialog.setTitle("請選擇性別");
        DialogInterface.OnClickListener ListItemClick = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                about_gender.setText(GenderListStr[which]);
                gender = which + "";
            }
        };

        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Nothing
            }
        };

        TimeListAlertDialog.setItems(GenderListStr, ListItemClick);
        TimeListAlertDialog.setNeutralButton("取消", OkClick);
        TimeListAlertDialog.show();
    }
    public float convertDpToPixel(float dp, Context context){
        float px = dp * getDensity(context);
        return px;
    }

    public float convertPixelToDp(float px, Context context){
        float dp = px / getDensity(context);
        return dp;
    }

    public float getDensity(Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        return metrics.density;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK ) {

        }
        return super.onKeyDown(keyCode, event);
    }

}
