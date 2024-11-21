package com.example.spacemaster

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.content.res.Resources
import android.graphics.Rect

class Bullet(res: Resources) {

    var x: Int = 0
    var y: Int = 0
    var width: Int
    var height: Int
    var bullet: Bitmap

    init {
        bullet = BitmapFactory.decodeResource(res, R.drawable.bullet)

        width = bullet.width
        height = bullet.height

        width /= 4
        height /= 4

        width = (width * GameView.screenRatioX).toInt()
        height = (height * GameView.screenRatioY).toInt()

        bullet = Bitmap.createScaledBitmap(bullet, width, height, false)
    }

    fun getCollisionShape(): Rect {
        return Rect(x, y, x + width, y + height)
    }
}
