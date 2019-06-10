package com.deflatedpickle.faosdance.settings.general

import com.deflatedpickle.faosdance.GlobalValues
import com.deflatedpickle.faosdance.ScalingType
import com.deflatedpickle.faosdance.settings.SettingsDialog
import com.deflatedpickle.faosdance.util.Lang
import com.deflatedpickle.faosdance.widgets.CollapsiblePanel
import java.awt.FlowLayout
import java.awt.Frame
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.*

class SpriteCategory(owner: Frame, val settings: SettingsDialog) :
    CollapsiblePanel(Lang.bundle.getString("settings.sprite")) {
    private val gridBagLayout = GridBagLayout()

    val animationCategory = AnimationCategory(owner, settings)
    val rotationCategory = RotationCategory(owner, settings)
    val scaleCategory = ScaleCategory(owner, settings)

    var scalingTypeCombobox: JComboBox<String>? = null
    var opacityWidgets: Triple<JComponent, JSlider, JSpinner>? = null
    var toggleHeldCheckbox: JCheckBox? = null

    init {
        this.layout = FlowLayout()

        this.panel.layout = gridBagLayout
        (this.titleComponent as Header).checkbox.apply {
            isSelected = GlobalValues.optionsMap.getMap("sprite")!!.getOption<Boolean>("visible")!!

            addActionListener {
                GlobalValues.optionsMap.getMap("sprite")!!.setOption("visible", this.isSelected)
                GlobalValues.updateScripts("sprite.visible", GlobalValues.optionsMap.getMap("sprite")!!.getOption<Boolean>("visible")!!)

                for (i in this@SpriteCategory.panel.components) {
                    i.isEnabled = this.isSelected

                    if (i is AnimationCategory || i is RotationCategory || i is ScaleCategory) {
                        for (c in (i as JPanel).components) {
                            c.isEnabled = this.isSelected
                        }
                    }
                }
            }
        }
        this.settings.widgets.add(this.titleComponent)

        // Scaling type label
        this.panel.add(JLabel("${Lang.bundle.getString("settings.sprite.scalingType")}:").also {
            gridBagLayout.setConstraints(it, GridBagConstraints().apply {
                anchor = GridBagConstraints.EAST
            })
        })

        // Scaling type drop-down
        val scalingValues = GlobalValues.enumToReadableNames(ScalingType::class.java)
        scalingTypeCombobox = JComboBox<String>(scalingValues).apply {
            selectedIndex = scalingValues.indexOf(GlobalValues.optionsMap.getMap("sprite")!!.getOption<String>("scaling_type")!!)

            addActionListener {
                GlobalValues.optionsMap.getMap("sprite")!!.setOption("scaling_type", (it.source as JComboBox<*>).selectedItem as String)
                GlobalValues.updateScripts("sprite.scaling_type", GlobalValues.optionsMap.getMap("sprite")!!.getOption<ScalingType>("scaling_type")!!)
            }

            gridBagLayout.setConstraints(this, GridBagConstraints().apply {
                fill = GridBagConstraints.HORIZONTAL
                weightx = 1.0
                gridwidth = GridBagConstraints.REMAINDER
            })
        }
        this.settings.widgets.add(scalingTypeCombobox!!)
        this.panel.add(scalingTypeCombobox)

        opacityWidgets = GlobalValues.addComponentSliderSpinner<Double>(
            this.panel,
            gridBagLayout,
            JLabel("${Lang.bundle.getString("settings.sprite.opacity")}:"),
            GlobalValues.optionsMap.getMap("sprite")!!.getOption<Double>("opacity")!!,
            1.0,
            0.1
        ).apply {
            third.addChangeListener {
                GlobalValues.optionsMap.getMap("sprite")!!.setOption("opacity", (it.source as JSpinner).model.value as Double)
                GlobalValues.updateScripts("sprite.opacity", GlobalValues.optionsMap.getMap("sprite")!!.getOption<Double>("opacity")!!)
            }
        }
        this.settings.widgets.add(opacityWidgets!!.first)
        this.settings.widgets.add(opacityWidgets!!.second)
        this.settings.widgets.add(opacityWidgets!!.third)

        toggleHeldCheckbox = JCheckBox(Lang.bundle.getString("settings.sprite.toggled_held")).apply {
            isSelected = GlobalValues.optionsMap.getMap("sprite")!!.getOption<Boolean>("toggle_held")!!

            addActionListener {
                GlobalValues.optionsMap.getMap("sprite")!!.setOption("toggle_held", this.isSelected)
                GlobalValues.updateScripts("sprite.toggle_held", GlobalValues.optionsMap.getMap("sprite")!!.getOption<Boolean>("toggle_held")!!)
            }

            gridBagLayout.setConstraints(this,
                GridBagConstraints().apply { gridwidth = GridBagConstraints.REMAINDER }
            )
        }
        this.settings.widgets.add(toggleHeldCheckbox!!)
        this.panel.add(toggleHeldCheckbox)

        val fillConstraint = GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            weightx = 1.0
            gridwidth = GridBagConstraints.REMAINDER
        }

        this.panel.add(animationCategory, fillConstraint)
        this.panel.add(rotationCategory, fillConstraint)
        this.panel.add(scaleCategory, fillConstraint)
    }
}