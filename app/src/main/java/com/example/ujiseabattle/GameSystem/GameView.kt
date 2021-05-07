package com.example.ujiseabattle.GameSystem

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.Color
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import androidx.core.content.ContextCompat
import com.example.ujiseabattle.R
import es.uji.vj1229.framework.Graphics
import kotlin.math.min

class GameView (context: Context,width:Int,height:Int)
{
    private val context = context
    private  val width = width
    private  val height = height


    private val totalColumns = 24
    private val totalRows = 14

    private val graphics : Graphics = Graphics(width, height)
    lateinit var presenter: GamePresenter

    private val canvasGrid: Array<Array<Vector2>> = Array(totalColumns) { Array<Vector2>(totalRows) { Vector2(0f,0f) } }

    private var cellSide = min(width.toFloat() / totalColumns,
            height.toFloat() / totalRows)
    private var xOffset = (width - totalColumns * cellSide) / 2.0f
    private var yOffset = (height - totalRows * cellSide) / 2.0f



    //EXECUTION

    init {
        //Start

        buildMatrix()
        graphics.setTextSize((cellSide*0.5f).toInt()*2)
        graphics.setTextColor(ContextCompat.getColor(context, R.color.Pink))

    }

    fun onDrawingRequested() : Bitmap
    {
        graphics.clear(ContextCompat.getColor(context, R.color.Red))
        drawGrid()
        drawBoard()

        return graphics.frameBuffer

    }

    //AUXILIAR METHODS

    fun drawGrid()
    {
        for(i in 0 until totalRows)
        {
            for(j in 0 until  totalColumns)
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

        drawSquare(verticalOffset,-cellSide*borderSize/2f,horizontalOffset,-cellSide*borderSize/2f,boardSize + borderSize,R.color.GreenYellow)



        for(i in 0 until boardSize)
        {
            for(j in 0 until boardSize)
            {
                drawSquare(i+verticalOffset ,j+ horizontalOffset,1f,R.color.CadetBlue)
            }
        }

        drawText("Place the ships",1,10)




    }

    private fun drawSquare(row:Int,xoffset:Float ,column:Int, yoffset:Float,size:Float, colorID: Int)
    {
        var realPos = canvasGrid[column][row]

        graphics.drawRect(realPos.x + xoffset,realPos.y + yoffset,cellSide*size,cellSide*size,ContextCompat.getColor(context, colorID))
    }


    private fun drawSquare(row:Int, column:Int, size:Float, colorID: Int)
    {
        val realPos = canvasGrid[column][row]

        graphics.drawRect(realPos.x,realPos.y,cellSide*size,cellSide*size,ContextCompat.getColor(context, colorID))
    }

    private fun drawText(text:String,row:Int, column:Int)
    {
        val realPos = canvasGrid[column][row]
        graphics.drawText(realPos.x,realPos.y,text)


    }

    fun buildMatrix()
    {
        for (row in 0 until  totalColumns){


            for(column in 0 until  totalRows)
            {

                val originX : Float = row * cellSide + xOffset
                val originY : Float = column * cellSide + yOffset
                canvasGrid[row][column] = Vector2(originX,originY)
            }

        }


    }


    fun test()
    {
        Log.d("Custom","hello")
    }
}