package com.example.ujiseabattle.GameSystem

import android.content.Context
import android.util.Log
import androidx.core.math.MathUtils
import com.example.ujiseabattle.GameElements.Ships.Ship
import com.example.ujiseabattle.GameElements.Tile
import es.uji.vj1229.framework.TouchHandler
import kotlin.math.min

class GamePresenter(context: Context,width:Int,height:Int)
{
    val totalColumns = 24
    val totalRows = 14
    val canvasGrid: Array<Array<Tile>> = Array(totalColumns) { Array<Tile>(totalRows) { Tile(Vector2.zero) }}
    var activeShips: MutableList<Ship> = mutableListOf<Ship>()

    val cellSide = min(width.toFloat() / totalColumns,
            height.toFloat() / totalRows)
    val xOffset = (width - totalColumns * cellSide) / 2.0f
    val yOffset = (height - totalRows * cellSide) / 2.0f

    enum class GameState {
        PLACING,
    }

    var gameState = GameState.PLACING


    var time : Float = 0f

    init {
        Assets.loadDrawableAssets(context)
        Assets.createResizedAssets(context,cellSide.toInt())
        buildMatrix()
        spawnShips()

    }

    private fun buildMatrix()
    {
        for (row in 0 until  totalColumns){


            for(column in 0 until  totalRows)
            {

                val originX : Float = row * cellSide + xOffset
                val originY : Float = column * cellSide + yOffset
                canvasGrid[row][column] = Tile(Vector2(originX,originY))
            }

        }


    }





    fun onUpdate(deltaTime: Float, touchEvents: MutableList<TouchHandler.TouchEvent>?)
    {
        dataUpdates(deltaTime)
        touchManagement(touchEvents)

    }

    private fun dataUpdates(deltaTime: Float)
    {
        time += deltaTime
    }

    private  fun touchManagement(touchEvents: MutableList<TouchHandler.TouchEvent>?)
    {
        if (touchEvents != null) {
            for (event in touchEvents) {
                val correctedEventX : Int = MathUtils.clamp(((event.x - xOffset) / cellSide).toInt(),0,totalColumns-1)
                val correctedEventY : Int = MathUtils.clamp(((event.y - yOffset) / cellSide).toInt(),0,totalRows-1)
                when (event.type) {
                    TouchHandler.TouchType.TOUCH_DOWN -> {
                        onTouchDown(correctedEventX,correctedEventY)
                    }
                    TouchHandler.TouchType.TOUCH_UP -> {
                        onTouchUp()
                    }
                    TouchHandler.TouchType.TOUCH_DRAGGED ->
                    {
                        onTouchDragged(correctedEventX,correctedEventY)
                    }
                }
            }
        }

    }

    var selectedShip : Ship? = null

    private  fun onTouchDown(x:Int,y:Int)
    {
        selectedShip =  canvasGrid[x][y].occupier
        selectedShip?.changeOrientation()
    }

    private  fun onTouchUp()
    {
        selectedShip = null
    }



    private  fun onTouchDragged(x:Int,y:Int)
    {
        selectedShip?.moveTo(y,x)
    }

    private  fun spawnShips()
    {
        //drawBitmap(Assets.horizontalCarrier,rowOrigin,columnOrigin)
        activeShips.add(Ship(10,10,Assets.CARRIER_LENGTH,Assets.horizontalCarrier,Assets.verticalCarrier,this))
       //activeShips.add(Ship(5,5,Assets.CARRIER_LENGTH,Assets.horizontalCarrier,this))

    }


}