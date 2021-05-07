package com.example.ujiseabattle.GameSystem

import android.util.Log
import es.uji.vj1229.framework.TouchHandler

class GameModel
{
    lateinit var  presenter: GamePresenter
    var time : Float = 0f


    fun onUpdate(deltaTime: Float, touchEvents: MutableList<TouchHandler.TouchEvent>?)
    {
        time += deltaTime


    }

}