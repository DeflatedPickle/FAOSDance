package com.deflatedpickle.faosdance.autoupdate

import com.deflatedpickle.faosdance.util.GlobalValues
import com.deflatedpickle.faosdance.util.Lang
import java.awt.*
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.imageio.ImageIO
import javax.swing.*
import javax.swing.border.EmptyBorder

class UpdateDialog : JFrame(Lang.bundle.getString("updater.title")) {
    val progressBar: JProgressBar

    private var resizeAdapter: ComponentAdapter

    init {
        this.iconImage = GlobalValues.icon.image

        this.isAlwaysOnTop = true
        this.isUndecorated = true
        this.background = Color(0, 0, 0, 0)

        this.layout = GridBagLayout()

        this.isAlwaysOnTop = true

        this.size = Dimension(400, 400)

        val constraints = GridBagConstraints()
        constraints.fill = GridBagConstraints.HORIZONTAL
        constraints.anchor = GridBagConstraints.CENTER
        constraints.weightx = 1.0

        val panel = JPanel()
        panel.isOpaque = false
        panel.layout = GridBagLayout()
        val padding = 8
        panel.border = BorderFactory.createCompoundBorder(
            SpeechBubbleBorder(Color.BLACK, 2, 4, 16),
            EmptyBorder(padding, padding, padding, padding)
        )
        this.contentPane.add(panel, constraints)

        val label =
            JTextArea("${Lang.bundle.getString("updater.content")}\n\n${Lang.bundle.getString("updater.secondary_content")}").apply {
                lineWrap = true
                wrapStyleWord = true
                isEditable = false

                font = JLabel().font
            }
        panel.add(label, constraints.apply {
            gridwidth = GridBagConstraints.REMAINDER
        })

        this.add(Box.createVerticalStrut(16), constraints)

        val bottomPanel = JPanel()
        bottomPanel.isOpaque = false
        this.contentPane.add(bottomPanel, constraints)

        bottomPanel.add(
            JLabel(
                ImageIcon(
                    ImageIO.read(ClassLoader.getSystemResource("update-chan.png")).getScaledInstance(
                        220 / 2,
                        320 / 2,
                        Image.SCALE_SMOOTH
                    ), "Update-Chan"
                )
            )
        )

        progressBar = JProgressBar(JProgressBar.VERTICAL, 0, 3)
        bottomPanel.add(progressBar, GridBagConstraints().apply {
            gridwidth = GridBagConstraints.REMAINDER
            fill = GridBagConstraints.VERTICAL
            anchor = GridBagConstraints.WEST
            weighty = 1.0
        })

        resizeAdapter = object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent) {
                panel.minimumSize = Dimension(this@UpdateDialog.size.width, label.preferredSize.height + 32)
            }
        }
        this.addComponentListener(resizeAdapter)

        this.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent) {
                UpdateUtil.updateDialog = null
            }
        })
    }

    override fun setVisible(b: Boolean) {
        UpdateUtil.updateDialog = this
        super.setVisible(b)
    }
}