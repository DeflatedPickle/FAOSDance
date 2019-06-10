package com.deflatedpickle.faosdance.widgets

import com.deflatedpickle.faosdance.component_border.ComponentPanel
import com.deflatedpickle.faosdance.util.Lang
import org.jdesktop.swingx.JXCollapsiblePane
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JPanel

open class CollapsiblePanel(title: String) : ComponentPanel() {
    class Header(title: String, collapsiblePanel: CollapsiblePanel) : JPanel() {
        class ToggledButton(toggledText: String, untoggledText: String, collapsiblePanel: CollapsiblePanel) :
            JButton() {
            var isToggled = false

            init {
                this.text = untoggledText

                addActionListener {
                    isToggled = !isToggled

                    this.text = if (isToggled) {
                        toggledText
                    } else {
                        untoggledText
                    }

                    collapsiblePanel.outerPanel.isCollapsed = !collapsiblePanel.outerPanel.isCollapsed
                }
            }
        }

        val checkbox = JCheckBox(title)
        val button = ToggledButton(
            Lang.bundle.getString("settings.collapse"),
            Lang.bundle.getString("settings.expand"),
            collapsiblePanel
        )

        init {
            this.add(checkbox)
            this.add(button)
        }
    }

    init {
        this.titleComponent = Header(title, this)
    }
}