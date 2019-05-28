package com.deflatedpickle.faosdance.settings.general

import com.deflatedpickle.faosdance.GlobalValues
import com.deflatedpickle.faosdance.component_border.ComponentPanel
import com.deflatedpickle.faosdance.settings.SettingsDialog
import com.deflatedpickle.faosdance.util.Lang
import java.awt.FlowLayout
import java.awt.Frame
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.*

class ReflectionCategory(owner: Frame, val settings: SettingsDialog) :
    ComponentPanel(JCheckBox(Lang.bundle.getString("settings.reflection"))) {
    private val gridBagLayout = GridBagLayout()

    val fadeCategory = FadeCategory(owner, settings)

    var paddingWidgets: Triple<JComponent, JSlider, JSpinner>? = null

    init {
        this.layout = FlowLayout()

        this.panel.layout = gridBagLayout
        (this.titleComponent as JCheckBox).apply {
            isSelected = GlobalValues.isReflectionVisible
            addActionListener {
                GlobalValues.isReflectionVisible = this.isSelected
                GlobalValues.updateScripts("isReflectionVisible", GlobalValues.isReflectionVisible)

                for (i in this@ReflectionCategory.panel.components) {
                    i.isEnabled = this.isSelected

                    if (i is FadeCategory) {
                        for (c in i.components) {
                            c.isEnabled = this.isSelected
                        }
                    }
                }
            }
        }
        this.settings.widgets.add(this.titleComponent)

        paddingWidgets = GlobalValues.addComponentSliderSpinner<Double>(
            this.panel,
            this.panel.layout as GridBagLayout,
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

        this.panel.add(fadeCategory.apply {
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