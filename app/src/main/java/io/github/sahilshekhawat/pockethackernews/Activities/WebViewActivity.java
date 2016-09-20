package io.github.sahilshekhawat.pockethackernews.Activities;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import io.github.sahilshekhawat.pockethackernews.R;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //toolbar.setTitle(getTitle());

        WebView mWebview = (WebView) findViewById(R.id.webview);
        /*mWebview.getSettings().setJavaScriptEnabled(true);

        if(getIntent().hasExtra("url")){
            String url = getIntent().getStringExtra("url");
            mWebview.loadUrl(url);
        }*/

        final ProgressDialog pd = ProgressDialog.show(WebViewActivity.this, "", "Loading...", true);

        mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript
        mWebview.getSettings().setLoadWithOverviewMode(true);
        mWebview.getSettings().setUseWideViewPort(true);
        mWebview.getSettings().setBuiltInZoomControls(true);

        mWebview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(WebViewActivity.this, description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                pd.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pd.dismiss();
                url = getIntent().getStringExtra("url");
            }

        });

        mWebview.loadUrl(getIntent().getStringExtra("url"));


    }
}
