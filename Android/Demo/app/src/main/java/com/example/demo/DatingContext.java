package com.example.demo;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

/**
 * Created by Bob on 2015/1/30.
 */
public class DatingContext {
    public static final String Domain = "http://www.anbon.tw/mj/index.php/";
    public static final String Search_ID = Domain + "Search_ID";
    public static HttpParams httpParameters;
    public int timeoutConnection = 5000;
    public int timeoutSocket = 4000;
    private static DatingContext mDemoContext;
    public Context mContext;
    private HashMap<String, Group> groupMap;
    private ArrayList<UserInfo> mUserInfos;
    private ArrayList<UserInfo> mFriendInfos;
    private SharedPreferences mPreferences;
    private RongIM.LocationProvider.LocationCallback mLastLocationCallback;
    private App myapi;
    public static DatingContext getInstance() {

        if (mDemoContext == null) {
            mDemoContext = new DatingContext();
        }
        return mDemoContext;
    }

    private DatingContext() {
    }

    private DatingContext(Context context) {
        mContext = context;
        mDemoContext = this;
        //http初始化 用于登录、注册使用
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        RongIM.setLocationProvider(new LocationProvider());

    }

    public static void init(Context context) {
        mDemoContext = new DatingContext(context);
    }

    public SharedPreferences getSharedPreferences() {
        return mPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.mPreferences = sharedPreferences;
    }

    public void setGroupMap(HashMap<String, Group> groupMap) {
        this.groupMap = groupMap;
    }

    public HashMap<String, Group> getGroupMap() {
        return groupMap;
    }


    public ArrayList<UserInfo> getUserInfos() {
        return mUserInfos;
    }

    public void setUserInfos(ArrayList<UserInfo> userInfos) {
        mUserInfos = userInfos;
    }

    /**
     * 临时存放用户数据
     *
     * @param userInfos
     */
    public void setFriends(ArrayList<UserInfo> userInfos) {

        this.mFriendInfos = userInfos;
    }

    public ArrayList<UserInfo> getFriends() {
        return mFriendInfos;
    }


    List<NameValuePair> params;
    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    public UserInfo getUserInfoById(String userId) {
    	if (android.os.Build.VERSION.SDK_INT > 9) {
    	    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	    StrictMode.setThreadPolicy(policy);
    	}
        UserInfo userInfoReturn = new UserInfo(userId, userId, null);

		params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("user_ID", userId));



		//TODO
		String result = postMethod_getCode(Search_ID ,params);
		if(result!=""){

    		Log.v("getUserInfoById",result);
            try {
                JSONObject o = new JSONObject(result);
                if(o.getString("status").equals("1")){
                    JSONObject o_o = o.getJSONObject("message");
                    userInfoReturn.setName(o_o.getString("name"));
                    userInfoReturn.setUserId(userId);
                    userInfoReturn.setPortraitUri(Uri.parse(o_o.getString("photo")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


		}else
		{
			Log.v("getUserInfoById","Fail!!!");
		}
		
		
////1202       
//        if (!TextUtils.isEmpty(userId) && mUserInfos != null) {
//            for (UserInfo userInfo : mUserInfos) {
//
//                if (userId.equals(userInfo.getUserId())) {
//                    userInfoReturn = userInfo;
//                    break;
//                }
//
//            }
//        	
//        }
        
        return userInfoReturn;
    }

    /**
     * 通过userid 获得username
     *
     * @param userId
     * @return
     */
    public String getUserNameByUserId(String userId) {
        UserInfo userInfoReturn = null;
        if (!TextUtils.isEmpty(userId) && mUserInfos != null) {
            for (UserInfo userInfo : mUserInfos) {

                if (userId.equals(userInfo.getUserId())) {
                    userInfoReturn = userInfo;
                    break;
                }
            }
        }
        return userInfoReturn.getName();
    }

    /**
     * 获取用户信息列表
     *
     * @param userIds
     * @return
     */
    public List<UserInfo> getUserInfoByIds(String[] userIds) {

        List<UserInfo> userInfoList = new ArrayList<UserInfo>();

        if (userIds != null && userIds.length > 0) {
            for (String userId : userIds) {
                for (UserInfo userInfo : mUserInfos) {
                    Log.e("", "0409-------getUserInfoByIds-" + userInfo.getUserId() + "---userid;" + userId);
                    if (userId.equals(userInfo.getUserId())) {
                        Log.e("", "0409-------getUserInfoByIds-" + userInfo.getName());
                        userInfoList.add(userInfo);
                    }
                }
            }
        }
        return userInfoList;
    }

    /**
     * 通过groupid 获得groupname
     *
     * @param groupid
     * @return
     */
    public String getGroupNameById(String groupid) {
        Group groupReturn = null;
        if (!TextUtils.isEmpty(groupid) && groupMap != null) {

            if (groupMap.containsKey(groupid)) {
                groupReturn = groupMap.get(groupid);
            }else
                return null;

        }
        return groupReturn.getName();
    }


    public RongIM.LocationProvider.LocationCallback getLastLocationCallback() {
        return mLastLocationCallback;
    }

    public void setLastLocationCallback(RongIM.LocationProvider.LocationCallback lastLocationCallback) {
        this.mLastLocationCallback = lastLocationCallback;
    }

    class LocationProvider implements RongIM.LocationProvider {

        /**
         * 位置信息提供者:LocationProvider 的回调方法，打开第三方地图页面。
         *
         * @param context  上下文
         * @param callback 回调
         */
        @Override
        public void onStartLocation(Context context, LocationCallback callback) {
            /**
             * demo 代码  开发者需替换成自己的代码。
             */
//            DatingContext.getInstance().setLastLocationCallback(callback);
//            Intent intent = new Intent(context, SOSOLocationActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);//SOSO地图
        }
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
