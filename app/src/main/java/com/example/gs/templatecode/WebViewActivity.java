package com.example.gs.templatecode;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * 带有上拉刷新的模板代码
 */
public class WebViewActivity extends BaseActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mWebView = findViewById(R.id.web_view);

        try {
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setDomStorageEnabled(true);
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            mWebView.getSettings().setDatabaseEnabled(true);
            String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();

            mWebView.getSettings().setGeolocationDatabasePath(dir);
            mWebView.getSettings().setGeolocationEnabled(true);
            mWebView.getSettings().setBlockNetworkImage(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
//
//
            mWebView.setWebViewClient(new WebViewClient() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    Log.e("xie", "error:" + error.getDescription());
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (handleUrlLoading(url)) {
                        return true;
                    } else {
                        return super.shouldOverrideUrlLoading(view, url);
                    }
                }

                @Override
                public void onPageFinished(WebView view, String url) {
//                    mWebView.loadUrl("javascript:alert('hello')");
//                    mWebView.loadUrl("javascript:androidCallBack()");
                }

                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                }
            });
            //必须要设置
            mWebView.setWebChromeClient(new WebChromeClient());

        } catch (Exception e) {
            Log.e("xie", "e:" + e.getMessage());
        }
        mWebView.loadUrl("file:///android_asset/test.html");
//        mWebView.loadUrl("javascript:alert('go')");
        findViewById(R.id.tv_alert).setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                Log.e("xie", "alert...");
//                mWebView.loadUrl("javascript:alertEmpty()");
                mWebView.loadUrl("javascript:alertValue('go')");
//                mWebView.evaluateJavascript("javascript:alert('" + "xie!" + "')",
//                        new ValueCallback<String>() {
//                    @Override
//                    public void onReceiveValue(String s) {
//
//                    }
//                });
            }
        });

    }

    private boolean handleUrlLoading(String url) {
        if ("next://qr".equals(url)) {
            startActivityForResult(new Intent(this, EmptyActivity.class), 1);
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            final String resultContent = data.getStringExtra("result_code");
            //要等WebView准备好
            mWebView.post(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:alertValue('" + resultContent + "')");
                }
            });
        }
    }
}
