package com.deflatedpickle.faosdance.gui

import com.deflatedpickle.faosdance.util.GlobalValues
import com.deflatedpickle.faosdance.gui.settings.SettingsDialog
import com.deflatedpickle.faosdance.util.Lang
import java.awt.event.WindowEvent
import javax.swing.JComponent
import javax.swing.JMenuItem
import javax.swing.JPopupMenu
import javax.swing.JSeparator

class ContextMenu : JPopupMenu() {
    companion object {
        val trayItems = mutableListOf<JComponent>()
    }

    init {
        JMenuItem(Lang.bundle.getString("menu.move_to_centre")).apply {
            this@ContextMenu.add(this)
            trayItems.add(this)

            addActionListener {
                GlobalValues.frame!!.setLocationRelativeTo(null)
            }
        }

        JSeparator().apply {
            this@ContextMenu.add(this)
            trayItems.add(this)
        }

        JMenuItem(Lang.bundle.getString("menu.settings")).apply {
            this@ContextMenu.add(this)
            trayItems.add(this)

            addActionListener {
                val dialog = SettingsDialog(GlobalValues.frame!!)
                // dialog.triggerWidgets()
                dialog.setLocationRelativeTo(GlobalValues.frame!!)

                dialog.isVisible = true
            }
        }

        JSeparator().apply {
            this@ContextMenu.add(this)
            trayItems.add(this)
        }

        JMenuItem(Lang.bundle.getString("menu.exit")).apply {
            this@ContextMenu.add(this)
            trayItems.add(this)

            addActionListener {
                GlobalValues.frame!!.dispatchEvent(WindowEvent(GlobalValues.frame!!, WindowEvent.WINDOW_CLOSING))
            }
        }
    }
}