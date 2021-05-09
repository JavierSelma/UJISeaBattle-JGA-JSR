package com.example.ujiseabattle.GameElements

import android.util.Log
import com.example.ujiseabattle.GameSystem.GamePresenter

class Button (val x:Int,val y:Int,val size:Int,val boxColor: Int,val text:String,val  textSize:Int,val  textColor:Int,val buttonID: Int,val activeType: Type,val p:GamePresenter)
{

    enum class Type {Square, Long}


    init {

        setOccupied(true)

    }

    fun setOccupied(value:Boolean)
    {
        var o : Button? = this
        if(!value)o = null

        if(activeType == Type.Long)
        {
            for(row in x until x + 2)
            {
                for(column in y until y+7)
                {
                    p.canvasGrid[column][row].occupierButton = o
                }
            }
        }
        else
        {
            for(row in x until x + size)
            {
                for(column in y until y+size)
                {
                    p.canvasGrid[column][row].occupierButton = o
                }
            }
        }



    }


}