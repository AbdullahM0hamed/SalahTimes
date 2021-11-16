package com.prayer.times.ui.controller.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.prayer.times.R
import com.prayer.times.databinding.SettingsItemBinding
import com.prayer.times.ui.controller.base.BaseController

data class SettingsItemMain(
    val name: String,
    val icon: Int
) : AbstractBindingItem<SettingsItemBinding>() {

    override val type: Int = R.id.settings_item_main
    
    override fun bindView(
        binding: SettingsItemBinding,
        payloads: List<Any>
    ) {
        binding.icon.setImageResource(icon)
        binding.name.text = name
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): SettingsItemBinding {
        return SettingsItemBinding.inflate(inflater, parent, false)
    }
}
