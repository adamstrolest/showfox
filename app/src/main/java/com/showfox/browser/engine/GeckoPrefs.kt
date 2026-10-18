package com.showfox.browser.engine

import android.content.Context
import androidx.core.content.edit

object GeckoPrefs {

    private const val PREFS_NAME = "showfox_prefs"
    private const val KEY_DRM_ENABLED = "drm_enabled"
    private const val KEY_PRIVATE_MODE = "private_mode"
    private const val KEY_DEFAULT_SEARCH = "default_search_engine"
    private const val KEY_UBO_ENABLED = "ubo_enabled"
    private const val KEY_CUSTOM_CURSOR = "custom_cursor_enabled"
    private const val KEY_FIRST_RUN = "first_run_complete"

    fun isDrmEnabled(context: Context): Boolean =
        prefs(context).getBoolean(KEY_DRM_ENABLED, true)

    fun setDrmEnabled(context: Context, enabled: Boolean) =
        prefs(context).edit { putBoolean(KEY_DRM_ENABLED, enabled) }

    fun isPrivateMode(context: Context): Boolean =
        prefs(context).getBoolean(KEY_PRIVATE_MODE, false)

    fun setPrivateMode(context: Context, enabled: Boolean) =
        prefs(context).edit { putBoolean(KEY_PRIVATE_MODE, enabled) }

    fun isUboEnabled(context: Context): Boolean =
        prefs(context).getBoolean(KEY_UBO_ENABLED, true)

    fun setUboEnabled(context: Context, enabled: Boolean) =
        prefs(context).edit { putBoolean(KEY_UBO_ENABLED, enabled) }

    fun isCustomCursorEnabled(context: Context): Boolean =
        prefs(context).getBoolean(KEY_CUSTOM_CURSOR, true)

    fun setCustomCursorEnabled(context: Context, enabled: Boolean) =
        prefs(context).edit { putBoolean(KEY_CUSTOM_CURSOR, enabled) }

    fun getDefaultSearchEngine(context: Context): String =
        prefs(context).getString(KEY_DEFAULT_SEARCH, "google") ?: "google"

    fun setDefaultSearchEngine(context: Context, engine: String) =
        prefs(context).edit { putString(KEY_DEFAULT_SEARCH, engine) }

    fun isFirstRunComplete(context: Context): Boolean =
        prefs(context).getBoolean(KEY_FIRST_RUN, false)

    fun setFirstRunComplete(context: Context) =
        prefs(context).edit { putBoolean(KEY_FIRST_RUN, true) }

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
}
