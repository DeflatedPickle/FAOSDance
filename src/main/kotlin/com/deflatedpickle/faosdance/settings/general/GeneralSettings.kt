package com.deflatedpickle.faosdance.settings.general

import com.deflatedpickle.faosdance.ConfigFile
import com.deflatedpickle.faosdance.GlobalValues
import com.deflatedpickle.faosdance.settings.SettingsDialog
import com.deflatedpickle.faosdance.util.Lang
import java.awt.Frame
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JOptionPane
import javax.swing.JPanel

class GeneralSettings(owner: Frame, val settings: SettingsDialog) : JPanel() {
    val spriteCategory = SpriteCategory(owner, settings)
    val locationCategory = LocationCategory(owner, settings)
    val reflectionCategory = ReflectionCategory(owner, settings)

    init {
        this.layout = BoxLayout(this, BoxLayout.Y_AXIS)
        this.add(spriteCategory)
        this.add(locationCategory)
        this.add(reflectionCategory)
    }
}