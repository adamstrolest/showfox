package com.showfox.browser.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.showfox.browser.R
import com.showfox.browser.engine.GeckoPrefs

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        findPreference<SwitchPreferenceCompat>("drm_enabled")?.apply {
            isChecked = GeckoPrefs.isDrmEnabled(requireContext())
            setOnPreferenceChangeListener { _, newValue ->
                GeckoPrefs.setDrmEnabled(requireContext(), newValue as Boolean)
                true
            }
        }

        findPreference<SwitchPreferenceCompat>("private_mode")?.apply {
            isChecked = GeckoPrefs.isPrivateMode(requireContext())
            setOnPreferenceChangeListener { _, newValue ->
                GeckoPrefs.setPrivateMode(requireContext(), newValue as Boolean)
                true
            }
        }

        findPreference<SwitchPreferenceCompat>("custom_cursor")?.apply {
            isChecked = GeckoPrefs.isCustomCursorEnabled(requireContext())
            setOnPreferenceChangeListener { _, newValue ->
                GeckoPrefs.setCustomCursorEnabled(requireContext(), newValue as Boolean)
                true
            }
        }

        findPreference<Preference>("clear_history")?.setOnPreferenceClickListener {
            true
        }

        findPreference<Preference>("clear_bookmarks")?.setOnPreferenceClickListener {
            true
        }

        findPreference<Preference>("about")?.setOnPreferenceClickListener {
            true
        }
    }
}
