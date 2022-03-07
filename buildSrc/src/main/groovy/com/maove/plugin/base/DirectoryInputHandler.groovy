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
                    ClassReader classReader = new ClassReader(file.bytes)
                    ClassWriter classWriter = new ClassWriter(classReader,ClassWriter.COMPUTE_MAXS)
                    ClassVisitor classVisitor =  closure.call(classWriter)
                    classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
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