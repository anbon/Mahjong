package co.nineka;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

public class WebViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        setTitle("");
        ActionBar ab = this.getActionBar();
        ab.setDisplayShowCustomEnabled(true);
        View homeIcon = findViewById(android.R.id.home);// icon���食�
        ((View) homeIcon.getParent()).setVisibility(View.GONE);
        LayoutInflater li = LayoutInflater.from(this);
        View customView = li.inflate(R.layout.drawerlayout, null);
        TextView actitle = (TextView) customView
                .findViewById(R.id.titletextView);
        actitle.setText(getIntent().getExtras().getString("title"));
        TextView back = (TextView) customView.findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();
            }

        });
        ab.setCustomView(customView);

        WebView myBrowser=(WebView)findViewById(R.id.mybrowser);

        WebSettings websettings = myBrowser.getSettings();
        websettings.setSupportZoom(true);
        websettings.setBuiltInZoomControls(true);
        websettings.setJavaScriptEnabled(true);

        myBrowser.setWebViewClient(new WebViewClient());

        myBrowser.loadUrl(getIntent().getExtras().getString("url"));
    }
}
