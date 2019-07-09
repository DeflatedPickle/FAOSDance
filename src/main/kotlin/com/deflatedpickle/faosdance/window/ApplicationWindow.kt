package com.deflatedpickle.faosdance.window

import com.deflatedpickle.faosdance.Direction
import com.deflatedpickle.faosdance.util.GlobalValues
import com.deflatedpickle.faosdance.util.Lang
import java.awt.Color
import java.awt.Point
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JFrame

class ApplicationWindow : JFrame(Lang.bundle.getString("window.title")) {
    var ctrlHeld = false

    var isGrabbed = false
    var clickedPoint = Point()
    var animation = ""

    val contextMenu: ContextMenu

    init {
        this.iconImage = GlobalValues.icon.image

        contextMenu = ContextMenu()

        this.defaultCloseOperation = EXIT_ON_CLOSE
        this.isAlwaysOnTop = true
        this.isUndecorated = true
        this.background = Color(0, 0, 0, 0)
        this.type = Type.UTILITY

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
                val width = GlobalValues.optionsMap.getMap("sprite")!!.getMap("size")!!.getOption<Double>("width")!!
                GlobalValues.optionsMap.getMap("sprite")!!.getMap("size")!!.setOption("width", width + (it.preciseWheelRotation * -1) / 100)

                val height = GlobalValues.optionsMap.getMap("sprite")!!.getMap("size")!!.getOption<Double>("height")!!
                GlobalValues.optionsMap.getMap("sprite")!!.getMap("size")!!.setOption("height", height + (it.preciseWheelRotation * -1) / 100)

                GlobalValues.resize(Direction.BOTH)
            }
        }

        GlobalValues.frame = this

        this.addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                super.mousePressed(e)

                if (e.button == 1 && GlobalValues.optionsMap.getMap("window")!!.getOption<Boolean>("solid")!!) {
                    isGrabbed = true
                    clickedPoint = e.point

                    animation = GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.getOption<String>("action")!!

                    if (GlobalValues.sheet != null && GlobalValues.optionsMap.getMap("sprite")!!.getOption<Boolean>("toggle_held")!!) {
                        GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.setOption("action", GlobalValues.sheet!!.spriteMap.keys.last())
                    }
                } else if (e.button == 3) {
                    contextMenu.show(this@ApplicationWindow, e.x, e.y)
                }
            }

            override fun mouseReleased(e: MouseEvent) {
                super.mouseReleased(e)

                if (e.button == 1 && GlobalValues.optionsMap.getMap("window")!!.getOption<Boolean>("solid")!!) {
                    isGrabbed = false

                    GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.setOption("action", animation)
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