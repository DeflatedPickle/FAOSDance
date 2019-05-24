package com.deflatedpickle.faosdance.window

import com.deflatedpickle.faosdance.Direction
import com.deflatedpickle.faosdance.GlobalValues
import com.deflatedpickle.faosdance.util.Lang
import java.awt.Color
import java.awt.Point
import java.awt.Window
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.UIManager

class ApplicationWindow : JFrame(Lang.bundle.getString("window.title")) {
    var ctrlHeld = false

    var isGrabbed = false
    var clickedPoint = Point()
    var animation = GlobalValues.currentAction

    val contextMenu: ContextMenu

    init {
        this.iconImage = GlobalValues.icon.image
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        SwingUtilities.updateComponentTreeUI(this)

        contextMenu = ContextMenu()

        this.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        this.isAlwaysOnTop = true
        this.isUndecorated = true
        this.background = Color(0, 0, 0, 0)
        this.type = Window.Type.UTILITY

        this.addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent) {
                if (e.keyCode == KeyEvent.VK_CONTROL) {
                    ctrlHeld = true
                }
            }

            override fun keyReleased(e: KeyEvent) {
                if (e.keyCode == KeyEvent.VK_CONTROL) {
                    ctrlHeld = false
                }
            }
        })

        this.addMouseWheelListener {
            if (ctrlHeld) {
                GlobalValues.xMultiplier += (it.preciseWheelRotation * -1) / 100
                GlobalValues.yMultiplier += (it.preciseWheelRotation * -1) / 100
                GlobalValues.resize(Direction.BOTH)
            }
        }

        GlobalValues.frame = this

        this.addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                super.mousePressed(e)

                if (e.button == 1 && GlobalValues.isSolid) {
                    isGrabbed = true
                    clickedPoint = e.point

                    animation = GlobalValues.currentAction

                    if (GlobalValues.sheet != null && GlobalValues.isToggleHeld) {
                        GlobalValues.currentAction = GlobalValues.sheet!!.spriteMap.keys.last()
                    }
                } else if (e.button == 3) {
                    contextMenu.show(this@ApplicationWindow, e.x, e.y)
                }
            }

            override fun mouseReleased(e: MouseEvent) {
                super.mouseReleased(e)

                if (e.button == 1 && GlobalValues.isSolid) {
                    isGrabbed = false

                    GlobalValues.currentAction = animation
                }
            }

            override fun mouseDragged(e: MouseEvent) {
                super.mouseDragged(e)

                if (isGrabbed) {
                    this@ApplicationWindow.location = Point(e.xOnScreen - clickedPoint.x, e.yOnScreen - clickedPoint.y)
                }
            }
        }.apply { this@ApplicationWindow.addMouseMotionListener(this) })
    }
}