package com.example.spacemaster

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.content.res.Resources
import android.graphics.Rect
import com.example.spacemaster.GameView.Companion.screenRatioX
import com.example.spacemaster.GameView.Companion.screenRatioY

class Astroid(res: Resources) {

    var speed = 20
    var wasShot = true
    var x = 0
    var y: Int
    var width: Int
    var height: Int
    var astroidCounter = 1
    var astroid1: Bitmap
    var astroid2: Bitmap
    var astroid3: Bitmap
    var astroid4: Bitmap

    init {
        astroid1 = BitmapFactory.decodeResource(res, R.drawable.astroid1)
        astroid2 = BitmapFactory.decodeResource(res, R.drawable.astroid2)
        astroid3 = BitmapFactory.decodeResource(res, R.drawable.astroid3)
        astroid4 = BitmapFactory.decodeResource(res, R.drawable.astroid4)

        width = astroid1.width
        height = astroid1.height

        width /= 6
        height /= 6

        width = (width * screenRatioX).toInt()
        height = (height * screenRatioY).toInt()

        astroid1 = Bitmap.createScaledBitmap(astroid1, width, height, false)
        astroid2 = Bitmap.createScaledBitmap(astroid2, width, height, false)
        astroid3 = Bitmap.createScaledBitmap(astroid3, width, height, false)
        astroid4 = Bitmap.createScaledBitmap(astroid4, width, height, false)

        y = -height
    }

    fun getAstroid(): Bitmap {
        return when (astroidCounter) {
            1 -> {
                astroidCounter++
                astroid1
            }
            2 -> {
                astroidCounter++
                astroid2
            }
            3 -> {
                astroidCounter++
                astroid3
            }
            else -> {
                astroidCounter = 1
                astroid4
            }
        }
    }

    fun getCollisionShape(): Rect {
        return Rect(x, y, x + width, y + height)
    }
}
