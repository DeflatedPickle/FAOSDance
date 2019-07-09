package com.deflatedpickle.faosdance.util

import com.deflatedpickle.faosdance.backend.NestedHashMap
import com.deflatedpickle.faosdance.backend.SpriteSheet
import com.moandjiezana.toml.Toml
import com.moandjiezana.toml.TomlWriter
import java.io.File
import java.util.HashMap

object ConfigFile {
    fun openConfig(): Toml {
        val currentFile = GlobalValues.configFile.readText()
        val config = Toml().read(currentFile)
        return config
    }

    fun writeConfig() {
        val config = TomlWriter()
        config.write(
            GlobalValues.optionsMap,
            GlobalValues.configFile
        )
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
            GlobalValues.sheet = SpriteSheet(
                File("${GlobalValues.optionsMap.getMap("sprite")!!.getOption<String>("sheet")!!}.png").inputStream(),
                File("${GlobalValues.optionsMap.getMap("sprite")!!.getOption<String>("sheet")!!}.txt").inputStream()
            )
            GlobalValues.configureSpriteSheet(GlobalValues.sheet!!)
        }

        GlobalValues.rootMap = GlobalValues.optionsMap

        return true
    }
}