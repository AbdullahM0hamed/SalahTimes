package com.prayer.times.ui.controller.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prayer.times.databinding.HeaderBinding
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericFastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter.Companion.items
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.prayer.times.databinding.PreferencesLayoutBinding
import com.prayer.times.ui.controller.base.BaseController

abstract class SettingsController : BaseController<PreferencesLayoutBinding>() {
    private lateinit var adapter: GenericFastAdapter
    private var itemAdapter: GenericItemAdapter = items()

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup
    ): View {
        binding = PreferencesLayoutBinding.inflate(inflater)
        return binding.root
    }

    abstract fun getSettingsList(): List<AbstractBindingItem<*>> 

    open fun hasHeader() = false

    private fun getHeader() = listOf(Header())

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)
        
        itemAdapter.set(getSettingsList())

        val list = mutableListOf(itemAdapter)
        if (hasHeader()) {
            val headerAdapter: GenericItemAdapter = items()
            headerAdapter.set(getHeader())
            list.add(headerAdapter)
        }

        adapter = FastAdapter.with(list)

        binding.recycler.layoutManager = LinearLayoutManager(view.context)
        binding.recycler.adapter = adapter
    }
}
