package com.example.ujiseabattle.GameSystem

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import androidx.core.content.ContextCompat
import com.example.ujiseabattle.GameElements.Button
import com.example.ujiseabattle.GameElements.Ships.Ship
import com.example.ujiseabattle.GameElements.Tile
import com.example.ujiseabattle.R
import es.uji.vj1229.framework.AnimatedBitmap
import es.uji.vj1229.framework.Graphics
import kotlin.math.exp
import kotlin.math.min

class GameView (width:Int,height:Int,private val context: Context,private  val p: GamePresenter)
{
    private val graphics : Graphics = Graphics(width, height)


    init {
        //Start
        graphics.setTypeface(Typeface.DEFAULT_BOLD)



    }

    fun onDrawingRequested() : Bitmap
    {
        graphics.clear(ContextCompat.getColor(context, R.color.LightGrey))
        //drawGrid()
        drawBoards()
        drawShips()
        drawExploded()
        drawButtons()
        drawTexts()
        drawAnimations()
        return graphics.frameBuffer

    }

    //AUXILIAR METHODS

    private fun drawAnimations()
    {
        val an = p.activeExplosion
        an?.animation?.update(p.system.deltaTime)
        var b =  an?.animation?.currentFrame

        if(an != null)
        {
            drawBitmap(b,an.row, an.column)
            if(an.animation?.isEnded == true)p.activeExplosion = null
        }



    }


    fun drawShips()
    {
        for(s in p.activeShips)
        {
            if(s.activeBitmap != null)drawBitmap(s.activeBitmap,s.x,s.y)
        }
    }

    fun drawGrid()
    {
        for(i in 0 until p.totalRows)
        {
            for(j in 0 until  p.totalColumns)
            {
                drawSquare(i ,j,1f,R.color.black)
            }
        }
    }





    private fun drawBoards()
    {
        if(p.gameState == GamePresenter.GameState.PLACING || p.gameState == GamePresenter.GameState.PLACED)
        {
            drawPlacingBoard()
        }
        else if(p.gameState == GamePresenter.GameState.MIDGAME || p.gameState == GamePresenter.GameState.END)
        {
            drawGameBoards()
        }
    }

    private  fun drawExploded()
    {
        for(row in 0 until p.totalRows)
        {
            for(column in 0 until p.totalColumns)
            {
                if(p.canvasGrid[column][row].tileType == Tile.TileType.Bombed)drawBitmap(Assets.exploded,row,column)

            }
        }
    }

    private fun drawGameBoards()
    {
        //Borders
        val borderSize = 0.2f
        drawSquare(p.placingBoardY,-p.cellSide*borderSize*0.625f,p.placingBoardX,-p.cellSide*borderSize*0.625f,p.boardSize + borderSize,R.color.black)
        drawSquare(p.placingBoardY,-p.cellSide*borderSize*0.625f,p.placingBoardX + p.boardSize+2,-p.cellSide*borderSize*0.625f,p.boardSize + borderSize,R.color.black)


        for(row in 0 until p.totalRows)
        {
            for(column in 0 until p.totalColumns)
            {
                if(p.canvasGrid[column][row].tileType == Tile.TileType.Background)continue

                var colorID = R.color.DarkBlue

                if(p.canvasGrid[column][row].tileType == Tile.TileType.Missed)colorID = R.color.Turquoise

                if(p.canvasGrid[column][row].tileType == Tile.TileType.Bombed)
                {
                    colorID = R.color.DodgerBlue

                }

                drawSquare(row ,column,1f,colorID)
            }
        }
    }

    fun drawPlacingBoard()
    {
        val borderSize = 0.2f

        drawSquare(p.placingBoardY,-p.cellSide*borderSize*0.625f,p.placingBoardX,-p.cellSide*borderSize*0.625f,p.boardSize + borderSize,R.color.black)



        for(i in 0 until p.boardSize)
        {
            for(j in 0 until p.boardSize)
            {
                drawSquare(i+p.placingBoardY ,j+ p.placingBoardX,1f,R.color.DarkBlue)
            }
        }
    }


    fun drawBitmap(bitmap: Bitmap?,row:Int,column: Int)
    {
        val realPos = p.canvasGrid[column][row].realPos
        graphics.drawBitmap(bitmap,realPos.x,realPos.y)
    }


    fun drawButtons()
    {

        for (b in p.activeButtons)
        {
            drawButton(b.x,b.y,b.size,b.boxColor,b.text,b.textSize,b.textColor,b.activeType)
        }

    }


    fun drawButton(row:Int, column:Int, boxSize:Int, boxColor: Int,text:String, textSize:Int, textColor:Int,t:Button.Type)
    {
        if(t == Button.Type.Square)
        {
            drawSquare(row,column,boxSize.toFloat(),boxColor)
            drawText(text,row,2.25f,column,0.5f,textSize,textColor)
        }
        else if(t == Button.Type.Long)
        {
            drawRectangle(row,column,7f,2f,boxColor)
            drawText(text,row,1.25f,column,0.25f,textSize,textColor)
        }
    }



    fun drawTexts(){

        if(p.gameState == GamePresenter.GameState.PLACING || p.gameState == GamePresenter.GameState.PLACED)
        {
            drawText("Drag to place your ships",1,0f,7,0f, 2, R.color.HotPink)
            drawText("Click to rotate your ships",p.totalRows-1,0.25f,7,0f, 2, R.color.HotPink)
        }
        else if(p.gameState == GamePresenter.GameState.MIDGAME)
        {
            var s = "Time to Battle: Your turn!"

            if(!p.gameModel.playerTurn)s = "Time to Battle: Computer's turn!"

            drawText(s,1,0f,7,0f, 2, R.color.HotPink)

            if(p.gameModel.playerTurn)
            {
                drawBitmap(Assets.arrow,p.totalRows-2,p.totalColumns/2-2)
            }

        }


    }
    private fun drawSquare(row:Int,xoffset:Float ,column:Int, yoffset:Float,size:Float, colorID: Int)
    {
        var realPos = p.canvasGrid[column][row].realPos

        graphics.drawRect(realPos.x + xoffset,realPos.y + yoffset,p.cellSide*size,p.cellSide*size,ContextCompat.getColor(context, colorID))
    }

    private fun drawRectangle(row:Int, column:Int, width:Float,height:Float, colorID: Int)
    {
        val realPos = p.canvasGrid[column][row].realPos
        val sizeMultiplier = 0.95f
        val toMove = (1f - sizeMultiplier)*0.5f

        graphics.drawRect(realPos.x + toMove,realPos.y+toMove,p.cellSide*width*sizeMultiplier,p.cellSide*height*sizeMultiplier,ContextCompat.getColor(context, colorID))
    }



    private fun drawSquare(row:Int, column:Int, size:Float, colorID: Int)
    {
        val realPos = p.canvasGrid[column][row].realPos
        val sizeMultiplier = 0.95f
        val toMove = (1f - sizeMultiplier)*0.5f

        graphics.drawRect(realPos.x + toMove,realPos.y+toMove,p.cellSide*size*sizeMultiplier,p.cellSide*size*sizeMultiplier,ContextCompat.getColor(context, colorID))
    }

    private fun drawText(text:String,row:Int,rowOffset:Float, column:Int, columnOffset:Float,textSize:Int, textColor:Int)
    {
        graphics.setTextSize((p.cellSide*0.5f).toInt()*textSize)
        graphics.setTextColor(ContextCompat.getColor(context, textColor))

        val realPos = p.canvasGrid[column][row].realPos
        graphics.drawText(realPos.x + columnOffset*p.cellSide,realPos.y+rowOffset*p.cellSide,text)


    }

}