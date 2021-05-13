package com.example.ujiseabattle.GameSystem

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.example.ujiseabattle.R

interface SoundPlayer {
    fun playSound(id:Int)
}

class SoundPlayerObject(val context:Context,val SoundOn : Boolean = true) : SoundPlayer
{

    var click = -1
    var derrota = -1
    var shipdestroyed = -1
    var shiphit = -1
    var shipplaced = -1
    var victoria = -1
    var watersplash = -1
    var shipwrong = -1

    private lateinit var soundPool: SoundPool

    init {
        prepareSoundPool(context)
    }

    override fun playSound(id:Int) {
        if(!SoundOn)return
        soundPool.play(id, 0.6f, 0.8f, 0, 0, 1f)
    }



    private fun prepareSoundPool(context: Context) {
        val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        soundPool = SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(attributes)
                .build()
        click = soundPool.load(context, R.raw.click, 0)
        derrota = soundPool.load(context, R.raw.derrota, 0)
        shipdestroyed = soundPool.load(context, R.raw.shipdestroyed, 0)
        shiphit = soundPool.load(context, R.raw.shiphit, 0)
        shipplaced = soundPool.load(context, R.raw.shipplaced, 0)
        victoria = soundPool.load(context, R.raw.victoria, 0)
        watersplash = soundPool.load(context, R.raw.watersplash, 0)
        shipwrong = soundPool.load(context, R.raw.shipwrong, 0)
    }
}

