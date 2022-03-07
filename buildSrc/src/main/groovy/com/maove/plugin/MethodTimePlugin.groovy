package com.maove.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author: maove
 * @date: 22-3-1
 * @desc:
 */
class MethodTimePlugin implements Plugin<Project>{

    @Override
    void apply(Project project) {
        println "apply MethodTimePlugin..."

        if (!project.plugins.hasPlugin(AppPlugin)) {
            throw GradleException("MethodTimePlugin Plugin, Android Application plugin required.")
        }
        project.extensions.create("methodTime", MethodTimeExt.class)
        def android = project.extensions.getByType(AppExtension.class)
        MethodTimeTransform transform  = new MethodTimeTransform(project)
        android.registerTransform(transform)
    }
}