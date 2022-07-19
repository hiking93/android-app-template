package com.github.hiking93.template.extension

import android.app.Activity
import androidx.core.view.WindowCompat

fun Activity.applyEdgeToEdge() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
}