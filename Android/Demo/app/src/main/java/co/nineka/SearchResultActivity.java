package co.nineka;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import co.nineka.util.RoundSquareTransform;

public class SearchResultActivity extends Activity {
    RelativeLayout result_item;
    ImageView result_photo;
    TextView result_name,result_distance;
    TextView titletextView;
    View customView;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_search_result);
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
        titletextView.setText("搜尋結果");
        TextView back = (TextView) customView.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ab.setCustomView(customView);
        result_item = (RelativeLayout) findViewById(R.id.result_item);
        result_photo = (ImageView) findViewById(R.id.result_photo);
        result_name = (TextView) findViewById(R.id.result_name);
        result_distance = (TextView) findViewById(R.id.result_distance);

        bundle = getIntent().getExtras();

        Picasso.with(this)
                .load(bundle.getString("photo"))
                .placeholder(R.drawable.about_photo_default)
                .transform(new RoundSquareTransform()).into(result_photo);
        result_name.setText(bundle.getString("name"));
        //result_distance.setText("");

        result_item.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SearchResultActivity.this, MemberActivity.class);
                intent.putExtras(bundle);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(intent);
                finish();
            }
        });
    }


}
