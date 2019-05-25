package com.deflatedpickle.faosdance

import com.deflatedpickle.faosdance.settings.ExtensionSettings
import com.deflatedpickle.faosdance.settings.SettingsDialog
import com.deflatedpickle.faosdance.window.ApplicationWindow
import com.deflatedpickle.faosdance.window.ContextMenu
import com.deflatedpickle.faosdance.window.SpritePanel
import java.awt.MenuItem
import java.awt.PopupMenu
import java.awt.SystemTray
import java.awt.TrayIcon
import java.io.BufferedInputStream
import java.io.File
import java.util.zip.ZipInputStream
import javax.swing.JMenuItem
import javax.swing.JSeparator


@Suppress("KDocMissingDocumentation")
fun main() {
    val scripts = mutableListOf<String>()
    // Makes sure the core class is always loaded first
    scripts.add(ClassLoader.getSystemResource("scripts/dance_extension.rb").readText())
    if (ClassLoader.getSystemResource("icon.png").protocol == "jar") {
        val zipInputStream = ZipInputStream(GlobalValues::class.java.protectionDomain.codeSource.location.openStream())

        while (true) {
            val entry = zipInputStream.nextEntry ?: break

            if (entry.name.startsWith("scripts/")) {
                if (entry.name != "scripts/") {
                    val bufferedInputStream = BufferedInputStream(zipInputStream)
                    scripts.add(bufferedInputStream.reader().readText())
                }
            }
        }
    }
    else {
        for (i in ClassLoader.getSystemResourceAsStream("scripts").bufferedReader().readLines().map { ClassLoader.getSystemResource("scripts/$it") }) {
            val f = File(i.file)
            if (f.name != "dance_extension.rb" && f.readText().contains("< DanceExtension")) {
                scripts.add(f.readText())
            }
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