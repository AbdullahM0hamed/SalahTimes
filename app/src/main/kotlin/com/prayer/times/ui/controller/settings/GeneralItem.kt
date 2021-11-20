package com.prayer.times.ui.controller.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.prayer.times.R
import com.prayer.times.databinding.GeneralItemBinding
import com.prayer.times.ui.controller.base.BaseController

data class GeneralItem(
    val name: String,
    val desc: String,
    val onClick: Function0<Unit> = {}
) : AbstractBindingItem<GeneralItemBinding>() {

    override val type: Int = R.id.general_item_main
    
    override fun bindView(
        binding: GeneralItemBinding,
        payloads: List<Any>
    ) {
        binding.root.setOnClickListener { onClick() }
        binding.title.text = name
        binding.summary.text = desc
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): GeneralItemBinding {
        return GeneralItemBinding.inflate(inflater, parent, false)
    }
}
