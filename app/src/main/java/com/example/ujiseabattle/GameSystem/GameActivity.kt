package com.example.ujiseabattle.GameSystem

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.example.ujiseabattle.R
import es.uji.vj1229.framework.GameActivity
import es.uji.vj1229.framework.Graphics
import es.uji.vj1229.framework.IGameController
import kotlin.math.min

class GameActivity : GameActivity()
{
    //UJI
    override fun buildGameController(): IGameController {
        requestedOrientation =  ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        val displayMetrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return GameController( this,displayMetrics,this)
    }

    fun restartGame()
    {
        val intent = Intent(this, com.example.ujiseabattle.MainActivity::class.java)
        startActivity(intent)
    }



}