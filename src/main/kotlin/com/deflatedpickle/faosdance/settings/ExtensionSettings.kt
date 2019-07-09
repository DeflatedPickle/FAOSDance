package com.deflatedpickle.faosdance.settings

import com.deflatedpickle.faosdance.*
import com.deflatedpickle.faosdance.component_border.ComponentPanel
import com.deflatedpickle.faosdance.util.GlobalValues
import com.deflatedpickle.faosdance.util.Icons
import com.deflatedpickle.faosdance.util.Lang
import org.jruby.RubyObject
import org.jruby.RubyString
import java.awt.*
import java.util.*
import javax.swing.*

class ExtensionSettings(owner: Frame, val settings: SettingsDialog) : JPanel() {
    val extensionTabbedPane = JTabbedPane(JTabbedPane.LEFT)

    val reloadExtensionButton = JButton(Lang.bundle.getString("settings.extensions.reload_all")).apply {
        addActionListener {
            GlobalValues.loadScripts()
            JOptionPane.showMessageDialog(
                GlobalValues.frame,
                Lang.bundle.getString("settings.extensions.reload_all.message"),
                GlobalValues.frame!!.title,
                JOptionPane.INFORMATION_MESSAGE
            )
        }
    }

    companion object {
        val extensionList = mutableListOf<RubyObject>()
    }

    init {
        this.layout = BoxLayout(this, BoxLayout.Y_AXIS)

        GlobalValues.widgetList = mutableListOf()

        var longestName = 0
        for (i in extensionList) {
            val name = (i.getInstanceVariable("@name") as RubyString).asJavaString()
            if (name.length - 1 > longestName) {
                longestName = name.length - 1
            }
        }

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
            mainPanel.add(JLabel("By $author").apply {
                this.font = this.font.deriveFont(10f)
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
            tabPanel.layout = GridBagLayout()
            tabPanel.add(JLabel(name).apply {
                val label = this
                this.isOpaque = false
                this.preferredSize = this.preferredSize.apply {
                    this.width = longestName * ((label.font.size * 100 / 1.5f) / 100).toInt()
                }
            }, GridBagConstraints().apply {
                this.anchor = GridBagConstraints.WEST
                this.fill = GridBagConstraints.HORIZONTAL
            })
            tabPanel.add(JCheckBox().apply {
                GlobalValues.widgetList!!.add(this)

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
                } else {
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
                        GlobalValues.optionsMap.getMap("extensions")!!.getOption<MutableList<String>>("enabled")!!.add(
                            name
                        )
                    } else {
                        RubyThread.rubyContainer.callMethod(i, "disable")
                        GlobalValues.optionsMap.getMap("extensions")!!.getOption<MutableList<String>>("enabled")!!.remove(
                            name
                        )
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
            }, GridBagConstraints())
            tabPanel.add(MovementPanel(extensionTabbedPane, mainPanel))
            extensionTabbedPane.setTabComponentAt(extensionTabbedPane.tabCount - 1, tabPanel)
            GlobalValues.extensionPanelMap[name] = subPanel
        }

        this.add(extensionTabbedPane)

        this.settings.widgets.add(reloadExtensionButton)
        this.add(reloadExtensionButton)
    }

    class MovementPanel(val tabbedPanel: JTabbedPane, panel: JPanel) : JPanel() {
        val upButton: JButton
        val downButton: JButton

        val buttonWidth = 20

        init {
            this.isOpaque = false

            upButton = JButton(Icons.upArrow).apply {
                preferredSize = Dimension(buttonWidth, preferredSize.height)
                isOpaque = false

                addActionListener {
                    val index = tabbedPanel.indexOfComponent(panel)
                    moveTab(index, index - 1)
                }
            }
            this.add(upButton)
            GlobalValues.tabButtonList.add(upButton)

            downButton = JButton(Icons.downArrow).apply {
                preferredSize = Dimension(buttonWidth, preferredSize.height)
                isOpaque = false

                addActionListener {
                    val index = tabbedPanel.indexOfComponent(panel)
                    moveTab(index, index + 1)
                }
            }
            this.add(downButton)
            GlobalValues.tabButtonList.add(downButton)

            if (GlobalValues.sheet == null) {
                upButton.isEnabled = false
                downButton.isEnabled = false
            }
        }

        fun moveTab(tabIndex: Int, tabDestination: Int) {
            val title = tabbedPanel.getTitleAt(tabIndex)
            val icon = tabbedPanel.getIconAt(tabIndex)
            val component = tabbedPanel.getComponentAt(tabIndex)
            val tabComponent = tabbedPanel.getTabComponentAt(tabIndex)

            tabbedPanel.remove(tabIndex)

            tabbedPanel.insertTab(title, icon, component, "", tabDestination)
            tabbedPanel.setTabComponentAt(tabDestination, tabComponent)

            // Enable/Disable the moving tab
            for (i in (tabComponent as JPanel).components) {
                if (i is MovementPanel) {
                    i.upButton.isEnabled = tabDestination > 0
                    i.downButton.isEnabled = tabDestination < ExtensionSettings.extensionList.size - 1
                }
            }

            // Enable/Disable the other tab
            for (i in (tabbedPanel.getTabComponentAt(tabIndex) as JPanel).components) {
                if (i is MovementPanel) {
                    i.upButton.isEnabled = tabIndex > 0
                    i.downButton.isEnabled = tabIndex < ExtensionSettings.extensionList.size - 1
                }
            }

            Collections.swap(RubyThread.extensions, tabIndex, tabDestination)
        }
    }
}