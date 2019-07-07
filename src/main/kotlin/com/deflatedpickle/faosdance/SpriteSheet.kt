package com.deflatedpickle.faosdance

import java.awt.image.BufferedImage
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.util.stream.Collectors
import javax.imageio.ImageIO
import javax.swing.JOptionPane


/**
 * A sprite sheet
 */
class SpriteSheet(val image: InputStream, val text: InputStream, var spriteNumX: Int = 8, var spriteNumY: Int? = null) {
    /**
     * The map of sprites cut from the sheet
     */
    val spriteMap: MutableMap<String, MutableList<BufferedImage>> = mutableMapOf()

    /**
     * The width of the sprites
     */
    @Suppress("MemberVisibilityCanBePrivate")
    var spriteWidth: Int = 0
    /**
     * The height of the sprites
     */
    @Suppress("MemberVisibilityCanBePrivate")
    var spriteHeight: Int = 0

    var sheet: BufferedImage? = null
    var animations = ""

    var loadedImage = false
    var loadedText = false

    init {
        // Load the sprite sheet
        sheet = ImageIO.read(image)
        loadedImage = true

        // Load the actions file
        val bufferedReader = BufferedReader(InputStreamReader(text))
        animations = bufferedReader.lines().collect(Collectors.joining(System.lineSeparator()))
        loadedText = true

        // Check if they were both loaded, then sort everything out
        if (loadedImage && loadedText) {
            if (spriteNumY == null) {
                spriteNumY = animations.lines().size
            }

            spriteWidth = sheet!!.width / spriteNumX
            spriteHeight = sheet!!.height / spriteNumY!!

            var gridX = 0
            var gridY = 0

            for (anim in animations.lines()) {
                spriteMap[anim] = mutableListOf()

                for (frame in 1..spriteNumX) {
                    spriteMap[anim]!!.add(sheet!!.getSubimage(gridX * spriteWidth, gridY * spriteHeight, spriteWidth, spriteHeight))
                    gridX++
                }

                gridX = 0
                gridY++
            }
        }
    }
}