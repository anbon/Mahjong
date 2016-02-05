package co.nineka;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.nineka.util.RoundSquareTransform;

public class MemberActivity extends Activity {
    TextView titletextView, member_gender, member_nickname, member_age, member_follow, join_btn;
    RelativeLayout member_comment_btn, join, member_follow_btn;
    View customView;
    Bundle bundle;
    ImageView member_photo;
    RatingBar ratingBar;
    Boolean isFollowed, isRooming;
    App myapi;
    App.LoadingDialog dialog;
    SharedPreferences pref;
    Thread mythread;
    @Override
    public void onPause() {
        super.onPause();
        if (mythread != null) {
            mythread.interrupt();
            mythread = null;
        }
        //isAlive = false;
    }
    @Override
    public void onStop() {
        super.onStop();

    }
    @Override
    public void onResume() {
        super.onResume();

    }
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_member);
        /*
		 * View  Initialize
		 */
        ActionBar ab = this.getActionBar();
        ab.setDisplayShowCustomEnabled(true);
        setTitle("");
        myapi = (App) this.getApplicationContext();
        View homeIcon = findViewById(android.R.id.home);
        ((View) homeIcon.getParent()).setVisibility(View.GONE);
        pref = getSharedPreferences("Account", 0);

        bundle = getIntent().getExtras();
        isFollowed = bundle.getString("follow").equals("1");
        isRooming = bundle.getString("Rooming").equals("1");
        LayoutInflater li = LayoutInflater.from(this);
        customView = li.inflate(R.layout.drawerlayout, null);
        member_comment_btn = (RelativeLayout) findViewById(R.id.member_comment_btn);
        member_comment_btn.setVisibility(View.GONE);
        join = (RelativeLayout) findViewById(R.id.join);
        join_btn = (TextView) findViewById(R.id.join_btn);
        if(isRooming){
            join.setVisibility(View.VISIBLE);
            join_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO RoomInfoActivity
                    Intent intent= new Intent(MemberActivity.this , RoomInfoActivity.class);
                    Bundle b = makeBundle(bundle.getString("RoomMessage"));
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });
        }
        member_follow_btn = (RelativeLayout) findViewById(R.id.member_follow_btn);
        if(!bundle.getString("num").equals(pref.getString("num","")))
            member_follow_btn.setVisibility(View.VISIBLE);


        titletextView = (TextView) customView.findViewById(R.id.titletextView);
        titletextView.setText("(ID:"+bundle.getString("num")+")");
        TextView back = (TextView) customView.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ab.setCustomView(customView);
        member_gender =  (TextView) findViewById(R.id.member_gender);
        member_nickname =  (TextView) findViewById(R.id.member_nickname);
        member_age =  (TextView) findViewById(R.id.member_age);
        member_follow = (TextView) findViewById(R.id.member_follow);
        member_follow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Follow();
            }
        });
        member_photo = (ImageView) findViewById(R.id.member_photo);

        member_gender.setText(bundle.getString("gender").equals("0")?"女":"男");
        member_nickname.setText(bundle.getString("name"));
        member_age.setText(bundle.getString("age"));
        Picasso.with(this)
                .load(bundle.getString("photo"))
                .placeholder(R.drawable.about_photo_default)
                .transform(new RoundSquareTransform()).into(member_photo);

        if(isFollowed){
            changeFollowStatus();
        }


        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        ratingBar.setRating(Float.valueOf(bundle.getString("rate", "0")));
        /*ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                if (rating < 1) {
                    ratingBar.setRating(1.0f);
                }
            }
        });*/
        ratingBar.setIsIndicator(true);
    }

    private Bundle makeBundle(String roomMessage) {
        Bundle b = new Bundle();
        try {
            JSONObject o = new JSONObject(roomMessage);
            b.putString("num", o.getJSONArray("users").getJSONObject(0).getString("Unum"));
            b.putString("name", o.getJSONArray("users").getJSONObject(0).getString("Uname"));
            b.putString("RoomNum", o.getString("RoomNum"));
            b.putString("base", o.getString("base"));
            b.putString("unit", o.getString("unit"));
            b.putString("circle", o.getString("circle"));
            b.putString("time", o.getString("time"));
            b.putString("location", o.getString("RoomName"));
            b.putString("people", o.getString("people"));
            b.putString("type", o.getString("type"));
            b.putString("cigarette", o.getString("cigarette"));
            b.putString("users", o.getJSONArray("users").toString());
            b.putString("rule", o.getString("rule"));
            b.putString("dis",o.getString("dis"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return b;
    }

    private void Follow() {
        mythread = new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog = myapi.new LoadingDialog(MemberActivity.this, "請稍後...", false);
                        dialog.execute();
                    }
                });
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("user_ID", pref.getString("num","")));
                params.add(new BasicNameValuePair("Follow_ID", bundle.getString("num","")));
                String result = myapi.postMethod_getCode(MemberActivity.this, App.follow, params);
                Log.v("MemberActivity", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.close();
                    }
                });
                try {
                    final JSONObject o = new JSONObject(result);

                    if(o.getString("status").equals("1")){
                        isFollowed = !isFollowed;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                changeFollowStatus();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog = myapi.new LoadingDialog(MemberActivity.this, "伺服器發生錯誤！", true);
                    dialog.execute();
                }

            }
        });
        mythread.start();
    }

    private void changeFollowStatus() {
        if(isFollowed){
            member_follow_btn.setBackgroundResource(R.drawable.btn_red_round_bg4);
            member_follow.setText("UnFollow");
        }else{
            member_follow_btn.setBackgroundResource(R.drawable.btn_blue_round_bg4);
            member_follow.setText("Follow");
        }
    }


}
