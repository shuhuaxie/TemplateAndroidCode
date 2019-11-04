package com.example.gs.templatecode;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

public class TestDim extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        Log.e("xie", "width:" + dm.widthPixels);
        Log.e("xie", "height:" + dm.heightPixels);

    }
}
