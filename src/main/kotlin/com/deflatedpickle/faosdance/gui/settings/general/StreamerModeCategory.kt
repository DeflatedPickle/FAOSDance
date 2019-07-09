package com.deflatedpickle.faosdance.gui.settings.general

import com.bric.colorpicker.ColorPicker
import com.bric.colorpicker.ColorPickerMode
import com.deflatedpickle.faosdance.util.GlobalValues
import com.deflatedpickle.faosdance.component_border.ComponentPanel
import com.deflatedpickle.faosdance.gui.settings.SettingsDialog
import com.deflatedpickle.faosdance.util.Lang
import java.awt.*
import javax.swing.*

class StreamerModeCategory(owner: Frame, val settings: SettingsDialog) :
    ComponentPanel(JCheckBox(Lang.bundle.getString("settings.streamer_mode"))) {
    private val gridBagLayout = GridBagLayout()

    init {
        this.layout = FlowLayout()

        this.panel.layout = gridBagLayout
        (this.titleComponent as JCheckBox).apply {
            isSelected = GlobalValues.optionsMap.getMap("streamer_mode")!!.getOption<Boolean>("enabled")!!

            addActionListener {
                GlobalValues.optionsMap.getMap("streamer_mode")!!.setOption("enabled", this.isSelected)
                GlobalValues.updateScripts("streamer_mode.enabled", GlobalValues.optionsMap.getMap("streamer_mode")!!.getOption<Boolean>("enabled")!!)

                if (isSelected) {
                    GlobalValues.frame!!.dispose()

                    GlobalValues.frame!!.background = Color(0, 0, 0, 255)
                    GlobalValues.frame!!.contentPane.background = Color(255, 0, 255, 255) // Magenta
                    GlobalValues.frame!!.isUndecorated = false

                    GlobalValues.frame!!.type = Window.Type.NORMAL
                    GlobalValues.frame!!.isVisible = true
                }
                else {
                    GlobalValues.frame!!.dispose()

                    GlobalValues.frame!!.isUndecorated = true
                    GlobalValues.frame!!.background = Color(0, 0, 0, 0)

                    GlobalValues.frame!!.type = Window.Type.UTILITY
                    GlobalValues.frame!!.isVisible = true
                }
            }
        }
        this.settings.widgets.add(this.titleComponent)

        this.panel.add(JLabel("${Lang.bundle.getString("settings.streamer_mode.background")}:").apply {
            gridBagLayout.setConstraints(this, GridBagConstraints().apply {
                anchor = GridBagConstraints.EAST
            })
        })
        val colourPicker = ColorPicker(true, false).apply {
            colorPanel.preferredSize = Dimension(100, 100)
            mode = ColorPickerMode.HUE
            color = Color(255, 0, 255, 255)

            setHSBControlsVisible(false)
            setHexControlsVisible(false)
            setPreviewSwatchVisible(false)

            addColorListener {
                GlobalValues.frame!!.contentPane.background = Color(it.red, it.green, it.blue, 255)
            }

            gridBagLayout.setConstraints(this,
                GridBagConstraints().apply {
                    anchor = GridBagConstraints.WEST
                    gridwidth = GridBagConstraints.REMAINDER
                }
            )
        }
        this.panel.add(colourPicker)
    }
}