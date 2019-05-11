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
                "always_on_top" to GlobalValues.isTopLevel,
                "scaling_type" to GlobalValues.sanatizeEnumValue(GlobalValues.scalingType.name)
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
            // Rotation
            "roation" to hashMapOf(
                "z" to GlobalValues.zRotation
            ),
            // Size
            "size" to hashMapOf(
                "width" to GlobalValues.xMultiplier,
                "height" to GlobalValues.yMultiplier
            ),
            // Reflection
            "reflection" to hashMapOf(
                "visible" to GlobalValues.isReflectionVisible,
                "padding" to GlobalValues.reflectionPadding,
                // Reflection -- Fade
                "fade" to hashMapOf(
                    "height" to GlobalValues.fadeHeight,
                    "opacity" to GlobalValues.fadeOpacity
                )
            ),
            // Extensions
            "extensions" to hashMapOf(
                "enabled" to GlobalValues.enabledExtensions
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
        val scalingType = config.getString("sprite.scaling_type")
        // Animation
        val play = config.getBoolean("animation.play")
        val rewind = config.getBoolean("animation.rewind")
        val frame = config.getLong("animation.frame")
        // Location
        val x = config.getLong("location.x")
        val y = config.getLong("location.y")
        // Rotation
        val rotationZ = config.getLong("rotation.z")
        // Size
        val width = config.getDouble("size.width")
        val height = config.getDouble("size.height")
        // Reflection
        val padding = config.getDouble("reflection.padding")
        val reflectionVisible = config.getBoolean("reflection.visible")
        // Reflection -- Fade
        val fadeHeight = config.getDouble("reflection.fade.height")
        val fadeOpacity = config.getDouble("reflection.fade.opacity")
        // Extensions
        val enabledExtensions = config.getList<String>("extensions.enabled")

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
        if (scalingType != null) {
            GlobalValues.scalingType = ScalingType.valueOf(GlobalValues.unsanatizeEnumValue(scalingType))
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
        // Rotation
        if (rotationZ != null) {
            GlobalValues.zRotation = rotationZ.toInt()
        }
        // Size
        if (width != null) {
            GlobalValues.xMultiplier = width
        }
        if (height != null) {
            GlobalValues.yMultiplier = height
        }
        // Reflection
        if (reflectionVisible != null) {
            GlobalValues.isReflectionVisible = reflectionVisible
        }
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
        // Extensions
        if (enabledExtensions != null) {
            GlobalValues.enabledExtensions = enabledExtensions
        }

        return true
    }
}