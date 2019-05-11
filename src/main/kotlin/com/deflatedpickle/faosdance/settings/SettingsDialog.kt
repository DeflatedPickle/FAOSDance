package com.deflatedpickle.faosdance.settings

import com.deflatedpickle.faosdance.ConfigFile
import com.deflatedpickle.faosdance.GlobalValues
import com.deflatedpickle.faosdance.settings.general.GeneralSettings
import com.deflatedpickle.faosdance.util.Lang
import java.awt.Dimension
import java.awt.Frame
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.*

class SettingsDialog(owner: Frame) : JDialog(
    owner,
    "${Lang.bundle.getString("window.title")} ${Lang.bundle.getString("window.settings")}",
    true
) {
    val widgets = mutableListOf<JComponent>()

    val generalSettings = GeneralSettings(owner, this)
    val extensionsSettings = ExtensionSettings(owner, this)

    val saveConfigurationButton = JButton(Lang.bundle.getString("settings.save_configuration")).apply {
        addActionListener {
            ConfigFile.writeConfig()
            JOptionPane.showMessageDialog(
                GlobalValues.frame,
                Lang.bundle.getString("settings.save_configuration.message"),
                GlobalValues.frame!!.title,
                JOptionPane.INFORMATION_MESSAGE
            )
        }
    }

    init {
        GlobalValues.settingsDialog = this
        this.layout = GridBagLayout()

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

        this.add(tabbedPane, GridBagConstraints().apply {
            fill = GridBagConstraints.BOTH
            weightx = 1.0
            weighty = 1.0
            gridwidth = GridBagConstraints.REMAINDER
        })

        this.widgets.add(saveConfigurationButton)
        this.add(saveConfigurationButton, GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            weightx = 1.0
            gridwidth = GridBagConstraints.REMAINDER
        })
    }

    fun triggerWidgets() {
        if (GlobalValues.sheet == null) {
            for (i in this.widgets) {
                i.isEnabled = false
            }
        }
    }
}