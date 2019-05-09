package com.deflatedpickle.faosdance

import java.awt.*
import javax.swing.*

object GlobalValues {
    val maxSize = 10.0

    var sheet: SpriteSheet? = null
    var currentAction = ""

    var opacity = 1.0

    var animFrame = 0

    var play = true

    var isReflectionVisible = true
    var reflectionPadding = 0.0

    var fadeHeight = 0.65f
    var fadeOpacity = 0.25f

    var xPosition = 0
    var yPosition = 0

    var xMultiplier = 0.5
    var yMultiplier = 0.5

    // var xRotation = 0
    // var yRotation = 0
    var zRotation = 0

    var fps = 8
    var rewind = false

    var isVisible = true
    var isSolid = true
    var isTopLevel = true

    var currentPath = System.getProperty("user.home")

    var timer: Timer? = null

    var frame: JFrame? = null

    var animationControls: Triple<JComponent, JSlider, JSpinner>? = null

    var oldWidth = 0
    var oldHeight = 0

    var effectiveSize: Rectangle? = null

    fun initPositions() {
        effectiveSize = getEffectiveScreenSize(frame!!)
        xPosition = effectiveSize!!.width / 2
        yPosition = effectiveSize!!.height / 2
    }

    fun resize(direction: Direction? = null) {
        val width = ((((sheet!!.spriteWidth * xMultiplier) * 1.2) * 100) / 100).toInt()
        val height = ((((sheet!!.spriteHeight * yMultiplier) * if (isReflectionVisible) 2 else 1) * 100) / 100).toInt()

        frame!!.minimumSize = Dimension(width, height)
        frame!!.setSize(width, height)

        // TODO: Clean this up
        when (direction) {
            Direction.HORIZONTAL -> {
                frame!!.setLocation(frame!!.x - ((frame!!.width - oldWidth) / 2), frame!!.y)
                oldWidth = frame!!.width
            }
            Direction.VERTICAL -> {
                frame!!.setLocation(frame!!.x, frame!!.y - ((frame!!.height - oldHeight) / 2))
                oldHeight = frame!!.height
            }
            Direction.BOTH -> {
                frame!!.setLocation(frame!!.x - ((frame!!.width - oldWidth) / 2), frame!!.y)
                oldWidth = frame!!.width
                frame!!.setLocation(frame!!.x, frame!!.y - ((frame!!.height - oldHeight) / 2))
                oldHeight = frame!!.height
            }
        }

        effectiveSize = getEffectiveScreenSize(frame!!)
    }

    fun configureSpriteSheet(sheet: SpriteSheet) {
        GlobalValues.sheet = sheet
        currentAction = GlobalValues.sheet!!.spriteMap.keys.first()
        resize()
    }

    // https://stackoverflow.com/a/29177069
    fun getEffectiveScreenSize(component: Component): Rectangle {
        val rectangle = Rectangle()

        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val bounds = Toolkit.getDefaultToolkit().getScreenInsets(component.graphicsConfiguration)

        rectangle.width =
            (screenSize.getWidth() - bounds.left.toDouble() - bounds.right.toDouble()).toInt() - frame!!.width
        rectangle.height =
            (screenSize.getHeight() - bounds.top.toDouble() - bounds.bottom.toDouble()).toInt() - frame!!.height

        rectangle.x = ((screenSize.getHeight() - component.height) / 2.0).toInt()
        rectangle.y = ((screenSize.getWidth() - component.width) / 2.0).toInt()

        return rectangle
    }
}