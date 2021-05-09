package com.example.ujiseabattle.GameElements

import com.example.ujiseabattle.GameElements.Ships.Ship
import com.example.ujiseabattle.GameSystem.Vector2

data class Tile(var realPos : Vector2)
{
    var friendly = true

    enum class TileType { Background,Clean,Missed,Bombed,Discovered}
    var tileType = TileType.Background

    var occupier : Ship? = null
    var occupierButton : Button? = null
}