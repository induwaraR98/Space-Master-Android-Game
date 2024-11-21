package com.example.spacemaster

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.content.res.Resources
import android.graphics.Rect

class UFO(private val gameView: GameView, screenY: Int, res: Resources) {

    var toShoot = 0
    var x = 0
    var y: Int
    var width: Int
    var height: Int
    var moveCounter = 0
    var shootCounter = 1
    var isGoingUp = false
    var ufo1: Bitmap
    var ufo2: Bitmap
    var shoot1: Bitmap
    var shoot2: Bitmap
    var shoot3: Bitmap
    var shoot4: Bitmap
    var shoot5: Bitmap
    var crashed: Bitmap

    init {
        ufo1 = BitmapFactory.decodeResource(res, R.drawable.spaceship)
        ufo2 = BitmapFactory.decodeResource(res, R.drawable.spaceship_move)
        shoot1 = BitmapFactory.decodeResource(res, R.drawable.spaceshipshoot1)
        shoot2 = BitmapFactory.decodeResource(res, R.drawable.spaceshipshoot2)
        shoot3 = BitmapFactory.decodeResource(res, R.drawable.spaceshipshoot3)
        shoot4 = BitmapFactory.decodeResource(res, R.drawable.spaceshipshoot4)
        shoot5 = BitmapFactory.decodeResource(res, R.drawable.spaceshipshoot5)
        crashed = BitmapFactory.decodeResource(res, R.drawable.spaceshipcrashed)

        width = ufo1.width
        height = ufo1.height

        width /= 4
        height /= 4

        width = (width * GameView.screenRatioX).toInt()
        height = (height * GameView.screenRatioY).toInt()

        ufo1 = Bitmap.createScaledBitmap(ufo1, width, height, false)
        ufo2 = Bitmap.createScaledBitmap(ufo2, width, height, false)
        shoot1 = Bitmap.createScaledBitmap(shoot1, width, height, false)
        shoot2 = Bitmap.createScaledBitmap(shoot2, width, height, false)
        shoot3 = Bitmap.createScaledBitmap(shoot3, width, height, false)
        shoot4 = Bitmap.createScaledBitmap(shoot4, width, height, false)
        shoot5 = Bitmap.createScaledBitmap(shoot5, width, height, false)
        crashed = Bitmap.createScaledBitmap(crashed, width, height, false)

        y = screenY / 2
        x = (64 * GameView.screenRatioX).toInt()
    }

    fun getUFO(): Bitmap {
        return when {
            toShoot != 0 -> {
                when (shootCounter) {
                    1 -> {
                        shootCounter++
                        shoot1
                    }
                    2 -> {
                        shootCounter++
                        shoot2
                    }
                    3 -> {
                        shootCounter++
                        shoot3
                    }
                    4 -> {
                        shootCounter++
                        shoot4
                    }
                    else -> {
                        shootCounter = 1
                        toShoot--
                        gameView.newBullet()
                        shoot5
                    }
                }
            }
            moveCounter == 0 -> {
                moveCounter++
                ufo1
            }
            else -> {
                moveCounter--
                ufo2
            }
        }
    }

    fun getCollisionShape(): Rect {
        return Rect(x, y, x + width, y + height)
    }

    fun getCrashedBitmap(): Bitmap {
        return crashed
    }
}
