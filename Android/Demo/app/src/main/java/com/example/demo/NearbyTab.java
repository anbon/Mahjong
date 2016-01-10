package com.example.demo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.util.CircleImageTransform;
import com.example.demo.util.XListView;
import com.example.demo.util.XListView.IXListViewListener;
import com.squareup.picasso.Picasso;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;


public class NearbyTab extends Fragment implements LocationListener,IXListViewListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static boolean isAlive = false;
    // TODO: Rename and change types of parameters
    private MyAdapter mAdapter;
    TextView current_no_room;
    XListView list1;
    private List<ChatRoomList> mList;
    MyHolder mViewHolder;
    private String mParam1;
    private String mParam2;
    static private Timer timer;
    private boolean getService = false;     //是否已開啟定位服務
    private LocationManager lms;
    private Location location;
    private String bestProvider = LocationManager.GPS_PROVIDER;
    App myapi;
    App.LoadingDialog dialog;
    ProgressBar pb;

    SharedPreferences pref;
    FragmentActivity mActivity;
    Context ctx;
    ActionBar ab;
    View customView;
    TextView titletextView;
    ImageView create;
    //private OnFragmentInteractionListener mListener;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NearbyTab.
     */
    public static NearbyTab newInstance(String param1, String param2) {
        NearbyTab fragment = new NearbyTab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public NearbyTab() {
        // Required empty public constructor
    }
    @Override
    public void onPause() {
        super.onPause();
        if(getService) {
            lms.removeUpdates(this);   //離開頁面時停止更新
            getService = false;
        }

        //isAlive = false;
    }
    @Override
    public void onStop() {
        super.onStop();

        isAlive = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isAlive = true;
        if(getService) {
            lms.requestLocationUpdates(bestProvider, 1000, 1, this);
            //服務提供者、更新頻率60000毫秒=1分鐘、最短距離、地點改變時呼叫物件
        }

    }
    AsyncTaskGetMessage a;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



        ctx = getActivity();
        mActivity = getActivity();
        mAdapter = new MyAdapter();
        mList = new ArrayList<ChatRoomList>();
        list1 = (XListView) mActivity.findViewById(R.id.listView1);
        list1.setAdapter(mAdapter);
        list1.setPullLoadEnable(false);
        list1.setXListViewListener(this);
        pb = (ProgressBar) mActivity.findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);
        ab = mActivity.getActionBar();
        ab.setDisplayShowCustomEnabled(true);
        myapi = (App) this.getActivity().getApplicationContext();
        pref = mActivity.getSharedPreferences("Account", 0);
        View homeIcon = mActivity.findViewById(android.R.id.home);
        ((View) homeIcon.getParent()).setVisibility(View.GONE);

        LayoutInflater li = LayoutInflater.from(mActivity);
        customView = li.inflate(R.layout.drawerlayout_nearby, null);

        titletextView = (TextView) customView.findViewById(R.id.titletextView);
        titletextView.setText(mActivity.getResources().getString(R.string.title_nearby));
        ab.setCustomView(customView);

        create = (ImageView) customView.findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gotoCreateRoom();
            }
        });
        current_no_room =(TextView) mActivity.findViewById(R.id.current_no_room);
        //取得系統定位服務
        LocationManager status = (LocationManager) (mActivity.getSystemService(Context.LOCATION_SERVICE));
        if(status.isProviderEnabled(LocationManager.GPS_PROVIDER)|| status.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            //如果GPS或網路定位開啟，呼叫locationServiceInitial()更新位置
            locationServiceInitial();
        } else {
            Toast.makeText(mActivity, "請開啟定位服務", Toast.LENGTH_LONG).show();
            getService = true; //確認開啟定位服務
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)); //開啟設定頁面
        }

        onRefresh();


    }

    private void parseJSON(String result) {
        try {

            Log.v("parseJSON", result);
            JSONObject o = new JSONObject(result);
            mList = new ArrayList<ChatRoomList>();
            JSONArray ja =  o.getJSONArray("message");
            if(o.getString("status").equals("1")) {
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject oo = ja.getJSONObject(i);
                    mList.add(new ChatRoomList(oo));
                    //add_NearbyCell(oo);
                }

                mAdapter.notifyDataSetChanged();
            }else if(o.getString("status").equals("-1") && NearbyTab.this.isVisible()){
                timer.cancel();
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog = myapi.new LoadingDialog(NearbyTab.this.getActivity(), "伺服器發生錯誤！\n請下拉刷新重試 或 連繫客服。", true);
                        dialog.execute();
                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(mList.isEmpty()){
            current_no_room.setVisibility(View.VISIBLE);
        }else{
            current_no_room.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nearby_tab, container, false);
    }


    private void gotoCreateRoom() {
        Bundle b = new Bundle();
        Intent i = new Intent(mActivity, CreateRoomActivity.class);
        i.putExtras(b);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void locationServiceInitial() {
        lms = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE); //取得系統定位服務

         //做法一,由程式判斷用GPS_provider 或 網路定位
           if ( lms.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
         {
             Log.v("locationServiceInitial", "採用網路定位");
             location = lms.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); //使用GPS定位座標
         }
         else if (lms.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Log.v("locationServiceInitial", "採用GPS定位");
            location = lms.getLastKnownLocation(LocationManager.GPS_PROVIDER);  //使用GPS定位座標
        }else if(lms.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)){
               Log.v("locationServiceInitial", "採用PASSIVE定位");
               location = lms.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
           }
        // 做法二,由Criteria物件判斷提供最準確的資訊
        /*Criteria criteria = new Criteria();  //資訊提供者選取標準
        bestProvider = lms.getBestProvider(criteria, true);    //選擇精準度最高的提供者
        Location location = lms.getLastKnownLocation(bestProvider);*/

        getLocation(location);
    }

    private void getLocation(Location location) { //將定位資訊顯示在畫面中
        if(location != null) {
            Double longitude = location.getLongitude();   //取得經度
            Double latitude = location.getLatitude();     //取得緯度
            pref.edit().putString("location_x", latitude+"").putString("location_y",longitude+"").apply();
            Log.v("getLocation", "( " + longitude+", " + latitude + " )");
        }
        else {
            Toast.makeText(mActivity, "無法定位座標", Toast.LENGTH_LONG).show();
        }
    }






    @Override
    public void onLocationChanged(Location location) {  //當地點改變時
        // TODO 自動產生的方法 Stub
        getLocation(location);
    }
    @Override
    public void onProviderDisabled(String arg0) {//當GPS或網路定位功能關閉時
        // TODO 自動產生的方法 Stub
        Toast.makeText(mActivity, "請開啟gps或3G網路", Toast.LENGTH_LONG).show();

    }
    @Override
    public void onProviderEnabled(String arg0) { //當GPS或網路定位功能開啟
        // TODO 自動產生的方法 Stub
    }
    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) { //定位狀態改變
        // TODO 自動產生的方法 Stub
    }
    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/
    public class AsyncTaskGetMessage extends AsyncTask<Boolean, Integer, String>
    {
        Boolean st;
        protected void onPreExecute()
        {
            if(mList.isEmpty())
                pb.setVisibility(View.VISIBLE);
            //refresh=true;
        }

        protected String doInBackground(Boolean... state)
        {
            List<NameValuePair> params;
            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_ID", pref.getString("num", "")));
            params.add(new BasicNameValuePair("location_x", pref.getString("location_x", "")));
            params.add(new BasicNameValuePair("location_y", pref.getString("location_y", "")));
            //Log.v("params",params.toString());

            return postMethod_getCode(App.seed, params);



        }
        protected void onPostExecute(String result)
        {

            onLoad();
            Log.v("AsyncTaskGetMessage", "result : " + result);
            try {
                JSONObject o = new JSONObject(result);
                if(o.getString("status").equals("1") || o.getString("status").equals("-1")){
                    pref.edit().putString("nearby", result).apply();
                    parseJSON(result);

                }
            } catch (JSONException e) {
                if(NearbyTab.this.isVisible()){
                    Toast.makeText(getActivity(),"網路連線狀態異常或不穩",Toast.LENGTH_LONG).show();
                }
                e.printStackTrace();
            }
            pb.setVisibility(View.INVISIBLE);

        }
    }


    @Override
    public void onRefresh() {
        locationServiceInitial();
        if(timer!=null)
            timer.cancel();


        new AsyncTaskGetMessage().execute(true);
        TimerTask t= new TimerTask() {
            public void run() {
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        parseJSON(pref.getString("nearby", ""));
                    }

                });

            }
        };
        timer = new Timer();
        timer.schedule(t, 5000, 5000);

        list1.stopRefresh();
        list1.stopLoadMore();

    }
    private void onLoad() {

    }
    @Override
    public void onLoadMore() {

    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            mViewHolder = new MyHolder();
            if(convertView != null)
            {
                mViewHolder = (MyHolder) convertView.getTag();
            }
            else
            {

                convertView = mActivity.getLayoutInflater().inflate(
                        R.layout.nearby_item_cell, parent, false);
                mViewHolder.nearby_name = (TextView) convertView.findViewById(R.id.nearby_name);
                mViewHolder.nearby_title = (TextView) convertView.findViewById(R.id.nearby_title);
                mViewHolder.nearby_base = (TextView) convertView.findViewById(R.id.nearby_base);
                mViewHolder.nearby_unit = (TextView) convertView.findViewById(R.id.nearby_unit);
                mViewHolder.nearby_time = (TextView) convertView.findViewById(R.id.nearby_time);
                mViewHolder.nearby_distance = (TextView) convertView.findViewById(R.id.nearby_distance);

                mViewHolder.nearby_photo = (ImageView) convertView.findViewById(R.id.nearby_photo);
                mViewHolder.nearby_ic_vacant = (ImageView) convertView.findViewById(R.id.nearby_ic_vacant);



                convertView.setTag(mViewHolder);

            }

            if (mViewHolder != null)
            {

                if(mList.get(position).getneed() == 1)
                    mViewHolder.nearby_ic_vacant.setImageResource(R.drawable.need1);
                else if(mList.get(position).getneed() == 2)
                    mViewHolder.nearby_ic_vacant.setImageResource(R.drawable.need2);
                else
                    mViewHolder.nearby_ic_vacant.setImageResource(R.drawable.need3);
                mViewHolder.nearby_name.setText(mList.get(position).getUname());
                mViewHolder.nearby_title.setText(mList.get(position).getRoomName());
                mViewHolder.nearby_base.setText("$"+mList.get(position).getbase());
                mViewHolder.nearby_unit.setText("$"+mList.get(position).getunit());
                mViewHolder.nearby_time.setText(mList.get(position).gettime());
                mViewHolder.nearby_distance.setText(mList.get(position).getdistance()+"km");

                Picasso.with(mActivity.getApplicationContext())
                        .load(mList.get(position).getphoto())
                        .transform(new CircleImageTransform()).into(mViewHolder.nearby_photo);
                convertView.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {

                        Intent intent= new Intent(mActivity, RoomInfoActivity.class);
                        Bundle b = new Bundle();

                        b.putString("num",mList.get(position).getUnum());
                        b.putString("name",mList.get(position).getUname());
                        b.putString("RoomNum", mList.get(position).getRoomNum());
                        b.putString("base",mList.get(position).getbase());
                        b.putString("unit",mList.get(position).getunit());
                        b.putString("circle",mList.get(position).getcircle());
                        b.putString("time",mList.get(position).gettime());
                        b.putString("location",mList.get(position).getRoomName());
                        b.putString("people",mList.get(position).getpeople());
                        b.putString("type",mList.get(position).gettype());
                        b.putString("cigarette",mList.get(position).getcigarette());
                        b.putString("users",mList.get(position).getusers());
                        b.putString("rule",mList.get(position).getrule());
                        b.putString("dis",mList.get(position).getdistance());


                        intent.putExtras(b);
                        startActivity(intent);
                    }
                });

            }

            return convertView;
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

    }
    /*
        ImageView nearby_photo = (ImageView) hiddenInfo.findViewById(R.id.nearby_photo);
        ImageView nearby_ic_vacant = (ImageView) hiddenInfo.findViewById(R.id.nearby_ic_vacant);
        TextView nearby_name = (TextView) hiddenInfo.findViewById(R.id.nearby_name);
        TextView nearby_title = (TextView) hiddenInfo.findViewById(R.id.nearby_title);
        TextView nearby_base = (TextView) hiddenInfo.findViewById(R.id.nearby_base);
        TextView nearby_unit = (TextView) hiddenInfo.findViewById(R.id.nearby_unit);
        TextView nearby_time = (TextView) hiddenInfo.findViewById(R.id.nearby_time);
        TextView nearby_distance = (TextView) hiddenInfo.findViewById(R.id.nearby_distance);
     */
    public class MyHolder {
        public TextView nearby_name;
        public TextView nearby_title;
        public TextView nearby_base;
        public TextView nearby_unit;
        public TextView nearby_time;
        public TextView nearby_distance;
        public ImageView nearby_photo;
        public ImageView nearby_ic_vacant;
    }
    class ChatRoomList {
        private String RoomName="";
        private String base="";
        private String unit="";
        private String time="";
        private String photo="";
        private String distance="";
        private String RoomNum="";
        private String people="";
        private String Uname="";
        private String Unum="";
        private String circle="";
        private String type="";
        private int need;
        private String cigarette="";
        private String rule="";
        private String users="";


        public ChatRoomList(JSONObject o){
            try {
                this.RoomName = o.getString("RoomName");
                this.RoomNum = o.getString("RoomNum");
                this.people = o.getString("people");
                this.users = o.getJSONArray("users").toString();
                this.need = Integer.parseInt(o.getString("people"))-o.getJSONArray("users").length() +1;
                this.Uname = o.getJSONArray("users").getJSONObject(0).getString("Uname");
                this.Unum = o.getJSONArray("users").getJSONObject(0).getString("Unum");
                this.photo = o.getJSONArray("users").getJSONObject(0).getString("photo");
                this.unit = o.getString("unit");
                this.base = o.getString("base");
                this.distance = o.getString("dis");
                this.time = o.getString("time");
                this.circle = o.getString("circle");
                this.type = o.getString("type");
                this.rule = o.getString("rule");
                this.cigarette = o.getString("cigarette");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        public String getRoomName() {
            return RoomName;
        }
        public String getbase() {
            return base;
        }
        public String getunit() {
            return unit;
        }
        public String gettime() {
            return time;
        }
        public String getphoto() {
            return photo;
        }
        public String getdistance() {
            return distance;
        }
        public String getRoomNum() {
            return RoomNum;
        }
        public String getpeople() {
            return people;
        }
        public String getUname() {
            return Uname;
        }
        public String getUnum() {
            return Unum;
        }
        public String getcircle() {
            return circle;
        }
        public String getcigarette() {
            return cigarette;
        }
        public String getrule() {
            return rule;
        }
        public String getusers() {
            return users;
        }
        public int getneed() {
            return need;
        }
        public String gettype() {
            return type;
        }

    }
    public static HttpParams httpParameters;
    public int timeoutConnection = 5000;
    public int timeoutSocket = 4000;
    public static HttpParams myhttpparams() {
        return httpParameters;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }*/

}
