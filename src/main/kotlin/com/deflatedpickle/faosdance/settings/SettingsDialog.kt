package com.deflatedpickle.faosdance.settings

import com.deflatedpickle.faosdance.GlobalValues
import com.deflatedpickle.faosdance.settings.general.GeneralSettings
import com.deflatedpickle.faosdance.util.Lang
import java.awt.Dimension
import java.awt.Frame
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JComponent
import javax.swing.JDialog
import javax.swing.JTabbedPane

class SettingsDialog(owner: Frame) : JDialog(
    owner,
    "${Lang.bundle.getString("window.title")} ${Lang.bundle.getString("window.settings")}",
    true
) {
    val widgets = mutableListOf<JComponent>()

    val generalSettings = GeneralSettings(owner, this)
    val extensionsSettings = ExtensionSettings(owner, this)

    init {
        GlobalValues.settingsDialog = this

        this.size = Dimension(440, 640)

        this.addWindowListener(object : WindowAdapter() {
            override fun windowClosed(e: WindowEvent) {
                GlobalValues.settingsDialog = null
            }
        })

        // Widgets
        val tabbedPane = JTabbedPane()
        tabbedPane.addTab("General", generalSettings)
        tabbedPane.addTab("Extensions", extensionsSettings)

        this.add(tabbedPane)
    }

    fun triggerWidgets() {
        for (i in this.widgets) {
            i.isEnabled = false
        }
    }
}