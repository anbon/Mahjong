package com.example.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

public class CreateRoomActivity extends Activity {
    private final String TAG = "CreateRoomActivity";
    App myapi;
    App.LoadingDialog dialog;
    Thread mythread;
    EditText base, unit, circle, rule, location;
    TextView create_time, create_ppl_in_need, create_category, create_cigarette;
    SharedPreferences pref;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_create_room);
        pref = getSharedPreferences("Account", 0);
        location = (EditText) findViewById(R.id.create_location);
        base = (EditText) findViewById(R.id.create_base);
        unit = (EditText) findViewById(R.id.create_unit);
        circle = (EditText) findViewById(R.id.create_circle);
        rule = (EditText) findViewById(R.id.create_rule);
        create_time = (TextView) findViewById(R.id.create_time);
        create_ppl_in_need = (TextView) findViewById(R.id.create_ppl_in_need);
        create_category = (TextView) findViewById(R.id.create_category);
        create_cigarette = (TextView) findViewById(R.id.create_cigarette);
        myapi = (App) this.getApplicationContext();

        rule.addTextChangedListener(new TextWatcher() {
            String lastdata = "";// record last time edittext

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                try {
                    //if Edittext1 line more than 2 ,then back to last record
                    if (rule.getLineCount() > 5)
                    {
                        rule.setText(lastdata);
                        rule.setSelection(lastdata.length());// set cursor to last word
                    }
                    else {
                        lastdata = text.toString();//save this time change
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
                // first time edittext changed
                lastdata = s.toString();

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        TimeListStr = new String[24];
        for(int i=0 ; i<24; i++){
            if(i<12){
                if(i!=0)
                    TimeListStr[i] = i+"pm";
                else
                    TimeListStr[i] = "12pm";
            }
            else{
                if(i!=12)
                    TimeListStr[i] = (i-12)+"am";
                else
                    TimeListStr[i] = "12am";
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_room, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void focus_base(View v) {
        //base.setText("");
        base.requestFocus();
        InputMethodManager imm = (InputMethodManager) base.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }
    public void focus_unit(View v) {
        //unit.setText("");
        unit.requestFocus();
        InputMethodManager imm = (InputMethodManager) unit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }
    public void focus_circle(View v) {
        //circle.setText("");
        circle.requestFocus();
        InputMethodManager imm = (InputMethodManager) circle.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }
    public void focus_rule(View v) {
        rule.requestFocus();
        InputMethodManager imm = (InputMethodManager) rule.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    String[] TimeListStr = {};
    public void TimeListAlertDialog(View v) {



        AlertDialog.Builder TimeListAlertDialog = new AlertDialog.Builder(this);
        TimeListAlertDialog.setTitle("請選擇時間");
        DialogInterface.OnClickListener ListItemClick = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                create_time.setText(TimeListStr[which]);
            }
        };

        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Nothing
            }
        };

        TimeListAlertDialog.setItems(TimeListStr, ListItemClick);
        TimeListAlertDialog.setNeutralButton("取消", OkClick);
        TimeListAlertDialog.show();

    }
    String[] PeopleListStr = {"1", "2", "3"};

    public void PeopleListAlertDialog(View v) {



        AlertDialog.Builder TimeListAlertDialog = new AlertDialog.Builder(this);
        TimeListAlertDialog.setTitle("請選擇人數");
        DialogInterface.OnClickListener ListItemClick = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                create_ppl_in_need.setText(PeopleListStr[which]);
            }
        };

        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Nothing
            }
        };

        TimeListAlertDialog.setItems(PeopleListStr, ListItemClick);
        TimeListAlertDialog.setNeutralButton("取消", OkClick);
        TimeListAlertDialog.show();

    }
    String type = "0";
    String[] CategoryListStr = {"手動", "電動"};
    public void CategoryListAlertDialog(View v) {



        AlertDialog.Builder TimeListAlertDialog = new AlertDialog.Builder(this);
        TimeListAlertDialog.setTitle("請選擇桌型");
        DialogInterface.OnClickListener ListItemClick = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                create_category.setText(CategoryListStr[which]);
                type = which+"";
            }
        };

        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Nothing
            }
        };

        TimeListAlertDialog.setItems(CategoryListStr, ListItemClick);
        TimeListAlertDialog.setNeutralButton("取消", OkClick);
        TimeListAlertDialog.show();

    }

    String[] CigaretteListStr = {"不菸", "桌菸"};
    String cigarette = "0";
    public void CigaretteListAlertDialog(View v) {



        AlertDialog.Builder TimeListAlertDialog = new AlertDialog.Builder(this);
        TimeListAlertDialog.setTitle("請選擇菸");
        DialogInterface.OnClickListener ListItemClick = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                create_cigarette.setText(CigaretteListStr[which]);
                cigarette = which + "";
            }
        };

        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Nothing
            }
        };

        TimeListAlertDialog.setItems(CigaretteListStr, ListItemClick);
        TimeListAlertDialog.setNeutralButton("取消", OkClick);
        TimeListAlertDialog.show();

    }
    public void cancel(View v) {
        finish();
    }

    String room_id = "";
    @SuppressWarnings("deprecation")
    public void confirm(View v) {

        if (RongIM.getInstance() != null && CheckIsComplete()) {
            final SharedPreferences pref = getSharedPreferences("Account", 0 );
            mythread = new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog = myapi.new LoadingDialog(CreateRoomActivity.this, "建立房間中...", false);
                            dialog.execute();
                        }
                    });
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("userid", pref.getString("num", "")));

                    params.add(new BasicNameValuePair("base", Integer.parseInt(base.getText().toString())+""));
                    params.add(new BasicNameValuePair("unit", Integer.parseInt(unit.getText().toString())+""));
                    params.add(new BasicNameValuePair("circle", Integer.parseInt(circle.getText().toString())+""));
                    params.add(new BasicNameValuePair("time", create_time.getText().toString()));
                    params.add(new BasicNameValuePair("name", location.getText().toString()));
                    params.add(new BasicNameValuePair("people", create_ppl_in_need.getText().toString()));
                    params.add(new BasicNameValuePair("type", type));
                    params.add(new BasicNameValuePair("cigarette", cigarette));
                    params.add(new BasicNameValuePair("rule", rule.getText().toString()));
                    String result = myapi.postMethod_getCode(CreateRoomActivity.this, App.Createchat, params);
                    Log.v(TAG, "result = " + result);
                    JSONObject o;
                    try {
                        o = new JSONObject(result);
                        room_id = o.getString("RoomNum");
                        if (RongIM.getInstance() != null) {
                            RongIM.getInstance().getRongIMClient().joinGroup(room_id, location.getText().toString(), new RongIMClient.OperationCallback() {

                                @Override
                                public void onSuccess() {
                                    ConversationActivity.isGuest=false;
                                    RongIM.getInstance().refreshUserInfoCache(new UserInfo(pref.getString("num","0"), pref.getString("name",""), Uri.parse(pref.getString("photo",""))));
                                    //RongIM.getInstance().refreshGroupInfoCache(new Group(room_id, location.getText().toString(), Uri.parse("http://rongcloud-web.qiniudn.com/docs_demo_rongcloud_logo.png")));
                                    RongIM.getInstance().startGroupChat(CreateRoomActivity.this, room_id, location.getText().toString());
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {

                                }
                            });

                            //RongIM.getInstance().startConversation(CreateRoomActivity.this, Conversation.ConversationType.CHATROOM, "9527", "這是測試");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.close();
                        }
                    });

                }
            });
            mythread.start();

        }
    }
    private boolean CheckIsComplete() {
        if(base.getText().toString().isEmpty() ||
                unit.getText().toString().isEmpty() ||
                circle.getText().toString().isEmpty() ||
                location.getText().toString().isEmpty() ||
                create_ppl_in_need.getText().toString().equals("人 數"))

        {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog = myapi.new LoadingDialog(CreateRoomActivity.this, "您還有欄位尚未填寫！", true);
                    dialog.execute();
                }
            });
            return false;
        }else if(Integer.parseInt(base.getText().toString())==0 ||
                Integer.parseInt(unit.getText().toString())==0 ||
                Integer.parseInt(circle.getText().toString())==0 ){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog = myapi.new LoadingDialog(CreateRoomActivity.this, "金額不得為0！", true);
                    dialog.execute();
                }
            });
        }

        return true;
    }
}
