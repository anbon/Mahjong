package com.example.demo;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class NearbyService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static HttpParams httpParameters;
    public int timeoutConnection = 5000;
    public int timeoutSocket = 4000;
    private static final String TAG = "NearbyService";
    private static final String ACTION_BAZ = "com.example.demo.action.BAZ";
    List<NameValuePair> params;
    App myapi;
    Handler handler=new Handler();
    Thread mythread;
    Runnable runnable;

    {
        runnable = new Runnable() {
            private String result;

            @Override
            public void run() {
                // TODO Auto-generated method stub
                //要做的事情
                mythread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("user_ID", pref.getString("num", "")));
                        params.add(new BasicNameValuePair("location_x", pref.getString("location_x", "")));
                        params.add(new BasicNameValuePair("location_y", pref.getString("location_y", "")));
                        //Log.v("params",params.toString());
                        String temp="";
                        temp = postMethod_getCode(App.seed, params);
                        try {
                            JSONObject o = new JSONObject(temp);
                            if(o.getString("status").equals("1")||o.getString("status").equals("-1"))
                                pref.edit().putString("nearby", temp).apply();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.v(TAG, "result : " + temp);
                    }

                });
                if (MainActivity.isAlive)
                    mythread.start();


                handler.postDelayed(this, 5000);
            }
        };
    }


    public NearbyService() {
        super("NearbyService");
    }
    SharedPreferences pref;
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Log.d(TAG, "Service Started!");
            myapi = (App) this.getApplicationContext();
            //int requestId = intent.getIntExtra("requestId", 0);
            pref = getSharedPreferences("Account", 0);
            handler.postDelayed(runnable, 5000);//每两秒执行一次runnable.

            Log.d(TAG, "Service Stopping!");

            this.stopSelf();
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @SuppressWarnings("deprecation")
    public String postMethod_getCode( String strPostURL,
                                     List<NameValuePair> params) {


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
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            for(int i=0;i< params.size();i++){
                //identify param type by Key
                if(params.get(i).getName().equals("photo")){
                    File f = new File(params.get(i).getValue());
                    /*Need to construct a FileBody with the file that needs to be attached and specify the mime type of the file. Add the fileBody to the request as an another part.
            This part will be considered as file part and the rest of them as usual form-data parts*/
                    FileBody fileBody = new FileBody(f, "application/octect-stream");
                    entity.addPart("photo",fileBody);
                }else{
                    entity.addPart(params.get(i).getName(),new StringBody(params.get(i).getValue()));
                }
            }
            post.setEntity(entity);

            //post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            httpParameters = new BasicHttpParams();
            // Set the timeout in milliseconds until a connection is
            // established.
            // The default value is zero, that means the timeout is not used.
            //ConnManagerParams.setTimeout(httpParameters, 2000);
            HttpConnectionParams.setConnectionTimeout(httpParameters,
                    this.timeoutConnection);
            HttpConnectionParams.setSocketBufferSize(httpParameters,1024);
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
            }else{
                return "{\"status\":\"-1\"}";
            }
            // return executeRequest(post);
        } catch (ConnectTimeoutException cte) {
            // Took too long to connect to remote host
            Log.i("tag", "ConnectTimeoutException");


            cte.printStackTrace();
        } catch (SocketTimeoutException ste) {
            // Remote host didn嚙踝蕭t respond in time
            Log.i("tag", "ConnectTimeoutException");


            ste.printStackTrace();
        } catch (HttpHostConnectException e) {
            Log.i("tag", "HttpHostConnectException");

            e.printStackTrace();
            return "{\"status\":\"-1\"}";
        } catch (UnknownHostException e) {
            Log.i("tag", "UnknownHostException");

            e.printStackTrace();
        } catch (Exception e) {
            Log.i("tag", "Exception");

            e.printStackTrace();
        }
        return "";
    }
    public static HttpParams myhttpparams() {
        return httpParameters;
    }
}
