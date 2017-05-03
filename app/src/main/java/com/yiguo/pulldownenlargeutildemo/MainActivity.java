package com.yiguo.pulldownenlargeutildemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yiguo.pulldownenlargeutil.PullDownEnlargeAnimUtil;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new PullDownEnlargeAnimUtil(findViewById(R.id.scrollView),findViewById(R.id.image));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //强迫症
        ImageView imageView = (ImageView) findViewById(R.id.image);
        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        lp.height = imageView.getWidth();
        imageView.setLayoutParams(lp);
    }
}
