package com.prayer.times.ui.controller.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericFastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter.Companion.items
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

    abstract fun getSettingsList(): List<AbstractItem<*>> 

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)
        
        itemAdapter.set(getSettingsList())
        adapter = FastAdapter.with(listOf(itemAdapter))

        binding.recyclerView.layoutManager = LinearLayoutManager(view.context)
        binding.recyclerView.adapter = adapter
    }
}
