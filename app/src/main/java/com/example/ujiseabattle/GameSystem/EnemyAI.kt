package com.example.ujiseabattle.GameSystem

import com.example.ujiseabattle.GameElements.Tile

class EnemyAI(private val p:GamePresenter)
{
    var plays: MutableList<Pair> = mutableListOf<Pair>()
    var playsDone: MutableList<Pair> = mutableListOf<Pair>()

    init {
        populatePlays()

    }

    private fun populatePlays()
    {

        for(row in p.placingBoardY until p.placingBoardY + p.boardSize)
        {
            for(column in p.placingBoardX until p.placingBoardX + p.boardSize)
            {
                plays.add(Pair(row,column))
            }
        }
    }

    fun getPlay(): Pair
    {
        var playType = "Smart"

        var play = getSmart()

        if(play == null)
        {
            playType = "Random"
            play = getRandom()
        }

        plays.remove(play)
        p.system.print("$playType -> " +  Pair(play.Row- 2, play.Column- 1).toString() + " -> ${plays.size} plays remaining ->" + "${playsDone.size} plays stored")
        playsDone.add(play)
        return play
    }

    fun getRandom() : Pair
    {
        return plays[(0 until  plays.size).random()]
    }

    fun getSmart() : Pair?
    {
        if(playsDone.isEmpty())return null

        for(i in playsDone.size-1 downTo 0)
        {
            val play = playsDone[i]

            if(p.canvasGrid[play.Column][play.Row].tileType == Tile.TileType.Bombed)
            {
                var nei = getNeiIfPossible(play.Row,play.Column)
                if(nei != null)return  nei
            }
            else
            {
                playsDone.remove(play)
            }

        }

        return  null
    }

    fun getNeiIfPossible(row:Int,column:Int):Pair?
    {
        //arriba
        if(p.canvasGrid[column][row-1].tileType == Tile.TileType.Clean)return  Pair(row-1,column)

        //derecha
        if(p.canvasGrid[column+1][row].tileType == Tile.TileType.Clean)return  Pair(row,column+1)
        //abajo
        if(p.canvasGrid[column][row+1].tileType == Tile.TileType.Clean)return  Pair(row+1,column)
        //izquierda
        if(p.canvasGrid[column-1][row].tileType == Tile.TileType.Clean)return  Pair(row,column-1)

        return  null
    }




}