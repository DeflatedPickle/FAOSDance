package com.deflatedpickle.faosdance.settings.general

import com.deflatedpickle.faosdance.util.GlobalValues
import com.deflatedpickle.faosdance.SpriteSheet
import com.deflatedpickle.faosdance.settings.SettingsDialog
import com.deflatedpickle.faosdance.util.Lang
import java.awt.Frame
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.*
import kotlin.math.roundToInt

class AnimationCategory(owner: Frame, val settings: SettingsDialog) : JPanel() {
    private val gridBagLayout = GridBagLayout()

    var actionCombobox: JComboBox<String>? = null
    var framesPerSecondWidgets: Triple<JComponent, JSlider, JSpinner>? = null
    var playCheckbox: JCheckBox? = null
    var rewindCheckbox: JCheckBox? = null

    init {
        this.layout = gridBagLayout
        this.border = BorderFactory.createTitledBorder(Lang.bundle.getString("settings.animation"))

        // Action label
        this.add(JLabel("${Lang.bundle.getString("settings.sprite.action")}:").also {
            gridBagLayout.setConstraints(it, GridBagConstraints().apply {
                anchor = GridBagConstraints.EAST
            })
        })

        // Action drop-down
        actionCombobox = JComboBox<String>().apply {
            addActionListener {
                GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.setOption("action", (it.source as JComboBox<*>).selectedItem as String)
                GlobalValues.updateScripts("sprite.action.animation", GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.getOption<String>("action")!!)
            }

            gridBagLayout.setConstraints(this, GridBagConstraints().apply {
                gridwidth = GridBagConstraints.REMAINDER
                fill = GridBagConstraints.HORIZONTAL
                weightx = 1.0
            })
        }
        if (GlobalValues.optionsMap.getMap("sprite")!!.getOption<SpriteSheet>("sheet") != null) {
            setActions()
        }
        this.settings.widgets.add(actionCombobox!!)
        this.add(actionCombobox!!)

        // FPS slider
        framesPerSecondWidgets = GlobalValues.addComponentSliderSpinner<Int>(
            this,
            gridBagLayout,
            JLabel("${Lang.bundle.getString("settings.sprite.frames_per_second")}:"),
            GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.getOption<Int>("fps")!!,
            144,
            1
        ).apply {
            third.addChangeListener {
                GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.setOption("fps", when {
                    (it.source as JSpinner).model.value is Int -> (it.source as JSpinner).model.value as Int
                    (it.source as JSpinner).model.value is Double -> ((it.source as JSpinner).model.value as Double).roundToInt()
                    else -> 0
                })
                GlobalValues.timer!!.delay = 1000 / GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.getOption<Int>("fps")!!
                GlobalValues.updateScripts("sprite.animation.fps", GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.getOption<Int>("fps")!!)
            }
        }
        this.settings.widgets.add(framesPerSecondWidgets!!.first)
        this.settings.widgets.add(framesPerSecondWidgets!!.second)
        this.settings.widgets.add(framesPerSecondWidgets!!.third)

        playCheckbox = JCheckBox(Lang.bundle.getString("settings.animation.play")).apply {
            isSelected = true

            addActionListener {
                GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.setOption("play", this.isSelected)
                rewindCheckbox!!.isEnabled = isSelected
                GlobalValues.updateScripts("sprite.animation.play", GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.getOption<Boolean>("play")!!)
            }
        }
        this.settings.widgets.add(playCheckbox!!)

        rewindCheckbox = JCheckBox(Lang.bundle.getString("settings.animation.rewind")).apply {
            addActionListener {
                GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.setOption("rewind", this.isSelected)
                GlobalValues.updateScripts("sprite.animation.rewind", GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.getOption<Boolean>("rewind")!!)
            }
        }
        this.settings.widgets.add(rewindCheckbox!!)

        GlobalValues.animationControls = GlobalValues.addComponentSliderSpinner<Int>(
            this,
            this.layout as GridBagLayout,
            JPanel().apply {
                this.add(playCheckbox)
                this.add(rewindCheckbox)
            },
            GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.getOption<Int>("frame")!!,
            7,
            0
        ).apply {
            third.addChangeListener {
                GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.setOption("frame", when {
                    (it.source as JSpinner).model.value is Int -> (it.source as JSpinner).model.value as Int
                    (it.source as JSpinner).model.value is Double -> ((it.source as JSpinner).model.value as Double).roundToInt()
                    else -> 0
                })
                GlobalValues.updateScripts("sprite.animation.frame", GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.getOption<Int>("frame")!!)

                GlobalValues.frame!!.revalidate()
                GlobalValues.frame!!.repaint()
            }
        }
        this.settings.widgets.add(GlobalValues.animationControls!!.first)
        this.settings.widgets.add(GlobalValues.animationControls!!.second)
        this.settings.widgets.add(GlobalValues.animationControls!!.third)
    }

    fun setActions() {
        if (GlobalValues.sheet != null) {
            actionCombobox!!.model = DefaultComboBoxModel<String>(GlobalValues.sheet!!.spriteMap.keys.toTypedArray())
            actionCombobox!!.selectedIndex = GlobalValues.sheet!!.spriteMap.keys.indexOf(
                GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.getOption<String>("action")!!)

            for (i in this.settings.widgets) {
                i.isEnabled = true
            }
        }
    }
}