package com.deflatedpickle.faosdance.settings.general

import com.deflatedpickle.faosdance.GlobalValues
import com.deflatedpickle.faosdance.settings.SettingsDialog
import com.deflatedpickle.faosdance.util.Lang
import java.awt.Frame
import java.awt.GridBagLayout
import javax.swing.BorderFactory
import javax.swing.JCheckBox
import javax.swing.JPanel

class WindowCategory(owner: Frame, val settings: SettingsDialog) : JPanel() {
    private val gridBagLayout = GridBagLayout()

    var alwaysOnTopCheckbox: JCheckBox? = null
    var solidCheckbox: JCheckBox? = null

    init {
        this.layout = gridBagLayout
        this.border = BorderFactory.createTitledBorder(Lang.bundle.getString("settings.window"))

        solidCheckbox = JCheckBox(Lang.bundle.getString("settings.window.solid")).apply {
            isSelected = GlobalValues.isSolid

            addActionListener {
                GlobalValues.isSolid = this.isSelected
                GlobalValues.updateScripts("isSolid", GlobalValues.isSolid)
            }
        }
        this.add(solidCheckbox)

        alwaysOnTopCheckbox = JCheckBox(Lang.bundle.getString("settings.window.always_on_top")).apply {
            isSelected = GlobalValues.isTopLevel

            addActionListener {
                GlobalValues.isTopLevel = this.isSelected
                GlobalValues.updateScripts("isTopLevel", GlobalValues.isTopLevel)
                GlobalValues.frame!!.isAlwaysOnTop = GlobalValues.isTopLevel
            }
        }
        this.add(alwaysOnTopCheckbox)
    }
}