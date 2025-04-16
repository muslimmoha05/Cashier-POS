package com.utopiatechhub.cashierpos.language

import android.content.Context
import java.util.*

object LocaleHelper {
    fun wrap(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = context.resources.configuration

        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return context.createConfigurationContext(config)
    }
}
