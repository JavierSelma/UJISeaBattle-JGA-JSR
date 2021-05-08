package com.example.ujiseabattle.GameSystem

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.ujiseabattle.R
import es.uji.vj1229.framework.AnimatedBitmap
import es.uji.vj1229.framework.SpriteSheet

object Assets {

    //region CARRIER
    const val CARRIER_LENGTH = 4;

    var horizontalCarrier : Bitmap? = null;
    var verticalCarrier : Bitmap? = null;
    var horizontalDestroyedCarrier : Bitmap? = null;
    var verticalDestroyedCarrier : Bitmap? = null;
    //endregion

    //region DESTROYER
    const val DESTR_LENGTH = 3;

    var horizontalDestroyer : Bitmap? = null;
    var verticalDestroyer : Bitmap? = null;
    var horizontalDestroyedDestroyer : Bitmap? = null;
    var verticalDestroyedDestroyer : Bitmap? = null;
    //endregion

    //region REGULAR SHIP
    const val RSHIP_LENGTH = 2;

    var horizontalRegular : Bitmap? = null;
    var verticalRegular : Bitmap? = null;
    var horizontalDestroyedRegular : Bitmap? = null;
    var verticalDestroyedRegular : Bitmap? = null;
    //endregion

    //region SHORT SHIP
    const val SSHIP_LENGTH = 1;

    var horizontalShort : Bitmap? = null;
    var verticalShort : Bitmap? = null;
    var horizontalDestroyedShort : Bitmap? = null;
    var verticalDestroyedShort : Bitmap? = null;
    //endregion

    //region WATER SPLASH
    private const val SPLASH_ROWS = 2
    private const val SPLASH_COLUMNS = 4
    private const val SPLASH_WIDTH = 62
    private const val SPLASH_HEIGHT = 33

    private var splash : SpriteSheet? = null;
    var waterSplash : AnimatedBitmap? = null;
    //endregion

    fun loadDrawableAssets(context: Context){
        val resources : Resources = context.resources
    }

    fun createResizedAssets(context : Context, cellSize : Int){
        val resources : Resources = context.resources

        //region CARRIER
        horizontalCarrier?.recycle()
        horizontalCarrier = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(resources, R.drawable.s4h),
                cellSize * CARRIER_LENGTH, cellSize, true)

        verticalCarrier?.recycle()
        verticalCarrier = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(resources, R.drawable.s4v),
                cellSize, cellSize * CARRIER_LENGTH, true)

        horizontalDestroyedCarrier?.recycle()
        horizontalDestroyedCarrier = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(resources, R.drawable.s4hd),
                cellSize * CARRIER_LENGTH, cellSize, true)

        verticalDestroyedCarrier?.recycle()
        verticalDestroyedCarrier = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(resources, R.drawable.s4vd),
                cellSize, cellSize * CARRIER_LENGTH, true)
        //endregion

        //region DESTROYER
        horizontalDestroyer?.recycle()
        horizontalDestroyer = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(resources, R.drawable.s3h),
                cellSize * DESTR_LENGTH, cellSize, true)

        verticalDestroyer?.recycle()
        verticalDestroyer = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(resources, R.drawable.s3v),
                cellSize, cellSize  * DESTR_LENGTH, true)

        horizontalDestroyedDestroyer?.recycle()
        horizontalDestroyedDestroyer = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(resources, R.drawable.s3hd),
                cellSize * DESTR_LENGTH, cellSize, true)

        verticalDestroyedDestroyer?.recycle()
        verticalDestroyedDestroyer = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(resources, R.drawable.s3vd),
                cellSize, cellSize * DESTR_LENGTH, true)
        //endregion

        //region REGULAR SHIP
        horizontalRegular?.recycle()
        horizontalRegular = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(resources, R.drawable.s2h),
                cellSize * RSHIP_LENGTH, cellSize, true)

        verticalRegular?.recycle()
        verticalRegular = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(resources, R.drawable.s2v),
                cellSize, cellSize * RSHIP_LENGTH, true)

        horizontalDestroyedRegular?.recycle()
        horizontalDestroyedRegular = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(resources, R.drawable.s2hd),
                cellSize * RSHIP_LENGTH, cellSize, true)

        verticalDestroyedRegular?.recycle()
        verticalDestroyedRegular = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(resources, R.drawable.s2vd),
                cellSize, cellSize * RSHIP_LENGTH, true)
        //endregion

        //region SHORT SHIP
        horizontalShort?.recycle()
        horizontalShort = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(resources, R.drawable.s1h),
                cellSize * SSHIP_LENGTH, cellSize, true)

        verticalShort?.recycle()
        verticalShort = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(resources, R.drawable.s1v),
                cellSize, cellSize  * SSHIP_LENGTH, true)

        horizontalDestroyedShort?.recycle()
        horizontalDestroyedShort = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(resources, R.drawable.s1hd),
                cellSize * SSHIP_LENGTH, cellSize, true)

        verticalDestroyedShort?.recycle()
        verticalDestroyedShort = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(resources, R.drawable.s1vd),
                cellSize, cellSize * SSHIP_LENGTH, true)
        //endregion

    }
}