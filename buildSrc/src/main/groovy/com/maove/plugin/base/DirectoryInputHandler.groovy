package com.maove.plugin.base

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.TransformOutputProvider
import org.apache.commons.io.FileUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
/**
 * @author: mao
 * @date: 21-3-8
 * @desc: 处理自己写的class输入
 */
public class DirectoryInputHandler extends BaseInputHandle{

    public DirectoryInputHandler(String injectClass){
        super(injectClass)
    }

    public void handleDirectoryInput(DirectoryInput directoryInput,
                                     TransformOutputProvider outputProvider,
                                     Closure closure){
        if (directoryInput.file.isDirectory()) {
            directoryInput.file.eachFileRecurse { File file ->
                String name = file.name
                println("handleDirectoryInput file ===>"+name)
                if (filterClass(name)) {
                    //把类文件交给ClassReader进行读取
                    ClassReader classReader = new ClassReader(file.bytes)
                    ClassWriter classWriter = new ClassWriter(classReader,ClassWriter.COMPUTE_MAXS)
                    ClassVisitor classVisitor =  closure.call(classWriter)
                    //accept 方法接受一个 ClassVisitor 实现类，并按照顺序调用 ClassVisitor 中的方法
                    classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
                    //ClassWriter 是将修改后的类的字节码以字节数组的形式输出
                    byte[] code = classWriter.toByteArray()
                    FileOutputStream fos = new FileOutputStream(
                            file.parentFile.absolutePath + File.separator + name)
                    fos.write(code)
                    fos.close()
                }
            }
        }
        //把文件夹类型的输入拷贝到目标输入
        //目标目录和原来目录一样
        def destDir = outputProvider.getContentLocation(
                directoryInput.name,
                directoryInput.contentTypes,
                directoryInput.scopes,
                Format.DIRECTORY)
        FileUtils.copyDirectory(directoryInput.file, destDir)
    }
}