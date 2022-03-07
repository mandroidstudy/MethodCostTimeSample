package com.maove.plugin.base

/**
 * @author: mao
 * @date: 21-3-8
 * @desc: 输入处理的基类
 */
public abstract class BaseInputHandle{

    protected String injectClass

    public BaseInputHandle(String injectClass){
        this.injectClass=injectClass
    }
    public boolean filterClass(String className){
        return (className.endsWith(".class") && !className.startsWith("R\$")
                && "R.class" != className && "BuildConfig.class" != className
                && !className.contains(injectClass))
    }
}