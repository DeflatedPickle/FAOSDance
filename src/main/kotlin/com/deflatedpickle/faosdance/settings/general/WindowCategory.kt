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
            isSelected = GlobalValues.optionsMap.getMap("window")!!.getOption<Boolean>("solid")!!

            addActionListener {
                GlobalValues.optionsMap.getMap("window")!!.setOption("solid", this.isSelected)
                GlobalValues.updateScripts("window.solid", GlobalValues.optionsMap.getMap("window")!!.getOption<Boolean>("solid")!!)
            }
        }
        this.settings.widgets.add(solidCheckbox!!)
        this.add(solidCheckbox)

        alwaysOnTopCheckbox = JCheckBox(Lang.bundle.getString("settings.window.always_on_top")).apply {
            isSelected = GlobalValues.optionsMap.getMap("window")!!.getOption<Boolean>("always_on_top")!!

            addActionListener {
                GlobalValues.optionsMap.getMap("window")!!.setOption("always_on_top", this.isSelected)
                GlobalValues.updateScripts("window.always_on_top", GlobalValues.optionsMap.getMap("window")!!.getOption<Boolean>("always_on_top")!!)
                GlobalValues.frame!!.isAlwaysOnTop = GlobalValues.optionsMap.getMap("window")!!.getOption<Boolean>("always_on_top")!!
            }
        }
        this.settings.widgets.add(alwaysOnTopCheckbox!!)
        this.add(alwaysOnTopCheckbox)
    }
}