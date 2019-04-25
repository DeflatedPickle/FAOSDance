package com.deflatedpickle.faosdance

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

    var delay = 120

    var isVisible = true
    var isSolid = true
    var isTopLevel = true

    var currentPath = System.getProperty("user.home")

    var timer: Timer? = null

    var frame: JFrame? = null
}