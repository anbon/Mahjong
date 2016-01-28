package co.nineka;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



import java.io.File;
import java.util.ArrayList;
import java.util.List;

import co.nineka.util.CycleWheelView;

public class FillInActivity extends Activity {
    App myapi;
    App.LoadingDialog dialog;
    Thread mythread;
    int wheel_position;
    SharedPreferences pref;
    ActionBar ab;
    View customView;;
    RelativeLayout relative_gender, relative_nickname, relative_age;
    TextView back, titletextView, about_gender, about_age;
    EditText about_nickname;
    //ImageView about_photo;
    List<String> class_name;
    //File tmpFile;
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
        //undo(getView());
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        InputMethodManager imm = (InputMethodManager) relative_age.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_fill_in);
        setTitle("");
        ab = getActionBar();
        ab.setDisplayShowCustomEnabled(true);
        View homeIcon = findViewById(android.R.id.home);
        ((View) homeIcon.getParent()).setVisibility(View.GONE);

        LayoutInflater li = LayoutInflater.from(this);
        customView = li.inflate(R.layout.drawerlayout, null);

        titletextView = (TextView) customView.findViewById(R.id.titletextView);
        titletextView.setText("個人資料");

        TextView back = (TextView) customView.findViewById(R.id.back);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ab.setCustomView(customView);
        myapi = (App) this.getApplicationContext();
        relative_gender = (RelativeLayout) findViewById(R.id.relative_gender);
        relative_nickname = (RelativeLayout) findViewById(R.id.relative_nickname);
        relative_age = (RelativeLayout) findViewById(R.id.relative_age);
        about_nickname = (EditText) findViewById(R.id.about_nickname);
        about_gender = (TextView) findViewById(R.id.about_gender);
        about_age = (TextView) findViewById(R.id.about_age);

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

                final Dialog dialog=new Dialog(FillInActivity.this, R.style.age_Dialog);
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
                cycleWheelView1.setDivider(ContextCompat.getColor(FillInActivity.this, R.color.lighter_gray), (int) convertDpToPixel(1, FillInActivity.this));
                cycleWheelView1.setSolid(ContextCompat.getColor(FillInActivity.this, android.R.color.white), ContextCompat.getColor(FillInActivity.this, android.R.color.white));
                cycleWheelView1.setLabelColor(ContextCompat.getColor(FillInActivity.this, R.color.text_gray));
                cycleWheelView1.setLabelSelectColor(ContextCompat.getColor(FillInActivity.this, R.color.darker_gray));
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


    }

    String gender ;
    String[] GenderListStr = {"女", "男"};
    public void GenderListAlertDialog(View v) {

        AlertDialog.Builder TimeListAlertDialog = new AlertDialog.Builder(FillInActivity.this);
        TimeListAlertDialog.setTitle("請選擇性別");
        DialogInterface.OnClickListener ListItemClick = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                about_gender.setText(GenderListStr[which]);
                gender = which + "";
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
