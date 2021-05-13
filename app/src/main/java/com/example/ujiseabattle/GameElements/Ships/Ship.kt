package com.example.ujiseabattle.GameElements.Ships

import android.graphics.Bitmap
import com.example.ujiseabattle.GameElements.Button
import com.example.ujiseabattle.GameElements.FieldTile
import com.example.ujiseabattle.GameElements.Tile
import com.example.ujiseabattle.GameSystem.GamePresenter
import com.example.ujiseabattle.GameSystem.Pair
import com.example.ujiseabattle.GameSystem.Vector2

class Ship(var x :Int,var y: Int,var size:Int,val bitmapPack: BitmapPack,var p:GamePresenter)
{
    var inHorizontal = true
    var activeBitmap : Bitmap? = bitmapPack.horizontal
    var occupiedTiles: MutableList<Tile> = mutableListOf<Tile>()
    var totalBombed  = 0

    init {
        setOccupied(true)
    }

    fun moveTo(x:Int,y:Int) : Boolean
    {
        //colision paredes y colision unidades
        if(!checkCollisions(x,y))return false



        //limpiar ocupados
        setOccupied(false)
        this.x = x;
        this.y = y;

        //rellenar ocupados
        setOccupied(true)
        return  true

    }

    fun increaseTotalBombed() : Boolean
    {
        p.soundPlayer.playSound(p.soundPlayer.shiphit)
        totalBombed += 1

        if(totalBombed == occupiedTiles.size)
        {
            for(t in occupiedTiles)
            {
                t.tileType = Tile.TileType.Discovered
                if(t.friendly)
                {
                    val pair = Pair(t.gridPos.Column,t.gridPos.Row)
                    pair.convertToAiCoords()

                    p.enemyAI.updateAs(pair,FieldTile.State.SUNK)
                }

            }

            activeBitmap = if(inHorizontal) bitmapPack.horizontalDestroyed
            else bitmapPack.verticalDestroyed

            p.soundPlayer.playSound(p.soundPlayer.shipdestroyed)
            return  true
        }

        return  false

    }

    fun getCurrentHealth(): Int {return  size - totalBombed}

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
        if(!value)occupiedTiles.clear()

        var o:Ship? = this
        if(!value)o = null

        for(i in 0 until size)
        {
            if(inHorizontal)
            {
                p.canvasGrid[y+i][x].occupier = o
                if(value)occupiedTiles.add(p.canvasGrid[y+i][x])
            }
            else
            {
                p.canvasGrid[y][x+i].occupier = o
                if(value)occupiedTiles.add(p.canvasGrid[y][x+i])
            }
        }
    }

    fun changeOrientation()
    {
        inHorizontal = !inHorizontal
        if(!checkCollisions(x,y))
        {
            inHorizontal = !inHorizontal
            return
        }
        inHorizontal = !inHorizontal

        setOccupied(false)

        if(inHorizontal)
        {
            activeBitmap = bitmapPack.vertical
            inHorizontal = false
        }
        else
        {
            activeBitmap = bitmapPack.horizontal
            inHorizontal = true

        }
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