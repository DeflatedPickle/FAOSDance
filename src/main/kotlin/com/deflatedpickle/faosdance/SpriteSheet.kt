package com.deflatedpickle.faosdance

import java.awt.image.BufferedImage
import java.io.File
import java.io.FileNotFoundException
import javax.imageio.ImageIO
import javax.swing.JOptionPane


/**
 * A sprite sheet
 */
class SpriteSheet(image: String, spriteNumX: Int = 8, var spriteNumY: Int? = null) {
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
        val sheetFile = File("$image.png")

        if (sheetFile.exists()) {
            sheet = ImageIO.read(sheetFile)
            loadedImage = true
        }
        else {
            JOptionPane.showMessageDialog(GlobalValues.frame, "Could not find the file: ${image.split(".").last()}.png", GlobalValues.frame!!.title, JOptionPane.ERROR_MESSAGE)
        }

        // Load the actions file
        val textFile = File("$image.txt")

        if (textFile.exists()) {
            animations = textFile.readText()
            loadedText = true
        }
        else {
            JOptionPane.showMessageDialog(GlobalValues.frame, "Could not find the file: ${image.split(".").last()}.txt", GlobalValues.frame!!.title, JOptionPane.ERROR_MESSAGE)
        }

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