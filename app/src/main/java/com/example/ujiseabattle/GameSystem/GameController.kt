package com.example.ujiseabattle.GameSystem

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.DisplayMetrics
import android.util.Log
import com.example.ujiseabattle.R
import es.uji.vj1229.framework.AnimatedBitmap
import es.uji.vj1229.framework.IGameController
import es.uji.vj1229.framework.SpriteSheet
import es.uji.vj1229.framework.TouchHandler

class GameController(context: Context,displayMetrics: DisplayMetrics) : IGameController
{

    private  val presenter : GamePresenter = GamePresenter(context,displayMetrics.widthPixels,displayMetrics.heightPixels)
    private val gameView : GameView = GameView(displayMetrics.widthPixels,displayMetrics.heightPixels,context,presenter)
    private val gameModel : GameModel = GameModel(presenter)


    init {
        presenter.gameModel = gameModel
    }

    override fun onUpdate(deltaTime: Float, touchEvents: MutableList<TouchHandler.TouchEvent>?)
    {

        presenter.onUpdate(deltaTime,touchEvents)
    }

    override fun onDrawingRequested(): Bitmap
    {

        return gameView.onDrawingRequested()
    }
}