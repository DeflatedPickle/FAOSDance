package com.deflatedpickle.faosdance.settings.general

import com.deflatedpickle.faosdance.GlobalValues
import com.deflatedpickle.faosdance.settings.SettingsDialog
import com.deflatedpickle.faosdance.util.Lang
import java.awt.Frame
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.*
import kotlin.math.roundToInt

class RotationPanel(owner: Frame, val settings: SettingsDialog) : JPanel() {
    private val gridBagLayout = GridBagLayout()

    var zRotationWidgets: Triple<JComponent, JSlider, JSpinner>? = null

    init {
        this.layout = gridBagLayout
        this.border = BorderFactory.createTitledBorder(Lang.bundle.getString("settings.rotation"))

        // TODO: Add 3D rotation
        zRotationWidgets = GlobalValues.addComponentSliderSpinner<Int>(
            this,
            this.layout as GridBagLayout,
            JLabel("${Lang.bundle.getString("settings.rotation.z")}:"),
            GlobalValues.zRotation,
            360,
            0
        ).apply {
            third.addChangeListener {
                GlobalValues.zRotation = when {
                    (it.source as JSpinner).model.value is Int -> (it.source as JSpinner).model.value as Int
                    (it.source as JSpinner).model.value is Double -> ((it.source as JSpinner).model.value as Double).roundToInt()
                    else -> 0
                }
            }
        }
        this.settings.widgets.add(zRotationWidgets!!.second)
        this.settings.widgets.add(zRotationWidgets!!.third)
    }
}