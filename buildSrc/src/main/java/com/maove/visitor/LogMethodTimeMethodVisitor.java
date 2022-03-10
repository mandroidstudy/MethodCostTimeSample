package com.maove.visitor;


import com.maove.MethodConfig;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * @author: maove
 * @date: 22-3-1
 * @desc:
 */
public class LogMethodTimeMethodVisitor extends AdviceAdapter {

    private String className;
    private String methodName;
    private String descriptor;
    private MethodConfig methodConfig;

    protected LogMethodTimeMethodVisitor(MethodVisitor methodVisitor, int access, String name, String descriptor,MethodConfig methodConfig) {
        super(Opcodes.ASM6, methodVisitor, access, name, descriptor);
        this.className = methodConfig.getClassName();
        this.methodName = name;
        this.descriptor = descriptor;
        this.methodConfig = methodConfig;
    }

    /**
     * 进入方法时调用
     */
    @Override
    protected void onMethodEnter() {
        mv.visitLdcInsn(className);
        mv.visitLdcInsn(methodName);
        mv.visitLdcInsn(descriptor);
        mv.visitMethodInsn(INVOKESTATIC,
                methodConfig.injectClass,
                methodConfig.methodIn,
                "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V",
                false);
    }

    /**
     * 即将从这个方法退出时调用
     * @param opcode
     */
    @Override
    protected void onMethodExit(int opcode) {
        mv.visitLdcInsn(className);
        mv.visitLdcInsn(methodName);
        mv.visitLdcInsn(descriptor);
        mv.visitMethodInsn(INVOKESTATIC,
                methodConfig.injectClass,
                methodConfig.methodOut,
                "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V",
                false);
    }

    /**
     * 在访问方法的过程中最后一个被调用的方法
     */
    @java.lang.Override
    public void visitEnd() {
        super.visitEnd();
    }
}
