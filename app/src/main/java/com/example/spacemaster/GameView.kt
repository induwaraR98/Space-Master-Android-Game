package com.example.spacemaster

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.view.MotionEvent
import android.view.SurfaceView
import kotlin.random.Random

class GameView(activity: GameActivity, screenX: Int, screenY: Int) : SurfaceView(activity), Runnable {

    private var thread: Thread? = null
    private var isPlaying = false
    private var isGameOver = false
    private var screenX: Int = 0
    private var screenY: Int = 0
    private var score = 0
    private var paint: Paint
    private var ufo: UFO
    private var astroids: Array<Astroid>
    private var prefs: SharedPreferences
    private var random: Random
    private var soundPool: SoundPool
    private var sound: Int = 0
    private var bullets: MutableList<Bullet>
    private var activity: GameActivity
    private var background1: Background
    private var background2: Background

    companion object {
        var screenRatioX: Float = 0f
        var screenRatioY: Float = 0f
    }

    init {
        this.activity = activity
        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build()
            soundPool = SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .build()
        } else {
            soundPool = SoundPool(1, AudioManager.STREAM_MUSIC, 0)
        }

        sound = soundPool.load(activity, R.raw.shoot, 1)

        this.screenX = screenX
        this.screenY = screenY
        screenRatioX = 1920f / screenX
        screenRatioY = 1080f / screenY

        background1 = Background(screenX, screenY, resources)
        background2 = Background(screenX, screenY, resources)

        ufo = UFO(this, screenY, resources)

        bullets = ArrayList()

        background2.x = screenX

        paint = Paint()
        paint.textSize = 120f
        paint.color = Color.WHITE

        astroids = Array(4) { Astroid(resources) }

        random = Random;
    }

    override fun run() {
        while (isPlaying) {
            update();
            draw();
            sleep();
        }
    }

    private fun update() {
        background1.x -= (10 * screenRatioX).toInt()
        background2.x -= (10 * screenRatioX).toInt()

        if (background1.x + background1.background.width < 0) {
            background1.x = screenX
        }
        if (background2.x + background2.background.width < 0) {
            background2.x = screenX
        }
        if (ufo.isGoingUp)
            ufo.y -= (30 * screenRatioY).toInt()
        else
            ufo.y += (30 * screenRatioY).toInt()

        if (ufo.y < 0)
            ufo.y = 0

        if (ufo.y > screenY - ufo.height)
            ufo.y = screenY - ufo.height

        val trash = mutableListOf<Bullet>()

        for (bullet in bullets) {
            if (bullet.x > screenX)
                trash.add(bullet)

            bullet.x += (50 * screenRatioX).toInt()

            for (astroid in astroids) {
                if (Rect.intersects(astroid.getCollisionShape(), bullet.getCollisionShape())) {
                    score++
                    astroid.x = -500
                    bullet.x = screenX + 500
                    astroid.wasShot = true
                }
            }
        }

        bullets.removeAll(trash)

        for (astroid in astroids) {
            astroid.x -= astroid.speed
            if (astroid.x + astroid.width < 0) {
                if (!astroid.wasShot) {
                    isGameOver = true
                    return
                }
                val bound = (30 * screenRatioX).toInt()
                astroid.speed = random.nextInt(bound)
                if (astroid.speed < 10 * screenRatioX)
                    astroid.speed = (10 * screenRatioX).toInt()

                astroid.x = screenX
                astroid.y = random.nextInt(screenY - astroid.height)

                astroid.wasShot = false
            }

            if (Rect.intersects(astroid.getCollisionShape(), ufo.getCollisionShape())) {
                isGameOver = true
                return
            }
        }
    }


    private fun draw() {
        if (holder.surface.isValid) {
            val canvas = holder.lockCanvas()
            canvas.drawBitmap(background1.background, background1.x.toFloat(), background1.y.toFloat(), paint)
            canvas.drawBitmap(background2.background, background2.x.toFloat(), background2.y.toFloat(), paint)

            for (astroid in astroids) {
                canvas.drawBitmap(astroid.getAstroid(), astroid.x.toFloat(), astroid.y.toFloat(), paint)
            }

            canvas.drawText(score.toString(), (screenX / 2).toFloat(), 164f, paint)

            if (isGameOver) {
                isPlaying = false
                canvas.drawBitmap(ufo.getCrashedBitmap(), ufo.x.toFloat(), ufo.y.toFloat(), paint)
                holder.unlockCanvasAndPost(canvas)
                saveIfHighScore()
                waitBeforeExiting()
                return
            }

            canvas.drawBitmap(ufo.getUFO(), ufo.x.toFloat(), ufo.y.toFloat(), paint)

            for (bullet in bullets) {
                canvas.drawBitmap(bullet.bullet, bullet.x.toFloat(), bullet.y.toFloat(), paint)
            }

            holder.unlockCanvasAndPost(canvas)
        }
    }


    private fun saveIfHighScore() {
        if (prefs.getInt("highscore", 0) < score) {
            val editor = prefs.edit()
            editor.putInt("highscore", score)
            editor.apply()
        }
    }

    private fun waitBeforeExiting() {
        try {
            Thread.sleep(3000)
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun sleep() {

        try {
            Thread.sleep(17)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    fun resume() {
        isPlaying = true
        thread = Thread(this)
        thread!!.start()
    }


    fun pause() {
        try {
            isPlaying = false
            thread!!.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (event.x < screenX / 2) {
                    ufo.isGoingUp = true
                }
            }
            MotionEvent.ACTION_UP -> {
                ufo.isGoingUp = false
                if (event.x > screenX / 2) {
                    ufo.toShoot++
                }
            }
        }
        return true
    }


    fun newBullet() {
        if (!prefs.getBoolean("isMute", false))
            soundPool.play(sound, 1f, 1f, 0, 0, 1f)

        val bullet = Bullet(resources)
        bullet.x = ufo.x + ufo.width
        bullet.y = ufo.y + (ufo.height / 2)
        bullets.add(bullet)
    }

}
