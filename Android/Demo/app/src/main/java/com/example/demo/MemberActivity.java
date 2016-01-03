package com.example.demo;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.demo.util.RoundSquareTransform;
import com.squareup.picasso.Picasso;

public class MemberActivity extends Activity {
    TextView titletextView, member_gender, member_nickname, member_age;
    View customView;
    Bundle bundle;
    ImageView member_photo;
    RatingBar ratingBar;
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_member);
        /*
		 * View  Initialize
		 */
        ActionBar ab = this.getActionBar();
        ab.setDisplayShowCustomEnabled(true);
        setTitle("");
        View homeIcon = findViewById(android.R.id.home);
        ((View) homeIcon.getParent()).setVisibility(View.GONE);
        bundle = getIntent().getExtras();
        LayoutInflater li = LayoutInflater.from(this);
        customView = li.inflate(R.layout.drawerlayout, null);

        titletextView = (TextView) customView.findViewById(R.id.titletextView);
        titletextView.setText(bundle.getString("name"));
        TextView back = (TextView) customView.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ab.setCustomView(customView);
        member_gender =  (TextView) findViewById(R.id.member_gender);
        member_nickname =  (TextView) findViewById(R.id.member_nickname);
        member_age =  (TextView) findViewById(R.id.member_age);
        member_photo = (ImageView) findViewById(R.id.member_photo);

        member_gender.setText(bundle.getString("gender").equals("0")?"女":"男");
        member_nickname.setText(bundle.getString("name"));
        member_age.setText(bundle.getString("age"));
        Picasso.with(this)
                .load(bundle.getString("photo"))
                .placeholder(R.drawable.about_photo_default)
                .transform(new RoundSquareTransform()).into(member_photo);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        ratingBar.setRating(Float.valueOf(bundle.getString("rate","0")));
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                if (rating < 1) {
                    ratingBar.setRating(1.0f);
                }
            }
        });
        ratingBar.setIsIndicator(true);
    }


}
