package com.deflatedpickle.faosdance.settings.general

import com.deflatedpickle.faosdance.GlobalValues
import com.deflatedpickle.faosdance.ScalingType
import com.deflatedpickle.faosdance.SpriteSheet
import com.deflatedpickle.faosdance.component_border.ComponentPanel
import com.deflatedpickle.faosdance.settings.SettingsDialog
import com.deflatedpickle.faosdance.util.Lang
import java.awt.FlowLayout
import java.awt.Frame
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.datatransfer.DataFlavor
import java.awt.dnd.DnDConstants
import java.awt.dnd.DropTarget
import java.awt.dnd.DropTargetDropEvent
import java.io.File
import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter

class SpriteCategory(owner: Frame, val settings: SettingsDialog) :
    ComponentPanel(JCheckBox(Lang.bundle.getString("settings.sprite"))) {
    companion object {
        fun loadSpriteSheet(path: String) {
            val tempSheet = SpriteSheet(path.substringBeforeLast("."))

            if (tempSheet.loadedImage && tempSheet.loadedText) {
                GlobalValues.configureSpriteSheet(tempSheet)
                GlobalValues.currentPath = path
            }

            for (i in GlobalValues.widgetList!!) {
                i.isEnabled = true
            }
        }
    }

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
        (this.titleComponent as JCheckBox).apply {
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

        // Open
        this.panel.add(JButton(Lang.bundle.getString("settings.sprite.open")).apply {
            dropTarget = object : DropTarget() {
                override fun drop(dtde: DropTargetDropEvent) {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY)
                    val droppedFiles = dtde.transferable.getTransferData(DataFlavor.javaFileListFlavor) as List<File>

                    if (droppedFiles.size == 1) {
                        loadSpriteSheet(droppedFiles[0].absolutePath)
                        settings.generalSettings.spriteCategory.animationCategory.setActions()
                    }
                }
            }

            addActionListener {
                val fileChooser = JFileChooser(GlobalValues.currentPath)
                fileChooser.addChoosableFileFilter(
                    FileNameExtensionFilter("PNG (*.png)", "png").also { fileChooser.fileFilter = it }
                )
                fileChooser.addChoosableFileFilter(
                    FileNameExtensionFilter("JPEG (*.jpg; *.jpeg)", "jpg", "jpeg")
                )
                val returnValue = fileChooser.showOpenDialog(owner)

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    loadSpriteSheet(fileChooser.selectedFile.absolutePath)
                    settings.generalSettings.spriteCategory.animationCategory.setActions()

                    GlobalValues.updateScripts("sprite.sheet", GlobalValues.optionsMap.getMap("sprite")!!.getOption<SpriteSheet>("sheet")!!)
                }
            }

            gridBagLayout.setConstraints(this, GridBagConstraints().apply {
                gridwidth = GridBagConstraints.REMAINDER
                fill = GridBagConstraints.HORIZONTAL
                weightx = 1.0
            })
        })

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