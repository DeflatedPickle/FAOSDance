package com.deflatedpickle.faosdance.widgets

import com.deflatedpickle.faosdance.component_border.ComponentPanel
import com.deflatedpickle.faosdance.util.Lang
import org.jdesktop.swingx.JXCollapsiblePane
import java.awt.Dimension
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JPanel
import javax.swing.UIManager

open class CollapsiblePanel(title: String) : ComponentPanel() {
    class Header(title: String, collapsiblePanel: CollapsiblePanel) : JPanel() {
        class ToggledButton(collapsiblePanel: CollapsiblePanel) :
            JButton() {
            var isToggled = false

            init {
                val buttonSize = 24
                this.preferredSize = Dimension(buttonSize, buttonSize)
                this.icon = UIManager.getIcon("Tree.collapsedIcon")

                addActionListener {
                    if (isToggled) {
                        this.icon = UIManager.getIcon("Tree.collapsedIcon")
                    } else {
                        this.icon = UIManager.getIcon("Tree.expandedIcon")
                    }

                    collapsiblePanel.panel.isCollapsed = !collapsiblePanel.panel.isCollapsed
                    isToggled = !isToggled
                }
            }
        }

        val checkbox = JCheckBox(title)
        val button = ToggledButton(collapsiblePanel)

        init {
            this.add(button)
            this.add(checkbox)
        }
    }

    init {
        this.titleComponent = Header(title, this)
    }
}