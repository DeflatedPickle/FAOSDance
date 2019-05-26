package com.deflatedpickle.faosdance.settings.general

import com.deflatedpickle.faosdance.GlobalValues
import com.deflatedpickle.faosdance.ScalingType
import com.deflatedpickle.faosdance.settings.SettingsDialog
import com.deflatedpickle.faosdance.util.Lang
import java.awt.Frame
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.*

class SpriteCategory(owner: Frame, val settings: SettingsDialog) : JPanel() {
    private val gridBagLayout = GridBagLayout()

    val animationCategory = AnimationCategory(owner, settings)
    val rotationCategory = RotationCategory(owner, settings)
    val scaleCategory = ScaleCategory(owner, settings)

    var scalingTypeCombobox: JComboBox<String>? = null
    var opacityWidgets: Triple<JComponent, JSlider, JSpinner>? = null
    var visibleCheckbox: JCheckBox? = null
    var alwaysOnTopCheckbox: JCheckBox? = null
    var toggleHeldCheckbox: JCheckBox? = null
    var solidCheckbox: JCheckBox? = null

    init {
        this.layout = gridBagLayout
        this.border = BorderFactory.createTitledBorder(Lang.bundle.getString("settings.sprite"))

        visibleCheckbox = JCheckBox(Lang.bundle.getString("settings.sprite.visible")).apply {
            isSelected = GlobalValues.isVisible

            addActionListener {
                GlobalValues.isVisible = this.isSelected
                GlobalValues.updateScripts("isVisible", GlobalValues.isVisible)
            }

            gridBagLayout.setConstraints(this,
                GridBagConstraints().apply { gridwidth = GridBagConstraints.REMAINDER }
            )
        }
        this.settings.widgets.add(visibleCheckbox!!)
        this.add(visibleCheckbox)

        // Scaling type label
        this.add(JLabel("${Lang.bundle.getString("settings.sprite.scalingType")}:").also {
            gridBagLayout.setConstraints(it, GridBagConstraints().apply {
                anchor = GridBagConstraints.EAST
            })
        })

        // Scaling type drop-down
        val scalingValues = GlobalValues.enumToReadableNames(ScalingType::class.java)
        scalingTypeCombobox = JComboBox<String>(scalingValues).apply {
            selectedIndex = scalingValues.indexOf(GlobalValues.sanatizeEnumValue(GlobalValues.scalingType.name))

            addActionListener {
                GlobalValues.scalingType = ScalingType.valueOf(GlobalValues.unsanatizeEnumValue(((it.source as JComboBox<*>).selectedItem as String)))
                GlobalValues.updateScripts("scalingType", GlobalValues.scalingType)
            }

            gridBagLayout.setConstraints(this, GridBagConstraints().apply {
                fill = GridBagConstraints.HORIZONTAL
                weightx = 1.0
                gridwidth = GridBagConstraints.REMAINDER
            })
        }
        this.settings.widgets.add(scalingTypeCombobox!!)
        this.add(scalingTypeCombobox)

        opacityWidgets = GlobalValues.addComponentSliderSpinner<Double>(
            this,
            gridBagLayout,
            JLabel("${Lang.bundle.getString("settings.sprite.opacity")}:"),
            GlobalValues.opacity,
            1.0,
            0.1
        ).apply {
            third.addChangeListener {
                GlobalValues.opacity = (it.source as JSpinner).model.value as Double
                GlobalValues.updateScripts("opacity", GlobalValues.opacity)
            }
        }
        this.settings.widgets.add(opacityWidgets!!.first)
        this.settings.widgets.add(opacityWidgets!!.second)
        this.settings.widgets.add(opacityWidgets!!.third)

        solidCheckbox = JCheckBox(Lang.bundle.getString("settings.sprite.solid")).apply {
            isSelected = GlobalValues.isSolid

            addActionListener {
                GlobalValues.isSolid = this.isSelected
                GlobalValues.updateScripts("isSolid", GlobalValues.isSolid)
            }
        }
        this.settings.widgets.add(solidCheckbox!!)
        this.add(solidCheckbox)

        alwaysOnTopCheckbox = JCheckBox(Lang.bundle.getString("settings.sprite.always_on_top")).apply {
            isSelected = GlobalValues.isTopLevel

            addActionListener {
                GlobalValues.isTopLevel = this.isSelected
                GlobalValues.updateScripts("isTopLevel", GlobalValues.isTopLevel)
                GlobalValues.frame!!.isAlwaysOnTop = GlobalValues.isTopLevel
            }
        }
        this.settings.widgets.add(alwaysOnTopCheckbox!!)
        this.add(alwaysOnTopCheckbox)

        toggleHeldCheckbox = JCheckBox(Lang.bundle.getString("settings.sprite.toggled_held")).apply {
            isSelected = GlobalValues.isToggleHeld

            addActionListener {
                GlobalValues.isToggleHeld = this.isSelected
                GlobalValues.updateScripts("isToggleHeld", GlobalValues.isToggleHeld)
            }

            gridBagLayout.setConstraints(this,
                GridBagConstraints().apply { gridwidth = GridBagConstraints.REMAINDER }
            )
        }
        this.settings.widgets.add(toggleHeldCheckbox!!)
        this.add(toggleHeldCheckbox)

        val fillConstraint = GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            weightx = 1.0
            gridwidth = GridBagConstraints.REMAINDER
        }

        this.add(animationCategory, fillConstraint)
        this.add(rotationCategory, fillConstraint)
        this.add(scaleCategory, fillConstraint)
    }
}