package com.deflatedpickle.faosdance.gui.settings.general

import com.deflatedpickle.faosdance.util.GlobalValues
import com.deflatedpickle.faosdance.HookPoint
import com.deflatedpickle.faosdance.gui.settings.SettingsDialog
import com.deflatedpickle.faosdance.util.Lang
import java.awt.Frame
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.*
import kotlin.math.roundToInt

class LocationCategory(owner: Frame, val settings: SettingsDialog) : JPanel() {
    private val gridBagLayout = GridBagLayout()

    var hookPointComboBox: JComboBox<String>? = null
    var xLocationWidgets: Triple<JComponent, JSlider, JSpinner>? = null
    var yLocationWidgets: Triple<JComponent, JSlider, JSpinner>? = null

    init {
        this.layout = gridBagLayout
        this.border = BorderFactory.createTitledBorder(Lang.bundle.getString("settings.location"))

        xLocationWidgets = GlobalValues.addComponentSliderSpinner<Int>(
            this,
            this.layout as GridBagLayout,
            JLabel("${Lang.bundle.getString("settings.location.x")}:"),
            GlobalValues.optionsMap.getMap("window")!!.getMap("location")!!.getOption<Int>("x")!!,
            GlobalValues.effectiveSize!!.width,
            0.0
        ).apply {
            third.addChangeListener {
                hookPointComboBox!!.selectedIndex = HookPoint.values().size - 1
                when {
                    (it.source as JSpinner).model.value is Int -> {
                        GlobalValues.frame!!.setLocation(
                            (it.source as JSpinner).model.value as Int,
                            GlobalValues.frame!!.y
                        )
                        GlobalValues.optionsMap.getMap("window")!!.getMap("location")!!.setOption("x", (it.source as JSpinner).model.value as Int)
                    }
                    (it.source as JSpinner).model.value is Double -> {
                        GlobalValues.frame!!.setLocation(
                            ((it.source as JSpinner).model.value as Double).roundToInt(),
                            GlobalValues.frame!!.y
                        )
                        GlobalValues.optionsMap.getMap("window")!!.getMap("location")!!.setOption("x", ((it.source as JSpinner).model.value as Double).roundToInt())
                    }
                }
                GlobalValues.updateScripts("window.location.x", GlobalValues.optionsMap.getMap("window")!!.getMap("location")!!.getOption<Int>("x")!!)
            }
        }
        this.settings.widgets.add(xLocationWidgets!!.first)
        this.settings.widgets.add(xLocationWidgets!!.second)
        this.settings.widgets.add(xLocationWidgets!!.third)
        val xEntry = xLocationWidgets!!.third

        yLocationWidgets = GlobalValues.addComponentSliderSpinner<Int>(
            this,
            this.layout as GridBagLayout,
            JLabel("${Lang.bundle.getString("settings.location.y")}:"),
            GlobalValues.optionsMap.getMap("window")!!.getMap("location")!!.getOption<Int>("y")!!,
            GlobalValues.effectiveSize!!.height,
            0.0
        ).apply {
            third.addChangeListener {
                hookPointComboBox!!.selectedIndex = HookPoint.values().size - 1
                when {
                    (it.source as JSpinner).model.value is Int -> {
                        GlobalValues.frame!!.setLocation(
                            GlobalValues.frame!!.x,
                            (it.source as JSpinner).model.value as Int
                        )
                        GlobalValues.optionsMap.getMap("window")!!.getMap("location")!!.setOption("y", (it.source as JSpinner).model.value as Int)
                    }
                    (it.source as JSpinner).model.value is Double -> {
                        GlobalValues.frame!!.setLocation(
                            GlobalValues.frame!!.x,
                            ((it.source as JSpinner).model.value as Double).roundToInt()
                        )
                        GlobalValues.optionsMap.getMap("window")!!.getMap("location")!!.setOption("y", ((it.source as JSpinner).model.value as Double).roundToInt())
                    }
                }
                GlobalValues.updateScripts("window.location.y", GlobalValues.optionsMap.getMap("window")!!.getMap("location")!!.getOption<Int>("y")!!)
            }
        }
        this.settings.widgets.add(yLocationWidgets!!.first)
        this.settings.widgets.add(yLocationWidgets!!.second)
        this.settings.widgets.add(yLocationWidgets!!.third)
        val yEntry = yLocationWidgets!!.third

        hookPointComboBox = JComboBox<String>(GlobalValues.enumToReadableNames(HookPoint::class.java)).apply {
            selectedIndex = HookPoint.values().size - 1
            addActionListener {
                when (HookPoint.valueOf(
                    ((it.source as JComboBox<*>).selectedItem as String).toUpperCase().replace(
                        " ",
                        "_"
                    )
                )) {
                    HookPoint.TOP_LEFT -> {
                        val selected = this.selectedIndex
                        xEntry.value = 0.0
                        yEntry.value = 0.0
                        this.selectedIndex = selected
                    }
                    HookPoint.TOP_CENTRE -> {
                        val selected = this.selectedIndex
                        xEntry.value = GlobalValues.effectiveSize!!.width / 2.0
                        yEntry.value = 0.0
                        this.selectedIndex = selected
                    }
                    HookPoint.TOP_RIGHT -> {
                        val selected = this.selectedIndex
                        xEntry.value = GlobalValues.effectiveSize!!.width.toDouble()
                        yEntry.value = 0.0
                        this.selectedIndex = selected
                    }
                    HookPoint.MIDDLE_LEFT -> {
                        val selected = this.selectedIndex
                        xEntry.value = 0.0
                        yEntry.value = GlobalValues.effectiveSize!!.height / 2.0
                        this.selectedIndex = selected
                    }
                    HookPoint.MIDDLE_CENTRE -> {
                        val selected = this.selectedIndex
                        xEntry.value = GlobalValues.effectiveSize!!.width / 2.0
                        yEntry.value = GlobalValues.effectiveSize!!.height / 2.0
                        this.selectedIndex = selected
                    }
                    HookPoint.MIDDLE_RIGHT -> {
                        val selected = this.selectedIndex
                        xEntry.value = GlobalValues.effectiveSize!!.width.toDouble()
                        yEntry.value = GlobalValues.effectiveSize!!.height / 2.0
                        this.selectedIndex = selected
                    }
                    HookPoint.BOTTOM_LEFT -> {
                        val selected = this.selectedIndex
                        xEntry.value = 0.0
                        yEntry.value = GlobalValues.effectiveSize!!.height.toDouble()
                        this.selectedIndex = selected
                    }
                    HookPoint.BOTTOM_CENTRE -> {
                        val selected = this.selectedIndex
                        xEntry.value = GlobalValues.effectiveSize!!.width / 2.0
                        yEntry.value = GlobalValues.effectiveSize!!.height.toDouble()
                        this.selectedIndex = selected
                    }
                    HookPoint.BOTTOM_RIGHT -> {
                        val selected = this.selectedIndex
                        xEntry.value = GlobalValues.effectiveSize!!.width.toDouble()
                        yEntry.value = GlobalValues.effectiveSize!!.height.toDouble()
                        this.selectedIndex = selected
                    }
                    HookPoint.CUSTOM -> {
                    }
                }
            }
        }
        (this.layout as GridBagLayout).setConstraints(hookPointComboBox, GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            weightx = 1.0
            gridwidth = GridBagConstraints.REMAINDER
        })
        this.settings.widgets.add(hookPointComboBox!!)
        this.add(hookPointComboBox)
    }
}