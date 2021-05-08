package com.example.ujiseabattle.GameElements

import com.example.ujiseabattle.GameElements.Ships.Ship
import com.example.ujiseabattle.GameSystem.Vector2

data class Tile(var realPos : Vector2)
{
    var occupier : Ship? = null
    var occupierButton : Button? = null
}