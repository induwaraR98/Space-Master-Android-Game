package com.example.spacemaster

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Background(screenX: Int, screenY: Int, res: Resources) {
    var x: Int = 0
    var y: Int = 0
    var background: Bitmap

    init {
        background = BitmapFactory.decodeResource(res, R.drawable.spaces_bg)
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false)
    }
}
