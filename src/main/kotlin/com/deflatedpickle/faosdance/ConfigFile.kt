package com.deflatedpickle.faosdance

import com.electronwill.nightconfig.core.file.CommentedFileConfig
import java.io.File

object ConfigFile {
    fun openConfig(): CommentedFileConfig {
        val currentFile = File(ConfigFile::class.java.protectionDomain.codeSource.location.toURI()).path + "/config.toml"
        val config = CommentedFileConfig.builder(currentFile).autosave().build()
        config.load()

        return config
    }

    fun loadAndUseConfig(): Boolean {
        val config = ConfigFile.openConfig()
        // Sprite
        val sheet = config.get<String>("sprite.sheet")
        val action = config.get<String>("sprite.action")
        val frameDelay = config.get<Int>("sprite.frame_delay")
        val visible = config.get<Boolean>("sprite.visible")
        val solid = config.get<Boolean>("sprite.solid")
        val alwaysOnTop = config.get<Boolean>("sprite.always_on_top")
        // Size
        val width = config.get<Double>("size.width")
        val height = config.get<Double>("size.height")
        // Reflection
        val padding = config.get<Double>("reflection.padding")
        // Reflection -- Fade
        val fadeHeight = config.get<Float>("reflection.fade.height")
        val fadeOpacity = config.get<Float>("reflection.fade.opacity")

        // Sprite
        if (sheet != null) { GlobalValues.configureSpriteSheet(SpriteSheet(sheet)) } else { return false }
        if (action != null) { GlobalValues.currentAction = action }
        if (frameDelay != null) { GlobalValues.delay = frameDelay }
        if (visible != null) { GlobalValues.isVisible = visible }
        if (solid != null) { GlobalValues.isSolid = solid }
        if (alwaysOnTop != null) { GlobalValues.isTopLevel = alwaysOnTop }
        // Size
        if (width != null) { GlobalValues.xMultiplier = width }
        if (height != null) { GlobalValues.yMultiplier = height }
        // Reflection
        if (padding != null) { GlobalValues.reflectionPadding = padding }
        // Reflection -- Fade
        if (fadeHeight != null) { GlobalValues.fadeHeight = fadeHeight }
        if (fadeOpacity != null) { GlobalValues.fadeOpacity = fadeOpacity }

        return true
    }
}