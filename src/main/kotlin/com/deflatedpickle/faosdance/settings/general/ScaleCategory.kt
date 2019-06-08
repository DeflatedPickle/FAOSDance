package com.deflatedpickle.faosdance.settings.general

import com.deflatedpickle.faosdance.Direction
import com.deflatedpickle.faosdance.GlobalValues
import com.deflatedpickle.faosdance.settings.SettingsDialog
import com.deflatedpickle.faosdance.util.Lang
import java.awt.Frame
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.*
import kotlin.math.roundToInt

class ScaleCategory(owner: Frame, val settings: SettingsDialog) : JPanel() {
    private val gridBagLayout = GridBagLayout()

    var xScaleWidgets: Triple<JComponent, JSlider, JSpinner>? = null
    var yScaleWidgets: Triple<JComponent, JSlider, JSpinner>? = null

    init {
        this.layout = gridBagLayout
        this.border = BorderFactory.createTitledBorder(Lang.bundle.getString("settings.size"))

        xScaleWidgets = GlobalValues.addComponentSliderSpinner<Double>(
            this,
            this.layout as GridBagLayout,
            JLabel("${Lang.bundle.getString("settings.size.width")}:"),
            GlobalValues.optionsMap.getMap("sprite")!!.getMap("size")!!.getOption<Double>("width")!!,
            GlobalValues.maxSize,
            -GlobalValues.maxSize
        ).apply {
            third.addChangeListener {
                GlobalValues.optionsMap.getMap("sprite")!!.getMap("size")!!.setOption("width", (it.source as JSpinner).model.value as Double)
                GlobalValues.updateScripts("sprite.size.width", GlobalValues.optionsMap.getMap("sprite")!!.getMap("size")!!.getOption<Int>("width")!!)
                GlobalValues.resize(Direction.HORIZONTAL)
            }
        }
        this.settings.widgets.add(xScaleWidgets!!.first)
        this.settings.widgets.add(xScaleWidgets!!.second)
        this.settings.widgets.add(xScaleWidgets!!.third)

        yScaleWidgets = GlobalValues.addComponentSliderSpinner<Double>(
            this,
            this.layout as GridBagLayout,
            JLabel("${Lang.bundle.getString("settings.size.height")}:"),
            GlobalValues.optionsMap.getMap("sprite")!!.getMap("size")!!.getOption<Double>("height")!!,
            GlobalValues.maxSize,
            -GlobalValues.maxSize
        ).apply {
            third.addChangeListener {
                GlobalValues.optionsMap.getMap("sprite")!!.getMap("size")!!.setOption("height", (it.source as JSpinner).model.value as Double)
                GlobalValues.updateScripts("sprite.size.height", GlobalValues.optionsMap.getMap("sprite")!!.getMap("size")!!.getOption<Int>("height")!!)
                GlobalValues.resize(Direction.VERTICAL)
            }
        }
        this.settings.widgets.add(yScaleWidgets!!.first)
        this.settings.widgets.add(yScaleWidgets!!.second)
        this.settings.widgets.add(yScaleWidgets!!.third)
    }
}