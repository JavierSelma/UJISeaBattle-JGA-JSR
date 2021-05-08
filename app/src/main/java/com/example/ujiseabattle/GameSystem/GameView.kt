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
        graphics.clear(ContextCompat.getColor(context, R.color.Red))
        drawGrid()
        drawBoard()
        drawTexts()
        drawGame()
        //drawOriginalShips(2,p.totalColumns/2 + 2)
        drawBitmap(Assets.verticalCarrier,3,3)


        return graphics.frameBuffer

    }

    //AUXILIAR METHODS

    fun drawGame()
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


    fun drawBoard()
    {
        val boardSize = 10
        val horizontalOffset = 1
        val verticalOffset = 2
        val borderSize = 0.2f

        drawSquare(verticalOffset,-p.cellSide*borderSize/2f,horizontalOffset,-p.cellSide*borderSize/2f,boardSize + borderSize,R.color.GreenYellow)



        for(i in 0 until boardSize)
        {
            for(j in 0 until boardSize)
            {
                drawSquare(i+verticalOffset ,j+ horizontalOffset,1f,R.color.CadetBlue)
            }
        }
    }
/*
    fun drawOriginalShips(rowOrigin: Int,columnOrigin: Int)
    {
        //CARRIER
        drawBitmap(Assets.horizontalCarrier,rowOrigin,columnOrigin)

        //DESTROYER
        drawBitmap(Assets.horizontalDestroyer,rowOrigin+2,columnOrigin)
        drawBitmap(Assets.horizontalDestroyer,rowOrigin+2,columnOrigin + Assets.DESTR_LENGTH+1)

        //REGULAR
        drawBitmap(Assets.horizontalRegular,rowOrigin+4,columnOrigin)
        drawBitmap(Assets.horizontalRegular,rowOrigin+4,columnOrigin + (Assets.RSHIP_LENGTH + 1)*1)
        drawBitmap(Assets.horizontalRegular,rowOrigin+4,columnOrigin + (Assets.RSHIP_LENGTH + 1)*2)

        //SHORT
        drawBitmap(Assets.horizontalShort,rowOrigin+6,columnOrigin + (Assets.SSHIP_LENGTH + 1)*0)
        drawBitmap(Assets.horizontalShort,rowOrigin+6,columnOrigin + (Assets.SSHIP_LENGTH + 1)*1)
        drawBitmap(Assets.horizontalShort,rowOrigin+6,columnOrigin + (Assets.SSHIP_LENGTH + 1)*2)
        drawBitmap(Assets.horizontalShort,rowOrigin+6,columnOrigin + (Assets.SSHIP_LENGTH + 1)*3)
        //drawBitmap(GameController.Assets.horizontalRegular,rowOrigin,columnOrigin+1)
        //drawBitmap(GameController.Assets.horizontalRegular,rowOrigin+3+1,columnOrigin+1)
    }
*/

    fun drawBitmap(bitmap: Bitmap?,row:Int,column: Int)
    {
        val realPos = p.canvasGrid[column][row].realPos
        graphics.drawBitmap(bitmap,realPos.x,realPos.y)
    }


    fun drawTexts(){
        drawText("Place the ships",1,10, 2, R.color.Pink)


    }
    private fun drawSquare(row:Int,xoffset:Float ,column:Int, yoffset:Float,size:Float, colorID: Int)
    {
        var realPos = p.canvasGrid[column][row].realPos

        graphics.drawRect(realPos.x + xoffset,realPos.y + yoffset,p.cellSide*size,p.cellSide*size,ContextCompat.getColor(context, colorID))
    }


    private fun drawSquare(row:Int, column:Int, size:Float, colorID: Int)
    {
        val realPos = p.canvasGrid[column][row].realPos

        graphics.drawRect(realPos.x,realPos.y,p.cellSide*size,p.cellSide*size,ContextCompat.getColor(context, colorID))
    }

    private fun drawText(text:String,row:Int, column:Int, textSize:Int, textColor:Int)
    {
        graphics.setTextSize((p.cellSide*0.5f).toInt()*textSize)
        graphics.setTextColor(ContextCompat.getColor(context, textColor))

        val realPos = p.canvasGrid[column][row].realPos
        graphics.drawText(realPos.x,realPos.y,text)


    }

}