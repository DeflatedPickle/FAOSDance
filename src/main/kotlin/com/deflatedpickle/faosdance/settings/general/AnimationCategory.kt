package com.deflatedpickle.faosdance.settings.general

import com.deflatedpickle.faosdance.GlobalValues
import com.deflatedpickle.faosdance.SpriteSheet
import com.deflatedpickle.faosdance.settings.SettingsDialog
import com.deflatedpickle.faosdance.util.Lang
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
import kotlin.math.roundToInt

class AnimationCategory(owner: Frame, val settings: SettingsDialog) : JPanel() {
    companion object {
        fun loadSpriteSheet(path: String) {
            val tempSheet = SpriteSheet(path.substringBeforeLast("."))

            if (tempSheet.loadedImage && tempSheet.loadedText) {
                GlobalValues.configureSpriteSheet(tempSheet)
                GlobalValues.currentPath = path
            }

            for (i in GlobalValues.extensionCheckBoxList!!) {
                i.isEnabled = true
            }
        }
    }

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
            addActionListener { GlobalValues.currentAction = (it.source as JComboBox<*>).selectedItem as String }

            gridBagLayout.setConstraints(this, GridBagConstraints().apply {
                fill = GridBagConstraints.HORIZONTAL
                weightx = 1.0
            })
        }
        if (GlobalValues.sheet != null) {
            setActions()
        }
        this.settings.widgets.add(actionCombobox!!)
        this.add(actionCombobox!!)

        // Open button
        this.add(JButton(Lang.bundle.getString("settings.sprite.open")).apply {
            dropTarget = object : DropTarget() {
                override fun drop(dtde: DropTargetDropEvent) {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY)
                    val droppedFiles = dtde.transferable.getTransferData(DataFlavor.javaFileListFlavor) as List<File>

                    if (droppedFiles.size == 1) {
                        loadSpriteSheet(droppedFiles[0].absolutePath)
                        setActions()
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
                    setActions()
                }
            }

            gridBagLayout.setConstraints(this, GridBagConstraints().apply {
                anchor = GridBagConstraints.EAST
                gridwidth = GridBagConstraints.REMAINDER
            })
        })

        // FPS slider
        framesPerSecondWidgets = GlobalValues.addComponentSliderSpinner<Int>(
            this,
            gridBagLayout,
            JLabel("${Lang.bundle.getString("settings.sprite.frames_per_second")}:"),
            GlobalValues.fps,
            144,
            1
        ).apply {
            third.addChangeListener {
                GlobalValues.fps = when {
                    (it.source as JSpinner).model.value is Int -> (it.source as JSpinner).model.value as Int
                    (it.source as JSpinner).model.value is Double -> ((it.source as JSpinner).model.value as Double).roundToInt()
                    else -> 0
                }
                GlobalValues.timer!!.delay = 1000 / GlobalValues.fps
            }
        }
        this.settings.widgets.add(framesPerSecondWidgets!!.first)
        this.settings.widgets.add(framesPerSecondWidgets!!.second)
        this.settings.widgets.add(framesPerSecondWidgets!!.third)

        playCheckbox = JCheckBox(Lang.bundle.getString("settings.animation.play")).apply {
            isSelected = true

            addActionListener {
                GlobalValues.play = this.isSelected
                rewindCheckbox!!.isEnabled = isSelected
            }
        }
        this.settings.widgets.add(playCheckbox!!)

        rewindCheckbox = JCheckBox(Lang.bundle.getString("settings.animation.rewind")).apply {
            addActionListener {
                GlobalValues.rewind = this.isSelected
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
            GlobalValues.animFrame,
            7,
            0
        ).apply {
            third.addChangeListener {
                GlobalValues.animFrame = when {
                    (it.source as JSpinner).model.value is Int -> (it.source as JSpinner).model.value as Int
                    (it.source as JSpinner).model.value is Double -> ((it.source as JSpinner).model.value as Double).roundToInt()
                    else -> 0
                }

                GlobalValues.frame!!.revalidate()
                GlobalValues.frame!!.repaint()
            }
        }
        this.settings.widgets.add(GlobalValues.animationControls!!.first)
        this.settings.widgets.add(GlobalValues.animationControls!!.second)
        this.settings.widgets.add(GlobalValues.animationControls!!.third)
    }

    fun setActions() {
        actionCombobox!!.model = DefaultComboBoxModel<String>(GlobalValues.sheet!!.spriteMap.keys.toTypedArray())
        actionCombobox!!.selectedIndex = GlobalValues.sheet!!.spriteMap.keys.indexOf(GlobalValues.currentAction)

        for (i in this.settings.widgets) {
            i.isEnabled = true
        }
    }
}