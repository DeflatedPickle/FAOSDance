package com.deflatedpickle.faosdance

import java.awt.*
import java.awt.event.ActionListener
import java.awt.Color
import java.awt.GradientPaint
import java.awt.AlphaComposite
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.image.BufferedImage
import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter


@Suppress("KDocMissingDocumentation")
fun main(args: Array<String>) {
    val sheet = SpriteSheet("", 8, 10)
    var currentAction = sheet.spriteMap.keys.first()

    var animFrame = 0

    var reflectionPadding = 0.0

    var fadeHeight = 0.65f
    var fadeOpacity = 0.25f

    var xMultiplier = 0.5
    var yMultiplier = 0.5

    fun addLabelSliderSpinner(parent: JComponent, gridBagLayout: GridBagLayout, text: String, defaultValue: Number, maxNumber: Number, minNumber: Number): Triple<JLabel, JSlider, JSpinner> {
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

    val frame = JFrame("FAOSDance")
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
    SwingUtilities.updateComponentTreeUI(frame)

    val dialog = JDialog(frame, "FAOSDance Settings", true).apply {
        this.isResizable = false
        val gridBagLayout = GridBagLayout()

        this.add(JLabel("Action:"))
        this.add(JComboBox<String>(sheet.spriteMap.keys.toTypedArray()).apply {
            selectedIndex = sheet.spriteMap.keys.indexOf(selectedItem as String)
            addActionListener { currentAction = (it.source as JComboBox<*>).selectedItem as String }

            gridBagLayout.setConstraints(this, GridBagConstraints().apply {
                fill = GridBagConstraints.HORIZONTAL
                weightx = 1.0
                gridwidth = GridBagConstraints.REMAINDER
            })
        })

        this.add(JPanel().apply {
            this.border = BorderFactory.createTitledBorder("Size")
            this.layout = GridBagLayout()

            addLabelSliderSpinner(this, this.layout as GridBagLayout, "Width:", xMultiplier, 5.0, 0.1).third.addChangeListener { xMultiplier = (it.source as JSpinner).model.value as Double }

            addLabelSliderSpinner(this, this.layout as GridBagLayout, "Height:", yMultiplier, 5.0, 0.1).third.addChangeListener { yMultiplier = (it.source as JSpinner).model.value as Double }

            gridBagLayout.setConstraints(this, GridBagConstraints().apply {
                fill = GridBagConstraints.BOTH
                weightx = 1.0
                gridwidth = GridBagConstraints.REMAINDER
            })
        })

        this.add(JPanel().apply {
            this.border = BorderFactory.createTitledBorder("Reflection")
            this.layout = GridBagLayout()

            addLabelSliderSpinner(this, this.layout as GridBagLayout, "Padding:", reflectionPadding, 50.0, 0.0).third.addChangeListener { reflectionPadding = (it.source as JSpinner).model.value as Double }

            this.add(JPanel().apply {
                this.border = BorderFactory.createTitledBorder("Fade")
                this.layout = GridBagLayout()

                addLabelSliderSpinner(this, this.layout as GridBagLayout, "Height:", fadeHeight, 0.9, 0.1).third.addChangeListener { fadeHeight = ((it.source as JSpinner).model.value as Double).toFloat() }

                addLabelSliderSpinner(this, this.layout as GridBagLayout, "Opacity:", fadeOpacity, 0.9, 0.1).third.addChangeListener { fadeOpacity = ((it.source as JSpinner).model.value as Double).toFloat() }
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

        this.layout = gridBagLayout
    }

    val contextMenu = JPopupMenu().apply {
        this.add(JMenuItem("Settings").apply {
            addActionListener {
                dialog.size = Dimension(400, 260)
                dialog.setLocationRelativeTo(frame)

                dialog.isVisible = true
            }
        })
    }

    var isGrabbed = false
    var clickedPoint = Point()
    var animation = currentAction
    frame.addMouseListener(object : MouseAdapter() {
        override fun mousePressed(e: MouseEvent) {
            super.mousePressed(e)

            if (e.button == 1) {
                isGrabbed = true
                clickedPoint = e.point

                animation = currentAction
                currentAction = sheet.spriteMap.keys.last()
            }
            else if (e.button == 3) {
                contextMenu.show(frame, e.x, e.y)
            }
        }

        override fun mouseReleased(e: MouseEvent) {
            super.mouseReleased(e)

            if (e.button == 1) {
                isGrabbed = false

                currentAction = animation
            }
        }

        override fun mouseDragged(e: MouseEvent) {
            super.mouseDragged(e)

            if (isGrabbed) {
                frame.location = Point(e.xOnScreen - clickedPoint.x, e.yOnScreen - clickedPoint.y)
            }
        }
    }.apply { frame.addMouseMotionListener(this) })

    val panel = object : JPanel() {
        init {
            isOpaque = false
        }

        override fun paintComponent(g: Graphics) {
            super.paintComponent(g)
            val g2D = g as Graphics2D
            g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)

            g2D.translate((this.width - sheet.spriteWidth * xMultiplier) / 2, (this.height - sheet.spriteHeight * yMultiplier) / 2)

            g2D.translate(0.0, -(sheet.spriteHeight * yMultiplier) / 2 - reflectionPadding)
            g2D.scale(xMultiplier, yMultiplier)
            g2D.drawRenderedImage(sheet.spriteMap[currentAction]!![animFrame], null)

            g2D.translate(0.0, (sheet.spriteHeight * yMultiplier) * 4 + (reflectionPadding * 4))
            g2D.scale(1.0, -1.0)

            val reflection = BufferedImage(sheet.spriteWidth, sheet.spriteHeight, BufferedImage.TYPE_INT_ARGB)
            val rg = reflection.createGraphics()
            rg.drawRenderedImage(sheet.spriteMap[currentAction]!![animFrame], null)
            rg.composite = AlphaComposite.getInstance(AlphaComposite.DST_IN)
            rg.paint = GradientPaint(0f, sheet.spriteHeight.toFloat() * fadeHeight, Color(0.0f, 0.0f, 0.0f, 0.0f),
                    0f, sheet.spriteHeight.toFloat(), Color(0.0f, 0.0f, 0.0f, fadeOpacity))
            rg.fillRect(0, 0, sheet.spriteWidth, sheet.spriteHeight)
            rg.dispose()
            g2D.drawRenderedImage(reflection, null)
        }
    }
    frame.add(panel)

    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

    frame.isUndecorated = true
    frame.isAlwaysOnTop = true
    frame.background = Color(0, 0, 0, 0)

    fun resize() {
        val width = ((((sheet.spriteWidth * xMultiplier) * 2) * 100) / 100).toInt()
        val height = ((((sheet.spriteHeight * yMultiplier) * 2) * 100) / 100).toInt()

        frame.minimumSize = Dimension(width, height)
        frame.setSize(width, height)
    }
    resize()

    val timer = Timer(120, ActionListener {
        animFrame++

        if (animFrame >= 8) {
            animFrame = 0
        }

        frame.revalidate()
        frame.repaint()
    })
    timer.start()

    frame.pack()
    frame.isVisible = true

    frame.setLocationRelativeTo(null)
}