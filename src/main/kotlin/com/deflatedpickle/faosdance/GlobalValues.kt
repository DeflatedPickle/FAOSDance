package com.deflatedpickle.faosdance

import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.Timer

object GlobalValues {
    val maxSize = 10.0

    var sheet: SpriteSheet? = null
    var currentAction = ""

    var animFrame = 0

    var reflectionPadding = 0.0

    var fadeHeight = 0.65f
    var fadeOpacity = 0.25f

    var xMultiplier = 0.5
    var yMultiplier = 0.5

    var fps = 8

    var isVisible = true
    var isSolid = true
    var isTopLevel = true

    var currentPath = System.getProperty("user.home")

    var timer: Timer? = null

    var frame: JFrame? = null

    fun resize() {
        val width = ((((sheet!!.spriteWidth * xMultiplier) * 2) * 100) / 100).toInt()
        val height = ((((sheet!!.spriteHeight * yMultiplier) * 2) * 100) / 100).toInt()

        frame!!.minimumSize = Dimension(width, height)
        frame!!.setSize(width, height)
    }

    fun configureSpriteSheet(sheet: SpriteSheet) {
        GlobalValues.sheet = sheet
        GlobalValues.currentAction = GlobalValues.sheet!!.spriteMap.keys.first()
        GlobalValues.resize()
    }
}