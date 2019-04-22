package com.deflatedpickle.faosdance

import java.awt.*
import java.awt.event.ActionListener
import javax.swing.JFrame
import javax.swing.Timer
import java.awt.Color
import java.awt.GradientPaint
import java.awt.AlphaComposite
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.image.BufferedImage
import javax.swing.JPanel


@Suppress("KDocMissingDocumentation")
fun main(args: Array<String>) {
    val sheet = SpriteSheet("", 8, 10)
    var currentAction = sheet.spriteMap.keys.first()

    var animFrame = 0

    var reflectionPadding = 0.0

    var fadeHeight = 0.65f
    var fadeOpacity = 0.25f

    var xMultiplier = 0.5
    var yMultiplier = 0.5

    val frame = JFrame("FAOSDance")

    var isGrabbed = false
    var clickedPoint = Point()
    var animation = currentAction
    frame.addMouseListener(object : MouseAdapter() {
        override fun mousePressed(e: MouseEvent) {
            super.mousePressed(e)

            isGrabbed = true
            clickedPoint = e.point

            animation = currentAction
            currentAction = sheet.spriteMap.keys.last()
        }

        override fun mouseReleased(e: MouseEvent) {
            super.mouseReleased(e)

            isGrabbed = false

            currentAction = animation
        }

        override fun mouseDragged(e: MouseEvent) {
            super.mouseDragged(e)

            if (isGrabbed) {
                frame.location = Point(e.xOnScreen - clickedPoint.x, e.yOnScreen - clickedPoint.y)
            }
        }
    }.apply { frame.addMouseMotionListener(this) })

    val panel = object : JPanel() {
        init {
            isOpaque = false
        }

        override fun paintComponent(g: Graphics) {
            super.paintComponent(g)
            val g2D = g as Graphics2D
            g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)

            g2D.translate((this.width - sheet.spriteWidth * xMultiplier) / 2, (this.height - sheet.spriteHeight * yMultiplier) / 2)

            g2D.translate(0.0, -(sheet.spriteHeight * yMultiplier) / 2 - reflectionPadding)
            g2D.scale(xMultiplier, yMultiplier)
            g2D.drawRenderedImage(sheet.spriteMap[currentAction]!![animFrame], null)

            g2D.translate(0.0, (sheet.spriteHeight * yMultiplier) * 4 + (reflectionPadding * 4))
            g2D.scale(1.0, -1.0)

            val reflection = BufferedImage(sheet.spriteWidth, sheet.spriteHeight, BufferedImage.TYPE_INT_ARGB)
            val rg = reflection.createGraphics()
            rg.drawRenderedImage(sheet.spriteMap[currentAction]!![animFrame], null)
            rg.composite = AlphaComposite.getInstance(AlphaComposite.DST_IN)
            rg.paint = GradientPaint(0f, sheet.spriteHeight.toFloat() * fadeHeight, Color(0.0f, 0.0f, 0.0f, 0.0f),
                    0f, sheet.spriteHeight.toFloat(), Color(0.0f, 0.0f, 0.0f, fadeOpacity))
            rg.fillRect(0, 0, sheet.spriteWidth, sheet.spriteHeight)
            rg.dispose()
            g2D.drawRenderedImage(reflection, null)
        }
    }
    frame.add(panel)

    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

    frame.isUndecorated = true
    frame.isAlwaysOnTop = true
    frame.background = Color(0, 0, 0, 0)

    fun resize() {
        val width = ((((sheet.spriteWidth * xMultiplier) * 2) * 100) / 100).toInt()
        val height = ((((sheet.spriteHeight * yMultiplier) * 2) * 100) / 100).toInt()

        frame.minimumSize = Dimension(width, height)
        frame.setSize(width, height)
    }
    resize()

    val timer = Timer(120, ActionListener {
        animFrame++

        if (animFrame >= 8) {
            animFrame = 0
        }

        frame.revalidate()
        frame.repaint()
    })
    timer.start()

    frame.pack()
    frame.isVisible = true

    frame.setLocationRelativeTo(null)
}