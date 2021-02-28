package com.prayer.times.ui.controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.prayer.times.databinding.PreferencesLayoutBinding
import com.prayer.times.ui.controller.base.BaseController

class SettingsController : BaseController<PreferencesLayoutBinding>() {
    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup
    ): View {
        binding = PreferencesLayoutBinding.inflate(inflater)
        return binding.root
    }
}
