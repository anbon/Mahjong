package co.nineka;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import co.nineka.util.TabAdapter;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;


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
    HandlerThread myHandlethread;
    View.OnClickListener listener;
    NearbyTab tab1;
    SearchTab tab2;
    AboutTab tab3;
    Thread mythread;
    App myapi;
    App.LoadingDialog dialog;
    SharedPreferences pref ;
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
        myapi = (App) this.getApplicationContext();
        pref = getSharedPreferences("Account", 0);
        isAlive = true;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog = myapi.new LoadingDialog(MainActivity.this, "與伺服器連接中...", false);
                dialog.execute();
            }
        });


        Intent intent = new Intent(Intent.ACTION_SYNC, null, this, NearbyService.class);
        intent.putExtra("requestId", 1);
        startService(intent);


        myHandlethread = new HandlerThread("MyHandlerThread") {
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
        myHandlethread.start();

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


        mythread = new Thread(new Runnable() {
            @Override
            public void run() {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("user_ID", pref.getString("num", "")));

                String result = myapi.postMethod_getCode(MainActivity.this, App.Buster, params);
                Log.v("Buster", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.close();
                    }
                });
                try {
                    final JSONObject o = new JSONObject(result);
                    if (o.getString("status").equals("1")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    BusterDialog(o.getJSONObject("message").getString("chatID"),
                                            o.getJSONObject("message").getString("level"),
                                            o.getJSONObject("message").getString("name"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    } else {

                        if(getIntent().getExtras()!=null)
                            if (getIntent().getExtras().containsKey("room_ID"))
                                gotoRoomInfo(getIntent().getExtras().getString("room_ID"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog = myapi.new LoadingDialog(MainActivity.this, "伺服器發生錯誤！", true);
                            dialog.execute();
                        }
                    });
                }
            }
        });
        if(getIntent().getExtras()!=null)
            if(getIntent().getExtras().getString("type","").equals("4")||
                    getIntent().getExtras().getString("type","").equals("6")){
                mythread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("user_ID", pref.getString("num", "")));

                        String result = myapi.postMethod_getCode(MainActivity.this, App.Buster, params);
                        Log.v("Buster", result);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.close();
                            }
                        });
                        try {
                            final JSONObject o = new JSONObject(result);
                            if (o.getString("status").equals("1")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            enterRongChatRoom(o.getJSONObject("message").getString("chatID"),
                                                    o.getJSONObject("message").getString("level"),
                                                    o.getJSONObject("message").getString("name"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog = myapi.new LoadingDialog(MainActivity.this, "您不在此房間內", true);
                                        dialog.execute();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog = myapi.new LoadingDialog(MainActivity.this, "伺服器發生錯誤！", true);
                                    dialog.execute();
                                }
                            });
                        }
                    }
                });
            }
        RongIM.connect(pref.getString("token",""), new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                //Connect Token 失效的状态处理，需要重新获取 Token
                Log.e("RongIM.connect", "——onTokenIncorrect— -");
            }

            @Override
            public void onSuccess(String userId) {
                Log.e("RongIM.connect", "——onSuccess— -" + userId);
                //RongIM.getInstance().getRongIMClient().setOnReceivePushMessageListener(myapi.getReceivePushMessageListener());
                mythread.start();

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("RongIM.connect", "——onError— -" + errorCode);
            }
        });


        //if(getIntent().hasExtra("Direct"))


}


    private void gotoRoomInfo(final String room_id) {
        mythread = new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog = myapi.new LoadingDialog(MainActivity.this, "請稍後...", false);
                        dialog.execute();
                    }
                });
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("room_id", room_id));
                String result = myapi.postMethod_getCode(MainActivity.this, App.getChatroom, params);
                Log.v("gotoRoomInfo", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.close();
                    }
                });
                try {
                    final JSONObject o = new JSONObject(result);

                    if(o.getString("status").equals("1")){
                        Intent intent = new Intent(MainActivity.this, RoomInfoActivity.class);
                        Bundle b = makeBundle(o.getString("message"));
                        intent.putExtras(b);
                        startActivity(intent);
                    }else{
                        //若房已關
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    dialog = myapi.new LoadingDialog(MainActivity.this, o.getString("message"), true);
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
                            dialog = myapi.new LoadingDialog(MainActivity.this, "伺服器發生錯誤！", true);
                            dialog.execute();
                        }
                    });

                }

            }
        });
        mythread.start();
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

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return b;
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

    private void BusterDialog(final String room_ID, final String level, final String name) {
        new AlertDialog.Builder(MainActivity.this)
                .setCancelable(false)
                .setTitle("警告")
                .setMessage("是否回到先前已" + (level.equals("1") ? "加入" : "創建") + "之房間？")
                .setNegativeButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface mdialog, int which) {
                        mythread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog = myapi.new LoadingDialog(MainActivity.this, "請稍後...", false);
                                        dialog.execute();
                                    }
                                });
                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("user_ID", pref.getString("num", "")));

                                String result = myapi.postMethod_getCode(MainActivity.this, App.Buster, params);
                                Log.v("Buster", result);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.close();
                                    }
                                });
                                try {
                                    final JSONObject o = new JSONObject(result);
                                    if (o.getString("status").equals("1")) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //確定嘗試加回房間
                                                enterRongChatRoom(room_ID, level, name);
                                            }
                                        });

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog = myapi.new LoadingDialog(MainActivity.this, "伺服器發生錯誤！", true);
                                            dialog.execute();
                                        }
                                    });
                                }
                            }
                        });
                        //if(getIntent().hasExtra("Direct"))
                        mythread.start();

                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface mdialog, int which) {
                        if (level.equals("1")) {
                            mythread = new Thread(new Runnable() {
                                @Override
                                public void run() {


                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog = myapi.new LoadingDialog(MainActivity.this, "請稍後...", false);
                                            dialog.execute();
                                        }
                                    });
                                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                                    params.add(new BasicNameValuePair("room_ID", room_ID));
                                    params.add(new BasicNameValuePair("user_ID", pref.getString("num", "")));

                                    String result = myapi.postMethod_getCode(MainActivity.this, App.memberLeave, params);
                                    Log.v("memberLeave", result);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.close();
                                        }
                                    });
                                    try {
                                        final JSONObject o = new JSONObject(result);
                                        if (o.getString("status").equals("1")) {
                                            //取消時，(這裡應當不可能為點擊房間已滿之意圖
                                            //前往follow通知 或 sharelink 之夾帶房間
                                            if(getIntent().getExtras()!=null)
                                                if (getIntent().getExtras().containsKey("room_ID"))
                                                    gotoRoomInfo(getIntent().getExtras().getString("room_ID"));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog = myapi.new LoadingDialog(MainActivity.this, "伺服器發生錯誤！", true);
                                                dialog.execute();
                                            }
                                        });
                                    }
                                }
                            });
                        } else {
                            //若是房主，按下取消:
                            mythread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog = myapi.new LoadingDialog(MainActivity.this, "請稍後...", false);
                                            dialog.execute();
                                        }
                                    });
                                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                                    params.add(new BasicNameValuePair("room_ID", room_ID));

                                    String result = myapi.postMethod_getCode(MainActivity.this, App.DeleteRoom, params);
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
                                            if (getIntent().getExtras()!=null)
                                                if(getIntent().getExtras().containsKey("room_ID"))
                                                    gotoRoomInfo(getIntent().getExtras().getString("room_ID"));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                dialog = myapi.new LoadingDialog(MainActivity.this, "伺服器發生錯誤！", true);
                                                dialog.execute();
                                                RetryDialog(room_ID, level, name);
                                            }
                                        });
                                    }
                                }
                            });

                        }
                        mythread.start();
                    }
                })
                .show();
    }
    private void RetryDialog(final String room_ID, final String level, final String name) {
        new AlertDialog.Builder(MainActivity.this)
                .setCancelable(false)
                .setTitle("警告")
                .setMessage("連接聊天室頁面失敗 ，是否再重試一次？")
                .setNegativeButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface mdialog, int which) {
                        mythread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog = myapi.new LoadingDialog(MainActivity.this, "請稍後...", false);
                                        dialog.execute();
                                    }
                                });
                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("user_ID", pref.getString("num", "")));

                                String result = myapi.postMethod_getCode(MainActivity.this, App.Buster, params);
                                Log.v("Buster", result);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.close();
                                    }
                                });
                                try {
                                    final JSONObject o = new JSONObject(result);
                                    if (o.getString("status").equals("1")) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                enterRongChatRoom(room_ID, level, name);
                                            }
                                        });

                                    }
                                    else
                                    {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog = myapi.new LoadingDialog(MainActivity.this, "您已被房主剔除！", true);
                                                dialog.execute();
                                            }
                                        });
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog = myapi.new LoadingDialog(MainActivity.this, "伺服器發生錯誤！", true);
                                            dialog.execute();
                                        }
                                    });
                                }
                            }
                        });
                        //if(getIntent().hasExtra("Direct"))
                        mythread.start();

                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface mdialog, int which) {
                        if (level.equals("1")) {
                            mythread = new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog = myapi.new LoadingDialog(MainActivity.this, "請稍後...", false);
                                            dialog.execute();
                                        }
                                    });
                                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                                    params.add(new BasicNameValuePair("room_ID", room_ID));
                                    params.add(new BasicNameValuePair("user_ID", pref.getString("num", "")));

                                    String result = myapi.postMethod_getCode(MainActivity.this, App.memberLeave, params);
                                    Log.v("memberLeave", result);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.close();
                                        }
                                    });
                                    try {
                                        final JSONObject o = new JSONObject(result);
                                        if (o.getString("status").equals("1")) {
                                            if(getIntent().getExtras()!=null)
                                                if(getIntent().getExtras().containsKey("room_ID"))
                                                    gotoRoomInfo(getIntent().getExtras().getString("room_ID"));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog = myapi.new LoadingDialog(MainActivity.this, "伺服器發生錯誤！", true);
                                                dialog.execute();
                                            }
                                        });
                                    }
                                }
                            });
                        } else {
                            mythread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog = myapi.new LoadingDialog(MainActivity.this, "請稍後...", false);
                                            dialog.execute();
                                        }
                                    });
                                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                                    params.add(new BasicNameValuePair("room_ID", room_ID));

                                    String result = myapi.postMethod_getCode(MainActivity.this, App.DeleteRoom, params);
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
                                            if(getIntent().getExtras()!=null)
                                                if(getIntent().getExtras().containsKey("room_ID"))
                                                    gotoRoomInfo(getIntent().getExtras().getString("room_ID"));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog = myapi.new LoadingDialog(MainActivity.this, "伺服器發生錯誤！", true);
                                                dialog.execute();
                                                RetryDialog(room_ID, level, name);
                                            }
                                        });

                                    }
                                }
                            });

                        }
                        mythread.start();
                    }
                })
                .show();
    }
    private void enterRongChatRoom(final String room_ID, final String level, final String name){
        //TODO 進入Rong聊天室
        RongIM.getInstance().getRongIMClient().joinGroup(room_ID, name, new RongIMClient.OperationCallback() {

            @Override
            public void onSuccess() {

                ConversationActivity.isGuest = level.equals("1");

                RongIM.getInstance().refreshUserInfoCache(new UserInfo(pref.getString("num", "0"), pref.getString("name", ""), Uri.parse(pref.getString("photo", ""))));
                RongIM.getInstance().startGroupChat(MainActivity.this, room_ID, name);

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("onError", "errorCode : " + errorCode);
                RetryDialog(room_ID, level, name);

            }
        });
    }
    public void LogOut(View v){
        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle("提醒")
                .setMessage("確定登出？")
                .setNegativeButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface mdialog, int which) {
                        pref.edit().clear().apply();
                        Intent intent = new Intent(MainActivity.this, StartActivity.class);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        startActivity(intent);
                        finish();
                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface mdialog, int which) {


                    }
                })
                .show();
    }
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
