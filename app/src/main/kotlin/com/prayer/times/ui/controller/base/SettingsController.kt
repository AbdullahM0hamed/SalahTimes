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
import com.prayer.times.R
import com.prayer.times.databinding.PreferencesLayoutBinding
import com.prayer.times.ui.MainActivity
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

    open fun hasActionBar() = true

    open fun getHeaderTitle(): String = ""

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

        if (hasActionBar()) {
            binding.actionBar.setVisibility(View.VISIBLE)
            binding.topAppBar.title = getHeaderTitle()
            binding.topAppBar.setNavigationOnClickListener {
                router.handleBack()
            }
        }

        adapter = FastAdapter.with(list.reversed())
        binding.recycler.layoutManager = LinearLayoutManager(view.context)
        binding.recycler.adapter = adapter
    }

    open fun hideBottomNav(): Boolean = true

    override fun onAttach(view: View) {
        if (!hideBottomNav()) return
        hideBottomNav()
    }

    override fun onDetach(view: View) {
        if (!hideBottomNav()) return
        (activity as? MainActivity)?.binding?.bottomNavigation?.visibility = View.VISIBLE
    }

    fun hideBottomNav() {
        (activity as? MainActivity)?.binding?.bottomNavigation?.visibility = View.GONE
    }
}
