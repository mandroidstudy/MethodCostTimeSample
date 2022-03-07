package com.maove.visitor;

import com.maove.MethodConfig;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author: maove
 * @date: 22-3-1
 * @desc:
 */
public class LogMethodTimeClassVisitor extends ClassVisitor {

    private MethodConfig methodConfig;

    public LogMethodTimeClassVisitor(ClassVisitor classVisitor, MethodConfig methodConfig) {
        super(Opcodes.ASM6, classVisitor);
        this.methodConfig = methodConfig;
    }

    @java.lang.Override
    public void visit(int version, int access, java.lang.String name, java.lang.String signature, java.lang.String superName, java.lang.String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        methodConfig.setClassName(name);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (methodVisitor==null){
            return null;
        }
        return new LogMethodTimeMethodVisitor(methodVisitor,access,name,descriptor,methodConfig);
    }
}
