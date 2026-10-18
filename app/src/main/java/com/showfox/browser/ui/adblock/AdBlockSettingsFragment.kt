package com.showfox.browser.ui.adblock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.showfox.browser.R
import com.showfox.browser.engine.GeckoPrefs

class AdBlockSettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.adblock_preferences, rootKey)

        findPreference<SwitchPreferenceCompat>("ubo_enabled")?.apply {
            isChecked = GeckoPrefs.isUboEnabled(requireContext())
            setOnPreferenceChangeListener { _, newValue ->
                GeckoPrefs.setUboEnabled(requireContext(), newValue as Boolean)
                true
            }
        }
    }
}
