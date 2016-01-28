package co.nineka;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

public class MemberActivity2 extends Activity {
    private final String TAG = "MemberActivity2";
    App myapi;
    App.LoadingDialog dialog;
    Thread mythread;
    TextView titletextView, member_gender, member_nickname, member_age, member_comment, member_ban, block_btn_1, block_btn_2;
    RelativeLayout frame_block_confirm, member_comment_btn;
    View customView;
    Bundle bundle;
    ImageView member_photo;
    RatingBar ratingBar;
    SharedPreferences pref;
    Animation FadeInAnimation, FadeOutAnimation;
    Boolean isOnPanel=false;
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
    public void onDestroy(){
        super.onDestroy();
        if(dialog!=null){
            dialog.close();
        }
    }
    @Override
    public void onResume(){
        super.onResume();

    }

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_member);
        pref = getSharedPreferences("Account", 0);
        /*
		 * View  Initialize
		 */
        ActionBar ab = this.getActionBar();
        ab.setDisplayShowCustomEnabled(true);
        setTitle("");
        View homeIcon = findViewById(android.R.id.home);
        ((View) homeIcon.getParent()).setVisibility(View.GONE);
        myapi = (App) this.getApplicationContext();
        bundle = getIntent().getExtras();
        LayoutInflater li = LayoutInflater.from(this);
        customView = li.inflate(R.layout.drawerlayout_member, null);
        FadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        FadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        titletextView = (TextView) customView.findViewById(R.id.titletextView);
        titletextView.setText(bundle.getString("Uname", ""));
        TextView back = (TextView) customView.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!isOnPanel)
                    finish();
                else
                    cancelBlockPanel(v);
            }
        });
        member_ban = (TextView) customView.findViewById(R.id.ban);
        member_ban.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!isOnPanel)
                    openBlockPanel();
                else
                    cancelBlockPanel(v);
            }
        });
        ab.setCustomView(customView);
        member_gender =  (TextView) findViewById(R.id.member_gender);
        member_nickname =  (TextView) findViewById(R.id.member_nickname);
        member_age =  (TextView) findViewById(R.id.member_age);
        member_comment = (TextView) findViewById(R.id.member_comment);
        member_photo = (ImageView) findViewById(R.id.member_photo);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        member_comment_btn = (RelativeLayout) findViewById(R.id.member_comment_btn);
        frame_block_confirm = (RelativeLayout) findViewById(R.id.frame_block_confirm);
        block_btn_1 = (TextView) findViewById(R.id.block_btn_1);
        block_btn_2 = (TextView) findViewById(R.id.block_btn_2);
        mythread = new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog = myapi.new LoadingDialog(MemberActivity2.this, "請稍後...", false);
                        dialog.execute();
                    }
                });
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("user_ID", bundle.getString("Unum")));
                String result = myapi.postMethod_getCode(MemberActivity2.this, App.Search_ID, params);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.close();
                    }
                });
                try {
                    final JSONObject o = new JSONObject(result);
                    Log.v(TAG, result);
                    if(o.getString("status").equals("1")){
                        final JSONObject o_o = o.getJSONObject("message");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    member_gender.setText(o_o.getString("gender").equals("0") ? "女" : "男");
                                    titletextView.setText(o_o.getString("name"));
                                    member_nickname.setText(o_o.getString("name"));
                                    member_age.setText(o_o.getString("age"));
                                    Picasso.with(MemberActivity2.this)
                                            .load(o_o.getString("photo"))
                                            .placeholder(R.drawable.about_photo_default)
                                            .transform(new RoundSquareTransform()).into(member_photo);
                                    Log.v(TAG, o_o.getString("rate"));
                                    ratingBar.setRating(Float.valueOf(o_o.getString("rate")));
                                    ratingBar.setIsIndicator(true);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    finish();
                }




            }
        });
        mythread.start();


        member_comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RatingDialog(MemberActivity2.this);
            }
        });

        if(pref.getString("num","").equals(bundle.getString("Unum"))){
            //TODO 看到自己時
            member_ban.setVisibility(View.GONE);
            member_comment_btn.setVisibility(View.GONE);
        }

        /*ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                if (rating < 1) {
                    ratingBar.setRating(1.0f);
                }
            }
        });*/

    }
    // Before running code in separate thread
    public void openBlockPanel(){
        isOnPanel = true;
        frame_block_confirm.setVisibility(View.VISIBLE);
        frame_block_confirm.setClickable(true);
        block_btn_1.setVisibility(View.VISIBLE);
        block_btn_2.setVisibility(View.VISIBLE);
        block_btn_1.setText("取  消");
        frame_block_confirm.startAnimation(FadeInAnimation);
        block_btn_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancelBlockPanel(v);
            }
        });
        block_btn_2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                block_btn_1.setText("確  認");
                block_btn_2.setVisibility(View.INVISIBLE);
                block_btn_1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //Log.v("onClick", "api");
                        addblock();
                        cancelBlockPanel(v);

                    }
                });
            }
        });
    }
    public void cancelBlockPanel(View view){
        if(isOnPanel) {
            isOnPanel = false;
            frame_block_confirm.startAnimation(FadeOutAnimation);
            frame_block_confirm.setVisibility(View.GONE);
            frame_block_confirm.setClickable(false);
            block_btn_1.setVisibility(View.INVISIBLE);
            block_btn_2.setVisibility(View.INVISIBLE);
        }

    }
    private void addblock(){
        mythread = new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog = myapi.new LoadingDialog(MemberActivity2.this, "請稍後...", false);
                        dialog.execute();
                    }
                });
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("user_ID", pref.getString("num","")));
                params.add(new BasicNameValuePair("block_ID", bundle.getString("Unum")));
                String result = myapi.postMethod_getCode(MemberActivity2.this, App.blockadd, params);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.close();
                    }
                });
                try {
                    final JSONObject o = new JSONObject(result);
                    Log.v(TAG, result);
                    if(o.getString("status").equals("1")){

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                dialog = myapi.new LoadingDialog(MemberActivity2.this, "成功加入黑名單！", true);
                                dialog.execute();

                            }
                        });
                    }else if(o.getString("status").equals("0")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog = myapi.new LoadingDialog(MemberActivity2.this, "加入黑名單失敗！", true);
                                dialog.execute();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //finish();
                }

            }
        });
        mythread.start();
    }
    Dialog alertd;
    RatingBar dialog_ratingBar;
    /**
     *
     * @author x51811danny
     * @see
     * @param
     *
     */
    float rating;
    public void RatingDialog(Activity act) {
        alertd = new Dialog(act);
        alertd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertd.setContentView(R.layout.rating_panel);
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
        dialogWindow.setAttributes(lp);
        TextView confirm = (TextView) alertd.findViewById(R.id.dialog_confirm);
        dialog_ratingBar = (RatingBar) alertd.findViewById(R.id.dialog_ratingBar);
        dialog_ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                if (rating < 1) {
                    ratingBar.setRating(1.0f);
                }
            }
        });
        ratingBar.setIsIndicator(false);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, dialog_ratingBar.getRating() + "");

                if(dialog_ratingBar.getRating()==0.0f){
                    rating = 1.0f;
                }else{
                    rating = dialog_ratingBar.getRating();
                }

                mythread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog = myapi.new LoadingDialog(MemberActivity2.this, "請稍後...", false);
                                dialog.execute();
                            }
                        });
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("Rated_ID", bundle.getString("Unum")));
                        params.add(new BasicNameValuePair("user_ID", pref.getString("num","")));

                        params.add(new BasicNameValuePair("rating", rating+""));
                        String result = myapi.postMethod_getCode(MemberActivity2.this, App.Rating, params);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.close();
                            }
                        });
                        try {
                            final JSONObject o = new JSONObject(result);
                            Log.v(TAG, result);
                            if (o.getString("status").equals("1")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        dialog = myapi.new LoadingDialog(MemberActivity2.this, "評分成功！", true);
                                        dialog.execute();

                                    }
                                });
                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        try {
                                            dialog = myapi.new LoadingDialog(MemberActivity2.this, o.getString("message"), true);
                                            dialog.execute();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            finish();
                        }


                    }
                });
                mythread.start();
                alertd.cancel();
                alertd.dismiss();

            }
        });
        ratingBar.setIsIndicator(true);
        alertd.show();
        // 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
        // dialog.onWindowAttributesChanged(lp);







    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && isOnPanel){
            cancelBlockPanel(new View(this));
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
