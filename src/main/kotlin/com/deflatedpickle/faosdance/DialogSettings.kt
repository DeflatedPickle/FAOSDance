package com.deflatedpickle.faosdance

import java.awt.*
import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.math.roundToInt

class DialogSettings(owner: Frame) : JDialog(owner, "FAOSDance Settings", true) {
    val gridBagLayout = GridBagLayout()

    init {
        this.isResizable = false

        createWidgets()
        this.size = Dimension(400, 320)

        this.layout = gridBagLayout
    }

    fun createWidgets() {
        contentPane.removeAll()

        if (GlobalValues.sheet != null) {
            this.add(JLabel("Action:"))

            this.add(JComboBox<String>(GlobalValues.sheet!!.spriteMap.keys.toTypedArray()).apply {
                selectedIndex = GlobalValues.sheet!!.spriteMap.keys.indexOf(selectedItem as String)
                addActionListener { GlobalValues.currentAction = (it.source as JComboBox<*>).selectedItem as String }

                gridBagLayout.setConstraints(this, GridBagConstraints().apply {
                    fill = GridBagConstraints.HORIZONTAL
                    weightx = 1.0
                })
            })
        }

        this.add(JButton("Open").apply {
            addActionListener {
                val fileChooser = JFileChooser(GlobalValues.currentPath)
                fileChooser.addChoosableFileFilter(FileNameExtensionFilter("PNG (*.png)", "png"))
                fileChooser.addChoosableFileFilter(FileNameExtensionFilter("JPEG (*.jpg; *.jpeg)", "jpg", "jpeg"))
                val returnValue = fileChooser.showOpenDialog(owner)

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    val tempSheet = SpriteSheet(fileChooser.selectedFile.absolutePath.substringBeforeLast("."))

                    if (tempSheet.loadedImage && tempSheet.loadedText) {
                        GlobalValues.configureSpriteSheet(tempSheet)
                        GlobalValues.currentPath = fileChooser.selectedFile.parentFile.absolutePath

                        createWidgets()
                    }
                }
            }

            gridBagLayout.setConstraints(this, GridBagConstraints().apply {
                if (GlobalValues.sheet == null) {
                    fill = GridBagConstraints.BOTH
                    weightx = 1.0
                    weighty = 1.0
                }

                gridwidth = GridBagConstraints.REMAINDER
            })
        })

        if (GlobalValues.sheet != null) {
            addLabelSliderSpinner(this, gridBagLayout, "Frames Per Second:", GlobalValues.fps, 144.0, 1.0).third.addChangeListener {
                GlobalValues.fps = ((it.source as JSpinner).model.value as Double).roundToInt()
                GlobalValues.timer!!.delay = 1000 / GlobalValues.fps
            }

            this.add(JCheckBox("Visible").apply {
                isSelected = GlobalValues.isVisible

                addActionListener { GlobalValues.isVisible = this.isSelected }
            })
            this.add(JCheckBox("Solid").apply {
                isSelected = GlobalValues.isSolid

                addActionListener { GlobalValues.isSolid = this.isSelected }
            })
            this.add(JCheckBox("Always On Top").apply {
                isSelected = GlobalValues.isTopLevel

                addActionListener {
                    GlobalValues.isTopLevel = this.isSelected
                    GlobalValues.frame!!.isAlwaysOnTop = GlobalValues.isTopLevel
                }

                gridBagLayout.setConstraints(this, GridBagConstraints().apply { gridwidth = GridBagConstraints.REMAINDER })
            })

            this.add(JPanel().apply {
                this.border = BorderFactory.createTitledBorder("Size")
                this.layout = GridBagLayout()

                addLabelSliderSpinner(this, this.layout as GridBagLayout, "Width:", GlobalValues.xMultiplier, GlobalValues.maxSize, 0.1).third.addChangeListener {
                    GlobalValues.xMultiplier = (it.source as JSpinner).model.value as Double
                    GlobalValues.resize()
                }

                addLabelSliderSpinner(this, this.layout as GridBagLayout, "Height:", GlobalValues.yMultiplier, GlobalValues.maxSize, 0.1).third.addChangeListener {
                    GlobalValues.yMultiplier = (it.source as JSpinner).model.value as Double
                    GlobalValues.resize()
                }

                gridBagLayout.setConstraints(this, GridBagConstraints().apply {
                    fill = GridBagConstraints.BOTH
                    weightx = 1.0
                    gridwidth = GridBagConstraints.REMAINDER
                })
            })

            this.add(JPanel().apply {
                this.border = BorderFactory.createTitledBorder("Reflection")
                this.layout = GridBagLayout()

                addLabelSliderSpinner(this, this.layout as GridBagLayout, "Padding:", GlobalValues.reflectionPadding, 100.0, -100.0).third.addChangeListener { GlobalValues.reflectionPadding = (it.source as JSpinner).model.value as Double }

                this.add(JPanel().apply {
                    this.border = BorderFactory.createTitledBorder("Fade")
                    this.layout = GridBagLayout()

                    addLabelSliderSpinner(this, this.layout as GridBagLayout, "Height:", GlobalValues.fadeHeight, 0.9, 0.1).third.addChangeListener { GlobalValues.fadeHeight = ((it.source as JSpinner).model.value as Double).toFloat() }

                    addLabelSliderSpinner(this, this.layout as GridBagLayout, "Opacity:", GlobalValues.fadeOpacity, 0.9, 0.1).third.addChangeListener { GlobalValues.fadeOpacity = ((it.source as JSpinner).model.value as Double).toFloat() }
                }.also {
                    (this.layout as GridBagLayout).setConstraints(it, GridBagConstraints().apply {
                        fill = GridBagConstraints.BOTH
                        weightx = 1.0
                        gridwidth = GridBagConstraints.REMAINDER
                    })
                })

                gridBagLayout.setConstraints(this, GridBagConstraints().apply {
                    fill = GridBagConstraints.BOTH
                    weightx = 1.0
                    gridwidth = GridBagConstraints.REMAINDER
                })
            })
        }

        revalidate()
        repaint()
    }

    private fun addLabelSliderSpinner(parent: Container, gridBagLayout: GridBagLayout, text: String, defaultValue: Number, maxNumber: Number, minNumber: Number): Triple<JLabel, JSlider, JSpinner> {
        val label = JLabel(text)
        val slider = JSlider((minNumber.toFloat() * 100).toInt(), (maxNumber.toFloat() * 100).toInt(), (defaultValue.toFloat() * 100).toInt())
        val spinner = JSpinner(SpinnerNumberModel(defaultValue.toDouble(), minNumber.toDouble(), maxNumber.toDouble(), 0.01))

        slider.value = (defaultValue.toFloat() * 100).toInt()
        spinner.value = defaultValue

        slider.addChangeListener { spinner.value = slider.value.toDouble() / 100 }
        spinner.addChangeListener { slider.value = (spinner.value as Double * 100).toInt() }

        parent.add(label)
        parent.add(slider)
        parent.add(spinner)

        gridBagLayout.setConstraints(slider, GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            weightx = 1.0
        })

        gridBagLayout.setConstraints(spinner, GridBagConstraints().apply {
            gridwidth = GridBagConstraints.REMAINDER
        })

        return Triple(label, slider, spinner)
    }
}