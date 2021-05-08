package com.example.ujiseabattle.GameSystem

import android.content.Context
import android.util.Log
import androidx.core.math.MathUtils
import com.example.ujiseabattle.GameElements.Button
import com.example.ujiseabattle.GameElements.Ships.BitmapPack
import com.example.ujiseabattle.GameElements.Ships.Ship
import com.example.ujiseabattle.GameElements.Tile
import com.example.ujiseabattle.R
import es.uji.vj1229.framework.TouchHandler
import kotlin.math.min

class GamePresenter(val context: Context,width:Int,height:Int)
{
    lateinit var  gameModel: GameModel
    val placingBoardX = 1
    val placingBoardY = 2
    val boardSize = 10

    val totalColumns = 24
    val totalRows = 14
    val canvasGrid: Array<Array<Tile>> = Array(totalColumns) { Array<Tile>(totalRows) { Tile(Vector2.zero) }}
    var activeShips: MutableList<Ship> = mutableListOf<Ship>()
    var activeButtons: MutableList<Button> = mutableListOf<Button>()

    val cellSide = min(width.toFloat() / totalColumns,
            height.toFloat() / totalRows)
    val xOffset = (width - totalColumns * cellSide) / 2.0f
    val yOffset = (height - totalRows * cellSide) / 2.0f

    enum class GameState { PLACING, PLACED}

    var gameState = GameState.PLACING
    set(value)
    {
        if(value == GameState.PLACING)
        {
            clearAllButtons()
        }
        else if(value == GameState.PLACED)
        {
            clearAllButtons()
            activeButtons.add(Button(7,17,4,R.color.DeepPink,"Battle!",2,R.color.white,0,this))
        }


        field = value
    }


    var time : Float = 0f

    init {

        Assets.loadDrawableAssets(context)
        Assets.createResizedAssets(context,cellSide.toInt())
        buildMatrix()
        spawnShips(2,totalColumns/2 + 2)

    }

    private  fun clearAllButtons()
    {
        for(b in activeButtons)
        {
            b.setOccupied(false)
        }

        activeButtons.clear()

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
                        touchTime = time;
                    }
                    TouchHandler.TouchType.TOUCH_UP -> {
                        if(time-touchTime<0.15f)onClick(correctedEventX,correctedEventY)
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
    var touchTime = 0f

    private  fun onTouchDown(x:Int,y:Int)
    {
        selectedShip =  canvasGrid[x][y].occupier
    }

    private fun onClick(x:Int,y:Int)
    {
        selectedShip?.changeOrientation()

        if(canvasGrid[x][y].occupierButton != null) canvasGrid[x][y].occupierButton?.let { buttonAction(it.buttonID) }
    }

    private  fun onTouchUp()
    {
        selectedShip = null
        gameState = if(gameModel.checkValidPlacement()) GameState.PLACED
        else GameState.PLACING

    }



    private  fun onTouchDragged(x:Int,y:Int)
    {
        selectedShip?.moveTo(y,x)
    }

    private  fun spawnShips(rowOrigin: Int,columnOrigin:Int)
    {
        //CARRIER
        val carrierPack = BitmapPack(Assets.horizontalCarrier,Assets.horizontalDestroyedCarrier,Assets.verticalCarrier,Assets.verticalDestroyedCarrier)
        activeShips.add(Ship(rowOrigin,columnOrigin,Assets.CARRIER_LENGTH,carrierPack,this))

        //DESTROYER
        val destroyerPack = BitmapPack(Assets.horizontalDestroyer,Assets.horizontalDestroyedDestroyer,Assets.verticalDestroyer,Assets.verticalDestroyedDestroyer)
        activeShips.add(Ship(rowOrigin+2*1,columnOrigin+((Assets.DESTR_LENGTH+1)*0),Assets.DESTR_LENGTH,destroyerPack,this))
        activeShips.add(Ship(rowOrigin+2*1,columnOrigin+((Assets.DESTR_LENGTH+1)*1),Assets.DESTR_LENGTH,destroyerPack,this))

        //REGULAR
        val regularPack = BitmapPack(Assets.horizontalRegular,Assets.horizontalDestroyedRegular,Assets.verticalRegular,Assets.verticalDestroyedRegular)
        activeShips.add(Ship(rowOrigin+2*2,columnOrigin+((Assets.RSHIP_LENGTH+1)*0),Assets.RSHIP_LENGTH,regularPack,this))
        activeShips.add(Ship(rowOrigin+2*2,columnOrigin+((Assets.RSHIP_LENGTH+1)*1),Assets.RSHIP_LENGTH,regularPack,this))
        activeShips.add(Ship(rowOrigin+2*2,columnOrigin+((Assets.RSHIP_LENGTH+1)*2),Assets.RSHIP_LENGTH,regularPack,this))

        //SHORT
        val shortPack = BitmapPack(Assets.horizontalShort,Assets.horizontalDestroyedShort,Assets.verticalShort,Assets.verticalDestroyedShort)
        activeShips.add(Ship(rowOrigin+2*3,columnOrigin+((Assets.SSHIP_LENGTH+1)*0),Assets.SSHIP_LENGTH,shortPack,this))
        activeShips.add(Ship(rowOrigin+2*3,columnOrigin+((Assets.SSHIP_LENGTH+1)*1),Assets.SSHIP_LENGTH,shortPack,this))
        activeShips.add(Ship(rowOrigin+2*3,columnOrigin+((Assets.SSHIP_LENGTH+1)*2),Assets.SSHIP_LENGTH,shortPack,this))
        activeShips.add(Ship(rowOrigin+2*3,columnOrigin+((Assets.SSHIP_LENGTH+1)*3),Assets.SSHIP_LENGTH,shortPack,this))

    }

    fun buttonAction(buttonID:Int)
    {
        when(buttonID)
        {
            0-> Log.d("Custom","$time  Phase ended")
        }
    }



}