package com.maove.methodcosttimesample

import android.content.Context
import android.widget.Toast
import org.jetbrains.annotations.NotNull

/**
 * @author maoweiyi
 * @time 2022/3/4
 * @describe
 */
class ktTest {
    fun hello(@NotNull ctx:Context){
        Toast.makeText(ctx,"hello",Toast.LENGTH_LONG).show()
    }
    fun hello(){
    }
}