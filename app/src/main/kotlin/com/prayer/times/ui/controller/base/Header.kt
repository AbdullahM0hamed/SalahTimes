package com.prayer.times.ui.controller.base

import android.view.LayoutInflater
import android.view.ViewGroup
import com.prayer.times.R
import com.prayer.times.databinding.HeaderBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class Header : AbstractBindingItem<HeaderBinding>() {

    override val type: Int = R.id.settings_header

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): HeaderBinding {
        return HeaderBinding.inflate(inflater, parent, false)
    }
}
