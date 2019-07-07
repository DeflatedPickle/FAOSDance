package com.deflatedpickle.faosdance.window

import com.deflatedpickle.faosdance.GlobalValues
import com.deflatedpickle.faosdance.RubyThread
import com.deflatedpickle.faosdance.ScalingType
import com.deflatedpickle.faosdance.settings.general.SpriteCategory
import java.awt.*
import java.awt.datatransfer.DataFlavor
import java.awt.dnd.DnDConstants
import java.awt.dnd.DropTarget
import java.awt.dnd.DropTargetDropEvent
import java.awt.event.ActionListener
import java.awt.image.BufferedImage
import java.io.File
import javax.swing.JPanel
import javax.swing.Timer

class SpritePanel : JPanel() {
    init {
        isOpaque = false

        dropTarget = object : DropTarget() {
            override fun drop(dtde: DropTargetDropEvent) {
                dtde.acceptDrop(DnDConstants.ACTION_COPY)
                val droppedFiles = dtde.transferable.getTransferData(DataFlavor.javaFileListFlavor) as List<File>

                if (droppedFiles.size == 1) {
                    SpriteCategory.loadSpriteSheet(droppedFiles[0])
                }
            }
        }

        GlobalValues.timer = Timer(1000 / GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.getOption<Int>("fps")!!, ActionListener {
            if (GlobalValues.sheet != null) {
                if (GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.getOption<Boolean>("play")!!) {
                    if (GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.getOption<Boolean>("rewind")!!) {
                        val frame = GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.getOption<Int>("frame")!!
                        GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.setOption("frame", frame - 1)

                        if (GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.getOption<Int>("frame")!! <= 0) {
                            GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.setOption("frame", 7)
                        }
                    } else {
                        val frame = GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.getOption<Int>("frame")!!
                        GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.setOption("frame", frame + 1)

                        if (GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.getOption<Int>("frame")!! >= 8) {
                            GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.setOption("frame", 0)
                        }
                    }

                    GlobalValues.animationControls?.second?.value = GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.getOption<Int>("frame")!!
                }

                GlobalValues.mutableSprite = GlobalValues.sheet!!.spriteMap[GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.getOption<String>("action")!!]!![GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.getOption<Int>("frame")!!]

                GlobalValues.frame!!.revalidate()
                GlobalValues.frame!!.repaint()
            }
        })
        GlobalValues.timer!!.start()
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)

        if (GlobalValues.sheet == null) return

