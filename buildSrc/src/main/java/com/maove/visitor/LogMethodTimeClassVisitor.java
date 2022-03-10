package com.maove.visitor;

import com.maove.MethodConfig;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;

/**
 * @author: maove
 * @date: 22-3-1
 * @desc:
 */
public class LogMethodTimeClassVisitor extends ClassVisitor {

    private MethodConfig methodConfig;

    public LogMethodTimeClassVisitor(ClassVisitor classVisitor, MethodConfig methodConfig) {
        //当前使用的ASM API版本
        super(Opcodes.ASM6, classVisitor);
        this.methodConfig = methodConfig;
    }

    /**
     * 访问类头部信息
     * @param version class版本
     * @param access class方法标识符
     * @param name class名称
     * @param signature 类签名
     * @param superName 父类名称
     * @param interfaces 实现的接口
     */
    @java.lang.Override
    public void visit(int version, int access, java.lang.String name, java.lang.String signature, java.lang.String superName, java.lang.String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        methodConfig.setClassName(name);
    }

    /**
     * 访问方法
     * @param access 方法访问标识符
     * @param name 方法名称
     * @param descriptor 方法描述符
     * @param signature 方法签名
     * @param exceptions 方法异常信息
     * @return
     */
    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (methodVisitor==null){
            return null;
        }
        return new LogMethodTimeMethodVisitor(methodVisitor,access,name,descriptor,methodConfig);
    }

    /**
     * 访问类上的注解
     * @param descriptor 描述
     * @param visible 运行时是否可见
     * @return
     */
    @java.lang.Override
    public AnnotationVisitor visitAnnotation(java.lang.String descriptor, boolean visible) {
        return super.visitAnnotation(descriptor, visible);
    }

    /**
     * 访问类字段
     * @return
     */
    @java.lang.Override
    public FieldVisitor visitField(int access, java.lang.String name, java.lang.String descriptor, java.lang.String signature, java.lang.Object value) {
        return super.visitField(access, name, descriptor, signature, value);
    }

    @java.lang.Override
    public void visitAttribute(Attribute attribute) {
        super.visitAttribute(attribute);
    }

    /**
     * 在访问类的过程中最后一个被调用的方法，我们可以在这个方法中为类追加信息
     */
    @java.lang.Override
    public void visitEnd() {
        super.visitEnd();
    }
}
