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
    val options: Array<String>
) : AbstractBindingItem<GeneralItemBinding>() {

    override val type: Int = R.id.general_item_mai
    val helper = PreferencesHelper(context)
    
    override fun bindView(
        binding: GeneralItemBinding,
        payloads: List<Any>
    ) {
        binding.title.text = name
        binding.summary.text = default
        binding.root.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(name)
            builder.setMessage(desc)
            builder.setSingleChoiceItems(
                options,
                options.indexOf(getValue()).let { if (it == -1) default else it }
            )
            builder.setPositiveButton(R.string.ok) { dialog, which ->
                val position = (dialog as AlertDialog).listView.checkedItemPosition
                helper.putString(options[position])
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

    fun getValue(): String {
        return helper.getString(key) ?: default
    }
}
