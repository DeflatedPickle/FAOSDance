package com.deflatedpickle.faosdance.gui.settings.general

import com.deflatedpickle.faosdance.util.GlobalValues
import com.deflatedpickle.faosdance.gui.settings.SettingsDialog
import com.deflatedpickle.faosdance.util.Lang
import java.awt.Frame
import java.awt.GridBagLayout
import javax.swing.*

class FadeCategory(owner: Frame, val settings: SettingsDialog) : JPanel() {
    private val gridBagLayout = GridBagLayout()

    var fadeHeightWidgets: Triple<JComponent, JSlider, JSpinner>? = null
    var fadeOpacityWidgets: Triple<JComponent, JSlider, JSpinner>? = null

    init {
        this.layout = gridBagLayout
        this.border = BorderFactory.createTitledBorder(Lang.bundle.getString("settings.reflection.fade"))

        fadeHeightWidgets = GlobalValues.addComponentSliderSpinner<Double>(
            this,
            this.layout as GridBagLayout,
            JLabel("${Lang.bundle.getString("settings.reflection.fade.height")}:"),
            GlobalValues.optionsMap.getMap("reflection")!!.getMap("fade")!!.getOption<Double>("height")!!,
            0.9,
            0.1
        ).apply {
            third.addChangeListener {
                GlobalValues.optionsMap.getMap("reflection")!!.getMap("fade")!!.setOption("height", ((it.source as JSpinner).model.value as Double).toFloat())
                GlobalValues.updateScripts("reflection.fade.height", GlobalValues.optionsMap.getMap("reflection")!!.getMap("fade")!!.getOption<Double>("height")!!)
            }
        }
        this.settings.widgets.add(fadeHeightWidgets!!.first)
        this.settings.widgets.add(fadeHeightWidgets!!.second)
        this.settings.widgets.add(fadeHeightWidgets!!.third)

        fadeOpacityWidgets = GlobalValues.addComponentSliderSpinner<Double>(
            this,
            this.layout as GridBagLayout,
            JLabel("${Lang.bundle.getString("settings.reflection.fade.opacity")}:"),
            GlobalValues.optionsMap.getMap("reflection")!!.getMap("fade")!!.getOption<Double>("opacity")!!,
            0.9,
            0.1
        ).apply {
            third.addChangeListener {
                GlobalValues.optionsMap.getMap("reflection")!!.getMap("fade")!!.setOption("opacity", ((it.source as JSpinner).model.value as Double).toFloat())
                GlobalValues.updateScripts("reflection.fade.opacity", GlobalValues.optionsMap.getMap("reflection")!!.getMap("fade")!!.getOption<Double>("opacity")!!)
            }
        }
        this.settings.widgets.add(fadeOpacityWidgets!!.first)
        this.settings.widgets.add(fadeOpacityWidgets!!.second)
        this.settings.widgets.add(fadeOpacityWidgets!!.third)
    }
}