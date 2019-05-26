package com.deflatedpickle.faosdance.settings.general

import com.deflatedpickle.faosdance.GlobalValues
import com.deflatedpickle.faosdance.settings.SettingsDialog
import com.deflatedpickle.faosdance.util.Lang
import java.awt.Frame
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.*

class ReflectionCategory(owner: Frame, val settings: SettingsDialog) : JPanel() {
    private val gridBagLayout = GridBagLayout()

    val fadeCategory = FadeCategory(owner, settings)

    var paddingWidgets: Triple<JComponent, JSlider, JSpinner>? = null
    var visibleCheckbox: JCheckBox? = null

    init {
        this.layout = gridBagLayout
        this.border = BorderFactory.createTitledBorder(Lang.bundle.getString("settings.reflection"))

        visibleCheckbox = JCheckBox(Lang.bundle.getString("settings.reflection.visible")).apply {
            isSelected = GlobalValues.isReflectionVisible

            addActionListener {
                GlobalValues.isReflectionVisible = this.isSelected
                GlobalValues.updateScripts("isReflectionVisible", GlobalValues.isReflectionVisible)
            }
        }
        (this.layout as GridBagLayout).setConstraints(
            visibleCheckbox, GridBagConstraints().apply { gridwidth = GridBagConstraints.REMAINDER }
        )
        this.settings.widgets.add(visibleCheckbox!!)
        this.add(visibleCheckbox)

        paddingWidgets = GlobalValues.addComponentSliderSpinner<Double>(
            this,
            this.layout as GridBagLayout,
            JLabel("${Lang.bundle.getString("settings.reflection.padding")}:"),
            GlobalValues.reflectionPadding,
            100.0,
            -100.0
        ).apply {
            third.addChangeListener {
                GlobalValues.reflectionPadding = (it.source as JSpinner).model.value as Double
                GlobalValues.updateScripts("reflectionPadding", GlobalValues.reflectionPadding)
            }
        }
        this.settings.widgets.add(paddingWidgets!!.first)
        this.settings.widgets.add(paddingWidgets!!.second)
        this.settings.widgets.add(paddingWidgets!!.third)

        this.add(fadeCategory.apply {
            gridBagLayout.setConstraints(this,
                GridBagConstraints().apply {
                    fill = GridBagConstraints.HORIZONTAL
                    weightx = 1.0
                    gridwidth = GridBagConstraints.REMAINDER
                }
            )
        })
    }
}