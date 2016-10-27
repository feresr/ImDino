package com.feresr.imdino;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface", "AddJavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webview);
        final WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(this, "Android");
        webView.loadUrl("file:///android_asset/index.html");
    }

    @JavascriptInterface
    public void onGameUpdate(float obstacleX, float currentSpeed) {

    }

    @JavascriptInterface
    public void onGameStart() {

    }

    @JavascriptInterface
    public void onGameOver(int score) {

    }
}
