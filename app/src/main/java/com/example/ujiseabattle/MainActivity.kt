package com.example.ujiseabattle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Switch
import com.example.ujiseabattle.GameSystem.GameActivity

class MainActivity : AppCompatActivity()
{
    var soundOn = false
    var smartOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSwitches()
    }

    fun startGame(view: View)
    {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("soundOn", soundOn)
        intent.putExtra("smartOn",smartOn)
        startActivity(intent)
    }


    fun setSwitches()
    {
        val soundSwitch = findViewById<Switch>(R.id.soundSwitch)

        soundSwitch.setOnCheckedChangeListener { buttonView, isChecked -> soundOn = isChecked }

        val smartSwitch = findViewById<Switch>(R.id.oponentSwitch)

        smartSwitch.setOnCheckedChangeListener { buttonView, isChecked -> smartOn = isChecked }


    }
}