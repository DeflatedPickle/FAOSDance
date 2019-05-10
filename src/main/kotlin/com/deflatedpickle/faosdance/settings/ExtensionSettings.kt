package com.deflatedpickle.faosdance.settings

import com.deflatedpickle.faosdance.GlobalValues
import com.deflatedpickle.faosdance.SpriteSheet
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

class ExtensionSettings(owner: Frame, val settings: SettingsDialog) : JPanel() {
    private val gridBagLayout = GridBagLayout()

    init {
        this.layout = gridBagLayout
    }
}