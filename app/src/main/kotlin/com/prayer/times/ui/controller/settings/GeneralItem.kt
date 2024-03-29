package com.prayer.times.ui.controller.settings

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.prayer.times.R
import com.prayer.times.databinding.GeneralItemBinding
import com.prayer.times.preference.PreferencesHelper
import com.prayer.times.ui.controller.base.BaseController

data class GeneralItem(
    val context: Context,
    val name: String,
    val desc: String,
    val key: String,
    val default: String,
    val options: Array<String>,
    val values: Array<String>
) : AbstractBindingItem<GeneralItemBinding>() {

    override val type: Int = R.id.general_item_main
    val helper = PreferencesHelper(context)
    
    override fun bindView(
        binding: GeneralItemBinding,
        payloads: List<Any>
    ) {
        binding.title.text = name
        binding.summary.text = getSummaryText(helper.getString(key) ?: default)
        binding.root.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(desc)
            builder.setSingleChoiceItems(
                options.map { it }.toTypedArray<CharSequence>(),
                getPosition()
            ) { _, _ -> }
            builder.setPositiveButton(R.string.ok) { dialog, _ ->
                val position = (dialog as AlertDialog).listView.checkedItemPosition
                helper.putString(key, values[position])
                binding.summary.text = getSummaryText(helper.getString(key) ?: default)
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): GeneralItemBinding {
        return GeneralItemBinding.inflate(inflater, parent, false)
    }

    fun getPosition(): Int {
        val value = helper.getString(key) ?: default
        return values.indexOf(value)
    }

    fun getSummaryText(value: String): String {
        return options[values.indexOf(value)]
    }
}
