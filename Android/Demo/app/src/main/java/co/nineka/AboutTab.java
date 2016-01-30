package co.nineka;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import co.nineka.util.CycleWheelView;
import co.nineka.util.RoundSquareTransform;


public class AboutTab extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "AboutTab";
    private final static int PHOTO = 1,GET = 33 ;
    public static boolean isAlive = false;
    private DisplayMetrics mPhone;
    App myapi;
    App.LoadingDialog dialog;
    Thread mythread;
    FragmentActivity mActivity;
    Context ctx;
    ActionBar ab;
    View customView;
    RelativeLayout relative_gender, relative_nickname, relative_age;
    EditText about_nickname;
    TextView back, titletextView, txt_change_photo, about_gender, about_age, about_logout;
    ImageView about_photo, edit;
    SharedPreferences pref;
    static boolean isEdit;
    File tmpFile,newFile;
    List<String> class_name;
    String pre_gender, pre_nickname, pre_age;
    int wheel_position;
    //private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NearbyTab.
     */
    public static AboutTab newInstance(String param1, String param2) {
        AboutTab fragment = new AboutTab();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public AboutTab() {
        // Required empty public constructor
    }
    @Override
    public void onPause() {
        super.onPause();
        //isAlive = false;
        if (mythread != null) {
            mythread.interrupt();
            mythread = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        isAlive = false;
        //undo(getView());
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        isAlive = false;
        InputMethodManager imm = (InputMethodManager) relative_age.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        undo(getView());
    }
    @Override
    public void onResume() {
        super.onResume();

        Picasso.with(AboutTab.this.getActivity())
                .load(pref.getString("photo",""))
                .placeholder(R.drawable.about_photo_default)
                .transform(new RoundSquareTransform()).into(about_photo);
        isAlive = true;
    }
    @Override
    public void onAttach(Activity activity) {
        mActivity = (FragmentActivity) activity;
        super.onAttach(activity);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        if (getArguments() != null) {

        }
        mPhone = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(mPhone);
        ctx = getActivity();
        pref = mActivity.getSharedPreferences("Account", 0);
        ab = mActivity.getActionBar();
        ab.setDisplayShowCustomEnabled(true);
        View homeIcon = mActivity.findViewById(android.R.id.home);
        ((View) homeIcon.getParent()).setVisibility(View.GONE);

        LayoutInflater li = LayoutInflater.from(mActivity);
        customView = li.inflate(R.layout.drawerlayout_edit, null);

        titletextView = (TextView) customView.findViewById(R.id.titletextView);
        titletextView.setText(mActivity.getResources().getString(R.string.title_about));

        back = (TextView) customView.findViewById(R.id.back);
        back.setVisibility(View.INVISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undo(v);
            }
        });
        edit = (ImageView) customView.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isEdit)
                    gotoAboutEditTab(v);
                else
                    gotoAboutTab(v);
            }
        });

        ab.setCustomView(customView);
        myapi = (App) this.getActivity().getApplicationContext();
        about_photo = (ImageView) mActivity.findViewById(R.id.about_photo);
        txt_change_photo = (TextView) mActivity.findViewById(R.id.txt_change_photo);
        relative_gender = (RelativeLayout) mActivity.findViewById(R.id.relative_gender);
        relative_nickname = (RelativeLayout) mActivity.findViewById(R.id.relative_nickname);
        relative_age = (RelativeLayout) mActivity.findViewById(R.id.relative_age);
        about_nickname = (EditText) mActivity.findViewById(R.id.about_nickname);
        about_gender = (TextView) mActivity.findViewById(R.id.about_gender);
        about_age = (TextView) mActivity.findViewById(R.id.about_age);
        about_logout = (TextView) mActivity.findViewById(R.id.about_logout);
        Picasso.with(AboutTab.this.getActivity())
                .load(pref.getString("photo",""))
                .placeholder(R.drawable.about_photo_default)
                .transform(new RoundSquareTransform()).into(about_photo);
        about_nickname.setText(pref.getString("name", ""));
        if(pref.getString("gender","").equals("0")){
            about_gender.setText("女");
            gender = "0";
        }
        else{
            about_gender.setText("男");
            gender = "1";
        }
        about_age.setText(pref.getString("age", ""));
        about_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //開啟相簿相片集，須由startActivityForResult且帶入requestCode進行呼叫，原因
                //為點選相片後返回程式呼叫onActivityResult
                Intent intent = new Intent(Intent.ACTION_PICK, null);

                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                //intent.setType("image/*");
                //intent.setAction(Intent.ACTION_GET_CONTENT);
                //startActivityForResult(Intent.createChooser(intent, "Complete action using"), PHOTO);
                startActivityForResult(intent, PHOTO);
            }
        });

        txt_change_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //開啟相簿相片集，須由startActivityForResult且帶入requestCode進行呼叫，原因
                //為點選相片後返回程式呼叫onActivityResult
                Intent intent = new Intent(Intent.ACTION_PICK, null);

                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                //intent.setType("image/*");
                //intent.setAction(Intent.ACTION_GET_CONTENT);
                //startActivityForResult(Intent.createChooser(intent, "Complete action using"), PHOTO);
                startActivityForResult(intent, PHOTO);
            }
        });
        relative_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GenderListAlertDialog(v);
            }
        });
        relative_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                about_nickname.requestFocus();
                InputMethodManager imm = (InputMethodManager) about_nickname.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }
        });
        class_name = new ArrayList<String>();
        for(int i = 0 ; i<100 ; i++) {
            class_name.add((i+1)+"");
        }
        relative_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog=new Dialog(getActivity(), R.style.age_Dialog);
                dialog.setContentView(R.layout.item_age_dialog);
                TextView cancel = (TextView)dialog.findViewById(R.id.textView1);
                TextView done = (TextView)dialog.findViewById(R.id.textView3);
                final String temp_age = about_age.getText().toString();
                cancel.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        about_age.setText(temp_age);
                        dialog.dismiss();
                    }
                });
                final CycleWheelView cycleWheelView1 = (CycleWheelView)dialog.findViewById(R.id.cycleWheelView1);
                /*CycleWheelView cycleWheelView2 = (CycleWheelView)dialog.findViewById(R.id.cycleWheelView2);
                cycleWheelView2.setVisibility(View.GONE);*/
                cycleWheelView1.setLabels(class_name);
                if(Integer.parseInt(temp_age)!=0)
                    cycleWheelView1.setSelection(Integer.parseInt(temp_age)-1);
                cycleWheelView1.setAlphaGradual(0.5f);
                cycleWheelView1.setDivider(ContextCompat.getColor(getActivity(), R.color.lighter_gray), (int) convertDpToPixel(1,getActivity()));
                cycleWheelView1.setSolid(ContextCompat.getColor(getActivity(), android.R.color.white), ContextCompat.getColor(getActivity(), android.R.color.white));
                cycleWheelView1.setLabelColor(ContextCompat.getColor(getActivity(), R.color.text_gray));
                cycleWheelView1.setLabelSelectColor(ContextCompat.getColor(getActivity(), R.color.darker_gray));
                try {
                    cycleWheelView1.setWheelSize(7);
                } catch (CycleWheelView.CycleWheelViewException e) {
                    e.printStackTrace();
                }
                cycleWheelView1.setOnWheelItemSelectedListener(new CycleWheelView.WheelItemSelectedListener() {
                    @Override
                    public void onItemSelected(int position, String label) {
                        about_age.setText(label);
                    }
                });

                done.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        wheel_position=cycleWheelView1.getSelection();
                        about_age.setText(cycleWheelView1.getSelectLabel());
                        dialog.dismiss();
                    }
                });

                Window dialogWindow = dialog.getWindow();
                dialogWindow.setGravity(Gravity.BOTTOM );
                dialog.show();

            }
        });
        if(!isEdit) {
            about_photo.setClickable(false);
            relative_gender.setClickable(false);
            relative_nickname.setClickable(false);
            relative_age.setClickable(false);
            back.setClickable(false);
        }

    }


    Uri uritempFile;
    //拍照完畢或選取圖片後呼叫此函式
    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data)
    {
        //藉由requestCode判斷是否為開啟相機或開啟相簿而呼叫的，且data不為null
        if ((requestCode == PHOTO ) && data != null && resultCode == Activity.RESULT_OK)
        {
            Uri uri = data.getData();
            /*File filePath=new File(Environment.getExternalStorageDirectory().toString()+"/Android/data/com.example.demo");
            tmpFile = new File(filePath,getPhotoFileName());*/
            //Log.v("tmpFile",tmpFile.toString());
            Intent i=getCropImageIntent(uri);
            startActivityForResult(i,GET);
            super.onActivityResult(requestCode, resultCode, data);
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////


        }else if(requestCode == GET && resultCode == Activity.RESULT_OK)
        {
            Uri uri= uritempFile;

            //image1.setImageBitmap(bmp);
            if(uri.getPath()!=null)
            {
                Log.v("uri.getPath()", uri.getPath());
                Bitmap bmp = BitmapFactory.decodeFile(uri.getPath()); //利用BitmapFactory去取得剛剛拍照的圖像
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog = myapi.new LoadingDialog(mActivity, "上傳中...", false);
                        dialog.execute();
                    }
                });
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("user_ID", pref.getString("num","")));
                params.add(new BasicNameValuePair("photo", uri.getPath()));
                String result = myapi.postMethod_getCode(mActivity , App.upload, params);
                Log.v("uploadImg","result = "+ result);

                try {
                    JSONObject o = new JSONObject(result);
                    if(o.getString("status").equals("1")) {

                        pref.edit().putString("photo",o.getString("message")).apply();

                        if (bmp.getWidth() > bmp.getHeight())
                            ScalePic(about_photo, bmp, mPhone.heightPixels);
                        else
                            ScalePic(about_photo, bmp, mPhone.widthPixels);
                    }else{

                    }
                } catch (JSONException e) {
                    Toast.makeText(mActivity,"無法連接伺服器",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }


                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.close();
                    }
                });
            }

        }
        //about_photo.setBackgroundResource(R.drawable.photo_border);
        super.onActivityResult(requestCode, resultCode, data);
    }
    public Intent getCropImageIntent(Uri photoUri)
    {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 600);
        intent.putExtra("outputY", 600);
        intent.putExtra("scale", true);
        uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/Android/data/co.nineka" + getPhotoFileName());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        //intent.putExtra("return-data", true);
        return intent;
    }
    private void ScalePic(ImageView photo, Bitmap bitmap,int phone)
    {

        //縮放比例預設為1
        float mScale = 1 ;
        Log.i(TAG, "phone = " + phone);
        boolean isLandScape = bitmap.getWidth()>bitmap.getHeight();
        int size = isLandScape ? bitmap.getHeight() : bitmap.getWidth();
        //如果圖片寬度大於手機寬度則進行縮放，否則直接將圖片放入ImageView內
        if(bitmap.getWidth() > mPhone.widthPixels )
        {
            Log.i(TAG,"圖片寬度大於手機寬度 " + "Size = " + size);
            //判斷縮放比例
            mScale = (float)phone/(float)bitmap.getWidth();
            Matrix mMat = new Matrix() ;
            mMat.setScale(mScale, mScale);


            Bitmap mScaleBitmap = Bitmap.createBitmap(bitmap,
                    isLandScape?  (( bitmap.getWidth() - size) / 2) : 0,
                    isLandScape? 0 :  (( bitmap.getHeight() - size) / 2),
                    size,
                    size,
                    mMat,
                    false);
            photo.setImageBitmap(getRoundedCornerBitmap(
                    mScaleBitmap, (mPhone.widthPixels < 1080) ? 180.0f : 300.0f));

        } else {
            Log.i(TAG, "圖片寬度小於手機寬度 " + "Size = " + size);
            Matrix mMat = new Matrix() ;
            mMat.setScale(mScale, mScale);
            Bitmap mScaleBitmap = Bitmap.createBitmap(bitmap,
                    isLandScape? (( bitmap.getWidth() - size) / 2) : 0,
                    isLandScape? 0 : (( bitmap.getHeight() - size) / 2),
                    size,
                    size,
                    mMat,
                    false);
            float round = (mPhone.widthPixels<1080)?180.0f:300.0f;
            int phoneSize = mPhone.widthPixels;
            if(size>=photo.getWidth()){
                round = round * ((float)size/(float)phoneSize);
            }else if(photo.getWidth()>size){
                round = round * ((float)size/(float)phoneSize);
            }
            /*if(about_photo.getWidth()>size) {
                round = 30.0f * ((float) size / (float) about_photo.getWidth());
                about_photo.setImageBitmap(getRoundedCornerBitmap(
                        mScaleBitmap, round));
            }*/

            photo.setImageBitmap(getRoundedCornerBitmap(
                        mScaleBitmap, round ));
            Log.i(TAG,"round = "+round);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_tab, container, false);
    }
    private void gotoAboutEditTab(View v){
        edit.setImageResource(R.drawable.about_confirm);
        txt_change_photo.setVisibility(View.VISIBLE);
        back.setVisibility(View.VISIBLE);
        back.setClickable(true);
        about_photo.setClickable(true);
        relative_gender.setClickable(true);
        relative_nickname.setClickable(true);
        relative_age.setClickable(true);
        about_nickname.setEnabled(true);
        relative_age.setEnabled(true);
        isEdit = true;
        pre_gender = about_gender.getText().toString();
        pre_nickname = about_nickname.getText().toString();
        pre_age = about_age.getText().toString();
    }
    @SuppressWarnings("deprecation")
    private void gotoAboutTab(View v){
        relative_nickname.clearFocus();
        InputMethodManager imm = (InputMethodManager) relative_age.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //if(imm.isActive())
        imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

        if(about_nickname.getText().toString().isEmpty()){
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog = myapi.new LoadingDialog(AboutTab.this.getActivity(), "暱稱不得為空！", true);
                    dialog.execute();
                }
            });
            return;
        }
        if(pre_age.equals(about_age.getText().toString())&&
                pre_gender.equals(about_gender.getText().toString())&&
                pre_nickname.equals(about_nickname.getText().toString())) {

            undo(v);
            return;
        }
        mythread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog = myapi.new LoadingDialog(AboutTab.this.getActivity(), "請稍後...", false);
                            dialog.execute();
                        }
                    });

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    //params.add(new BasicNameValuePair("filename", f.getName()));
                    params.add(new BasicNameValuePair("user_ID", pref.getString("num","")));
                    params.add(new BasicNameValuePair("gender", gender));
                    params.add(new BasicNameValuePair("name", about_nickname.getText().toString()));
                    params.add(new BasicNameValuePair("age", about_age.getText().toString()));
                    //TODO 串 上傳帳戶資訊API
                    final String result = myapi.postMethod_getCode(mActivity, App.User_Info, params);
                    dialog.close();
                    try {
                        JSONObject o = new JSONObject(result);
                        if(o.getString("status").equals("1")){
                            back.setClickable(false);
                            about_photo.setClickable(false);
                            relative_gender.setClickable(false);
                            relative_nickname.setClickable(false);
                            relative_age.setClickable(false);
                            isEdit = false;
                            //TODO  存入pref
                            pref.edit()
                                    .putString("gender", gender)
                                    .putString("name",about_nickname.getText().toString())
                                    .putString("age", about_age.getText().toString())
                                    .apply();
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    edit.setImageResource(R.drawable.edit);
                                    relative_nickname.clearFocus();
                                    txt_change_photo.setVisibility(View.GONE);
                                    back.setVisibility(View.INVISIBLE);
                                    about_nickname.setEnabled(false);
                                    relative_age.setEnabled(false);
                                    dialog = myapi.new LoadingDialog(AboutTab.this.getActivity(), "更改個人資料成功！", true);
                                    dialog.execute();
                                }
                            });
                        }else{
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog = myapi.new LoadingDialog(AboutTab.this.getActivity(), "更改個人資料失敗！", true);
                                    dialog.execute();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(mActivity, result, Toast.LENGTH_LONG).show();
                        }
                    });

                }catch (NullPointerException ne){
                    ne.printStackTrace();
                }

            }
        });
        mythread.start();

    }
    private void undo(View v) {
        edit.setImageResource(R.drawable.edit);
        relative_nickname.clearFocus();
        txt_change_photo.setVisibility(View.GONE);
        back.setVisibility(View.INVISIBLE);
        back.setClickable(false);
        about_photo.setClickable(false);
        relative_gender.setClickable(false);
        relative_nickname.setClickable(false);
        relative_age.setClickable(false);
        about_nickname.setEnabled(false);
        relative_age.setEnabled(false);
        about_gender.setText(pre_gender);
        about_nickname.setText(pre_nickname);
        about_age.setText(pre_age);
        isEdit = false;

    }
    String gender ;
    String[] GenderListStr = {"女", "男"};
    public void GenderListAlertDialog(View v) {

        AlertDialog.Builder TimeListAlertDialog = new AlertDialog.Builder(mActivity);
        TimeListAlertDialog.setTitle("請選擇性別");
        DialogInterface.OnClickListener ListItemClick = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                about_gender.setText(GenderListStr[which]);
                gender = which+"";
            }
        };

        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Nothing
            }
        };

        TimeListAlertDialog.setItems(GenderListStr, ListItemClick);
        TimeListAlertDialog.setNeutralButton("取消", OkClick);
        TimeListAlertDialog.show();

    }

    //圓角轉換函式，帶入Bitmap圖片及圓角數值則回傳圓角圖，回傳Bitmap再置入ImageView
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,float roundPx)
    {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }


    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'"+pref.getString("num","")+"'_yyyy_MM_dd_HH_mm_ss");
        return dateFormat.format(date) + ".jpg";
    }
    public float convertDpToPixel(float dp, Context context){
        float px = dp * getDensity(context);
        return px;
    }

    public float convertPixelToDp(float px, Context context){
        float dp = px / getDensity(context);
        return dp;
    }

    public float getDensity(Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        return metrics.density;
    }
}
