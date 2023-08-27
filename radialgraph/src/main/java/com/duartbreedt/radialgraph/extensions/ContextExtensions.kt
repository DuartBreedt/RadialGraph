package com.duartbreedt.radialgraph.extensions

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

internal fun Context.getColorCompat(@ColorRes colorRes: Int) = ContextCompat.getColor(this, colorRes)