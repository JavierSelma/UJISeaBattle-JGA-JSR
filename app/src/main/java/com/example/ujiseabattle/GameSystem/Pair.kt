package com.example.ujiseabattle.GameSystem

data class Pair(var Row:Int,var Column:Int)
{
    override fun toString(): String {
        return "[$Row][$Column]"
    }

    fun convertToRealCoords()
    {
        Row+= 2
        Column += 1
    }

    fun convertToAiCoords()
    {
        Row-= 2
        Column -= 1
    }
}
