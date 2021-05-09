package com.example.ujiseabattle.GameSystem

import android.util.Log
import java.time.LocalDateTime

class System ()
{
    var time : Float = 0f
    var deltaTime : Float = 0f

    fun print(msg:String)
    {
        Log.d("Custom","$time -> $msg")
    }

    fun updateValues(deltaTime:Float)
    {
        this.deltaTime = deltaTime
        time += deltaTime

    }


}