package com.prayer.times.ui.controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.prayer.times.databinding.QiblahBinding
import com.prayer.times.ui.controller.base.BaseController

class QiblahController : BaseController<QiblahBinding>() {
    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup
    ): View {
        val binding = QiblahBinding.inflate(inflater)
        return binding.root
    }
}
