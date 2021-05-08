package com.example.ujiseabattle.GameElements.Ships

import android.graphics.Bitmap
import com.example.ujiseabattle.GameSystem.GamePresenter
import com.example.ujiseabattle.GameSystem.Vector2

class Ship(var x :Int,var y: Int,var size:Int,var bitmapHorizontal:Bitmap?,var bitmapVertical:Bitmap?,var p:GamePresenter)
{
    var inHorizontal = true
    var activeBitmap : Bitmap? = bitmapHorizontal

    init {
        setOccupied(true)
    }

    fun moveTo(x:Int,y:Int)
    {
        //colision paredes y colision unidades
        if(!checkCollisions(x,y))return



        //limpiar ocupados
        setOccupied(false)
        this.x = x;
        this.y = y;

        //rellenar ocupados
        setOccupied(true)


    }

    private  fun checkCollisions(desiredX: Int,desiredY: Int):Boolean
    {
        //Check paredes
        if(inHorizontal)
        {
            val lastY = desiredY + size -1
            if(lastY >= p.totalColumns)return false
        }
        else
        {
            val lastX = desiredX + size -1
            if(lastX >= p.totalRows)return false
        }

        //Check unidades
        for(i in 0 until size )
        {
            if(inHorizontal)
            {
                val s = p.canvasGrid[desiredY+i][desiredX].occupier
                if( s != null && s != this)return  false
            }
            else
            {
                val s = p.canvasGrid[desiredY][desiredX+i].occupier
                if( s != null && s != this)return  false
            }

        }

        return  true
    }


    private fun setOccupied(value:Boolean)
    {
        var o:Ship? = this
        if(!value)o = null

        for(i in 0 until size)
        {
            if(inHorizontal)p.canvasGrid[y+i][x].occupier = o
            else p.canvasGrid[y][x+1].occupier = o
        }
    }

    fun changeOrientation()
    {
        setOccupied(false)
        inHorizontal = !inHorizontal
        activeBitmap = bitmapVertical
        setOccupied(true)



    }

    /*
    var realPos : Vector2 = Vector2.zero
    set(newPos:Vector2)
    {
        //metodo de mover
        field = newPos
    }
    */

}