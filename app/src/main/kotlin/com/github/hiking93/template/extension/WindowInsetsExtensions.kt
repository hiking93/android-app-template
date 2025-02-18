package com.github.hiking93.template.extension

import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsAnimationCompat.Callback.DISPATCH_MODE_STOP
import androidx.core.view.WindowInsetsAnimationCompat.Callback.DispatchMode
import androidx.core.view.WindowInsetsCompat

fun WindowInsetsCompat.getContentInsets(): Insets {
    return getInsets(
        WindowInsetsCompat.Type.systemBars() or
                WindowInsetsCompat.Type.displayCutout() or
                WindowInsetsCompat.Type.ime(),
    )
}

fun WindowInsetsCompat.consumeContentInsets(
    left: Boolean = false,
    top: Boolean = false,
    right: Boolean = false,
    bottom: Boolean = false,
): WindowInsetsCompat {
    fun WindowInsetsCompat.Builder.consume(mask: Int): WindowInsetsCompat.Builder {
        val insets = this@consumeContentInsets.getInsets(mask)
        return setInsets(
            mask,
            Insets.of(
                if (left) 0 else insets.left,
                if (top) 0 else insets.top,
                if (right) 0 else insets.right,
                if (bottom) 0 else insets.bottom,
            ),
        )
    }
    return WindowInsetsCompat.Builder(this)
        .consume(WindowInsetsCompat.Type.systemBars())
        .consume(WindowInsetsCompat.Type.displayCutout())
        .consume(WindowInsetsCompat.Type.ime())
        .build()
}

fun View.doOnWindowInsetsChanged(
    listenToAnimation: Boolean = false,
    @DispatchMode dispatchMode: Int = DISPATCH_MODE_STOP,
    callback: (v: View, insets: WindowInsetsCompat) -> WindowInsetsCompat,
) {
    var isAnimationRunning = false
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        if (isAnimationRunning) insets else callback(v, insets)
    }
    if (listenToAnimation) {
        ViewCompat.setWindowInsetsAnimationCallback(
            this,
            object : WindowInsetsAnimationCompat.Callback(dispatchMode) {

                override fun onPrepare(animation: WindowInsetsAnimationCompat) {
                    super.onPrepare(animation)
                    isAnimationRunning = true
                }

                override fun onProgress(
                    insets: WindowInsetsCompat,
                    runningAnimations: MutableList<WindowInsetsAnimationCompat>,
                ) = callback(this@doOnWindowInsetsChanged, insets)

                override fun onEnd(animation: WindowInsetsAnimationCompat) {
                    super.onEnd(animation)
                    isAnimationRunning = false
                }
            }
        )
    }
}