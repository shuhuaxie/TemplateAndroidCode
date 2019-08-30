package com.example.gs.templatecode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EmptyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        findViewById(R.id.tv_set_result).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("result_code","q_p .");
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
