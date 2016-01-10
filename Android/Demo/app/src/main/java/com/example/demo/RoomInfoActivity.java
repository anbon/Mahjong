package com.example.demo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.demo.util.CircleImageTransform;
import com.example.demo.util.RoundSquareTransform;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

public class RoomInfoActivity extends Activity {
    private final String TAG = "RoomInfoActivity";
    App myapi;
    App.LoadingDialog dialog;
    Thread mythread;
    TextView create_host,create_time, create_ppl_in_need, create_category, create_cigarette, create_base, create_unit, create_circle, create_rule, room_name;
    ImageView photo0, photo1, photo2, photo3,info_photo;
    SharedPreferences pref;
    Bundle bundle;
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
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
        //
        if(!this.isFinishing())
            dialog.close();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_room_info);
        create_time = (TextView) findViewById(R.id.create_time);
        create_ppl_in_need = (TextView) findViewById(R.id.create_ppl_in_need);
        create_category = (TextView) findViewById(R.id.create_category);
        create_cigarette = (TextView) findViewById(R.id.create_cigarette);
        create_base = (TextView) findViewById(R.id.create_base);
        create_unit = (TextView) findViewById(R.id.create_unit);
        create_circle = (TextView) findViewById(R.id.create_circle);
        create_rule = (TextView) findViewById(R.id.create_rule);
        create_host = (TextView) findViewById(R.id.create_host);
        room_name = (TextView) findViewById(R.id.room_name);
        photo0 = (ImageView) findViewById(R.id.photo0);
        photo1 = (ImageView) findViewById(R.id.photo1);
        photo2 = (ImageView) findViewById(R.id.photo2);
        photo3 = (ImageView) findViewById(R.id.photo3);
        info_photo = (ImageView) findViewById(R.id.info_photo);
        myapi = (App) this.getApplicationContext();
        pref = getSharedPreferences("Account", 0);
        bundle = getIntent().getExtras();

        create_time.setText(bundle.getString("time"));
        create_host.setText(bundle.getString("name"));
        room_name.setText(bundle.getString("location"));
        create_ppl_in_need.setText(bundle.getString("people"));
        create_base.setText(bundle.getString("base"));
        if(bundle.getString("cigarette").equals("0"))
            create_cigarette.setText("不菸");
        else if(bundle.getString("cigarette").equals("1")){
            create_cigarette.setText("桌菸");
        }
        else{
            create_cigarette.setText("雀菸");
        }
        if(bundle.getString("type").equals("0"))
            create_category.setText("手動");
        else{
            create_category.setText("電動");
        }
        create_unit.setText(bundle.getString("unit"));
        create_circle.setText(bundle.getString("circle"));
        create_rule.setText(bundle.getString("rule", ""));



        Picasso.with(RoomInfoActivity.this)
                .load(R.drawable.more2)
                .transform(new RoundSquareTransform()).into(photo0);
        Picasso.with(RoomInfoActivity.this)
                .load(R.drawable.more2)
                .transform(new RoundSquareTransform()).into(photo1);
        Picasso.with(RoomInfoActivity.this)
                .load(R.drawable.more2)
                .transform(new RoundSquareTransform()).into(photo2);


        parseResult(bundle.getString("users"));


    }
    @SuppressWarnings("deprecation")
    private void parseResult(final String result) {

        Log.i(TAG, "users : " + result);

        //TODO Temporary

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                photo1.setVisibility(View.VISIBLE);

                try {
                    final JSONArray a = new JSONArray(result);
                    final int vacant = Integer.parseInt(bundle.getString("people")) - a.length()+1;
                    Picasso.with(RoomInfoActivity.this)
                            .load(a.getJSONObject(0).getString("photo"))
                            .transform(new CircleImageTransform()).into(info_photo);
                        if (vacant==1) {
                            if(a.length()==3) {
                                Picasso.with(RoomInfoActivity.this)
                                        .load(a.getJSONObject(0).getString("photo"))
                                        .transform(new RoundSquareTransform()).into(photo3);
                                photo3.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            Log.v("onClick", a.getJSONObject(0).getString("Unum"));
                                            JSONObject o_o = a.getJSONObject(0);
                                            gotoMemberActivity2(o_o.getString("Unum"),
                                                    o_o.getString("Uname"),
                                                    o_o.getString("age"),
                                                    o_o.getString("gender"),
                                                    o_o.getString("rate"),
                                                    o_o.getString("photo"),
                                                    o_o.getString("level"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                Picasso.with(RoomInfoActivity.this)
                                        .load(a.getJSONObject(1).getString("photo"))
                                        .transform(new RoundSquareTransform()).into(photo2);
                                photo2.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            Log.v("onClick", a.getJSONObject(1).getString("Unum"));
                                            JSONObject o_o = a.getJSONObject(1);
                                            gotoMemberActivity2(o_o.getString("Unum"),
                                                    o_o.getString("Uname"),
                                                    o_o.getString("age"),
                                                    o_o.getString("gender"),
                                                    o_o.getString("rate"),
                                                    o_o.getString("photo"),
                                                    o_o.getString("level"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                Picasso.with(RoomInfoActivity.this)
                                        .load(a.getJSONObject(2).getString("photo"))
                                        .transform(new RoundSquareTransform()).into(photo1);
                                photo1.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            Log.v("onClick", a.getJSONObject(2).getString("Unum"));
                                            JSONObject o_o = a.getJSONObject(2);
                                            gotoMemberActivity2(o_o.getString("Unum"),
                                                    o_o.getString("Uname"),
                                                    o_o.getString("age"),
                                                    o_o.getString("gender"),
                                                    o_o.getString("rate"),
                                                    o_o.getString("photo"),
                                                    o_o.getString("level"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                            else if(a.length()==2){
                                Picasso.with(RoomInfoActivity.this)
                                        .load(a.getJSONObject(0).getString("photo"))
                                        .transform(new RoundSquareTransform()).into(photo2);
                                photo2.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            Log.v("onClick", a.getJSONObject(0).getString("Unum"));
                                            JSONObject o_o = a.getJSONObject(0);
                                            gotoMemberActivity2(o_o.getString("Unum"),
                                                    o_o.getString("Uname"),
                                                    o_o.getString("age"),
                                                    o_o.getString("gender"),
                                                    o_o.getString("rate"),
                                                    o_o.getString("photo"),
                                                    o_o.getString("level"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                Picasso.with(RoomInfoActivity.this)
                                        .load(a.getJSONObject(1).getString("photo"))
                                        .transform(new RoundSquareTransform()).into(photo1);
                                photo1.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            Log.v("onClick", a.getJSONObject(1).getString("Unum"));
                                            JSONObject o_o = a.getJSONObject(1);
                                            gotoMemberActivity2(o_o.getString("Unum"),
                                                    o_o.getString("Uname"),
                                                    o_o.getString("age"),
                                                    o_o.getString("gender"),
                                                    o_o.getString("rate"),
                                                    o_o.getString("photo"),
                                                    o_o.getString("level"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                photo3.setVisibility(View.GONE);
                            }else{
                                Picasso.with(RoomInfoActivity.this)
                                        .load(a.getJSONObject(0).getString("photo"))
                                        .transform(new RoundSquareTransform()).into(photo1);
                                photo1.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            Log.v("onClick", a.getJSONObject(0).getString("Unum"));
                                            JSONObject o_o = a.getJSONObject(0);
                                            gotoMemberActivity2(o_o.getString("Unum"),
                                                    o_o.getString("Uname"),
                                                    o_o.getString("age"),
                                                    o_o.getString("gender"),
                                                    o_o.getString("rate"),
                                                    o_o.getString("photo"),
                                                    o_o.getString("level"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                photo3.setVisibility(View.GONE);
                                photo2.setVisibility(View.GONE);
                            }
                        }
                        if (vacant==2) {
                            if(a.length()==2){
                                Picasso.with(RoomInfoActivity.this)
                                        .load(a.getJSONObject(0).getString("photo"))
                                        .transform(new RoundSquareTransform()).into(photo3);
                                photo3.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            Log.v("onClick", a.getJSONObject(0).getString("Unum"));
                                            JSONObject o_o = a.getJSONObject(0);
                                            gotoMemberActivity2(o_o.getString("Unum"),
                                                    o_o.getString("Uname"),
                                                    o_o.getString("age"),
                                                    o_o.getString("gender"),
                                                    o_o.getString("rate"),
                                                    o_o.getString("photo"),
                                                    o_o.getString("level"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                Picasso.with(RoomInfoActivity.this)
                                        .load(a.getJSONObject(1).getString("photo"))
                                        .transform(new RoundSquareTransform()).into(photo2);
                                photo2.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            Log.v("onClick", a.getJSONObject(1).getString("Unum"));
                                            JSONObject o_o = a.getJSONObject(1);
                                            gotoMemberActivity2(o_o.getString("Unum"),
                                                    o_o.getString("Uname"),
                                                    o_o.getString("age"),
                                                    o_o.getString("gender"),
                                                    o_o.getString("rate"),
                                                    o_o.getString("photo"),
                                                    o_o.getString("level"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }else{
                                photo3.setVisibility(View.GONE);
                                Picasso.with(RoomInfoActivity.this)
                                        .load(a.getJSONObject(0).getString("photo"))
                                        .transform(new RoundSquareTransform()).into(photo2);
                                photo2.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            Log.v("onClick", a.getJSONObject(0).getString("Unum"));
                                            JSONObject o_o = a.getJSONObject(0);
                                            gotoMemberActivity2(o_o.getString("Unum"),
                                                    o_o.getString("Uname"),
                                                    o_o.getString("age"),
                                                    o_o.getString("gender"),
                                                    o_o.getString("rate"),
                                                    o_o.getString("photo"),
                                                    o_o.getString("level"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }else{
                            Picasso.with(RoomInfoActivity.this)
                                    .load(a.getJSONObject(0).getString("photo"))
                                    .transform(new RoundSquareTransform()).into(photo3);
                            photo3.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    try {
                                        Log.v("onClick", a.getJSONObject(0).getString("Unum"));
                                        JSONObject o_o = a.getJSONObject(0);
                                        gotoMemberActivity2(o_o.getString("Unum"),
                                                            o_o.getString("Uname"),
                                                            o_o.getString("age"),
                                                            o_o.getString("gender"),
                                                            o_o.getString("rate"),
                                                            o_o.getString("photo"),
                                                            o_o.getString("level"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }


                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        });

    }
    private void gotoMemberActivity2(String Unum, String Uname, String age, String gender, String rate, String photo, String level){
        Intent i = new Intent(this , MemberActivity3.class);
        Bundle b = new Bundle();
        b.putString("Unum",Unum);
        b.putString("Uname",Uname);
        b.putString("age",age);
        b.putString("gender",gender);
        b.putString("rate",rate);
        b.putString("photo",photo);
        b.putString("level",level);
        i.putExtras(b);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
    private Handler handler = new Handler( );
    private Runnable runnable = new Runnable( ) {
        public void run ( ) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_ID", pref.getString("num", "")));
            params.add(new BasicNameValuePair("room_ID", bundle.getString("RoomNum")));
            String result = myapi.postMethod_getCode(RoomInfoActivity.this, App.waitChat, params);
            Log.v("waitChat", result);
            try {
                JSONObject o = new JSONObject(result);
                if(o.getString("status").equals("0")){
                    if(dialog.alertd.isShowing())
                        handler.postDelayed(this, 5000);
                    else
                        handler.removeCallbacks(this);

                }else if(o.getString("status").equals("1")){
                    handler.removeCallbacks(runnable);
                    if(!RoomInfoActivity.this.isFinishing() && dialog.alertd.isShowing())
                        enterRongChatRoom(); //進入Rong
                }else if(o.getString("status").equals("2")){
                    handler.removeCallbacks(runnable);
                    if(dialog.alertd.isShowing()) {
                        dialog.close();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog = myapi.new LoadingDialog(RoomInfoActivity.this, "請求被拒", true);
                                if (!RoomInfoActivity.this.isFinishing())
                                    dialog.execute();
                            }
                        });
                    }
                }
            } catch (JSONException e) {
                dialog.close();
                handler.removeCallbacks(runnable); //停止Timer
            }


        }
    };
    public void confirm(View v){
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        mythread = new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog = myapi.new LoadingDialog(RoomInfoActivity.this, "送出請求", false);
                        dialog.execute();
                    }
                });
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("user_ID", pref.getString("num", "")));
                params.add(new BasicNameValuePair("room_ID", bundle.getString("RoomNum")));
                String result = myapi.postMethod_getCode(RoomInfoActivity.this, App.applyChat, params);
                Log.v("applyChat", result);
                try {
                    final JSONObject o = new JSONObject(result);
                    if(o.getString("status").equals("0")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.close();
                                try {
                                    dialog = myapi.new LoadingDialog(RoomInfoActivity.this, o.getString("message"), true);
                                    dialog.execute();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }else if(o.getString("status").equals("1")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.close();
                                dialog = myapi.new LoadingDialog(RoomInfoActivity.this, "等待回復", true);
                                if (!RoomInfoActivity.this.isFinishing())
                                    dialog.execute();
                            }
                        });
                        handler.postDelayed(runnable, 1000);
                    }
                } catch (JSONException e) {
                    dialog.close();
                    e.printStackTrace();
                }

            }
        });
        mythread.start();



    }
    private void enterRongChatRoom(){
        //TODO 進入Rong聊天室
        RongIM.getInstance().getRongIMClient().joinGroup(bundle.getString("RoomNum"), bundle.getString("location"), new RongIMClient.OperationCallback() {

            @Override
            public void onSuccess() {
                ConversationActivity.isGuest = true;
                RongIM.getInstance().refreshUserInfoCache(new UserInfo(pref.getString("num","0"), pref.getString("name",""), Uri.parse(pref.getString("photo",""))));
                RongIM.getInstance().startGroupChat(RoomInfoActivity.this, bundle.getString("RoomNum"), bundle.getString("location"));
                finish();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_room_info, menu);
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
}
