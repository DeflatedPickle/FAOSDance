package com.deflatedpickle.faosdance.util

import java.util.*

object Lang {
    private val inputStream = javaClass.classLoader.getResourceAsStream("lang.properties")
    private val properties = Properties()

    val locale: Locale
    val bundle: ResourceBundle

    init {
        properties.load(inputStream)
        Locale.setDefault(Locale.UK)

        locale = Locale(properties.getProperty("LANGUAGE"), properties.getProperty("REGION"))
        bundle = ResourceBundle.getBundle("lang/", locale)
    }
}