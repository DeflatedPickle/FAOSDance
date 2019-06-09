package com.deflatedpickle.faosdance.settings

import com.deflatedpickle.faosdance.GlobalValues
import com.deflatedpickle.faosdance.RubyThread
import com.deflatedpickle.faosdance.SpriteSheet
import com.deflatedpickle.faosdance.component_border.ComponentBorder
import com.deflatedpickle.faosdance.component_border.ComponentPanel
import org.jruby.RubyObject
import org.jruby.RubyString
import java.awt.*
import javax.swing.*

class ExtensionSettings(owner: Frame, val settings: SettingsDialog) : JPanel() {
    val extensionTabbedPane = JTabbedPane(JTabbedPane.LEFT)

    companion object {
        val extensionList = mutableListOf<RubyObject>()
    }

    init {
        this.layout = BoxLayout(this, BoxLayout.Y_AXIS)

        GlobalValues.extensionCheckBoxList = mutableListOf()

        for (i in extensionList) {
            val name = (i.getInstanceVariable("@name") as RubyString).asJavaString()
            val description = (i.getInstanceVariable("@description") as RubyString).asJavaString()
            val author = (i.getInstanceVariable("@author") as RubyString).asJavaString()

            val mainPanel = JPanel()
            mainPanel.layout = BoxLayout(mainPanel, BoxLayout.Y_AXIS)

            mainPanel.add(JLabel(name).apply {
                this.font = this.font.deriveFont(36f)
                this.alignmentX = Component.CENTER_ALIGNMENT
            })
            mainPanel.add(JLabel(description).apply {
                this.font = this.font.deriveFont(14f)
                this.alignmentX = Component.CENTER_ALIGNMENT
            })

            val subPanel = JPanel()
            subPanel.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
            subPanel.layout = GridBagLayout()
            RubyThread.rubyContainer.callMethod(i, "settings", subPanel)
            subPanel.add(Box.createVerticalGlue(), GridBagConstraints().apply {
                this.anchor = GridBagConstraints.NORTH
                this.fill = GridBagConstraints.VERTICAL
                this.weighty = 1.0
            })
            mainPanel.add(JScrollPane(subPanel).apply { border = BorderFactory.createEmptyBorder() })

            extensionTabbedPane.addTab(null, mainPanel)

            val tabPanel = JPanel()
            tabPanel.isOpaque = false
            tabPanel.add(JLabel(name).apply { isOpaque = false }, BorderLayout.WEST)
            tabPanel.add(JCheckBox().apply {
                GlobalValues.extensionCheckBoxList!!.add(this)
                this.isEnabled = false

                if (GlobalValues.optionsMap.getMap("extensions")!!.getOption<List<String>>("enabled")!!.contains(name)) {
                    this.isSelected = true

                    for (c in subPanel.components) {
                        c.isEnabled = true

                        if (c is ComponentPanel) {
                            c.titleComponent.isEnabled = true
                            for (ch in c.panel.components) {
                                ch.isEnabled = false
                            }
                        }
                    }
                }
                else {
                    if (GlobalValues.optionsMap.getMap("sprite")!!.getOption<SpriteSheet>("sheet") == null) {
                        this.isEnabled = false

                        for (c in subPanel.components) {
                            c.isEnabled = false

                            if (c is ComponentPanel) {
                                c.titleComponent.isEnabled = false
                                for (ch in c.panel.components) {
                                    ch.isEnabled = false
                                }
                            }
                        }
                    }
                }

                isOpaque = false
                addActionListener {
                    if (this.isSelected) {
                        RubyThread.rubyContainer.callMethod(i, "enable")
                        GlobalValues.optionsMap.getMap("extensions")!!.getOption<MutableList<String>>("enabled")!!.add(name)
                    }
                    else {
                        RubyThread.rubyContainer.callMethod(i, "disable")
                        GlobalValues.optionsMap.getMap("extensions")!!.getOption<MutableList<String>>("enabled")!!.remove(name)
                    }

                    for (c in subPanel.components) {
                        c.isEnabled = this.isSelected
                    }

                    for (c in subPanel.components) {
                        c.isEnabled = this.isSelected

                        if (c is ComponentPanel) {
                            c.titleComponent.isEnabled = this.isSelected
                        }
                    }

                    i.setInstanceVariable("@enabled", RubyThread.ruby.newBoolean(this.isSelected))
                }
            }, BorderLayout.EAST)
            extensionTabbedPane.setTabComponentAt(extensionTabbedPane.tabCount - 1, tabPanel)
            GlobalValues.extensionPanelMap[name] = subPanel
        }

        this.add(extensionTabbedPane)
    }
}