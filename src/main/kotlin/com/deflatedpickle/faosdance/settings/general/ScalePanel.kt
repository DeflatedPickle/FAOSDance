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

class ScalePanel(owner: Frame, val settings: SettingsDialog) : JPanel() {
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
            GlobalValues.xMultiplier,
            GlobalValues.maxSize,
            -GlobalValues.maxSize
        ).apply {
            third.addChangeListener {
                GlobalValues.xMultiplier = (it.source as JSpinner).model.value as Double
                GlobalValues.resize(Direction.HORIZONTAL)
            }
        }
        this.settings.widgets.add(xScaleWidgets!!.second)
        this.settings.widgets.add(xScaleWidgets!!.third)

        yScaleWidgets = GlobalValues.addComponentSliderSpinner<Double>(
            this,
            this.layout as GridBagLayout,
            JLabel("${Lang.bundle.getString("settings.size.height")}:"),
            GlobalValues.yMultiplier,
            GlobalValues.maxSize,
            -GlobalValues.maxSize
        ).apply {
            third.addChangeListener {
                GlobalValues.yMultiplier = (it.source as JSpinner).model.value as Double
                GlobalValues.resize(Direction.VERTICAL)
            }
        }
        this.settings.widgets.add(yScaleWidgets!!.second)
        this.settings.widgets.add(yScaleWidgets!!.third)
    }
}