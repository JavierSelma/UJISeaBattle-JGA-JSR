package com.example.ujiseabattle.GameSystem

import es.uji.vj1229.framework.AnimatedBitmap

data class Animation(val row:Int,val column:Int,val animation:AnimatedBitmap?)
{
    init {
        animation?.restart()
    }
}
