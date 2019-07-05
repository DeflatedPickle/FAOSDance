package com.deflatedpickle.faosdance.settings.general

import com.deflatedpickle.faosdance.settings.SettingsDialog
import java.awt.Frame
import javax.swing.BoxLayout
import javax.swing.JPanel

class GeneralSettings(owner: Frame, val settings: SettingsDialog) : JPanel() {
    val windowCategory = WindowCategory(owner, settings)
    val spriteCategory = SpriteCategory(owner, settings)
    val locationCategory = LocationCategory(owner, settings)
    val reflectionCategory = ReflectionCategory(owner, settings)
    val streamerModeCategory = StreamerModeCategory(owner, settings)

    init {
        this.layout = BoxLayout(this, BoxLayout.Y_AXIS)
        this.add(windowCategory)
        this.add(spriteCategory)
        this.add(locationCategory)
        this.add(reflectionCategory)
        this.add(streamerModeCategory)
    }
}