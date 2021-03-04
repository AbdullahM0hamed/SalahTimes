package com.prayer.times.ui.controller.settings

import android.view.View
import androidx.viewbinding.ViewBinding
import android.widget.ImageView
import android.widget.TextView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.prayer.times.R
import com.prayer.times.ui.controller.base.BaseController

data class SettingsItemMain(
    val name: String,
    val icon: Int,
    val controller: BaseController<ViewBinding>
) : AbstractItem<SettingsItemMain.ViewHolder>() {

    override val type: Int
        get() = R.id.settings_item_main

    override val layoutRes: Int
        get() = R.layout.preference

    class ViewHolder(view: View) : FastAdapter.ViewHolder<SettingsItemMain>(view) {
        var icon: ImageView = view.findViewById(android.R.id.icon)
        var name: TextView = view.findViewById(android.R.id.title)

        override fun bindView(item: SettingsItemMain, payloads: List<Any>) {
            icon.setImageResource(item.icon)
            name.text = item.name
        }

        override fun unbindView(item: SettingsItemMain) {
            icon.setImageResource(null)
            name.text = null
        }
    }
}
