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
import com.example.ujiseabattle.R
import es.uji.vj1229.framework.Graphics
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
        drawPlacingBoard()
        drawShips()
        drawButtons()
        drawTexts()


        return graphics.frameBuffer

    }

    //AUXILIAR METHODS

    fun drawShips()
    {
        for(s in p.activeShips)
        {
            drawBitmap(s.activeBitmap,s.x,s.y)
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
            drawButton(b.x,b.y,b.size,b.boxColor,b.text,b.textSize,b.textColor)
        }

        /*
        if(p.gameState == GamePresenter.GameState.PLACED)
        {
            drawButton(7,17,4,R.color.DeepPink,"Battle!",2,R.color.white)
        }
        */

    }


    fun drawButton(row:Int, column:Int, boxSize:Int, boxColor: Int,text:String, textSize:Int, textColor:Int)
    {
        drawSquare(row,column,boxSize.toFloat(),boxColor)
        drawText(text,row,2.25f,column,0.5f,textSize,textColor)
    }



    fun drawTexts(){
        drawText("Drag to place your ships",1,0f,7,0f, 2, R.color.HotPink)
        drawText("Click to rotate your ships",p.totalRows-1,0.25f,7,0f, 2, R.color.HotPink)


    }
    private fun drawSquare(row:Int,xoffset:Float ,column:Int, yoffset:Float,size:Float, colorID: Int)
    {
        var realPos = p.canvasGrid[column][row].realPos

        graphics.drawRect(realPos.x + xoffset,realPos.y + yoffset,p.cellSide*size,p.cellSide*size,ContextCompat.getColor(context, colorID))
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