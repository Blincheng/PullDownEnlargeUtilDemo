package com.mic.pulldownenlargeutil;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Blin on 2017/3/23.
 */

public class PullDownEnlargeAnimUtil {
    /**
     * 要放大的子View
     * */
    private View childView;
    /**
     * 监听滚动事件的父View
     * */
    private View parentView;
    /**
     * X方向放大的倍率
     * */
    private float MAGNIFICATION_X = 1.0f;
    /**
     * Y方向放大的倍率
     * */
    private float MAGNIFICATION_Y = 1.0f;
    /**
     * 是否正在放大
     * */
    private boolean isScaling;
    /**
     * 记录首次按下位置
     * */
    private float mFirstPosition = 0;
    /**
     * 下拉偏移量（主要用来逆向位移）
     * */
    private int nowDistance;
    /**
     * 要放大控件的初始宽高，第一次初始化在第一次走onTouch()的时候
     * */
    private int width = 0;
    private int height = 0;

    public PullDownEnlargeAnimUtil(View parentView,View childView){
        this.childView = childView;
        this.parentView = parentView;
        initEvent();
    }
    private void initEvent(){
        parentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                initView();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        //手指离开后恢复图片
                        isScaling = false;
                        replayAnim();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!isScaling) {
                            if (parentView.getScrollY() == 0) {
                                //滚动到顶部时需要记录当前位置
                                mFirstPosition = event.getY();
                            } else {
                                break;
                            }
                        }
                        int distance = (int) ((event.getY() - mFirstPosition) * 0.3); // 这边添加了一个对应滚动的系数，可以自由调节
                        if (distance < 0) {
                            break;
                        }
                        // 处理放大
                        isScaling = true;
                        ViewGroup.LayoutParams lp = childView.getLayoutParams();
                        lp.height = (int) (height + distance*MAGNIFICATION_Y);
                        lp.width = (int) (width+ distance*MAGNIFICATION_X);
                        childView.setLayoutParams(lp);
                        childView.scrollTo((int) ((distance/2)*MAGNIFICATION_X),0);
                        nowDistance = distance;
                        return true;
                }
                return false;
            }
        });
    }
    private void initView(){
        if(width == 0||height == 0){
            width = childView.getWidth();
            height = childView.getHeight();
        }
    }
    //松手后的回弹动画
    public void replayAnim() {
        final ViewGroup.LayoutParams lp = childView.getLayoutParams();
        final float w = childView.getLayoutParams().width;// 图片当前宽度
        final float h = childView.getLayoutParams().height;
        final float newW = width;// 图片原宽度
        final float newH = height;
        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0.0F, 1.0F).setDuration(200);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                lp.width = (int) (w - (w - newW) * cVal);
                lp.height = (int) (h - (h - newH) * cVal);
                childView.setLayoutParams(lp);
                childView.scrollTo((int) ((int) (nowDistance*(1-cVal)/2)*MAGNIFICATION_X),0);
            }
        });
        valueAnimator.start();
    }
}
