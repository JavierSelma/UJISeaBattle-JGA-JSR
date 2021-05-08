package com.example.ujiseabattle.GameSystem

import android.util.Log
import com.example.ujiseabattle.GameElements.Ships.Ship
import com.example.ujiseabattle.GameElements.Tile
import es.uji.vj1229.framework.TouchHandler

class GameModel(private val p: GamePresenter)
{

    fun  checkValidPlacement():Boolean
    {
        for(row in 0 until p.totalRows)
        {
            for(column in 0 until p.totalColumns)
            {
                if(!isPlacementBoard(row,column) && p.canvasGrid[column][row].occupier != null)return  false
            }
        }

        return  true
    }

    private fun isPlacementBoard(row:Int, column:Int):Boolean
    {
       val rowOk  = (row>= p.placingBoardY && row < p.placingBoardY + p.boardSize)
        val columnOK = (column>= p.placingBoardX && column < p.placingBoardX + p.boardSize)

        return  rowOk && columnOK
    }

}