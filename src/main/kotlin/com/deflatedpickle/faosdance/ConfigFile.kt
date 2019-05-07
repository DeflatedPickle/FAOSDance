package com.deflatedpickle.faosdance

import com.moandjiezana.toml.Toml
import com.moandjiezana.toml.TomlWriter
import java.io.File
import java.io.StringWriter

object ConfigFile {
    fun openConfig(): Toml {
        val currentFile = ClassLoader.getSystemClassLoader().getResource("config.toml").readText()

        val config = Toml().read(currentFile)

        return config
    }

    fun writeConfig() {
        val currentFile = File(ClassLoader.getSystemClassLoader().getResource("config.toml").file)

        val config = TomlWriter()

        val configMap = hashMapOf(
            // Sprite
            "sprite" to hashMapOf(
                "sheet" to GlobalValues.sheet!!.image.replace("\\", "/"),
                "action" to GlobalValues.currentAction,
                "fps" to GlobalValues.fps,
                "opacity" to GlobalValues.opacity,
                "visible" to GlobalValues.isVisible,
                "solid" to GlobalValues.isSolid,
                "always_on_top" to GlobalValues.isTopLevel
                ),
            // Animation
            "animation" to hashMapOf(
                "play" to GlobalValues.play,
                "rewind" to GlobalValues.rewind,
                "frame" to GlobalValues.animFrame
            ),
            // Location
            "location" to hashMapOf(
                "x" to GlobalValues.xPosition,
                "y" to GlobalValues.yPosition
            ),
            // Size
            "size" to hashMapOf(
                "width" to GlobalValues.xMultiplier,
                "height" to GlobalValues.yMultiplier
            ),
            // Reflection
            "reflection" to hashMapOf(
                "padding" to GlobalValues.reflectionPadding,
                // Reflection -- Fade
                "fade" to hashMapOf(
                    "height" to GlobalValues.fadeHeight,
                    "opacity" to GlobalValues.fadeOpacity
                )
            )
        )

        config.write(configMap, currentFile)
    }

    fun loadAndUseConfig(): Boolean {
        val config = openConfig()
        // Sprite
        val sheet = config.getString("sprite.sheet")
        val action = config.getString("sprite.action")
        val fps = config.getLong("sprite.fps")
        val opacity = config.getDouble("sprite.opacity")
        val visible = config.getBoolean("sprite.visible")
        val solid = config.getBoolean("sprite.solid")
        val alwaysOnTop = config.getBoolean("sprite.always_on_top")
        // Animation
        val play = config.getBoolean("animation.play")
        val rewind = config.getBoolean("animation.rewind")
        val frame = config.getLong("animation.frame")
        // Location
        val x = config.getLong("location.x")
        val y = config.getLong("location.y")
        // Size
        val width = config.getDouble("size.width")
        val height = config.getDouble("size.height")
        // Reflection
        val padding = config.getDouble("reflection.padding")
        // Reflection -- Fade
        val fadeHeight = config.getDouble("reflection.fade.height")
        val fadeOpacity = config.getDouble("reflection.fade.opacity")

        // Sprite
        if (sheet != null) {
            GlobalValues.configureSpriteSheet(SpriteSheet(sheet))
        } else {
            return false
        }
        if (action != null) {
            GlobalValues.currentAction = action
        }
        if (fps != null) {
            GlobalValues.fps = fps.toInt()
        }
        if (opacity != null) {
            GlobalValues.opacity = opacity
        }
        if (visible != null) {
            GlobalValues.isVisible = visible
        }
        if (solid != null) {
            GlobalValues.isSolid = solid
        }
        if (alwaysOnTop != null) {
            GlobalValues.isTopLevel = alwaysOnTop
        }
        // Animation
        if (play != null) {
            GlobalValues.play = play
        }
        if (rewind != null) {
            GlobalValues.rewind = rewind
        }
        if (frame != null) {
            GlobalValues.animFrame = frame.toInt()
        }
        // Location
        if (x != null) {
            GlobalValues.xPosition = x.toInt()
        }
        if (y != null) {
            GlobalValues.yPosition = y.toInt()
        }
        // Size
        if (width != null) {
            GlobalValues.xMultiplier = width
        }
        if (height != null) {
            GlobalValues.yMultiplier = height
        }
        // Reflection
        if (padding != null) {
            GlobalValues.reflectionPadding = padding
        }
        // Reflection -- Fade
        if (fadeHeight != null) {
            GlobalValues.fadeHeight = fadeHeight.toFloat()
        }
        if (fadeOpacity != null) {
            GlobalValues.fadeOpacity = fadeOpacity.toFloat()
        }

        return true
    }
}