package com.maove.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.maove.MethodConfig
import com.maove.plugin.base.DirectoryInputHandler
import com.maove.plugin.base.JarInputHandler
import com.maove.visitor.LogMethodTimeClassVisitor
import org.gradle.api.Project


/**
 * @author: maove
 * @date: 22-3-1
 * @desc:
 */
class MethodTimeTransform extends Transform{

    private Project project

    public MethodTimeTransform(Project project){
        this.project = project
    }

    //当前transform的名称
    @Override
    String getName() {
        return MethodTimeTransform.simpleName
    }

    //告知编译器，当前transform处理的输入类型，通常是class
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    //告知编译器，当前transform处理范围
    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    //是否支持增量
    @Override
    boolean isIncremental() {
        return false
    }

    //编译器会把所有的class收集封装到TransformInvocation中，然后传给这个方法
    /**
     * 1、遍历所有的输入
     * 2、对input进行二次处理
     * 3、将input拷贝到目标目录
     */
    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)

        MethodConfig methodConfig = initExtension()
        if (!methodConfig.isOpen) return
        if(methodConfig.injectClass == null
                || methodConfig.injectClass.length() == 0){
            return
        }
        String injectClass = methodConfig.injectClass.substring(methodConfig.injectClass.lastIndexOf("/")+1);
        TransformOutputProvider outputProvider = transformInvocation.outputProvider
        if (outputProvider != null)  outputProvider.deleteAll()
        DirectoryInputHandler directoryInputHandler=new DirectoryInputHandler(injectClass)
        JarInputHandler jarInputHandler=new JarInputHandler(injectClass)
        //所有的输入
        transformInvocation.inputs.each { TransformInput transformInput ->
            //文件夹类型
            transformInput.directoryInputs.each { DirectoryInput directoryInput ->
                directoryInputHandler.handleDirectoryInput(directoryInput, outputProvider){
                    return new LogMethodTimeClassVisitor(it,methodConfig)
                }
            }
            //jar包类型
            transformInput.jarInputs.each { JarInput jarInput ->
                jarInputHandler.handleJarInput(jarInput, outputProvider){
                    return new LogMethodTimeClassVisitor(it,methodConfig)
                }
            }
        }
    }

    private MethodConfig initExtension(){
        MethodTimeExt methodExt = project.methodTime
        MethodConfig methodConfig = new MethodConfig()
        methodConfig.isOpen = methodExt.isOpen
        methodConfig.injectClass = methodExt.injectClass
        methodConfig.methodIn = methodExt.methodIn
        methodConfig.methodOut = methodExt.methodOut
        return methodConfig
    }

}