package com.beatareka.jwtlogin.view.binding

import android.view.View
import androidx.databinding.BindingConversion

object BindingConverter {

    @BindingConversion
    @JvmStatic
    fun booleanToVisibility(isVisible: Boolean): Int {
        return if (isVisible) View.VISIBLE else View.GONE
    }
}
