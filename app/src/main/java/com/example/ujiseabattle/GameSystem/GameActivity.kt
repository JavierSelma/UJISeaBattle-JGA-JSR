package com.example.ujiseabattle.GameSystem

import android.content.Intent
import android.content.pm.ActivityInfo
import android.util.DisplayMetrics
import es.uji.vj1229.framework.GameActivity
import es.uji.vj1229.framework.IGameController

class GameActivity : GameActivity()
{
    //UJI
    override fun buildGameController(): IGameController {
        requestedOrientation =  ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        val soundOn = intent.extras?.get("soundOn") as Boolean

        val smartOn = intent.extras?.get("smartOn") as Boolean



        val displayMetrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return GameController(this, displayMetrics, this,soundOn,smartOn)
    }

    fun restartGame()
    {
        val intent = Intent(this, com.example.ujiseabattle.MainActivity::class.java)
        startActivity(intent)
    }



}