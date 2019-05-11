package com.deflatedpickle.faosdance

import com.deflatedpickle.faosdance.settings.ExtensionSettings
import com.deflatedpickle.faosdance.settings.SettingsDialog
import com.deflatedpickle.faosdance.util.Lang
import org.jruby.RubyBoolean
import java.awt.*
import java.awt.AlphaComposite
import java.awt.datatransfer.DataFlavor
import java.awt.dnd.DnDConstants
import java.awt.dnd.DropTarget
import java.awt.dnd.DropTargetDropEvent
import java.awt.event.*
import java.awt.image.BufferedImage
import java.io.File
import javax.swing.*


@Suppress("KDocMissingDocumentation")
fun main() {
    val icon = ImageIcon(ClassLoader.getSystemResource("icon.png"), "FAOSDance")

    val scripts = mutableListOf<String>()
    // Makes sure the core class is always loaded first
    scripts.add(File(ClassLoader.getSystemResource("scripts/dance_extension.rb").path).readText())
    for (i in File(ClassLoader.getSystemResource("scripts").path).listFiles()) {
        if (i.name != "dance_extension") {
            scripts.add(i.readText())
        }
    }
    RubyThread.queue = scripts

    val frame = JFrame(Lang.bundle.getString("window.title"))
    frame.iconImage = icon.image
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
    SwingUtilities.updateComponentTreeUI(frame)

    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.isAlwaysOnTop = true
    frame.isUndecorated = true
    frame.background = Color(0, 0, 0, 0)
    frame.type = Window.Type.UTILITY

    var ctrlHeld = false
    frame.addKeyListener(object : KeyAdapter() {
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

    frame.addMouseWheelListener {
        if (ctrlHeld) {
            GlobalValues.xMultiplier += (it.preciseWheelRotation * -1) / 100
            GlobalValues.yMultiplier += (it.preciseWheelRotation * -1) / 100
            GlobalValues.resize(Direction.BOTH)
        }
    }

    GlobalValues.frame = frame

    val config = ConfigFile.loadAndUseConfig()

    // Enable scripts in the config
    for (i in ExtensionSettings.extensionList) {
        if (GlobalValues.enabledExtensions.contains(i.getInstanceVariable("@name").asJavaString())) {
            i.setInstanceVariable("@enabled", RubyThread.ruby.getTrue())
        }
    }

    val contextMenu = JPopupMenu()
    val menuItems = mutableListOf<JComponent>()

    JMenuItem(Lang.bundle.getString("menu.move_to_centre")).apply {
        contextMenu.add(this)
        menuItems.add(this)

        addActionListener {
            frame.setLocationRelativeTo(null)
        }
    }

    JSeparator().apply {
        contextMenu.add(this)
        menuItems.add(this)
    }

    JMenuItem(Lang.bundle.getString("menu.settings")).apply {
        contextMenu.add(this)
        menuItems.add(this)

        addActionListener {
            val dialog = SettingsDialog(frame)
            dialog.triggerWidgets()
            dialog.setLocationRelativeTo(frame)

            dialog.isVisible = true
        }
    }

    JSeparator().apply {
        contextMenu.add(this)
        menuItems.add(this)
    }

    JMenuItem(Lang.bundle.getString("menu.exit")).apply {
        contextMenu.add(this)
        menuItems.add(this)

        addActionListener {
            frame.dispatchEvent(WindowEvent(frame, WindowEvent.WINDOW_CLOSING))
        }
    }

    if (SystemTray.isSupported()) {
        val systemTray = SystemTray.getSystemTray()

        val trayIcon = TrayIcon(icon.image, frame.title, object : PopupMenu() {
            init {
                for (i in menuItems) {
                    if (i is JMenuItem) {
                        this.add(MenuItem(i.text).apply { addActionListener(i.actionListeners[0]) })
                    } else if (i is JSeparator) {
                        this.addSeparator()
                    }
                }
            }
        }).apply { isImageAutoSize = true }
        systemTray.add(trayIcon)
    }

    var isGrabbed = false
    var clickedPoint = Point()
    var animation = GlobalValues.currentAction
    frame.addMouseListener(object : MouseAdapter() {
        override fun mousePressed(e: MouseEvent) {
            super.mousePressed(e)

            if (e.button == 1 && GlobalValues.isSolid) {
                isGrabbed = true
                clickedPoint = e.point

                animation = GlobalValues.currentAction

                if (GlobalValues.sheet != null) {
                    GlobalValues.currentAction = GlobalValues.sheet!!.spriteMap.keys.last()
                }
            } else if (e.button == 3) {
                contextMenu.show(frame, e.x, e.y)
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
                frame.location = Point(e.xOnScreen - clickedPoint.x, e.yOnScreen - clickedPoint.y)
            }
        }
    }.apply { frame.addMouseMotionListener(this) })

    val panel = object : JPanel() {
        init {
            isOpaque = false

            dropTarget = object : DropTarget() {
                override fun drop(dtde: DropTargetDropEvent) {
                    // super.drop(dtde)

                    dtde.acceptDrop(DnDConstants.ACTION_COPY)
                    val droppedFiles = dtde.transferable.getTransferData(DataFlavor.javaFileListFlavor) as List<File>

                    if (droppedFiles.size == 1) {
                        val tempSheet = SpriteSheet(droppedFiles[0].absolutePath.substringBeforeLast("."))

                        if (tempSheet.loadedImage && tempSheet.loadedText) {
                            GlobalValues.configureSpriteSheet(tempSheet)
                            GlobalValues.currentPath = droppedFiles[0].parentFile.absolutePath
                        }
                    }
                }
            }
        }

        override fun paintComponent(g: Graphics) {
            super.paintComponent(g)

            if (GlobalValues.sheet == null || !GlobalValues.isVisible) return

            val g2D = g as Graphics2D
            g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)

            for (i in RubyThread.extensions) {
                if (GlobalValues.isEnabled(i)) {
                    RubyThread.rubyContainer.callMethod(i, "pre_draw", g2D)
                }
            }

            // Change the opacity
            g2D.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, GlobalValues.opacity.toFloat())

            // Rotate the matrix
            g2D.rotate(
                Math.toRadians(GlobalValues.zRotation.toDouble()),
                (this.width / 2).toDouble(),
                (this.height / 2).toDouble()
            )

            // Translate to the centre
            g2D.translate(
                (this.width - GlobalValues.sheet!!.spriteWidth * GlobalValues.xMultiplier) / 2,
                (this.height - GlobalValues.sheet!!.spriteHeight * GlobalValues.yMultiplier) / 2
            )

            // Translate upwards the size of the sprite plus the reflection padding
            if (GlobalValues.isReflectionVisible) {
                g2D.translate(
                    0.0,
                    -(GlobalValues.sheet!!.spriteHeight * GlobalValues.yMultiplier) / 2 - GlobalValues.reflectionPadding
                )
            }
            // Scale the sprite
            g2D.scale(GlobalValues.xMultiplier, GlobalValues.yMultiplier)
            // Draw the sprite
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
                GlobalValues.sheet!!.spriteMap[GlobalValues.currentAction]!![GlobalValues.animFrame],
                null
            )
            spriteGraphics.dispose()
            g2D.drawRenderedImage(sprite, null)

            for (i in RubyThread.extensions) {
                if (GlobalValues.isEnabled(i)) {
                    RubyThread.rubyContainer.callMethod(i, "post_draw_sprite", g2D)
                }
            }

            if (!GlobalValues.isReflectionVisible) return

            // Move to the bottom of the sprite
            g2D.translate(
                0.0,
                (GlobalValues.sheet!!.spriteHeight) * 2 + (GlobalValues.reflectionPadding)
            )
            // Flip the matrix, so the reflection is upside down
            g2D.scale(1.0, -1.0)

            // Create a new image for the reflection
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
                GlobalValues.sheet!!.spriteMap[GlobalValues.currentAction]!![GlobalValues.animFrame],
                null
            )
            reflectionGraphics.composite = AlphaComposite.getInstance(AlphaComposite.DST_IN)
            // Apply a gradient paint, so the reflection fades out
            reflectionGraphics.paint = GradientPaint(
                0f,
                GlobalValues.sheet!!.spriteHeight.toFloat() * GlobalValues.fadeHeight,
                Color(0.0f, 0.0f, 0.0f, 0.0f),
                0f,
                GlobalValues.sheet!!.spriteHeight.toFloat(),
                Color(0.0f, 0.0f, 0.0f, GlobalValues.fadeOpacity)
            )
            reflectionGraphics.fillRect(0, 0, GlobalValues.sheet!!.spriteWidth, GlobalValues.sheet!!.spriteHeight)
            reflectionGraphics.dispose()
            g2D.drawRenderedImage(reflection, null)

            for (i in RubyThread.extensions) {
                if (GlobalValues.isEnabled(i)) {
                    RubyThread.rubyContainer.callMethod(i, "post_draw_reflection", g2D)
                }
            }

            for (i in RubyThread.extensions) {
                if (GlobalValues.isEnabled(i)) {
                    RubyThread.rubyContainer.callMethod(i, "post_draw", g2D)
                }
            }
        }
    }
    frame.add(panel)

    GlobalValues.timer = Timer(1000 / GlobalValues.fps, ActionListener {
        if (GlobalValues.play && GlobalValues.sheet != null) {
            if (GlobalValues.rewind) {
                GlobalValues.animFrame--

                if (GlobalValues.animFrame <= 0) {
                    GlobalValues.animFrame = 7
                }
            } else {
                GlobalValues.animFrame++

                if (GlobalValues.animFrame >= 8) {
                    GlobalValues.animFrame = 0
                }
            }

            GlobalValues.animationControls?.second?.value = GlobalValues.animFrame

            frame.revalidate()
            frame.repaint()
        }
    })
    GlobalValues.timer!!.start()

    frame.pack()
    frame.isVisible = true

    if (!config) {
        GlobalValues.initPositions()
    }

    frame.setLocation(GlobalValues.xPosition, GlobalValues.yPosition)

    if (!config) {
        val dialog = SettingsDialog(frame)
        dialog.triggerWidgets()
        dialog.setLocationRelativeTo(frame)

        dialog.isVisible = true
    }
}