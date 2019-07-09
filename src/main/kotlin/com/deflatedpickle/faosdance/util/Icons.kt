package com.deflatedpickle.faosdance.util

import org.pushingpixels.photon.icon.SvgBatikResizableIcon
import java.awt.Dimension

object Icons {
    private val initialSize = Dimension(16, 16)

    val upArrow = SvgBatikResizableIcon.getSvgIcon(ClassLoader.getSystemResource("icons/svg/production/up_arrow.svg"),
        initialSize
    )
    val downArrow = SvgBatikResizableIcon.getSvgIcon(ClassLoader.getSystemResource("icons/svg/production/down_arrow.svg"),
        initialSize
    )
}