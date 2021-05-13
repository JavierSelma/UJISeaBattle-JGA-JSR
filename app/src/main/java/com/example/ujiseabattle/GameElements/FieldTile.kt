package com.example.ujiseabattle.GameElements

import com.example.ujiseabattle.GameSystem.Pair

class FieldTile (var x: Int,var y:Int)
{
    enum class State{UNTRIED,MISS,HIT,SUNK}
    var currentState = State.UNTRIED


}