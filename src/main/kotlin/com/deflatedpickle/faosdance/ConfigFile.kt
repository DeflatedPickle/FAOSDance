package com.deflatedpickle.faosdance

import com.moandjiezana.toml.Toml
import com.moandjiezana.toml.TomlWriter
import java.util.HashMap

object ConfigFile {
    fun openConfig(): Toml {
        val currentFile = GlobalValues.configFile.readText()
        val config = Toml().read(currentFile)
        return config
    }

    fun writeConfig() {
        val config = TomlWriter()

        val configMap = hashMapOf(
            // Window
            "window" to hashMapOf(
                "solid" to GlobalValues.optionsMap.getMap("window")!!.getOption<Boolean>("solid"),
                "always_on_top" to GlobalValues.optionsMap.getMap("window")!!.getOption<Boolean>("always_on_top"),
                // Location
                "location" to hashMapOf(
                    "x" to GlobalValues.optionsMap.getMap("window")!!.getMap("location")!!.getOption<Int>("x"),
                    "y" to GlobalValues.optionsMap.getMap("window")!!.getMap("location")!!.getOption<Int>("y")
                )
            ),
            // Settings
            "settings" to hashMapOf(
                "open_on_start" to GlobalValues.optionsMap.getMap("settings")!!.getOption<Boolean>("open_on_start")
            ),
            // Sprite
            "sprite" to hashMapOf(
                "sheet" to GlobalValues.sheet!!.image.replace("\\", "/"),
                "opacity" to GlobalValues.optionsMap.getMap("sprite")!!.getOption<Double>("opacity"),
                "visible" to GlobalValues.optionsMap.getMap("sprite")!!.getOption<Boolean>("visible"),
                "toggle_held" to GlobalValues.optionsMap.getMap("sprite")!!.getOption<Boolean>("toggle_held"),
                "scaling_type" to GlobalValues.sanatizeEnumValue(GlobalValues.optionsMap.getMap("sprite")!!.getOption<String>("scaling_type")!!),
                // Animation
                "animation" to hashMapOf(
                    "action" to GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.getOption<String>("action"),
                    "fps" to GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.getOption<Int>("fps"),
                    "play" to GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.getOption<Boolean>("play"),
                    "rewind" to GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.getOption<Boolean>("rewind"),
                    "frame" to GlobalValues.optionsMap.getMap("sprite")!!.getMap("animation")!!.getOption<Int>("frame")
                ),
                // Rotation
                "rotation" to hashMapOf(
                    "z" to GlobalValues.optionsMap.getMap("sprite")!!.getMap("rotation")!!.getOption<Int>("z")
                ),
                // Size
                "size" to hashMapOf(
                    "width" to GlobalValues.optionsMap.getMap("sprite")!!.getMap("size")!!.getOption<Double>("width"),
                    "height" to GlobalValues.optionsMap.getMap("sprite")!!.getMap("size")!!.getOption<Double>("height")
                )
            ),
            // Reflection
            "reflection" to hashMapOf(
                "visible" to GlobalValues.optionsMap.getMap("reflection")!!.getOption<Boolean>("visible"),
                "padding" to GlobalValues.optionsMap.getMap("reflection")!!.getOption<Double>("padding"),
                // Reflection -- Fade
                "fade" to hashMapOf(
                    "height" to GlobalValues.optionsMap.getMap("reflection")!!.getMap("fade")!!.getOption<Double>("height"),
                    "opacity" to GlobalValues.optionsMap.getMap("reflection")!!.getMap("fade")!!.getOption<Double>("opacity")
                )
            ),
            // Extensions
            "extensions" to hashMapOf(
                "enabled" to GlobalValues.optionsMap.getMap("extensions")!!.getOption<List<String>>("enabled")
            )
        )

        config.write(configMap, GlobalValues.configFile)
    }

    fun loadAndUseConfig(): Boolean {
        fun recursiveCast(value: Any): Any {
            val newV: Any

            if (value is HashMap<*, *>) {
                newV = NestedHashMap<String, Any>()

                for ((kk, vv) in value) {
                    newV[kk as String] = recursiveCast(vv)
                }
            }
            else {
                newV = value
            }

            return newV
        }

        val config = openConfig()

        for ((k, v) in config.toMap()) {
            GlobalValues.optionsMap[k] = recursiveCast(v)
        }

        if (GlobalValues.optionsMap.getMap("sprite")!!.getOption<String>("sheet") != "") {
            GlobalValues.sheet = SpriteSheet(GlobalValues.optionsMap.getMap("sprite")!!.getOption<String>("sheet")!!)
        }

        GlobalValues.rootMap = GlobalValues.optionsMap

        return true
    }
}