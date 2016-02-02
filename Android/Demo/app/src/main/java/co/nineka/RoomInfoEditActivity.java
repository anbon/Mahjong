package co.nineka;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
    TextView create_host,create_time, create_ppl_in_need, create_category, create_cigarette, create_base, create_unit, create_circle, create_rule, room_name, delete_room_btn;
    RelativeLayout create_ppl_in_need_btn;
    private Intent intent = new Intent("co.nineka.RECEIVER");
    ImageView back, photo0, photo1, photo2, photo3,info_photo;
    SharedPreferences pref;
    Bundle bundle;
    Activity mActivity;
    String usersArray, mPeople;
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
        delete_room_btn = (TextView) findViewById(R.id.delete_room_btn);
        back = (ImageView) findViewById(R.id.back);
        photo0 = (ImageView) findViewById(R.id.photo0);
        photo1 = (ImageView) findViewById(R.id.photo1);
        photo2 = (ImageView) findViewById(R.id.photo2);
        photo3 = (ImageView) findViewById(R.id.photo3);
        info_photo = (ImageView) findViewById(R.id.info_photo);
        create_ppl_in_need_btn = (RelativeLayout) findViewById(R.id.create_ppl_in_need_btn);
        myapi = (App) this.getApplicationContext();
        pref = getSharedPreferences("Account", 0);
        bundle = getIntent().getExtras();
        mActivity = this;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
        create_time.setText(bundle.getString("time"));
        create_host.setText(bundle.getString("name"));
        room_name.setText(bundle.getString("location"));
        if(!ConversationActivity.isGuest){
            room_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChangeTitle();
                }
            });
        }
        create_ppl_in_need.setText(bundle.getString("people"));
        if(!ConversationActivity.isGuest) {
            create_ppl_in_need_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PeopleListAlertDialog();
                }
            });
        }
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
        if(!ConversationActivity.isGuest)
            delete_room_btn.setVisibility(View.VISIBLE);


        usersArray = bundle.getString("users");
        mPeople = bundle.getString("people");
        parseResult(bundle.getString("users"), bundle.getString("people"));


    }



    @SuppressWarnings("deprecation")
    private void parseResult(final String result, final String people) {

        Log.i(TAG, "users : " + result);

        //TODO Temporary

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                photo3.setVisibility(View.VISIBLE);
                photo2.setVisibility(View.VISIBLE);
                Picasso.with(RoomInfoEditActivity.this)
                        .load(R.drawable.more2)
                        .transform(new RoundSquareTransform()).into(photo0);
                Picasso.with(RoomInfoEditActivity.this)
                        .load(R.drawable.more2)
                        .transform(new RoundSquareTransform()).into(photo1);
                Picasso.with(RoomInfoEditActivity.this)
                        .load(R.drawable.more2)
                        .transform(new RoundSquareTransform()).into(photo2);
                try {
                    final JSONArray a = new JSONArray(result);
                    final int vacant = Integer.parseInt(people) - a.length() + 1;
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
                                                o_o.getString("level"),0);
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
                                                o_o.getString("level"),0);
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
                            Log.v("parseUsersJson", "4人房缺1人");
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
                                                o_o.getString("level"), 3);
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
                                                o_o.getString("level"), 2);
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
                                                o_o.getString("level"), 1);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            photo0.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Do Nothing
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
                                                o_o.getString("level"), 2);
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
                                                o_o.getString("level"), 1);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            photo0.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Do Nothing
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
                                                o_o.getString("level"), 1);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            photo0.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Do Nothing
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
                                                o_o.getString("level"), 3);
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
                                                o_o.getString("level"), 2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            photo1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Do Nothing
                                }
                            });
                            photo0.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Do Nothing
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
                                                o_o.getString("level"), 2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            photo1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Do Nothing
                                }
                            });
                            photo0.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Do Nothing
                                }
                            });

                        }
                    } else if(vacant == 3){
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
                                            o_o.getString("level"), 3);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        photo2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Do Nothing
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
                Log.v("kickMember", "position = " + position +"\n" +result);
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
                                        /*Picasso.with(RoomInfoEditActivity.this)
                                                .load(R.drawable.more2)
                                                .transform(new RoundSquareTransform()).into(photo0);
                                        photo0.setEnabled(false);*/
                                        if(mPeople.equals("3"))
                                            usersArray = KickMember(usersArray,3);
                                        else if(mPeople.equals("2"))
                                            usersArray = KickMember(usersArray,2);
                                        else if(mPeople.equals("1"))
                                            usersArray = KickMember(usersArray,1);
                                        //mPeople = (Integer.parseInt(mPeople)-1)+"";

                                        parseResult(usersArray, mPeople);

                                        break;
                                    case 1:
                                        /*Picasso.with(RoomInfoEditActivity.this)
                                                .load(R.drawable.more2)
                                                .transform(new RoundSquareTransform()).into(photo1);
                                        photo1.setEnabled(false);*/
                                        if(mPeople.equals("3"))
                                            usersArray = KickMember(usersArray,2);
                                        else if(mPeople.equals("2"))
                                            usersArray = KickMember(usersArray,1);

                                        //mPeople = (Integer.parseInt(mPeople)-1)+"";

                                        parseResult(usersArray, mPeople);
                                        break;
                                    case 2:
                                        /*Picasso.with(RoomInfoEditActivity.this)
                                                .load(R.drawable.more2)
                                                .transform(new RoundSquareTransform()).into(photo2);
                                        photo2.setEnabled(false);*/
                                        usersArray = KickMember(usersArray,1);
                                        //mPeople = (Integer.parseInt(mPeople)-1)+"";

                                        parseResult(usersArray, mPeople);
                                        break;
                                    case 3:
                                        break;
                                }
                                dialog = myapi.new LoadingDialog(RoomInfoEditActivity.this, "已將該成員剔除！", true);
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

    private String KickMember(String usersArray, int which) {
        try {
            JSONArray a = new JSONArray(usersArray);
            JSONArray b = new JSONArray();
            Log.v("which", which+"");
            for(int i = 0; i <  a.length(); i++){
                if(which != i){
                    Log.v("retain", a.getJSONObject(i).getString("Uname"));
                    b.put(a.getJSONObject(i));
                }
            }
            Log.v("KickMember", b.toString());
            return b.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return usersArray;
    }

    Dialog alertd;
    EditText change_title_edit;
    TextView change_title_confirm;
    private void ChangeTitle() {
        alertd = new Dialog(this);
        alertd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertd.setContentView(R.layout.change_title_dialog);
        alertd.setCanceledOnTouchOutside(true);
        alertd.setCancelable(true);
        Window dialogWindow = alertd.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //dialogWindow.setGravity(Gravity.CENTER | Gravity.TOP);
        View v = dialogWindow.getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        lp.dimAmount = 0.5f;

        lp.y = -200; // 新位置Y坐标
        lp.alpha = 1f;
        change_title_edit = (EditText) alertd.findViewById(R.id.change_title_edit);
        change_title_confirm = (TextView) alertd.findViewById(R.id.change_title_confirm);

        change_title_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(change_title_edit.getText().toString().trim().isEmpty()){
                    Toast.makeText(v.getContext(),"房間名稱不得為空！", Toast.LENGTH_SHORT).show();
                }else{
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
                            params.add(new BasicNameValuePair("title", change_title_edit.getText().toString()));
                            params.add(new BasicNameValuePair("room_ID", bundle.getString("RoomNum")));

                            String result = myapi.postMethod_getCode(RoomInfoEditActivity.this, App.changeTitle, params);
                            Log.v("changeTitle", result);
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
                                            room_name.setText(change_title_edit.getText().toString());
                                            dialog = myapi.new LoadingDialog(RoomInfoEditActivity.this, "成功更改房間名稱！", true);
                                            dialog.execute();
                                        }
                                    });
                                    intent.putExtra("json", "{\"type\":11,\"title\":\""+change_title_edit.getText().toString()+"\"}");
                                    RoomInfoEditActivity.this.sendBroadcast(intent);
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

                    alertd.cancel();
                    alertd.dismiss();
                }

            }
        });

        alertd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(room_name.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        alertd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        alertd.show();
    }
    String[] PeopleListStr = {"1", "2", "3"};

    JSONArray a;
    private void PeopleListAlertDialog() {
        try {
            a = new JSONArray(usersArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AlertDialog.Builder TimeListAlertDialog = new AlertDialog.Builder(this);
        TimeListAlertDialog.setTitle("請選擇人數");
        DialogInterface.OnClickListener ListItemClick = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface mdialog, final int which) {

                //TODO
                if(mPeople.equals(PeopleListStr[which]))
                    return;
                if(Integer.parseInt(PeopleListStr[which])< a.length()-1){
                    Toast.makeText(RoomInfoEditActivity.this, "缺人數不得小於房間內現有人數！",Toast.LENGTH_SHORT).show();
                    return;
                }
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
                        params.add(new BasicNameValuePair("room_ID", bundle.getString("RoomNum")));
                        params.add(new BasicNameValuePair("people", PeopleListStr[which]));

                        String result = myapi.postMethod_getCode(RoomInfoEditActivity.this, App.changePeople, params);
                        Log.v("changePeople", result);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.close();
                            }
                        });
                        try {
                            final JSONObject o = new JSONObject(result);

                            if(o.getString("status").equals("1")){
                                mPeople = PeopleListStr[which];

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        parseResult(usersArray, mPeople);
                                        create_ppl_in_need.setText(PeopleListStr[which]);
                                    }
                                });
                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                    try {
                                        dialog = myapi.new LoadingDialog(RoomInfoEditActivity.this, o.getString("message"), true);
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
                                    dialog = myapi.new LoadingDialog(RoomInfoEditActivity.this, "伺服器發生錯誤！", true);
                                    dialog.execute();
                                }
                            });
                        }
                    }
                });
                mythread.start();

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

    public void Share(View v){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "九咖");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, myapi.Share + "/" + bundle.getString("RoomNum"));

        startActivity(Intent.createChooser(sharingIntent, "分享此房間連結到..."));
    }

    public void DeleteRoom(View v){

        new AlertDialog.Builder(RoomInfoEditActivity.this)
                .setTitle("警告")
                .setMessage("確定要刪除本房間？")
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(), "000", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface mdialog, int which) {
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
                                params.add(new BasicNameValuePair("room_ID", bundle.getString("RoomNum")));

                                String result = myapi.postMethod_getCode(RoomInfoEditActivity.this, App.DeleteRoom, params);
                                Log.v("DeleteRoom", result);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.close();
                                    }
                                });
                                try {
                                    final JSONObject o = new JSONObject(result);
                                    if (o.getString("status").equals("1")) {
                                        intent.putExtra("json", "{\"type\":22}");
                                        RoomInfoEditActivity.this.sendBroadcast(intent);
                                        RoomInfoEditActivity.this.finish();
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
                })
                .show();
    }



}
