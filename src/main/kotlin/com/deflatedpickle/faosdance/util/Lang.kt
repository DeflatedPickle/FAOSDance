package com.deflatedpickle.faosdance.util

import com.deflatedpickle.faosdance.GlobalValues
import java.util.*

object Lang {
    private val inputStream = GlobalValues.langProperties.inputStream()
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