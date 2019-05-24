package com.deflatedpickle.faosdance.settings.general

import com.deflatedpickle.faosdance.GlobalValues
import com.deflatedpickle.faosdance.settings.SettingsDialog
import com.deflatedpickle.faosdance.util.Lang
import java.awt.Frame
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.*
import kotlin.math.roundToInt

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
            GlobalValues.fadeHeight,
            0.9,
            0.1
        ).apply {
            third.addChangeListener {
                GlobalValues.fadeHeight = ((it.source as JSpinner).model.value as Double).toFloat()
            }
        }
        this.settings.widgets.add(fadeHeightWidgets!!.first)
        this.settings.widgets.add(fadeHeightWidgets!!.second)
        this.settings.widgets.add(fadeHeightWidgets!!.third)

        fadeOpacityWidgets = GlobalValues.addComponentSliderSpinner<Double>(
            this,
            this.layout as GridBagLayout,
            JLabel("${Lang.bundle.getString("settings.reflection.fade.opacity")}:"),
            GlobalValues.fadeOpacity,
            0.9,
            0.1
        ).apply {
            third.addChangeListener {
                GlobalValues.fadeOpacity = ((it.source as JSpinner).model.value as Double).toFloat()
            }
        }
        this.settings.widgets.add(fadeOpacityWidgets!!.first)
        this.settings.widgets.add(fadeOpacityWidgets!!.second)
        this.settings.widgets.add(fadeOpacityWidgets!!.third)
    }
}