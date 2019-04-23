package com.deflatedpickle.faosdance

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO


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

    init {
        val sheet = ImageIO.read(File("$image.png"))!!
        val animations = File("$image.txt").readText()

        if (spriteNumY == null) {
            spriteNumY = animations.lines().size
        }

        spriteWidth = sheet.width / spriteNumX
        spriteHeight = sheet.height / spriteNumY!!

        var gridX = 0
        var gridY = 0

        for (anim in animations.lines()) {
            spriteMap[anim] = mutableListOf()

            for (frame in 1..spriteNumX) {
                spriteMap[anim]!!.add(sheet.getSubimage(gridX * spriteWidth, gridY * spriteHeight, spriteWidth, spriteHeight))
                gridX++
            }

            gridX = 0
            gridY++
        }
    }
}