package com.example.ujiseabattle.GameElements

import android.util.Log
import com.example.ujiseabattle.GameSystem.GamePresenter

class Button (val x:Int,val y:Int,val size:Int,val boxColor: Int,val text:String,val  textSize:Int,val  textColor:Int,val buttonID: Int,val p:GamePresenter)
{


    init {

        setOccupied(true)

    }

    fun setOccupied(value:Boolean)
    {
        var o : Button? = this
        if(!value)o = null

        for(row in y until y + size)
        {
            for(column in x until x+size)
            {
                p.canvasGrid[row][column].occupierButton = o
            }
        }
    }


}