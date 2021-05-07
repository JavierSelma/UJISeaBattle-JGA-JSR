package com.example.ujiseabattle.GameSystem

import android.content.Context
import android.graphics.Bitmap
import android.util.DisplayMetrics
import android.util.Log
import es.uji.vj1229.framework.IGameController
import es.uji.vj1229.framework.TouchHandler

class GameController(context: Context,displayMetrics: DisplayMetrics) : IGameController
{
    private val gameModel : GameModel = GameModel()
    private val gameView : GameView = GameView(context,displayMetrics.widthPixels,displayMetrics.heightPixels)
    private val gamePresenter: GamePresenter = GamePresenter(gameView,gameModel)




    init
    {
        //Awake
        //Start
        gameView.presenter = gamePresenter
        gameModel.presenter = gamePresenter

    }

    override fun onUpdate(deltaTime: Float, touchEvents: MutableList<TouchHandler.TouchEvent>?)
    {

        gameModel.onUpdate(deltaTime,touchEvents)
    }

    override fun onDrawingRequested(): Bitmap
    {

        return gameView.onDrawingRequested()
    }
}