package com.deflatedpickle.faosdance

import java.awt.*
import java.awt.event.ActionListener
import java.awt.Color
import java.awt.GradientPaint
import java.awt.AlphaComposite
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.image.BufferedImage
import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter


@Suppress("KDocMissingDocumentation")
fun main(args: Array<String>) {
    val frame = JFrame("FAOSDance")
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
    SwingUtilities.updateComponentTreeUI(frame)

    val contextMenu = JPopupMenu().apply {
        this.add(JMenuItem("Settings").apply {
            addActionListener {
                val dialog = DialogSettings(frame)
                dialog.size = Dimension(400, 260)
                dialog.setLocationRelativeTo(frame)

                dialog.isVisible = true
            }
        })
    }

    var isGrabbed = false
    var clickedPoint = Point()
    var animation = GlobalValues.currentAction
    frame.addMouseListener(object : MouseAdapter() {
        override fun mousePressed(e: MouseEvent) {
            super.mousePressed(e)

            if (e.button == 1) {
                isGrabbed = true
                clickedPoint = e.point

                animation = GlobalValues.currentAction

                if (GlobalValues.sheet != null) {
                    GlobalValues.currentAction = GlobalValues.sheet!!.spriteMap.keys.last()
                }
            }
            else if (e.button == 3) {
                contextMenu.show(frame, e.x, e.y)
            }
        }

        override fun mouseReleased(e: MouseEvent) {
            super.mouseReleased(e)

            if (e.button == 1) {
                isGrabbed = false

                GlobalValues.currentAction = animation
            }
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

            if (GlobalValues.sheet == null) return

            val g2D = g as Graphics2D
            g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)

            g2D.translate((this.width - GlobalValues.sheet!!.spriteWidth * GlobalValues.xMultiplier) / 2, (this.height - GlobalValues.sheet!!.spriteHeight * GlobalValues.yMultiplier) / 2)

            g2D.translate(0.0, -(GlobalValues.sheet!!.spriteHeight * GlobalValues.yMultiplier) / 2 - GlobalValues.reflectionPadding)
            g2D.scale(GlobalValues.xMultiplier, GlobalValues.yMultiplier)
            g2D.drawRenderedImage(GlobalValues.sheet!!.spriteMap[GlobalValues.currentAction]!![GlobalValues.animFrame], null)

            g2D.translate(0.0, (GlobalValues.sheet!!.spriteHeight * GlobalValues.yMultiplier) * 4 + (GlobalValues.reflectionPadding * 4))
            g2D.scale(1.0, -1.0)

            val reflection = BufferedImage(GlobalValues.sheet!!.spriteWidth, GlobalValues.sheet!!.spriteHeight, BufferedImage.TYPE_INT_ARGB)
            val rg = reflection.createGraphics()
            rg.drawRenderedImage(GlobalValues.sheet!!.spriteMap[GlobalValues.currentAction]!![GlobalValues.animFrame], null)
            rg.composite = AlphaComposite.getInstance(AlphaComposite.DST_IN)
            rg.paint = GradientPaint(0f, GlobalValues.sheet!!.spriteHeight.toFloat() * GlobalValues.fadeHeight, Color(0.0f, 0.0f, 0.0f, 0.0f),
                    0f, GlobalValues.sheet!!.spriteHeight.toFloat(), Color(0.0f, 0.0f, 0.0f, GlobalValues.fadeOpacity))
            rg.fillRect(0, 0, GlobalValues.sheet!!.spriteWidth, GlobalValues.sheet!!.spriteHeight)
            rg.dispose()
            g2D.drawRenderedImage(reflection, null)
        }
    }
    frame.add(panel)

    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

    frame.isUndecorated = true
    frame.isAlwaysOnTop = true
    frame.background = Color(0, 0, 0, 0)

    val timer = Timer(120, ActionListener {
        GlobalValues.animFrame++

        if (GlobalValues.animFrame >= 8) {
            GlobalValues.animFrame = 0
        }

        frame.revalidate()
        frame.repaint()
    })
    timer.start()

    frame.pack()
    frame.isVisible = true

    frame.setLocationRelativeTo(null)

    val dialog = DialogSettings(frame)
    dialog.size = Dimension(400, 260)
    dialog.setLocationRelativeTo(frame)

    dialog.isVisible = true
}