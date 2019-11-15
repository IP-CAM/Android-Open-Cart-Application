package com.oshop.hm.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.oshop.hm.R;

public class SellerPanel extends AppCompatActivity {

    WebView webView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_panel);

        webView = (WebView) findViewById(R.id.webview);
        Toast.makeText(this, getResources().getString(R.string.please_login_to_control_seller_pannel), Toast.LENGTH_LONG).show();

        // ***** Enable javascript for web view****

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                if(url.contains("success")){
//                    Intent resultIntent = new Intent();
//                    resultIntent.putExtra("telr_success_message",telr_success_message);
//                    setResult(Activity.RESULT_OK, resultIntent);
//                    finish();
//                    return true;
//                }
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            public void onPageFinished(WebView view, String url) {
            }
        });
        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        // ***** Enable javascript for web view****

        String url = "http://bisuraa.com/o-shop/index.php?route=account/account";
        webView.loadUrl(url);
    }
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
