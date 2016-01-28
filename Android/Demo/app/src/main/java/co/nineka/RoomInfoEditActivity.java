package co.nineka;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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


import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.nineka.util.CircleImageTransform;
import co.nineka.util.RoundSquareTransform;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

public class RoomInfoEditActivity extends Activity {
    private final String TAG = "RoomInfoActivity";
    App myapi;
    App.LoadingDialog dialog;
    Thread mythread;
    TextView create_host,create_time, create_ppl_in_need, create_category, create_cigarette, create_base, create_unit, create_circle, create_rule, room_name;
    ImageView back, photo0, photo1, photo2, photo3,info_photo;
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
        setContentView(R.layout.activity_room_info_edit);
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
        else if(bundle.getString("cigarette").equals("2")){
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



        Picasso.with(RoomInfoEditActivity.this)
                .load(R.drawable.more2)
                .transform(new RoundSquareTransform()).into(photo0);
        Picasso.with(RoomInfoEditActivity.this)
                .load(R.drawable.more2)
                .transform(new RoundSquareTransform()).into(photo1);
        Picasso.with(RoomInfoEditActivity.this)
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
                    final int vacant = Integer.parseInt(bundle.getString("people")) - a.length() + 1;
                    Picasso.with(RoomInfoEditActivity.this)
                            .load(a.getJSONObject(0).getString("photo"))
                            .transform(new CircleImageTransform()).into(info_photo);
                    if (vacant == 0) {
                        if (a.length() == 4) {
                            Picasso.with(RoomInfoEditActivity.this)
                                    .load(a.getJSONObject(0).getString("photo"))
                                    .transform(new RoundSquareTransform()).into(photo3);
                            photo3.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    try {
                                        Log.v("onClick", a.getJSONObject(0).getString("Unum"));
                                        JSONObject o_o = a.getJSONObject(0);
                                        choose_Action(o_o.getString("Unum"),
                                                o_o.getString("Uname"),
                                                o_o.getString("age"),
                                                o_o.getString("gender"),
                                                o_o.getString("rate"),
                                                o_o.getString("photo"),
                                                o_o.getString("level"),3);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            Picasso.with(RoomInfoEditActivity.this)
                                    .load(a.getJSONObject(1).getString("photo"))
                                    .transform(new RoundSquareTransform()).into(photo2);
                            photo2.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    try {
                                        Log.v("onClick", a.getJSONObject(1).getString("Unum"));
                                        JSONObject o_o = a.getJSONObject(1);
                                        choose_Action(o_o.getString("Unum"),
                                                o_o.getString("Uname"),
                                                o_o.getString("age"),
                                                o_o.getString("gender"),
                                                o_o.getString("rate"),
                                                o_o.getString("photo"),
                                                o_o.getString("level"),2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            Picasso.with(RoomInfoEditActivity.this)
                                    .load(a.getJSONObject(2).getString("photo"))
                                    .transform(new RoundSquareTransform()).into(photo1);
                            photo1.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    try {
                                        Log.v("onClick", a.getJSONObject(2).getString("Unum"));
                                        JSONObject o_o = a.getJSONObject(2);
                                        choose_Action(o_o.getString("Unum"),
                                                o_o.getString("Uname"),
                                                o_o.getString("age"),
                                                o_o.getString("gender"),
                                                o_o.getString("rate"),
                                                o_o.getString("photo"),
                                                o_o.getString("level"),1);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            Picasso.with(RoomInfoEditActivity.this)
                                    .load(a.getJSONObject(3).getString("photo"))
                                    .transform(new RoundSquareTransform()).into(photo0);
                            photo0.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    try {
                                        Log.v("onClick", a.getJSONObject(3).getString("Unum"));
                                        JSONObject o_o = a.getJSONObject(3);
                                        choose_Action(o_o.getString("Unum"),
                                                o_o.getString("Uname"),
                                                o_o.getString("age"),
                                                o_o.getString("gender"),
                                                o_o.getString("rate"),
                                                o_o.getString("photo"),
                                                o_o.getString("level"),3);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else if (a.length() == 3) {
                            Picasso.with(RoomInfoEditActivity.this)
                                    .load(a.getJSONObject(0).getString("photo"))
                                    .transform(new RoundSquareTransform()).into(photo2);
                            photo2.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    try {
                                        Log.v("onClick", a.getJSONObject(0).getString("Unum"));
                                        JSONObject o_o = a.getJSONObject(0);
                                        choose_Action(o_o.getString("Unum"),
                                                o_o.getString("Uname"),
                                                o_o.getString("age"),
                                                o_o.getString("gender"),
                                                o_o.getString("rate"),
                                                o_o.getString("photo"),
                                                o_o.getString("level"),2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            Picasso.with(RoomInfoEditActivity.this)
                                    .load(a.getJSONObject(1).getString("photo"))
                                    .transform(new RoundSquareTransform()).into(photo1);
                            photo1.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    try {
                                        Log.v("onClick", a.getJSONObject(1).getString("Unum"));
                                        JSONObject o_o = a.getJSONObject(1);
                                        choose_Action(o_o.getString("Unum"),
                                                o_o.getString("Uname"),
                                                o_o.getString("age"),
                                                o_o.getString("gender"),
                                                o_o.getString("rate"),
                                                o_o.getString("photo"),
                                                o_o.getString("level"),1);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            Picasso.with(RoomInfoEditActivity.this)
                                    .load(a.getJSONObject(2).getString("photo"))
                                    .transform(new RoundSquareTransform()).into(photo0);
                            photo0.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    try {
                                        Log.v("onClick", a.getJSONObject(2).getString("Unum"));
                                        JSONObject o_o = a.getJSONObject(2);
                                        choose_Action(o_o.getString("Unum"),
                                                o_o.getString("Uname"),
                                                o_o.getString("age"),
                                                o_o.getString("gender"),
                                                o_o.getString("rate"),
                                                o_o.getString("photo"),
                                                o_o.getString("level"),2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            photo3.setVisibility(View.GONE);
                        } else if (a.length() == 2) {
                            Picasso.with(RoomInfoEditActivity.this)
                                    .load(a.getJSONObject(0).getString("photo"))
                                    .transform(new RoundSquareTransform()).into(photo1);
                            photo1.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    try {
                                        Log.v("onClick", a.getJSONObject(0).getString("Unum"));
                                        JSONObject o_o = a.getJSONObject(0);
                                        choose_Action(o_o.getString("Unum"),
                                                o_o.getString("Uname"),
                                                o_o.getString("age"),
                                                o_o.getString("gender"),
                                                o_o.getString("rate"),
                                                o_o.getString("photo"),
                                                o_o.getString("level"),1);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            Picasso.with(RoomInfoEditActivity.this)
                                    .load(a.getJSONObject(1).getString("photo"))
                                    .transform(new RoundSquareTransform()).into(photo0);
                            photo0.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    try {
                                        Log.v("onClick", a.getJSONObject(1).getString("Unum"));
                                        JSONObject o_o = a.getJSONObject(1);
                                        choose_Action(o_o.getString("Unum"),
                                                o_o.getString("Uname"),
                                                o_o.getString("age"),
                                                o_o.getString("gender"),
                                                o_o.getString("rate"),
                                                o_o.getString("photo"),
                                                o_o.getString("level"),0);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            photo3.setVisibility(View.GONE);
                            photo2.setVisibility(View.GONE);
                        } else {

                        }
                    } else if (vacant == 1) {
                        if (a.length() == 3) {
                            Picasso.with(RoomInfoEditActivity.this)
                                    .load(a.getJSONObject(0).getString("photo"))
                                    .transform(new RoundSquareTransform()).into(photo3);
                            photo3.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    try {
                                        Log.v("onClick", a.getJSONObject(0).getString("Unum"));
                                        JSONObject o_o = a.getJSONObject(0);
                                        choose_Action(o_o.getString("Unum"),
                                                o_o.getString("Uname"),
                                                o_o.getString("age"),
                                                o_o.getString("gender"),
                                                o_o.getString("rate"),
                                                o_o.getString("photo"),
                                                o_o.getString("level"),3);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            Picasso.with(RoomInfoEditActivity.this)
                                    .load(a.getJSONObject(1).getString("photo"))
                                    .transform(new RoundSquareTransform()).into(photo2);
                            photo2.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    try {
                                        Log.v("onClick", a.getJSONObject(1).getString("Unum"));
                                        JSONObject o_o = a.getJSONObject(1);
                                        choose_Action(o_o.getString("Unum"),
                                                o_o.getString("Uname"),
                                                o_o.getString("age"),
                                                o_o.getString("gender"),
                                                o_o.getString("rate"),
                                                o_o.getString("photo"),
                                                o_o.getString("level"),2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            Picasso.with(RoomInfoEditActivity.this)
                                    .load(a.getJSONObject(2).getString("photo"))
                                    .transform(new RoundSquareTransform()).into(photo1);
                            photo1.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    try {
                                        Log.v("onClick", a.getJSONObject(2).getString("Unum"));
                                        JSONObject o_o = a.getJSONObject(2);
                                        choose_Action(o_o.getString("Unum"),
                                                o_o.getString("Uname"),
                                                o_o.getString("age"),
                                                o_o.getString("gender"),
                                                o_o.getString("rate"),
                                                o_o.getString("photo"),
                                                o_o.getString("level"),1);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else if (a.length() == 2) {
                            Picasso.with(RoomInfoEditActivity.this)
                                    .load(a.getJSONObject(0).getString("photo"))
                                    .transform(new RoundSquareTransform()).into(photo2);
                            photo2.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    try {
                                        Log.v("onClick", a.getJSONObject(0).getString("Unum"));
                                        JSONObject o_o = a.getJSONObject(0);
                                        choose_Action(o_o.getString("Unum"),
                                                o_o.getString("Uname"),
                                                o_o.getString("age"),
                                                o_o.getString("gender"),
                                                o_o.getString("rate"),
                                                o_o.getString("photo"),
                                                o_o.getString("level"),2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            Picasso.with(RoomInfoEditActivity.this)
                                    .load(a.getJSONObject(1).getString("photo"))
                                    .transform(new RoundSquareTransform()).into(photo1);
                            photo1.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    try {
                                        Log.v("onClick", a.getJSONObject(1).getString("Unum"));
                                        JSONObject o_o = a.getJSONObject(1);
                                        choose_Action(o_o.getString("Unum"),
                                                o_o.getString("Uname"),
                                                o_o.getString("age"),
                                                o_o.getString("gender"),
                                                o_o.getString("rate"),
                                                o_o.getString("photo"),
                                                o_o.getString("level"),1);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            photo3.setVisibility(View.GONE);
                        } else {
                            Picasso.with(RoomInfoEditActivity.this)
                                    .load(a.getJSONObject(0).getString("photo"))
                                    .transform(new RoundSquareTransform()).into(photo1);
                            photo1.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    try {
                                        Log.v("onClick", a.getJSONObject(0).getString("Unum"));
                                        JSONObject o_o = a.getJSONObject(0);
                                        choose_Action(o_o.getString("Unum"),
                                                o_o.getString("Uname"),
                                                o_o.getString("age"),
                                                o_o.getString("gender"),
                                                o_o.getString("rate"),
                                                o_o.getString("photo"),
                                                o_o.getString("level"),1);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            photo3.setVisibility(View.GONE);
                            photo2.setVisibility(View.GONE);
                        }
                    }
                    if (vacant == 2) {
                        if (a.length() == 2) {
                            Picasso.with(RoomInfoEditActivity.this)
                                    .load(a.getJSONObject(0).getString("photo"))
                                    .transform(new RoundSquareTransform()).into(photo3);
                            photo3.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    try {
                                        Log.v("onClick", a.getJSONObject(0).getString("Unum"));
                                        JSONObject o_o = a.getJSONObject(0);
                                        choose_Action(o_o.getString("Unum"),
                                                o_o.getString("Uname"),
                                                o_o.getString("age"),
                                                o_o.getString("gender"),
                                                o_o.getString("rate"),
                                                o_o.getString("photo"),
                                                o_o.getString("level"),3);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            Picasso.with(RoomInfoEditActivity.this)
                                    .load(a.getJSONObject(1).getString("photo"))
                                    .transform(new RoundSquareTransform()).into(photo2);
                            photo2.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    try {
                                        Log.v("onClick", a.getJSONObject(1).getString("Unum"));
                                        JSONObject o_o = a.getJSONObject(1);
                                        choose_Action(o_o.getString("Unum"),
                                                o_o.getString("Uname"),
                                                o_o.getString("age"),
                                                o_o.getString("gender"),
                                                o_o.getString("rate"),
                                                o_o.getString("photo"),
                                                o_o.getString("level"),2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else {
                            photo3.setVisibility(View.GONE);
                            Picasso.with(RoomInfoEditActivity.this)
                                    .load(a.getJSONObject(0).getString("photo"))
                                    .transform(new RoundSquareTransform()).into(photo2);
                            photo2.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    try {
                                        Log.v("onClick", a.getJSONObject(0).getString("Unum"));
                                        JSONObject o_o = a.getJSONObject(0);
                                        choose_Action(o_o.getString("Unum"),
                                                o_o.getString("Uname"),
                                                o_o.getString("age"),
                                                o_o.getString("gender"),
                                                o_o.getString("rate"),
                                                o_o.getString("photo"),
                                                o_o.getString("level"),2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    } else {
                        Picasso.with(RoomInfoEditActivity.this)
                                .load(a.getJSONObject(0).getString("photo"))
                                .transform(new RoundSquareTransform()).into(photo3);
                        photo3.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                try {
                                    Log.v("onClick", a.getJSONObject(0).getString("Unum"));
                                    JSONObject o_o = a.getJSONObject(0);
                                    choose_Action(o_o.getString("Unum"),
                                            o_o.getString("Uname"),
                                            o_o.getString("age"),
                                            o_o.getString("gender"),
                                            o_o.getString("rate"),
                                            o_o.getString("photo"),
                                            o_o.getString("level"),3);
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
    boolean isHost;
    String[] ChooseListStr = {"查 看", "踢 除"};
    private void choose_Action(final String Unum, final String Uname, final String age, final String gender,
                               final String rate, final String photo, final String level, final int position){

        if(pref.getString("num","").equals(Unum)){
            isHost = true;
        }else{
            isHost = false;
        }
        if(ConversationActivity.isGuest||isHost){
            Intent i = new Intent(RoomInfoEditActivity.this , MemberActivity3.class);
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
            return;
        }


        AlertDialog.Builder TimeListAlertDialog = new AlertDialog.Builder(this);
        TimeListAlertDialog.setTitle("請選擇操作");
        DialogInterface.OnClickListener ListItemClick = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(which == 1 || isHost){
                    //TODO DeleteMember
                    DeleteMember(Unum, position);
                }else{
                    Intent i = new Intent(RoomInfoEditActivity.this , MemberActivity3.class);
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
            }
        };

        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Nothing
            }
        };

        TimeListAlertDialog.setItems(ChooseListStr, ListItemClick);

        TimeListAlertDialog.setNeutralButton("取消", OkClick);
        TimeListAlertDialog.show();

    }


    private void DeleteMember(final String Unum, final int position) {
        mythread = new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog = myapi.new LoadingDialog(RoomInfoEditActivity.this, "請稍後...", false);
                        dialog.execute();
                    }
                });
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("user_ID", Unum));
                params.add(new BasicNameValuePair("room_ID", bundle.getString("RoomNum")));

                String result = myapi.postMethod_getCode(RoomInfoEditActivity.this, App.kickMember, params);
                Log.v("RoomInfoEditActivity", result);
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
                                switch(position){
                                    case 0:
                                        //...
                                        Picasso.with(RoomInfoEditActivity.this)
                                                .load(R.drawable.more2)
                                                .transform(new RoundSquareTransform()).into(photo0);
                                        photo0.setEnabled(false);
                                        break;
                                    case 1:
                                        Picasso.with(RoomInfoEditActivity.this)
                                                .load(R.drawable.more2)
                                                .transform(new RoundSquareTransform()).into(photo1);
                                        photo1.setEnabled(false);
                                        break;
                                    case 2:
                                        Picasso.with(RoomInfoEditActivity.this)
                                                .load(R.drawable.more2)
                                                .transform(new RoundSquareTransform()).into(photo2);
                                        photo2.setEnabled(false);
                                        break;
                                    case 3:
                                        break;
                                }
                                dialog = myapi.new LoadingDialog(RoomInfoEditActivity.this, "請稍後...", true);
                                dialog.execute();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog = myapi.new LoadingDialog(RoomInfoEditActivity.this, "伺服器發生錯誤！", true);
                            dialog.execute();
                        }
                    });
                }

            }
        });
        mythread.start();



    }


}
