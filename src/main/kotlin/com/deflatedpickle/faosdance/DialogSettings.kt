package com.deflatedpickle.faosdance

import java.awt.Dimension
import java.awt.Frame
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter

class DialogSettings(owner: Frame) : JDialog(owner, "FAOSDance Settings", true) {
    val gridBagLayout = GridBagLayout()

    init {
        this.isResizable = false

        createWidgets()

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
                    GlobalValues.sheet = SpriteSheet(fileChooser.selectedFile.absolutePath.substringBeforeLast("."))
                    GlobalValues.currentAction = GlobalValues.sheet!!.spriteMap.keys.first()
                    resize()

                    createWidgets()
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
            this.add(JPanel().apply {
                this.border = BorderFactory.createTitledBorder("Size")
                this.layout = GridBagLayout()

                addLabelSliderSpinner(this, this.layout as GridBagLayout, "Width:", GlobalValues.xMultiplier, 5.0, 0.1).third.addChangeListener { GlobalValues.xMultiplier = (it.source as JSpinner).model.value as Double }

                addLabelSliderSpinner(this, this.layout as GridBagLayout, "Height:", GlobalValues.yMultiplier, 5.0, 0.1).third.addChangeListener { GlobalValues.yMultiplier = (it.source as JSpinner).model.value as Double }

                gridBagLayout.setConstraints(this, GridBagConstraints().apply {
                    fill = GridBagConstraints.BOTH
                    weightx = 1.0
                    gridwidth = GridBagConstraints.REMAINDER
                })
            })

            this.add(JPanel().apply {
                this.border = BorderFactory.createTitledBorder("Reflection")
                this.layout = GridBagLayout()

                addLabelSliderSpinner(this, this.layout as GridBagLayout, "Padding:", GlobalValues.reflectionPadding, 50.0, 0.0).third.addChangeListener { GlobalValues.reflectionPadding = (it.source as JSpinner).model.value as Double }

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

    private fun addLabelSliderSpinner(parent: JComponent, gridBagLayout: GridBagLayout, text: String, defaultValue: Number, maxNumber: Number, minNumber: Number): Triple<JLabel, JSlider, JSpinner> {
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


    fun resize() {
        val width = ((((GlobalValues.sheet!!.spriteWidth * GlobalValues.xMultiplier) * 2) * 100) / 100).toInt()
        val height = ((((GlobalValues.sheet!!.spriteHeight * GlobalValues.yMultiplier) * 2) * 100) / 100).toInt()

        owner.minimumSize = Dimension(width, height)
        owner.setSize(width, height)
    }
}