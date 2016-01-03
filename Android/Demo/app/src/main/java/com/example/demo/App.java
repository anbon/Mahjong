package com.example.demo;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.rong.imkit.CustomizeMessage;
import io.rong.imkit.CustomizeMessageItemProvider;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.MyGroupConversationProvider;
import io.rong.imkit.MyTextMessageItemProvider;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;


/**
 * Created by x51811danny on 2015/10/12.
 */

public class App extends Application {
    public static HttpParams httpParameters;
    //http://www.anbon.tw//mj//assets//photo//21_2015_12_29_19_51_24.jpg
    public static final String Photo_Repository = "http:/www.anbon.tw/mj/assets/photo/";
    public static final String Domain = "http://www.anbon.tw/mj/index.php/";
    public static final String Verify = Domain + "verify";
    public static final String Search_User = Domain + "Search_User";
    public static final String Getmember = Domain + "getmember";
    public static final String Createchat = Domain + "createchat";
    public static final String getChatroom = Domain + "getChatroom";
    public static final String upload = Domain + "User_Photo";
    public static final String User_Info = Domain + "User_Info";
    public static final String seed = Domain + "seed";
    public static final String chatamount = Domain + "chatamount";
    public static final String UploadTest = "http://172.20.10.5:8080/" + "uploadfile.php";
    public int timeoutConnection = 5000;
    public int timeoutSocket = 4000;
    public boolean shutdown = true;
    public static int heightPixels, widthPixels ;
    public static String Token = "";

