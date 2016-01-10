package com.example.demo;

/**
 * Created by x51811danny on 2015/10/18.
 */
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.demo.util.CircleImageTransform;
import com.example.demo.util.RoundSquareTransform;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.rong.imkit.CustomizeMessageItemProvider;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by Bob on 2015/4/16.
 */
public class ConversationActivity extends FragmentActivity  {

    private static final String TAG = ConversationActivity.class.getSimpleName();
    public static boolean isAlive, isGuest;
    App myapi;
    App.LoadingDialog dialog;
    Thread mythread;
    SharedPreferences pref;

    /**
     * 目标 Id
     */
    private String mTargetId;

    /**
     * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
     */
    private String mTargetIds;
    @Override
    public void onDestroy() {
        isAlive = false;
        handler.removeCallbacks(runnable);
        dhandler.removeCallbacks(drunnable);
        super.onDestroy();
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
        if (mythread != null) {
            mythread.interrupt();
            mythread = null;
        }
        //
        //handler.removeCallbacks(runnable, 5000);
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
        dhandler.removeCallbacks(drunnable);
        dhandler.postDelayed(drunnable, 1000);
        if(!isGuest) {
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable, 2500);
        }
        //
        /*handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 2000);*/

    }
    /**
        * 会话类型
        */
    private Conversation.ConversationType mConversationType;
    TextView titletextView;
    View customView;
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        //唯一有用的代码，加载一个 layout
        setContentView(R.layout.conversation);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        isAlive = true;
        alertd = new Dialog(this);
        /*
		 * View  Initialize
		 */
        ActionBar ab = this.getActionBar();
        ab.setDisplayShowCustomEnabled(true);
        setTitle("");
        View homeIcon = findViewById(android.R.id.home);
        ((View) homeIcon.getParent()).setVisibility(View.GONE);

        LayoutInflater li = LayoutInflater.from(this);
        customView = li.inflate(R.layout.drawerlayout, null);

        titletextView = (TextView) customView.findViewById(R.id.titletextView);
        TextView back = (TextView) customView.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ab.setCustomView(customView);
        myapi = (App) this.getApplicationContext();
        pref = getSharedPreferences("Account", 0);

        Intent intent = getIntent();

        getIntentDate(intent);


    }

    private void getIntentDate(Intent intent) {
        mTargetId = intent.getData().getQueryParameter("targetId");
        mTargetIds = intent.getData().getQueryParameter("targetIds");
        //intent.getData().getLastPathSegment();//获得当前会话类型
        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));

        //继承的是ActionBarActivity，直接调用 自带的 Actionbar，下面是Actionbar 的配置，如果不用可忽略…
        titletextView.setText(intent.getData().getQueryParameter("title"));
        //TODO Custom Title Bar
        /*setLogo(R.drawable.rc_bar_logo);
        setDisplayHomeAsUpEnabled(true);
        setHomeAsUpIndicator(R.drawable.rc_bar_back);*/
        enterFragment(mConversationType, mTargetId);
    }

    /**
     * 加载会话页面 ConversationFragment
     *
     * @param mConversationType 会话类型
     * @param mTargetId 目标 Id
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {

        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();

        fragment.setUri(uri);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private Handler handler = new Handler( );
    private Handler dhandler = new Handler( );
    private Runnable runnable = new Runnable( ) {
        public void run ( ) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_ID", pref.getString("num", "")));
            params.add(new BasicNameValuePair("room_ID", mTargetId));
            String result = myapi.postMethod_getCode(ConversationActivity.this, App.SearchChat, params);
            Log.v("SearchChat", result);
            try {
                JSONObject o = new JSONObject(result);
                if (o.getString("status").equals("0")) {
                    if (!alertd.isShowing())
                        handler.postDelayed(this, 5000);
                } else if (o.getString("status").equals("1")) {
                    handler.removeCallbacks(runnable);
                    //TODO show  RequestDialog
                    JSONObject o_o = o.getJSONObject("message");
                    if (!alertd.isShowing())
                        RequestDialog(o_o.getString("Uname"),
                                    o_o.getString("Unum"),
                                    o_o.getString("age"),
                                    o_o.getString("gender"),
                                    o_o.getString("rate"),
                                    o_o.getString("photo"),
                                    o_o.getString("level"));
                }
            } catch (JSONException e) {
                //dialog.close();
                handler.postDelayed(this, 5000);
            }

        }
    };
    private Runnable drunnable = new Runnable( ) {
        public void run ( ) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_ID", pref.getString("num", "")));
            params.add(new BasicNameValuePair("room_ID", mTargetId));
            String result = myapi.postMethod_getCode(ConversationActivity.this, App.destiny, params);
            Log.v("destiny", result);
            try {
                final JSONObject o = new JSONObject(result);
                if (o.getString("status").equals("0")) {
                    if (ConversationActivity.this.hasWindowFocus())
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    dialog = myapi.new LoadingDialog(ConversationActivity.this, o.getString("message") , false);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                TimerTask t= new TimerTask() {
                                    public void run() {
                                        if(!ConversationActivity.this.isFinishing())
                                            finish();
                                        cancel();
                                    }
                                };
                                Timer timer = new Timer();
                                timer.schedule(t, 5000); //5秒後關閉
                                if(!ConversationActivity.this.isFinishing())
                                    dialog.execute();
                            }
                        });
                    else{
                        dhandler.postDelayed(this, 3000);
                    }
                } else if (o.getString("status").equals("1")) {
                    dhandler.postDelayed(this, 3000);
                }
            } catch (JSONException e) {
                //dialog.close();
                dhandler.postDelayed(this, 3000);
            }

        }
    };
    //TODO 按下Back dismiss dialog 並重啟Runnable

    Dialog alertd;
    TextView txt_loading;
    public void RequestDialog(final String Uname, final String Unum, final String age, final String gender, final String rate, final String photo, final String level) {

        alertd = new Dialog(this);
        alertd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertd.setContentView(R.layout.request_dialog);

        alertd.setCancelable(false);
        Window dialogWindow = alertd.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //dialogWindow.setGravity(Gravity.CENTER | Gravity.TOP);
        View v = dialogWindow.getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        lp.dimAmount = 0;

        lp.y = -200; // 新位置Y坐标
        lp.alpha = 1f;


        // 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
        // dialog.onWindowAttributesChanged(lp);
        dialogWindow.setAttributes(lp);


        RelativeLayout loading_title_bar = (RelativeLayout) alertd.findViewById(R.id.loading_title_bar);
        txt_loading = (TextView) alertd.findViewById(R.id.request_message);
        txt_loading.setText(Uname);
        ImageView request_photo = (ImageView) alertd.findViewById(R.id.request_photo);
        Picasso.with(ConversationActivity.this)
                .load(photo)
                .transform(new CircleImageTransform()).into(request_photo);
        request_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMemberActivity3(Unum,
                        Uname,
                        age,
                        gender,
                        rate,
                        photo,
                        level);

            }
        });
        TextView request_cancel = (TextView) alertd.findViewById(R.id.request_cancel);
        TextView request_confirm = (TextView) alertd.findViewById(R.id.request_confirm);

        request_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                VerifyChat("0",Unum);

            }
        });

        request_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerifyChat("1",Unum);
            }
        });
        alertd.show();

    }
    private void gotoMemberActivity3(String Unum, String Uname, String age, String gender, String rate, String photo, String level){
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
    private void VerifyChat(final String status , final String Unum){
        mythread = new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog = myapi.new LoadingDialog(ConversationActivity.this, "請稍後...", false);
                        dialog.execute();
                    }
                });
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("status", status));
                params.add(new BasicNameValuePair("user_ID", Unum));
                params.add(new BasicNameValuePair("room_ID", mTargetId));
                String result = myapi.postMethod_getCode(ConversationActivity.this, App.VerifyChat, params);
                dialog.close();
                Log.v("VerifyChat", result);
                try {
                    final JSONObject o = new JSONObject(result);
                    if(o.getString("status").equals("1")){

                        handler.postDelayed(runnable, 2000);
                        alertd.cancel();
                        alertd.dismiss();
                    }else if(o.getString("status").equals("0")){

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    dialog = myapi.new LoadingDialog(ConversationActivity.this, o.getString("message"), true);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if(!ConversationActivity.this.isFinishing())
                                    dialog.execute();
                            }
                        });
                    }
                } catch (JSONException e) {
                    dialog.close();
                    e.printStackTrace();
                }
                if (!ConversationActivity.this.isFinishing())
                    dialog.close();
            }
        });
        mythread.start();
    }
    /**
     * 设置 actionbar 事件
     */
   /* private void setActionBar() {

        mTitle = (TextView) findViewById(R.id.txt1);
        mBack = (RelativeLayout) findViewById(R.id.back);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }*/
}