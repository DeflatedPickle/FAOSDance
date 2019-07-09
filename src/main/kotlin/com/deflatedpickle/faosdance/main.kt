package com.deflatedpickle.faosdance

import com.deflatedpickle.faosdance.autoupdate.UpdateDialog
import com.deflatedpickle.faosdance.autoupdate.UpdateUtil
import com.deflatedpickle.faosdance.settings.ExtensionSettings
import com.deflatedpickle.faosdance.settings.SettingsDialog
import com.deflatedpickle.faosdance.util.ConfigFile
import com.deflatedpickle.faosdance.util.GlobalValues
import com.deflatedpickle.faosdance.window.ApplicationWindow
import com.deflatedpickle.faosdance.window.ContextMenu
import com.deflatedpickle.faosdance.window.SpritePanel
import java.awt.MenuItem
import java.awt.PopupMenu
import java.awt.SystemTray
import java.awt.TrayIcon
import javax.swing.JMenuItem
import javax.swing.JSeparator
import javax.swing.UIManager

@Suppress("KDocMissingDocumentation")
fun main() {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

    if (UpdateUtil.isOutdated) {
        val dialog = UpdateDialog()
        dialog.setLocationRelativeTo(null)

        dialog.isVisible = true
        UpdateUtil.update()
    }
    else {
        val frame = ApplicationWindow()

        val config = ConfigFile.loadAndUseConfig()

        // Enable scripts in the config
        for (i in ExtensionSettings.extensionList) {
            if (GlobalValues.optionsMap.getMap("extensions")!!.getOption<List<String>>("enabled")!!.contains(i.getInstanceVariable("@name").asJavaString())) {
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

        GlobalValues.initPositions()

        frame.setLocation(GlobalValues.optionsMap.getMap("window")!!.getMap("location")!!.getOption<Int>("x")!!, GlobalValues.optionsMap.getMap("window")!!.getMap("location")!!.getOption<Int>("y")!!)

        if (GlobalValues.optionsMap.getMap("settings")!!.getOption<Boolean>("open_on_start")!!) {
            val dialog = SettingsDialog(frame)
            dialog.triggerWidgets()
            dialog.setLocationRelativeTo(frame)

            dialog.isVisible = true
        }
    }
}