    @Override
    public void onCreate() {
        super.onCreate();

        /*ImageLoader imageLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        imageLoader.init(config);*/
        //String Token = "OW1vTU673AyDiI4aPLQZFWOg2od3MJbz+Sffv/HHZPjnxIx1xAww9wRjhJbOgvkK2aJ/==";
        /**
         * 初始化融云
         */

        RongIM.init(this);


        //Thread.setDefaultUncaughtExceptionHandler(new RongExceptionHandler(this));
        RongIM.getInstance().registerMessageTemplate(new MyTextMessageItemProvider());
        DatingContext.init(this);
        RongIM.setConversationBehaviorListener(new MyConversationBehaviorListener());
        //
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

            @Override
            public UserInfo getUserInfo(String userId) {

                Log.v("getUserInfo", userId);
                return DatingContext.getInstance().getUserInfoById(userId);//根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
            }

        }, true);





        /*RongIM.connect(Token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                Log.d("onTokenIncorrect","Token Incorrect!");
            }

            @Override
            public void onSuccess(String s) {
                Log.d("onSuccess","Token Success!");
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.d("onError","Token Error!");
            }
        });*/

    }
    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
    class MyConversationBehaviorListener implements RongIM.ConversationBehaviorListener{

        /**
         * 当点击用户头像后执行。
         *
         * @param context           上下文。
         * @param conversationType  会话类型。
         * @param userInfo          被点击的用户的信息。
         * @return 如果用户自己处理了点击后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
         */
        @Override
        public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
            return false;
        }

        /**
         * 当长按用户头像后执行。
         *
         * @param context          上下文。
         * @param conversationType 会话类型。
         * @param userInfo         被点击的用户的信息。
         * @return 如果用户自己处理了点击后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
         */
        @Override
        public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
            return false;
        }

        /**
         * 当点击消息时执行。
         *
         * @param context 上下文。
         * @param view    触发点击的 View。
         * @param message 被点击的消息的实体信息。
         * @return 如果用户自己处理了点击后的逻辑，则返回 true， 否则返回 false, false 走融云默认处理方式。
         */
        @Override
        public boolean onMessageClick(Context context, View view, Message message) {


            return false;
        }

        /**
         * 当长按消息时执行。
         *
         * @param context 上下文。
         * @param view    触发点击的 View。
         * @param message 被长按的消息的实体信息。
         * @return 如果用户自己处理了长按后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
         */
        @Override
        public boolean onMessageLongClick(Context context, View view, Message message) {
            return false;
        }
        /**
         * 当点击链接消息时执行。
         *
         * @param context 上下文。
         * @param link    被点击的链接。
         * @return 如果用户自己处理了点击后的逻辑处理，则返回 true， 否则返回 false, false 走融云默认处理方式。
         */
        @Override
        public boolean onMessageLinkClick(Context context, String link) {
            return false;
        }
    }
    @SuppressWarnings("deprecation")
    public String postMethod_getCode(Activity ctx, String strPostURL,
                                     List<NameValuePair> params) {
        shutdown = false;
        if(strPostURL.equals(UploadTest)){
            timeoutSocket = 20000;
            timeoutConnection = 20000;
        }
        else
            timeoutConnection = 5000;
        HttpClient client;
        HttpPost post = new HttpPost(strPostURL);

        if (params == null) {
            Log.v("postMethod_getCode",strPostURL);
            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("", ""));
        }
        else
            timeoutSocket = 4000;
        try {
            //setup multipart entity
            //post.addHeader("Content-Type","multipart/form-data");
            //MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,null,Charset.forName("UTF-8"));
            MultipartEntity entity = new MultipartEntity();
            Charset chars = Charset.forName("UTF-8");
            for(int i=0;i< params.size();i++){
                //identify param type by Key
                if(params.get(i).getName().equals("photo")){
                    File f = new File(params.get(i).getValue());
                    Log.e("upload", f.getPath());
                    //post.setHeader("Content-Type","multipart/form-data");
                    /*Need to construct a FileBody with the file that needs to be attached and specify the mime type of the file. Add the fileBody to the request as an another part.
            This part will be considered as file part and the rest of them as usual form-data parts*/
                    /*FileBody fileBody = new FileBody(f,"image/png");
                                      fileBody.getMediaType();*/
                    //entity.addPart("file",fileBody);
                    Bitmap photoBM = BitmapFactory.decodeFile(f.getPath());
                    if (photoBM == null) {
                        return null;
                    }
                    ByteArrayOutputStream photoBao = new ByteArrayOutputStream();
                    boolean successCompress = photoBM.compress(Bitmap.CompressFormat.JPEG,
                            80, photoBao);
                    if (!successCompress) {
                        return null;
                    }
                    ByteArrayBody byteArrayBody = new ByteArrayBody(
                            photoBao.toByteArray(), f.getName());
                    photoBM.recycle();
                    // InputStreamBody inbody = new InputStreamBody(new InputStream,
                    // filename);
                    entity.addPart("file", byteArrayBody);
                }else{
                    entity.addPart(params.get(i).getName(),new StringBody(params.get(i).getValue(), chars));
                }
            }
            post.setEntity(entity);
            //post.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
            //post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            httpParameters = new BasicHttpParams();
            // Set the timeout in milliseconds until a connection is
            // established.
            // The default value is zero, that means the timeout is not used.
            //ConnManagerParams.setTimeout(httpParameters, 2000);
            HttpConnectionParams.setConnectionTimeout(httpParameters,
                    this.timeoutConnection);
            HttpConnectionParams.setSocketBufferSize(httpParameters, 1024);
            HttpConnectionParams.setSoTimeout(httpParameters,
                    this.timeoutSocket);
            client = new DefaultHttpClient(myhttpparams());
            client.getParams().setParameter(
                    CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

            HttpResponse httpResponse = client.execute(post);
            /*HttpResponse httpResponse = new DefaultHttpClient(myhttpparams())
                    .execute(post);*/

            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String strRes = EntityUtils.toString(httpResponse.getEntity());
                    return strRes;
                }
           // return executeRequest(post);
        } catch (ConnectTimeoutException cte) {
            // Took too long to connect to remote host
            Log.i("tag", "ConnectTimeoutException");
            alertdialog(
                    ctx,
                    getResources().getString(R.string.alertdialog_timeouttitle),
                    getResources().getString(
                            R.string.alertdialog_timeoutcontent), shutdown);

            cte.printStackTrace();
        } catch (SocketTimeoutException ste) {
            // Remote host didn嚙踝蕭t respond in time
            Log.i("tag", "ConnectTimeoutException");

            alertdialog(
                    ctx,
                    getResources().getString(R.string.alertdialog_timeouttitle),
                    getResources().getString(
                            R.string.alertdialog_timeoutcontent), shutdown);

            ste.printStackTrace();
        } catch (HttpHostConnectException e) {
            Log.i("tag", "HttpHostConnectException");

            alertdialog(ctx,
                    getResources().getString(R.string.alertdialog_fixtitle),
                    getResources().getString(R.string.alertdialog_fixcontent),
                    true);

            e.printStackTrace();

        } catch (UnknownHostException e) {
            Log.i("tag", "UnknownHostException");
            alertdialog(
                    ctx,
                    getResources().getString(R.string.alertdialog_exception),
                    getResources().getString(
                            R.string.alertdialog_timeoutcontent), shutdown);

            e.printStackTrace();
        } catch (Exception e) {
            Log.i("tag", "Exception");
            alertdialog(ctx,
                    getResources().getString(R.string.alertdialog_exception),
                    getResources().getString(R.string.alertdialog_fixcontent),
                    shutdown);
            e.printStackTrace();
        }
        return "";
    }



    static AlertDialog.Builder dialog;

    public static void alertdialog(final Activity ctx, final String title,
                                   final String msg, final boolean finish) {
        ctx.runOnUiThread(new Runnable() {
            public void run() {
                dialog = new AlertDialog.Builder(ctx);
                dialog.setTitle(title);
                dialog.setIcon(android.R.drawable.ic_dialog_alert);
                dialog.setMessage(msg);
                dialog.setCancelable(false);
                dialog.setNegativeButton(
                        ctx.getResources()
                                .getString(R.string.login_btn_confirm),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                                dialog.cancel();
                                if (finish)
                                    ((Activity) ctx).finish();

                            }
                        });
                if (!((Activity) ctx).isFinishing())
                    dialog.show();
            }
        });
    }
    public static HttpParams myhttpparams() {
        return httpParameters;
    }

    public class LoadingDialog extends AsyncTask<Void, Integer, Dialog> {
        // Before running code in separate thread
        Activity ctx;
        Dialog alertd;
        TextView txt_loading;
        /**
         *
         * @author x51811danny
         * @see
         * @param
         *
         */
        public LoadingDialog(Activity act,String msg, Boolean cancelable) {
            ctx = act;
            alertd = new Dialog(ctx);
            alertd.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertd.setContentView(R.layout.loading);

            alertd.setCancelable(cancelable);
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
            txt_loading = (TextView) alertd.findViewById(R.id.txt_loading);
            txt_loading.setText(msg);
            //loading_title_bar.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_top));
            //ImageView image = (ImageView) alertd.findViewById(R.id.loading_dots);


            /*final AnimationDrawable animation = (AnimationDrawable) image
                    .getDrawable();
            alertd.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    animation.start();
                }
            });*/


        }

        @Override
        protected void onPreExecute() {
            alertd.show();
        }

        @Override
        protected Dialog doInBackground(Void... params) {
            return alertd;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPostExecute(Dialog result) {
        }

        public void close(){
            if(alertd.isShowing())
                alertd.dismiss();
        }

        public void setMessage(String msg){
            txt_loading.setText(msg);
        }

        public void setCancelable(boolean cancelable){
            alertd.setCancelable(cancelable);
            alertd.setCanceledOnTouchOutside(cancelable);
        }

    }
}
