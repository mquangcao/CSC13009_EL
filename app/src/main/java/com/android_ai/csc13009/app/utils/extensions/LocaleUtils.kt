package com.android_ai.csc13009.app.utils.extensions

import android.content.Context
import java.util.Locale

object LocaleUtils {
    private var sLocale: Locale? = null

    fun setLocale(locale: Locale?) {
        sLocale = locale
        if (locale != null) {
            sLocale?.let { Locale.setDefault(it) }
        }
    }

    fun setLocale(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        val resources = context.resources
        resources.updateConfiguration(config, resources.displayMetrics)

        // Lưu ngôn ngữ vào SharedPreferences
        context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
            .edit()
            .putString("Lang", language)
            .apply()
    }

    fun getLocale(context: Context): Locale? {
        val locale = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
            .getString("Lang", "en")?.let {
                Locale(
                    it
                )
            }
        return locale
    }

    fun getLocaleString(context: Context): String? {
        val locale = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
            .getString("Lang", "en")
        return locale
    }

    fun getLocale(): Locale? {
        return sLocale
    }
}