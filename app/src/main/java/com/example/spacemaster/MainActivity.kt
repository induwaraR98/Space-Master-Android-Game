package com.example.spacemaster

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import com.example.spacemaster.R

class MainActivity : AppCompatActivity() {

    private var isMute = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.play).setOnClickListener {
            startActivity(Intent(this@MainActivity, GameActivity::class.java))
        }

        val highScoreTxt = findViewById<TextView>(R.id.highScore)

        val prefs = getSharedPreferences("game", MODE_PRIVATE)
        highScoreTxt.text = "HighScore: " + prefs.getInt("highscore", 0)

        isMute = prefs.getBoolean("isMute", false)

        val volumeCtrl = findViewById<ImageView>(R.id.volumeControll)

        volumeCtrl.setImageResource(if (isMute) R.drawable.mute else R.drawable.high_volume)

        volumeCtrl.setOnClickListener {
            isMute = !isMute
            volumeCtrl.setImageResource(if (isMute) R.drawable.mute else R.drawable.high_volume)

            val editor = prefs.edit()
            editor.putBoolean("isMute", isMute)
            editor.apply()
        }
    }
}
