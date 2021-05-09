package com.example.ujiseabattle.GameSystem

import android.util.Log
import com.example.ujiseabattle.GameElements.Ships.BitmapPack
import com.example.ujiseabattle.GameElements.Ships.Ship
import com.example.ujiseabattle.GameElements.Tile
import es.uji.vj1229.framework.TouchHandler

class GameModel(private val p: GamePresenter)
{
    var playerTurn = true

    val maxScore = 10
    var playerScore = 0
    set(value) {
        field = value
        if(playerScore == maxScore)
        {
            p.gameState = GamePresenter.GameState.END
        }
    }

    var enemyScore = 0
    set(value) {
        field = value
        if(enemyScore == maxScore)
        {
            p.gameState = GamePresenter.GameState.END
        }
    }

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

    fun setPlayingBoards()
    {
        for(row in p.placingBoardY until p.placingBoardY + p.boardSize)
        {
            for(column in p.placingBoardX until p.placingBoardX+ p.boardSize)
            {
                p.canvasGrid[column][row].tileType = Tile.TileType.Clean
            }
        }

        val newColumn = p.placingBoardX + p.boardSize + 2

        for(row in p.placingBoardY until p.placingBoardY + p.boardSize)
        {
            for(column in newColumn until newColumn+ p.boardSize)
            {
                p.canvasGrid[column][row].tileType = Tile.TileType.Clean
                p.canvasGrid[column][row].friendly = false
            }
        }

        for (i in 0 until  4)
        {
            when(i)
            {
                0->
                {
                    val carrierPack = BitmapPack(Assets.horizontalCarrier,Assets.horizontalDestroyedCarrier,Assets.verticalCarrier,Assets.verticalDestroyedCarrier)
                    placeRandomShip(Ship(0,0,4,carrierPack,p),p.placingBoardY,newColumn)
                }
                1->
                {
                    for(i in 0 until 2)
                    {
                        val destroyerPack = BitmapPack(Assets.horizontalDestroyer,Assets.horizontalDestroyedDestroyer,Assets.verticalDestroyer,Assets.verticalDestroyedDestroyer)
                        placeRandomShip(Ship(0,0,3,destroyerPack,p),p.placingBoardY,newColumn)
                    }
                }
                2->
                {
                    for(i in 0 until 3)
                    {
                        val regularPack = BitmapPack(Assets.horizontalRegular,Assets.horizontalDestroyedRegular,Assets.verticalRegular,Assets.verticalDestroyedRegular)
                        placeRandomShip(Ship(0,0,2,regularPack,p),p.placingBoardY,newColumn)
                    }
                }
                3->
                {
                    for(i in 0 until 4)
                    {
                        val shortPack = BitmapPack(Assets.horizontalShort,Assets.horizontalDestroyedShort,Assets.verticalShort,Assets.verticalDestroyedShort)
                        placeRandomShip(Ship(0,0,1,shortPack,p),p.placingBoardY,newColumn)
                    }
                }

            }
        }
    }

    fun placeRandomShip(s:Ship,boardRow:Int,boardColumn: Int)
    {
        var placed = false

        while (!placed)
        {
            p.activeShips.add(s)
            if(Math.random() >= 0.5f)s.changeOrientation()
            var row : Int = 0
            var column :Int = 0

            if(s.inHorizontal)
            {
                row  = (boardRow until boardRow+p.boardSize).random()
                column = (boardColumn until boardColumn+p.boardSize - s.size).random()
            }
            else
            {
                row  = (boardRow until boardRow+p.boardSize-s.size).random()
                column = (boardColumn until boardColumn+p.boardSize).random()
            }





            placed =  s.moveTo(row,column)
        }

        s.activeBitmap = null
    }

}