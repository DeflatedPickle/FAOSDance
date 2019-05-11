package com.deflatedpickle.faosdance

import com.deflatedpickle.faosdance.settings.ExtensionSettings
import com.deflatedpickle.faosdance.settings.SettingsDialog
import com.deflatedpickle.faosdance.util.Lang
import com.deflatedpickle.faosdance.window.ApplicationWindow
import com.deflatedpickle.faosdance.window.ContextMenu
import com.deflatedpickle.faosdance.window.SpritePanel
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
    val scripts = mutableListOf<String>()
    // Makes sure the core class is always loaded first
    scripts.add(File(ClassLoader.getSystemResource("scripts/dance_extension.rb").path).readText())
    for (i in File(ClassLoader.getSystemResource("scripts").path).listFiles()) {
        if (i.name != "dance_extension") {
            scripts.add(i.readText())
        }
    }
    RubyThread.queue = scripts

    val frame = ApplicationWindow()

    val config = ConfigFile.loadAndUseConfig()

    // Enable scripts in the config
    for (i in ExtensionSettings.extensionList) {
        if (GlobalValues.enabledExtensions.contains(i.getInstanceVariable("@name").asJavaString())) {
            i.setInstanceVariable("@enabled", RubyThread.ruby.getTrue())
        }
    }

    if (SystemTray.isSupported()) {
        val systemTray = SystemTray.getSystemTray()

        val trayIcon = TrayIcon(GlobalValues.icon.image, frame.title, object : PopupMenu() {
            init {
                for (i in ContextMenu.trayItems) {
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

    val panel = SpritePanel()
    frame.add(panel)

    frame.pack()
    frame.isVisible = true

    if (!config) {
        GlobalValues.initPositions()
    }
    else {
        GlobalValues.resize()
    }

    frame.setLocation(GlobalValues.xPosition, GlobalValues.yPosition)

    if (!config) {
        val dialog = SettingsDialog(frame)
        dialog.triggerWidgets()
        dialog.setLocationRelativeTo(frame)

        dialog.isVisible = true
    }
}