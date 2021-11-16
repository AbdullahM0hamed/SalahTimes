package com.prayer.times.ui.controller.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.prayer.times.R
import com.prayer.times.databinding.HeaderBinding
import com.prayer.times.databinding.SettingsItemBinding
import com.prayer.times.ui.controller.base.BaseController

data class SettingsItemMain(
    val name: String? = null,
    val icon: Int? = null,
    val header: Boolean = false
) : AbstractBindingItem<ViewBinding>() {

    override val type: Int = R.id.settings_item_main
    private var itemBinding: SettingsItemBinding? = null
    private var headerBinding: HeaderBinding? = null
    
    override fun bindView(binding: ViewBinding, payloads: List<Any>) {
        if (!header) {
            itemBinding = binding as SettingsItemBinding
            itemBinding!!.icon.setImageResource(icon)
            itemBinding!!.name.text = name
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ViewBinding {
        if (header) {
            return HeaderBinding.inflate(inflater, parent, false)
        }

        return SettingsItemBinding.inflate(inflater, parent, false)
    }
}
