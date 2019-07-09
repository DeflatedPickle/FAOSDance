package com.deflatedpickle.faosdance.gui.settings.general

import com.deflatedpickle.faosdance.backend.Direction
import com.deflatedpickle.faosdance.util.GlobalValues
import com.deflatedpickle.faosdance.gui.settings.SettingsDialog
import com.deflatedpickle.faosdance.util.Lang
import java.awt.Frame
import java.awt.GridBagLayout
import javax.swing.*

class ScaleCategory(owner: Frame, val settings: SettingsDialog) : JPanel() {
    private val gridBagLayout = GridBagLayout()

    var bothScaleWidgets: Triple<JComponent, JSlider, JSpinner>? = null
    var xScaleWidgets: Triple<JComponent, JSlider, JSpinner>? = null
    var yScaleWidgets: Triple<JComponent, JSlider, JSpinner>? = null

    init {
        this.layout = gridBagLayout
        this.border = BorderFactory.createTitledBorder(Lang.bundle.getString("settings.size"))

        bothScaleWidgets = GlobalValues.addComponentSliderSpinner<Double>(
            this,
            this.layout as GridBagLayout,
            JLabel("${Lang.bundle.getString("settings.size.both")}:"),
            GlobalValues.optionsMap.getMap("sprite")!!.getMap("size")!!.getOption<Double>("both")!!,
            GlobalValues.maxSize,
            -GlobalValues.maxSize
        ).apply {
            third.addChangeListener {
                xScaleWidgets!!.third.value = third.value
                yScaleWidgets!!.third.value = third.value
            }
        }
        this.settings.widgets.add(bothScaleWidgets!!.first)
        this.settings.widgets.add(bothScaleWidgets!!.second)
        this.settings.widgets.add(bothScaleWidgets!!.third)

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