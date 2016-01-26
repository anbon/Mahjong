package com.example.demo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SearchTab extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static boolean isAlive = false;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Activity mActivity;
    Context ctx;
    ActionBar ab;
    View customView;
    TextView titletextView, search_confirm;
    EditText search_input;
    SharedPreferences pref;
    App myapi;
    App.LoadingDialog dialog;
    Thread mythread;
    //private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NearbyTab.
     */
    public static SearchTab newInstance(String param1, String param2) {
        SearchTab fragment = new SearchTab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SearchTab() {
        // Required empty public constructor
    }
    @Override
    public void onPause() {
        super.onPause();
        if (mythread != null) {
            mythread.interrupt();
            mythread = null;
        }
        //isAlive = false;
    }
    @Override
    public void onStop() {
        super.onStop();
        isAlive = false;
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //if(imm.isActive())
        imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }
    @Override
    public void onResume() {
        super.onResume();

        isAlive = true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        ctx = getActivity();
        mActivity = getActivity();
        ab = mActivity.getActionBar();
        ab.setDisplayShowCustomEnabled(true);
        myapi = (App) mActivity.getApplicationContext();
        View homeIcon = mActivity.findViewById(android.R.id.home);
        ((View) homeIcon.getParent()).setVisibility(View.GONE);
        pref = mActivity.getSharedPreferences("Account", 0);
        LayoutInflater li = LayoutInflater.from(mActivity);
        customView = li.inflate(R.layout.drawerlayout, null);

        titletextView = (TextView) customView.findViewById(R.id.titletextView);
        titletextView.setText(mActivity.getResources().getString(R.string.title_search));
        ab.setCustomView(customView);
        //

        search_confirm = (TextView) mActivity.findViewById(R.id.search_confirm);
        search_input = (EditText) mActivity.findViewById(R.id.search_input);


        search_confirm.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mythread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog = myapi.new LoadingDialog(mActivity, "請稍後...", false);
                                dialog.execute();
                            }
                        });

                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("user_ID", pref.getString("num","")));
                        params.add(new BasicNameValuePair("username", search_input.getText().toString()));
                        String result = myapi.postMethod_getCode(mActivity, App.Search_User, params);
                        Log.i("SearchTab", result);

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.close();
                            }
                        });
                        parseResult(result);
                    }
                });
                if(search_input.getText().toString().isEmpty()){
                    Toast.makeText(mActivity, "您尚未輸入", Toast.LENGTH_SHORT).show();
                }
                else
                    mythread.start();
            }
        });

    }

    private void parseResult(String result) {
        try {
            JSONObject o = new JSONObject(result);
            if(o.getString("status").equals("1")){

                JSONObject o_o = o.getJSONObject("message");
                Bundle b= new Bundle();
                b.putString("num", o_o.getString("num"));
                b.putString("name", o_o.getString("name"));
                b.putString("email", o_o.getString("email"));
                b.putString("username", o_o.getString("username"));
                b.putString("password", o_o.getString("password"));
                b.putString("gender", o_o.getString("gender"));
                b.putString("age", o_o.getString("age"));
                b.putString("photo", o_o.getString("photo"));
                b.putString("rate", o_o.getString("rate"));
                b.putString("level", o_o.getString("level"));
                b.putString("follow", o_o.getString("follow"));
                b.putString("Rooming", o_o.getString("Rooming"));
                b.putString("RoomMessage", o.getString("RoomMessage"));
                Intent intent = new Intent();
                intent.putExtras(b);
                intent.setClass(mActivity, SearchResultActivity.class);
                //mActivity.overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
                startActivity(intent);
            }else if(o.getString("status").equals("0")){
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog = myapi.new LoadingDialog(mActivity, "查無此帳號!", true);
                        dialog.execute();
                    }
                });

            }
        } catch (JSONException e) {
            e.printStackTrace();
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog = myapi.new LoadingDialog(mActivity, "伺服器發生錯誤!", true);
                    dialog.execute();
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_tab, container, false);
    }






    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/



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
