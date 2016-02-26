package co.nineka;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.baidu.android.pushservice.BasicPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.android.pushservice.PushNotificationBuilder;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class StartActivity extends Activity {
    App myapi;
    App.LoadingDialog dialog;
    ProgressBar pb;
    SharedPreferences pref;
    ImageView start_register, start_signin;
    String room_ID="",json ;
    LoginButton loginButton;
    CallbackManager callbackManager;
    private AccessToken accessToken;
    String fb_id,email,nickname,sex;
    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }
    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        ActionBar ab = this.getActionBar();
        ab.hide();
        setContentView(R.layout.activity_start);
        myapi = (App) this.getApplicationContext();
        start_register = (ImageView) findViewById(R.id.start_register) ;
        start_signin = (ImageView) findViewById(R.id.start_signin) ;
        pref = getSharedPreferences("Account", 0);
        dialog = myapi.new LoadingDialog(this,"手機認證已成功",true);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile","email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                accessToken = loginResult.getAccessToken();
                GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback()
                {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response)
                    {
                        Log.d("FB", object.toString());
                        Log.d("FB", object.optString("name"));
                        Log.d("FB", object.optString("link"));
                        Log.d("FB", object.optString("id"));
                        Log.d("FB", object.optString("email"));
                        Log.d("FB", object.optString("gender"));
                        Log.d("FB", object.optString("birthday"));
                        if(object.optString("gender","0").equals("male"))
                        {
                            sex="1";
                        }
                        else
                        {
                            sex="0";
                        }

                        fb_id=object.optString("id");
                        email=object.optString("email");
                        nickname=object.optString("name");

                        start_register.setVisibility(View.GONE);
                        start_signin.setVisibility(View.GONE);
                        new AsyncTaskLogin().execute();
                        //refresh=false;
                        loginButton.setVisibility(View.GONE);
                        LoginManager.getInstance().logOut();
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
        PushManager.startWork(getApplicationContext(),
                PushConstants.LOGIN_TYPE_API_KEY, "rReedOVCXcDjpc4nLU9iZKaS");

        BasicPushNotificationBuilder n = new BasicPushNotificationBuilder();
        n.setNotificationDefaults(Notification.DEFAULT_ALL);
        n.setNotificationVibrate(new long[]{200, 500, 800, 300, 300, 300});
        n.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
        n.setStatusbarIcon(R.drawable.ic_launcher);
        n.setNotificationSound(PreferenceManager.getDefaultSharedPreferences(this).
                getString("pref_tone", "content://settings/system/notification_sound"));

        PushManager.setDefaultNotificationBuilder(getApplicationContext(), n);

        if(getIntent().getExtras()!=null)
        {
            //Log.v("getIntent()", getIntent().getStringExtra("room_ID"));
            json = getIntent().getStringExtra("json");

            //Log.v("json", json);
        }else{

        }
        if(getIntent().getScheme()!=null)
            onNewIntent(getIntent());


        if(pref.getString("password","").isEmpty()){
            pb.setVisibility(View.INVISIBLE);
        }else{

            start_register.setVisibility(View.GONE);
            start_signin.setVisibility(View.GONE);
            loginButton.setVisibility(View.GONE);
            if(pref.contains("fb_id")){
                fb_id=pref.getString("fb_id","");
            }
            new AsyncTaskLogin().execute();
        }
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "co.nineka",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        //dialog.execute();
    }

    protected void onNewIntent(Intent intent) {
        Log.v("onNewIntent", "onNewIntent");
        String action = intent.getAction();
        String data = intent.getDataString();
        if (Intent.ACTION_VIEW.equals(action) && data != null) {
            room_ID = data.substring(data.lastIndexOf("/") + 1);
            Log.v("onNewIntent", room_ID);
        }
    }

    public void goRegisterActivity(View v){
        Intent intent = new Intent();
        intent.setClass(this, RegisterActivity.class);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(intent);
    }

    public void goSignInActivity(View v){
        Intent intent = new Intent();
        intent.setClass(this, SignInActivity.class);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    public class AsyncTaskLogin extends AsyncTask<Boolean, Integer, String>
    {
        JSONObject o_o;

        protected void onPreExecute()
        {

            pb.setVisibility(View.VISIBLE);

        }

        protected String doInBackground(Boolean... state)
        {

            SharedPreferences pref2 = getSharedPreferences("Setting", 0);
            List<NameValuePair> params;
            params = new ArrayList<NameValuePair>();

            if(fb_id==null) {
                params.add(new BasicNameValuePair("username", pref.getString("username", "")));
                params.add(new BasicNameValuePair("password", pref.getString("password", "")));
                params.add(new BasicNameValuePair("channel_Id", pref2.getString("channel_Id", "")));
                params.add(new BasicNameValuePair("DeviceType", "3"));

                //Log.v("params",params.toString());

                return myapi.postMethod_getCode(StartActivity.this, App.Verify, params);
            }else{
                params.add(new BasicNameValuePair("FB_ID", fb_id));
                if(email!=null) {
                    params.add(new BasicNameValuePair("Email", email));
                    params.add(new BasicNameValuePair("Name", nickname));
                    params.add(new BasicNameValuePair("gender", sex));
                }else{
                    params.add(new BasicNameValuePair("Email", pref.getString("username","")));
                    //params.add(new BasicNameValuePair("Name", ""));
                }
                params.add(new BasicNameValuePair("channel_Id", pref2.getString("channel_Id", "")));


                params.add(new BasicNameValuePair("DeviceType", "3"));

                return myapi.postMethod_getCode(StartActivity.this, App.FB_Login, params);
            }
        }
        protected void onPostExecute(String result)
        {
            Log.v("AsyncTaskLogin", "result : " + result);
            try {
                JSONObject o = new JSONObject(result);

                if(o.getString("status").equals("1")){
                    App.Token =  o.getString("message");
                    JSONObject o_o = o.getJSONObject("data");
                    Log.v("StartActivity",o_o.toString());
                    pref.edit()
                            .putString("token", App.Token)
                            .putString("num", o_o.getString("num"))
                            .putString("name", o_o.getString("name"))
                            .putString("email", o_o.getString("email"))
                            .putString("username", o_o.getString("username"))
                            .putString("password", o_o.getString("password"))
                            .putString("gender", o_o.getString("gender"))
                            .putString("age", o_o.getString("age"))
                            .putString("photo", o_o.getString("photo"))
                            .putString("rate", o_o.getString("rate"))
                            .putString("level", o_o.getString("level")).apply();

                    if(fb_id!=null){
                        pref.edit().putString("fb_id", fb_id).apply();
                    }
                    Intent intent = new Intent();
                    intent.setClass(StartActivity.this, MainActivity.class);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                    if(!room_ID.isEmpty() || json!=null) {
                        intent.putExtra("room_ID", room_ID);
                        Bundle b = new Bundle();
                        if(json!=null) {

                            o_o = new JSONObject(json);
                            b.putString("type", o_o.getInt("type")+"");
                            if(!o_o.isNull("message"))
                                b.putString("room_ID", o_o.getString("message"));
                            Log.v("AsyncTaskLogin", "type = " + o_o.getString("type"));

                        }
                        intent.putExtras(b);
                        //Log.v("AsyncTaskLogin", "type = "+ intent.getStringExtra("type"));
                    }
                    startActivity(intent);
                    finish();

                }
                else if(o.getString("status").equals("999")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog = myapi.new LoadingDialog(StartActivity.this, "登入請確實填寫",true);
                            dialog.execute();
                        }
                    });
                }
                else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.close();
                            dialog = myapi.new LoadingDialog(StartActivity.this,"帳號或密碼填寫錯誤",true);
                            dialog.execute();
                        }
                    });

                }
            } catch (JSONException e) {
                start_register.setVisibility(View.VISIBLE);
                start_signin.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.VISIBLE);
                e.printStackTrace();
            }
            pb.setVisibility(View.INVISIBLE);

        }
    }


}
