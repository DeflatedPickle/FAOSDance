package com.deflatedpickle.faosdance

import com.moandjiezana.toml.Toml

object ConfigFile {
    fun openConfig(): Toml {
        val currentFile = ClassLoader.getSystemClassLoader().getResource("config.toml").readText()

        val config = Toml().read(currentFile)

        return config
    }

    fun loadAndUseConfig(): Boolean {
        val config = ConfigFile.openConfig()
        // Sprite
        val sheet = config.getString("sprite.sheet")
        val action = config.getString("sprite.action")
        val frameDelay = config.getLong("sprite.frame_delay")
        val visible = config.getBoolean("sprite.visible")
        val solid = config.getBoolean("sprite.solid")
        val alwaysOnTop = config.getBoolean("sprite.always_on_top")
        // Size
        val width = config.getDouble("size.width")
        val height = config.getDouble("size.height")
        // Reflection
        val padding = config.getDouble("reflection.padding")
        // Reflection -- Fade
        val fadeHeight = config.getDouble("reflection.fade.height")
        val fadeOpacity = config.getDouble("reflection.fade.opacity")

        // Sprite
        if (sheet != null) { GlobalValues.configureSpriteSheet(SpriteSheet(sheet)) } else { return false }
        if (action != null) { GlobalValues.currentAction = action }
        if (frameDelay != null) { GlobalValues.delay = frameDelay.toInt() }
        if (visible != null) { GlobalValues.isVisible = visible }
        if (solid != null) { GlobalValues.isSolid = solid }
        if (alwaysOnTop != null) { GlobalValues.isTopLevel = alwaysOnTop }
        // Size
        if (width != null) { GlobalValues.xMultiplier = width }
        if (height != null) { GlobalValues.yMultiplier = height }
        // Reflection
        if (padding != null) { GlobalValues.reflectionPadding = padding }
        // Reflection -- Fade
        if (fadeHeight != null) { GlobalValues.fadeHeight = fadeHeight.toFloat() }
        if (fadeOpacity != null) { GlobalValues.fadeOpacity = fadeOpacity.toFloat() }

        return true
    }
}