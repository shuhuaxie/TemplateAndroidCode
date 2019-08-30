package com.example.gs.templatecode;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_load_more).setOnClickListener(this);
        findViewById(R.id.tv_web_view).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_load_more:
                startActivity(new Intent(this, RecyclerViewLoadMoreActivity.class));
                break;
            case R.id.tv_web_view:
                startActivity(new Intent(this, WebViewActivity.class));
                break;
        }
    }
}
