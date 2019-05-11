package com.deflatedpickle.faosdance.settings.general

import com.deflatedpickle.faosdance.ConfigFile
import com.deflatedpickle.faosdance.GlobalValues
import com.deflatedpickle.faosdance.settings.SettingsDialog
import com.deflatedpickle.faosdance.util.Lang
import java.awt.Frame
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JOptionPane
import javax.swing.JPanel

class GeneralSettings(owner: Frame, val settings: SettingsDialog) : JPanel() {
    val spritePanel = SpritePanel(owner, settings)
    val locationPanel = LocationPanel(owner, settings)
    val reflectionPanel = ReflectionPanel(owner, settings)

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
        this.layout = BoxLayout(this, BoxLayout.Y_AXIS)
        this.add(spritePanel)
        this.add(locationPanel)
        this.add(reflectionPanel)

        this.settings.widgets.add(saveConfigurationButton)
        this.add(saveConfigurationButton)
    }
}