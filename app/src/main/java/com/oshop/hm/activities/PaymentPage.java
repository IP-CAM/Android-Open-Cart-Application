package com.oshop.hm.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.oshop.hm.R;

public class PaymentPage extends AppCompatActivity {

    WebView webView ;
    String telr_success_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);

        webView = (WebView) findViewById(R.id.webview);
        Toast.makeText(this, getResources().getString(R.string.please_wait_page_complete), Toast.LENGTH_LONG).show();
        setResult(Activity.RESULT_CANCELED);

        // ***** Enable javascript for web view****

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.contains("success")){
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("telr_success_message",telr_success_message);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                    return true;
                } else if(url.contains("failed")){
                    Intent resultIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, resultIntent);
                    finish();
                    return false;
                }
                else if(url.contains("checkout")){
                    Intent resultIntent = new Intent();
                    setResult(Activity.CONTEXT_RESTRICTED, resultIntent);
                    finish();
                    return false;
                }
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

        String url = "http://bisuraa.com/o-shop";
        try{
            url = getIntent().getStringExtra("telr_url");
            telr_success_message = getIntent().getStringExtra("telr_success_message");
        }catch (Exception e){
            Toast.makeText(this, "interenal Error X01", Toast.LENGTH_SHORT).show();
        }
        webView.loadUrl(url);
    }

    private boolean isPaidSuccess(String url){
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        setResult(Activity.CONTEXT_RESTRICTED, resultIntent);
        finish();
        super.onBackPressed();
    }
}
