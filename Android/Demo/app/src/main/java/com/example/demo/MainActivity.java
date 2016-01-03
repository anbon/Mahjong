package com.example.demo;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.HandlerThread;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.app.ActionBar;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.demo.util.TabAdapter;

import java.util.ArrayList;
import java.util.logging.Handler;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;


public class MainActivity extends FragmentActivity {
    //用來更新UI Thread
    Handler mHandler;
    private ViewPager mViewPager;
    private ArrayList<View> mViewList;
    private ArrayList<String> Titles;
    private int one;
    private TabAdapter mAdapter;
    int offset = 0;
    int bmpW;
    ImageView img_cursor;
    RelativeLayout relative_guid1, relative_guid2, relative_guid3;
    ImageView img_guid1, img_guid2, img_guid3;
    View customView;
    TextView titletextView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    HandlerThread mythread;
    View.OnClickListener listener;
    NearbyTab tab1;
    SearchTab tab2;
    AboutTab tab3;
    public static Boolean isAlive = true;
    int preposition = 0;
    private int[] tabs_title = { R.string.title_nearby,
            R.string.title_search, R.string.title_about };
    private RelativeLayout[] tab_relativelayouts;
    private ImageView[] tab_imgs;
    private int[] tabs_img_blue = {R.drawable.tab_nearby_blue,
            R.drawable.tab_search_blue,R.drawable.tab_about_blue};
    private int[] tabs_img_white = {R.drawable.tab_nearby_white,
            R.drawable.tab_search_white,R.drawable.tab_about_white};
    private int[] tabs_layout = { R.layout.fragment_nearby_tab,
            R.layout.fragment_search_tab,R.layout.fragment_about_tab };
    @Override
    public void onDestroy() {
        super.onDestroy();
        isAlive = false;
    }
    @Override
    public void onResume() {
        super.onResume();
        isAlive = true;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        mViewPager = (ViewPager) findViewById(R.id.ViewPager);
        isAlive = true;
        //mHandler = new Handler();


        //String Token = "vMleUWEOyE5BfsgHxZ6t1X/HM1K4crFYiwk+NfnKcpeOCpjbko8rq/i/nj1+B/pR7dRIQbhP85c=";
        //vMleUWEOyE5BfsgHxZ6t1X/HM1K4crFYiwk+NfnKcpeOCpjbko8rq/i/nj1+B/pR7dRIQbhP85c= ->id=777
        //
            //*
         //* IMKit SDK调用第二步
         //*
         //* 建立与服务器的连接
         //*
        RongIM.connect(App.Token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                //Connect Token 失效的状态处理，需要重新获取 Token
            }

            @Override
            public void onSuccess(String userId) {
                Log.e("MainActivity", "——onSuccess— -" + userId);


            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("MainActivity", "——onError— -" + errorCode);
            }
        });
        Intent intent = new Intent(Intent.ACTION_SYNC, null, this, NearbyService.class);
        intent.putExtra("requestId", 1);
        startService(intent);

        mythread = new HandlerThread("MyHandlerThread") {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {

                        // tab click listener
                        listener = new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                fragmentManager.executePendingTransactions();
                                int position = 0;
                                fragmentTransaction = fragmentManager.beginTransaction();
                                tab1 = null;
                                tab2 = null;
                                tab3 = null;

                                switch (v.getId()) {
                                    case R.id.tab_nearby:
                                        position = 0;
                                        if (position != preposition) {
                                            mViewPager.setCurrentItem(0);
                                            tab1 = new NearbyTab();
                                            fragmentTransaction.replace(R.id.ViewPager, tab1)
                                                    .commitAllowingStateLoss();
                                        }
                                        break;
                                    case R.id.tab_search:
                                        position = 1;
                                        if (position != preposition) {
                                            mViewPager.setCurrentItem(1);
                                            tab2 = new SearchTab();
                                            fragmentTransaction.replace(R.id.ViewPager, tab2)
                                                    .commitAllowingStateLoss();
                                        }
                                        // Intent intent = new Intent();
                                        // intent.setClass(MainActivity.this, CategoryTab.class);
                                        // startActivity(intent);
                                        break;

                                    case R.id.tab_about:
                                        position = 2;
                                        if (position != preposition) {
                                            mViewPager.setCurrentItem(2);

                                                tab3 = new AboutTab();
                                            fragmentTransaction.replace(R.id.ViewPager, tab3, "fragAbout")
                                                    .commitAllowingStateLoss();

                                        }
                                        break;

                                    default:
                                        break;
                                }

                                final int positionj = position;

                                one = offset * 2 + bmpW;
                                // mViewPager.setCurrentItem(position);
                                Animation animation = new TranslateAnimation(preposition * one,
                                        positionj * one, 0, 0);

                                animation.setFillAfter(true);
                                animation.setDuration(300); //
                                img_cursor.startAnimation(animation);

                                setSelectedTabColor(preposition, position);
                                preposition = positionj;

                            }
                        };
                        initImageandImageView();
                        tabsInit();

                        gotonearbytab();
                    }
                });

            }
        };
        mythread.start();

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
        ab.setCustomView(customView);
}
    /**
     *  initial tab and its cursor
     */
    private void initImageandImageView() {
        img_cursor = (ImageView) findViewById(R.id.cursor);
        relative_guid1 = (RelativeLayout) findViewById(R.id.tab_nearby);
        relative_guid2 = (RelativeLayout) findViewById(R.id.tab_search);
        relative_guid3 = (RelativeLayout) findViewById(R.id.tab_about);
        img_guid1 = (ImageView) findViewById(R.id.icon_nearby);
        img_guid2 = (ImageView) findViewById(R.id.icon_search);
        img_guid3 = (ImageView) findViewById(R.id.icon_about);
        relative_guid1.setBackgroundColor(Color.parseColor("#ffffff"));
        tab_relativelayouts = new RelativeLayout[tabs_title.length];
        tab_imgs = new ImageView[tabs_title.length];
        tab_relativelayouts[0] = relative_guid1;
        tab_relativelayouts[1] = relative_guid2;
        tab_relativelayouts[2] = relative_guid3;
        tab_imgs[0] = img_guid1;
        tab_imgs[1] = img_guid2;
        tab_imgs[2] = img_guid3;

        for (int i = 0; i < tab_relativelayouts.length; i++) {
            tab_relativelayouts[i].setOnClickListener(listener);
        }

        bmpW = BitmapFactory.decodeResource(getResources(),
                R.drawable.icon_cursor).getWidth();

        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        App.heightPixels = dm.heightPixels;
        App.widthPixels = dm.widthPixels;
        offset = (screenW / tabs_title.length - bmpW) / 2;

        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        // set cousor width
        img_cursor.getLayoutParams().width = screenW / tabs_title.length;
        img_cursor.setScaleType(ImageView.ScaleType.FIT_XY);
        img_cursor.requestLayout();
        img_cursor.setImageMatrix(matrix);

    }
    /**
     *  Tabs Initialize
     */

    private void tabsInit(){
        mViewList = new ArrayList<View>();
        LayoutInflater Inflater = LayoutInflater.from(this);
        Titles = new ArrayList<String>();

        for (int i = 0; i < tabs_title.length; i++) {
            mViewList.add(Inflater.inflate(tabs_layout[i], null));
            Titles.add(getResources().getString(tabs_title[i]));
        }

        tab1 = null;
        tab2 = null;
        tab3 = null;

        mAdapter = new TabAdapter(mViewList, Titles);
        mViewPager.setAdapter(mAdapter);
    }
    private void setSelectedTabColor(int preposition, int position) {
        tab_relativelayouts[preposition].setBackgroundColor(Color.parseColor("#61cee5"));
        tab_imgs[preposition].setImageResource(tabs_img_white[preposition]);
        tab_relativelayouts[position].setBackgroundColor(Color.WHITE);
        tab_imgs[position].setImageResource(tabs_img_blue[position]);

    }

    public void gotonearbytab() {
        fragmentTransaction = fragmentManager.beginTransaction();
        mViewPager.setCurrentItem(0);
        Log.v("gotomaintab()", "loding tab1....");
        tab1 = new NearbyTab();
        fragmentTransaction.replace(R.id.ViewPager, tab1)
                .commitAllowingStateLoss();
        tab2 = null;
        tab3 = null;

    }
    public void refreshpager() {
        Log.i("refreshpager", "refreshpager");
        mViewPager.getAdapter().notifyDataSetChanged();

    }

    /*
    if (RongIM.getInstance() != null) {

        /**
         * 刷新用户缓存数据。
         *
         * @param userInfo 需要更新的用户缓存数据。
         */
            /*RongIM.getInstance().refreshUserInfoCache(new UserInfo("777", "立人", Uri.parse("http://rongcloud-web.qiniudn.com/docs_demo_rongcloud_logo.png")));
            ArrayList<String> userIds = new ArrayList<String>();
            userIds.add("777");//增加 userId。



            RongIM.getInstance().createDiscussionChat(MainActivity.this, userIds, "讨论组名称");
    }
    */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK &&
                (NearbyTab.isAlive||
                    SearchTab.isAlive||
                    AboutTab.isAlive)) {
            isAlive = false;
            moveTaskToBack(true);

        }
        return super.onKeyDown(keyCode, event);
    }
}
