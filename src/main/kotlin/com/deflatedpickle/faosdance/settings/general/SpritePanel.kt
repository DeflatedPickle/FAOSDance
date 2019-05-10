package com.deflatedpickle.faosdance.settings.general

import com.deflatedpickle.faosdance.GlobalValues
import com.deflatedpickle.faosdance.settings.SettingsDialog
import com.deflatedpickle.faosdance.util.Lang
import java.awt.Frame
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.*

class SpritePanel(owner: Frame, val settings: SettingsDialog) : JPanel() {
    private val gridBagLayout = GridBagLayout()

    val animationPanel = AnimationPanel(owner, settings)

    var opacityWidgets: Triple<JComponent, JSlider, JSpinner>? = null
    var visibleCheckbox: JCheckBox? = null
    var alwaysOnTopCheckbox: JCheckBox? = null
    var solidCheckbox: JCheckBox? = null

    init {
        this.layout = gridBagLayout
        this.border = BorderFactory.createTitledBorder(Lang.bundle.getString("settings.sprite"))

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
            }
        }
        this.settings.widgets.add(opacityWidgets!!.second)
        this.settings.widgets.add(opacityWidgets!!.third)

        visibleCheckbox = JCheckBox(Lang.bundle.getString("settings.sprite.visible")).apply {
            isSelected = GlobalValues.isVisible

            addActionListener { GlobalValues.isVisible = this.isSelected }
        }
        this.settings.widgets.add(visibleCheckbox!!)
        this.add(visibleCheckbox)

        alwaysOnTopCheckbox = JCheckBox(Lang.bundle.getString("settings.sprite.always_on_top")).apply {
            isSelected = GlobalValues.isTopLevel

            addActionListener {
                GlobalValues.isTopLevel = this.isSelected
                GlobalValues.frame!!.isAlwaysOnTop = GlobalValues.isTopLevel
            }
        }
        this.settings.widgets.add(alwaysOnTopCheckbox!!)
        this.add(alwaysOnTopCheckbox)

        solidCheckbox = JCheckBox(Lang.bundle.getString("settings.sprite.solid")).apply {
            isSelected = GlobalValues.isSolid

            addActionListener { GlobalValues.isSolid = this.isSelected }

            gridBagLayout.setConstraints(this,
                GridBagConstraints().apply { gridwidth = GridBagConstraints.REMAINDER }
            )
        }
        this.settings.widgets.add(solidCheckbox!!)
        this.add(solidCheckbox)

        this.add(animationPanel.apply {
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