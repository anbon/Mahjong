package com.example.demo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
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
    public void confirm(View v){
        RongIM.getInstance().getRongIMClient().joinGroup(bundle.getString("RoomNum"), bundle.getString("location"), new RongIMClient.OperationCallback() {

            @Override
            public void onSuccess() {
                RongIM.getInstance().refreshUserInfoCache(new UserInfo(pref.getString("num","0"), pref.getString("name",""), Uri.parse(pref.getString("photo",""))));
                //RongIM.getInstance().refreshGroupInfoCache(new Group(bundle.getString("RoomNum"),  bundle.getString("location"), Uri.parse("http://rongcloud-web.qiniudn.com/docs_demo_rongcloud_logo.png")));
                RongIM.getInstance().startGroupChat(RoomInfoActivity.this , bundle.getString("RoomNum"),  bundle.getString("location"));
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
