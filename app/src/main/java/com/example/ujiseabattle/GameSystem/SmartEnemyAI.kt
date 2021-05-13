package com.example.ujiseabattle.GameSystem

import com.example.ujiseabattle.GameElements.FieldTile
import com.example.ujiseabattle.GameElements.Tile
import java.lang.reflect.Field
import kotlin.math.log2

class SmartEnemyAI(private  val p:GamePresenter) : AI
{
    data class Arguments (var full:Int,var partial: Int)
    {
        override fun toString(): String {
            return  "f: $full p: $partial"
        }
    }


    private val field: Array<Array<FieldTile>> = Array(p.boardSize) { Array<FieldTile>(p.boardSize) { FieldTile(-1,-1) }}


    init {
        populateField()
    }

    private  fun populateField()
    {
        for(x in 0 until p.boardSize)
        {
            for(y in 0 until p.boardSize){
                field[x][y].x = x
                field[x][y].y = y
            }
        }
    }


    private fun view_GetVector(view:String, p:Pair)
    {
        //di dj
        when(view)
        {
            "l"->
            {
                p.Column = -1
                p.Row = 0
            }
            "r"->
            {
                p.Column = 1
                p.Row = 0

            }
            "u"->
            {
                p.Column = 0
                p.Row = -1

            }
            "d"->
            {
                p.Column = 0
                p.Row = 1

            }
        }
    }


    private fun field_IsInBounds(x:Int, y:Int) : Boolean
    {
        return ((0 <= x) && (x < p.boardSize)) && ((0 <= y) && (y < p.boardSize));
    }

    private fun field_GetExtent( x:Int, y:Int,dir:String, status:FieldTile.State) : Int
    {
        if(!field_IsInBounds(x,y))return 0

        // Get the directions
        var  distance = 0;
        val p = Pair(0,0)
        view_GetVector(dir,p);

        var i = x
        var j = y


        while (field_IsInBounds(i, j) && (field[i][j].currentState == status) ){
        i += p.Column
        j += p.Row
        distance++;
        }
        return distance
    }


    private fun ai_GetMinimumLenght(arg:Arguments)
    {
        // Get the minimum length remaining

        var lengthMin = Int.MAX_VALUE
        var partialMin = Int.MAX_VALUE


        for (ship in p.activeShips)
        {
            if(!ship.occupiedTiles[0].friendly)continue

            val health = ship.getCurrentHealth()

            if(health>0)
            {
                val length = ship.size

                if(health<length)
                {
                    var fragmentMin = log2((length-health).toDouble()).toInt()
                    if(fragmentMin == 0)fragmentMin = 1

                    if(fragmentMin < partialMin)partialMin = fragmentMin

                }
                else if(length<lengthMin)
                {
                    lengthMin = length
                    if(lengthMin<partialMin)partialMin = lengthMin
                }
            }

        }



        if (arg.full != 0) {
            arg.full = lengthMin;
        }
        if (arg.partial != 0) {
            arg.partial = partialMin;
        }

    }

    override fun updateAs(p: Pair, state: FieldTile.State) {
        field[p.Row][p.Column].currentState = state
    }

    override fun getPlay(): Pair
    {
        var fullMin = 0
        var partialMin = 0

        val args = Arguments(fullMin,partialMin)
        ai_GetMinimumLenght(args)

        var  probabilityMax = -1
        var tileX = -1
        var tileY = -1

        for(x in 0 until p.boardSize)
        {
            for(y in 0 until p.boardSize)
            {
                val tile = field[x][y]

                if(tile.currentState != FieldTile.State.UNTRIED) continue


                val viewLeft = field_GetExtent(x,y,"l",FieldTile.State.UNTRIED) // todos tienen que ser >= 1
                val viewRight = field_GetExtent(x,y,"r",FieldTile.State.UNTRIED)
                val viewUp = field_GetExtent(x,y,"u",FieldTile.State.UNTRIED)
                val viewDown = field_GetExtent(x,y,"d",FieldTile.State.UNTRIED)

                // Find any nearby hits, beginning at our neighbors
                // and extending outwards.

                val nearLeft  = field_GetExtent(x-1,y,"l",FieldTile.State.HIT) // tienen que ser >=0
                val nearRight = field_GetExtent(x+1,y,"r",FieldTile.State.HIT)
                val nearUp    = field_GetExtent(x,y-1,"u",FieldTile.State.HIT)
                val nearDown  = field_GetExtent(x,y+1,"d",FieldTile.State.HIT)

                val nearHorizontal = nearLeft +nearRight
                val nearVertical = nearUp + nearDown
                var blockedHorizontal = false

                if(nearLeft>0 || nearRight >=0)blockedHorizontal = (viewRight+viewLeft) <= (partialMin-nearHorizontal);
                else blockedHorizontal = (viewRight+viewLeft) <= (fullMin-nearHorizontal);

                var blockedVertical = false

                if (nearUp > 0 || nearDown > 0)blockedVertical = (viewUp+viewDown) <= (partialMin-nearVertical);
                else blockedVertical = (viewUp+viewDown) <= (fullMin-nearVertical);

                var probability = 0

                if (!blockedHorizontal || !blockedVertical)
                {
                    probability = viewLeft*viewRight + viewUp*viewDown;
                    probability += (nearHorizontal+nearVertical)*(p.boardSize*p.boardSize);
                }

                if (probability > probabilityMax) {
                    probabilityMax = probability;
                    tileX = x;
                    tileY = y;
                }

            }
        }


        val pair = Pair(tileX,tileY)
        pair.convertToRealCoords()
        return  pair


    }


}