        val g2D = g as Graphics2D
        g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, when (ScalingType.valueOf(GlobalValues.unsanatizeEnumValue(GlobalValues.optionsMap.getMap("sprite")!!.getOption<String>("scaling_type")!!))) {
            ScalingType.BILINEAR -> RenderingHints.VALUE_INTERPOLATION_BILINEAR
            ScalingType.BICUBIC -> RenderingHints.VALUE_INTERPOLATION_BICUBIC
            ScalingType.NEAREST_NEIGHBOR -> RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
        })

        for (i in RubyThread.extensions) {
            if (GlobalValues.isEnabled(i)) {
                RubyThread.rubyContainer.callMethod(i, "pre_draw", g2D)
            }
        }

        // Change the opacity
        g2D.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, GlobalValues.optionsMap.getMap("sprite")!!.getOption<Double>("opacity")!!.toFloat())

        // Rotate the matrix
        g2D.rotate(
            Math.toRadians(GlobalValues.optionsMap.getMap("sprite")!!.getMap("rotation")!!.getOption<Int>("z")!!.toDouble()),
            (this.width / 2).toDouble(),
            (this.height / 2).toDouble()
        )

        // Translate to the centre
        g2D.translate(
            (this.width - GlobalValues.sheet!!.spriteWidth * GlobalValues.optionsMap.getMap("sprite")!!.getMap("size")!!.getOption<Double>("width")!!) / 2,
            (this.height - GlobalValues.sheet!!.spriteHeight * GlobalValues.optionsMap.getMap("sprite")!!.getMap("size")!!.getOption<Double>("height")!!) / 2
        )

        // Translate upwards the size of the sprite plus the reflection padding
        if (GlobalValues.optionsMap.getMap("reflection")!!.getOption<Boolean>("visible")!!) {
            g2D.translate(
                0.0,
                -(GlobalValues.sheet!!.spriteHeight * GlobalValues.optionsMap.getMap("sprite")!!.getMap("size")!!.getOption<Double>("height")!!) / 2 - GlobalValues.optionsMap.getMap("reflection")!!.getOption<Double>("padding")!!
            )
        }
        // Scale the sprite
        // println(GlobalValues.optionsMap.getMap("sprite")!!.getMap("size")!!.getOption<Double>("width")!!)
        g2D.scale(GlobalValues.optionsMap.getMap("sprite")!!.getMap("size")!!.getOption<Double>("width")!!, GlobalValues.optionsMap.getMap("sprite")!!.getMap("size")!!.getOption<Double>("height")!!)

        // Draw the sprite
        if (GlobalValues.optionsMap.getMap("sprite")!!.getOption<Boolean>("visible")!!) {
            val sprite = BufferedImage(
                GlobalValues.sheet!!.spriteWidth,
                GlobalValues.sheet!!.spriteHeight,
                BufferedImage.TYPE_INT_ARGB
            )
            val spriteGraphics = sprite.createGraphics()

            for (i in RubyThread.extensions) {
                if (GlobalValues.isEnabled(i)) {
                    RubyThread.rubyContainer.callMethod(i, "pre_draw_sprite", spriteGraphics)
                }
            }

            spriteGraphics.drawRenderedImage(
                GlobalValues.mutableSprite,
                null
            )

            for (i in RubyThread.extensions) {
                if (GlobalValues.isEnabled(i)) {
                    RubyThread.rubyContainer.callMethod(i, "during_draw_sprite", spriteGraphics)
                }
            }

            spriteGraphics.dispose()
            g2D.drawRenderedImage(sprite, null)

            for (i in RubyThread.extensions) {
                if (GlobalValues.isEnabled(i)) {
                    RubyThread.rubyContainer.callMethod(i, "post_draw_sprite", g2D)
                }
            }
        }

        // Move to the bottom of the sprite
        g2D.translate(
            0.0,
            (GlobalValues.sheet!!.spriteHeight) * 2 + (GlobalValues.optionsMap.getMap("reflection")!!.getOption<Double>("padding")!!)
        )
        // Flip the matrix, so the reflection is upside down
        g2D.scale(1.0, -1.0)

        // Create a new image for the reflection
        if (GlobalValues.optionsMap.getMap("reflection")!!.getOption<Boolean>("visible")!!) {
            val reflection = BufferedImage(
                GlobalValues.sheet!!.spriteWidth,
                GlobalValues.sheet!!.spriteHeight,
                BufferedImage.TYPE_INT_ARGB
            )
            val reflectionGraphics = reflection.createGraphics()

            for (i in RubyThread.extensions) {
                if (GlobalValues.isEnabled(i)) {
                    RubyThread.rubyContainer.callMethod(i, "pre_draw_reflection", reflectionGraphics)
                }
            }

            // Draw the reflection
            reflectionGraphics.drawRenderedImage(
                GlobalValues.mutableSprite,
                null
            )

            for (i in RubyThread.extensions) {
                if (GlobalValues.isEnabled(i)) {
                    RubyThread.rubyContainer.callMethod(i, "during_draw_reflection", reflectionGraphics)
                }
            }

            reflectionGraphics.composite = AlphaComposite.getInstance(AlphaComposite.DST_IN)
            // Apply a gradient paint, so the reflection fades out
            reflectionGraphics.paint = GradientPaint(
                0f,
                GlobalValues.sheet!!.spriteHeight.toFloat() * GlobalValues.optionsMap.getMap("reflection")!!.getMap("fade")!!.getOption<Float>("height")!!,
                Color(0.0f, 0.0f, 0.0f, 0.0f),
                0f,
                GlobalValues.sheet!!.spriteHeight.toFloat(),
                Color(0.0f, 0.0f, 0.0f, GlobalValues.optionsMap.getMap("reflection")!!.getMap("fade")!!.getOption<Float>("opacity")!!)
            )
            reflectionGraphics.fillRect(0, 0, GlobalValues.sheet!!.spriteWidth, GlobalValues.sheet!!.spriteHeight)
            reflectionGraphics.dispose()
            g2D.drawRenderedImage(reflection, null)

            for (i in RubyThread.extensions) {
                if (GlobalValues.isEnabled(i)) {
                    RubyThread.rubyContainer.callMethod(i, "post_draw_reflection", g2D)
                }
            }
        }

        for (i in RubyThread.extensions) {
            if (GlobalValues.isEnabled(i)) {
                RubyThread.rubyContainer.callMethod(i, "post_draw", g2D)
            }
        }
    }
}