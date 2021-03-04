package com.prayer.times.ui.controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.mikepenz.fastadapter.GenericFastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter.Companion.items
import com.prayer.times.databinding.PreferencesLayoutBinding
import com.prayer.times.ui.controller.base.BaseController

class SettingsController : BaseController<PreferencesLayoutBinding>() {
    private lateinit var adapter: GenericFastAdapter
    private var itemAdapter: GenericItemAdapter = items()

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup
    ): View {
        binding = PreferencesLayoutBinding.inflate(inflater)
        return binding.root
    }

    abstract fun getSettingsList(): List<AbstractBindingItem> 

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)
        
        itemAdapter.set(getSettingsList())
        adapter = FastAdapter.with(listOf(itemAdapter))

        binding.recyclerView.layoutManager = LinearLayoutManager(view.context)
        binding.recyclerView.adapter = adapter
    }
}
