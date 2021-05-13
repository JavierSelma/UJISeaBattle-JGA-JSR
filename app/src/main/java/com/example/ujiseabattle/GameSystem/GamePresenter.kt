package com.example.ujiseabattle.GameSystem

import android.content.Context
import android.media.AudioAttributes
import android.util.Log
import androidx.core.math.MathUtils
import com.example.ujiseabattle.GameElements.Button
import com.example.ujiseabattle.GameElements.FieldTile
import com.example.ujiseabattle.GameElements.Ships.BitmapPack
import com.example.ujiseabattle.GameElements.Ships.Ship
import com.example.ujiseabattle.GameElements.Tile
import com.example.ujiseabattle.R
import es.uji.vj1229.framework.AnimatedBitmap
import es.uji.vj1229.framework.TouchHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min

class GamePresenter(val context: Context,width:Int,height:Int,val gameActivity: GameActivity,val soundOn:Boolean,val smartOn:Boolean)
{
    val soundPlayer = SoundPlayerObject(context,soundOn)

    val system = System()
    lateinit var  gameModel: GameModel


    val placingBoardX = 1
    val placingBoardY = 2
    val boardSize = 10

    val totalColumns = 24
    val totalRows = 14
    val canvasGrid: Array<Array<Tile>> = Array(totalColumns) { Array<Tile>(totalRows) { Tile(Vector2.zero) }}
    var activeShips: MutableList<Ship> = mutableListOf<Ship>()
    var activeButtons: MutableList<Button> = mutableListOf<Button>()
    var activeExplosion : Animation? = null

    val cellSide = min(width.toFloat() / totalColumns,
            height.toFloat() / totalRows)
    val xOffset = (width - totalColumns * cellSide) / 2.0f
    val yOffset = (height - totalRows * cellSide) / 2.0f

    var winMessage :String = ""

    var  enemyAI : AI
    enum class GameState { PLACING, PLACED,MIDGAME,END}

    var gameState = GameState.PLACING
    set(value)
    {
        clearAllButtons()

        if(value == GameState.PLACING)
        {

        }
        else if(value == GameState.PLACED)
        {
            activeButtons.add(Button(7,17,4,R.color.DeepPink,"Battle!",2,R.color.white,0,Button.Type.Square,this))
        }
        else if(value == GameState.MIDGAME)
        {
            gameModel.setPlayingBoards()
        }
        else if(value == GameState.END)
        {
            val b = Button( totalRows-2,totalColumns/2-3,4,R.color.DeepPink,"Restart Game",2,R.color.white,1,Button.Type.Long,this)
            activeButtons.add(b)

        }


        field = value
    }



    init {



        Assets.loadDrawableAssets(context)
        Assets.createResizedAssets(context,cellSide.toInt())
        buildMatrix()
        spawnShips(2,totalColumns/2 + 2)

        enemyAI = if(smartOn) SmartEnemyAI(this)
        else EnemyAI(this)


        //enemyAI = EnemyAI(this)


    }




    private  fun createExplosion(row:Int,column: Int){
        var animation = Animation(row,column,Assets.explosionAB)
        activeExplosion = animation
    }

    private  fun createSplash(row:Int,column: Int){
        soundPlayer.playSound(soundPlayer.watersplash)
        var animation = Animation(row,column,Assets.splashAB)
        activeExplosion = animation
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
                val t = Tile(Vector2(originX,originY))
                t.gridPos.Row  = row
                t.gridPos.Column = column
                canvasGrid[row][column] = t

            }

        }


    }





    fun onUpdate(deltaTime: Float, touchEvents: MutableList<TouchHandler.TouchEvent>?)
    {
        system.updateValues(deltaTime)
        touchManagement(touchEvents)

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
                        touchTime = system.time;
                    }
                    TouchHandler.TouchType.TOUCH_UP -> {
                        if(system.time-touchTime<0.15f)onClick(correctedEventX,correctedEventY)
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
        if(gameState == GameState.PLACING || gameState == GameState.PLACED)
        {
            selectedShip =  canvasGrid[x][y].occupier
        }
    }

    private fun onClick(x:Int,y:Int)
    {
        soundPlayer.playSound(soundPlayer.click)

        if(gameState == GameState.PLACED || gameState == GameState.PLACING)
        {
            selectedShip?.changeOrientation()
        }
        else if(gameState == GameState.MIDGAME)
        {
           playerPlay(x,y)
        }

        if(canvasGrid[x][y].occupierButton != null) canvasGrid[x][y].occupierButton?.let { buttonAction(it.buttonID) }
    }

    private  fun playerPlay(x:Int,y:Int)
    {
        if(!gameModel.playerTurn)return
        if(canvasGrid[x][y].tileType != Tile.TileType.Clean)return

        if(canvasGrid[x][y].friendly)return

        if(canvasGrid[x][y].occupier != null)
        {
            canvasGrid[x][y].tileType = Tile.TileType.Bombed
            createExplosion(y,x)
            val shipShinked =  canvasGrid[x][y].occupier?.increaseTotalBombed()
            if( shipShinked == true)gameModel.playerScore++
        }
        else
        {
            canvasGrid[x][y].tileType = Tile.TileType.Missed
            createSplash(y,x)
            gameModel.playerTurn = false
            startComputersTurn()
        }





    }

    fun startComputersTurn()
    {
        if(gameState == GameState.END)return

        GlobalScope.launch(context = Dispatchers.Main) {
            delay(1000)
            val hit =  processComputerPlay(enemyAI.getPlay())
            delay(1000)
            if(!hit)gameModel.playerTurn = true
            else startComputersTurn()
            }
    }

    fun processComputerPlay(play: Pair):Boolean
    {
        val row = play.Row
        val column = play.Column

        if(canvasGrid[column][row].occupier != null)
        {
            //si acierta a un barco


            canvasGrid[column][row].tileType = Tile.TileType.Bombed
            val shipShinked =  canvasGrid[column][row].occupier?.increaseTotalBombed()
            if( shipShinked == true)gameModel.enemyScore++

            if(smartOn)
            {
                play.convertToAiCoords()
                enemyAI.updateAs(play,FieldTile.State.HIT)
            }

            createExplosion(row,column)

            return true
        }
        else
        {
            //si pega al agua

            createSplash(row,column)
            canvasGrid[column][row].tileType = Tile.TileType.Missed
            play.convertToAiCoords()
            enemyAI.updateAs(play,FieldTile.State.MISS)
            return false
        }



    }


    private  fun onTouchUp()
    {


        if(gameState == GameState.PLACING || gameState == GameState.PLACED)
        {
            selectedShip = null
            gameState = if(gameModel.checkValidPlacement())
            {
                soundPlayer.playSound(soundPlayer.shipplaced)
                GameState.PLACED
            }
            else
            {
                soundPlayer.playSound(soundPlayer.shipwrong)
                GameState.PLACING
            }
        }




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
            0->
            {
                gameState = GameState.MIDGAME
            }
            1-> gameActivity.restartGame()
        }
    }

    fun showAllEnemyShips()
    {
        for(col in 0 until totalColumns)
        {
            for(row in 0 until  totalRows)
            {
                val t = canvasGrid[col][row]
                if(!t.friendly && (t.tileType == Tile.TileType.Bombed || t.tileType ==  Tile.TileType.Clean) && t.occupier != null)
                {
                    val s = t.occupier

                    var finalBM = s?.bitmapPack?.horizontal

                    if(s?.inHorizontal == false)finalBM = s?.bitmapPack?.vertical

                    s?.activeBitmap = finalBM
                }
            }
        }
    }

}