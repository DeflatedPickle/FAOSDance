package com.deflatedpickle.faosdance.autoupdate

import java.awt.*
import javax.swing.border.AbstractBorder
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.geom.Area
import java.awt.geom.RoundRectangle2D

// Credit: https://stackoverflow.com/a/16909994
class SpeechBubbleBorder(val color: Color, val thickness: Int = 4, val radii: Int = 8, val pointerSize: Int = 7) :
    AbstractBorder() {

    enum class Side {
        LEFT,
        RIGHT,
        CENTRE
    }

    private var insets: Insets? = null
    private var stroke: BasicStroke? = null
    private var strokePad: Int
    private val pointerPad = 4
    private val side = Side.CENTRE
    private val hints = RenderingHints(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON
    )

    init {
        stroke = BasicStroke(thickness.toFloat())
        strokePad = thickness / 2

        val pad = radii + strokePad
        val bottomPad = pad + pointerSize + strokePad
        insets = Insets(pad, pad, bottomPad, pad)
    }

    override fun getBorderInsets(c: Component): Insets? {
        return insets
    }

    override fun getBorderInsets(c: Component, insets: Insets): Insets? {
        return getBorderInsets(c)
    }

    override fun paintBorder(
        c: Component,
        g: Graphics,
        x: Int, y: Int,
        width: Int, height: Int
    ) {
        val g2 = g as Graphics2D

        val bottomLineY = height - thickness - pointerSize

        val bubble = RoundRectangle2D.Double(
            0 + strokePad.toDouble(),
            0 + strokePad.toDouble(),
            width - thickness.toDouble() * 5,
            bottomLineY.toDouble(),
            radii.toDouble(),
            radii.toDouble()
        )

        val pointer = Polygon()

        when (side) {
            Side.LEFT -> {
                // left point
                pointer.addPoint(
                    strokePad + radii + pointerPad,
                    bottomLineY
                )
                // right point
                pointer.addPoint(
                    strokePad + radii + pointerPad + pointerSize,
                    bottomLineY
                )
                // bottom point
                pointer.addPoint(
                    strokePad + radii + pointerPad + pointerSize / 2,
                    height - strokePad
                )
            }
            Side.RIGHT -> {
                // left point
                pointer.addPoint(
                    width - (strokePad + radii + pointerPad),
                    bottomLineY
                )
                // right point
                pointer.addPoint(
                    width - (strokePad + radii + pointerPad + pointerSize),
                    bottomLineY
                )
                // bottom point
                pointer.addPoint(
                    width - (strokePad + radii + pointerPad + pointerSize / 2),
                    height - strokePad
                )
            }
            Side.CENTRE -> {
                // left point
                pointer.addPoint(
                    width / 2 - (strokePad + radii + pointerPad),
                    bottomLineY
                )
                // right point
                pointer.addPoint(
                    width / 2 - (strokePad + radii + pointerPad + pointerSize),
                    bottomLineY
                )
                // bottom point
                pointer.addPoint(
                    width / 2 - (strokePad + radii + pointerPad + pointerSize / 2),
                    height - strokePad
                )
            }
        }

        val area = Area(bubble)
        area.add(Area(pointer))

        g2.color = Color.WHITE
        g2.fill(area)

        g2.setRenderingHints(hints)

        g2.color = color
        g2.stroke = stroke
        g2.draw(area)
    }

    override fun isBorderOpaque(): Boolean {
        return false
    }
}