package com.maove;

/**
 * @author: maove
 * @date: 22-3-1
 * @desc:
 */
public class MethodConfig {
    public boolean isOpen = true;
    public String injectClass;
    public String methodIn;
    public String methodOut;

    private String className;
    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
}
