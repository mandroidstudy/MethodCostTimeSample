package com.maove.libtest;

/**
 * @author maoweiyi
 * @time 2022/3/2
 * @describe
 */
public class TestJava {
    public static void test1(){
        System.out.println("hello!!!");
    }

    public static void testJava2(){
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
