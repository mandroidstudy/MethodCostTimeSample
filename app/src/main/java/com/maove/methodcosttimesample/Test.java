package com.maove.methodcosttimesample;

import android.view.View;

/**
 * @author maoweiyi
 * @time 2022/3/1
 * @describe
 */
public class Test {

    public Test(View.OnClickListener listener){

    }

    private int test() {

        return 1;
    }

    private int test(String p1) {
        return 1;
    }

    private void test(String p1,String p2) {
    }

    public native void nativeTest();
}
