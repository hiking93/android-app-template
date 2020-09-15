package com.hiking.template

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.hiking.template.extension.applyEdgeToEdge
import com.hiking.template.extension.dpToPxSize
import kotlinx.android.synthetic.main.activity_main.*
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupWindow()
    }

    private fun setupWindow() {
        applyEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { v, insets ->
            textView.text = makeContent(insets)
            rootLayout.updatePadding(
                left = insets.systemWindowInsetLeft,
                right = insets.systemWindowInsetRight
            )
            contentLayout.updatePadding(
                bottom = insets.systemWindowInsetBottom + 16f.dpToPxSize(v.context)
            )
            WindowInsetsCompat.Builder(insets)
                .setSystemWindowInsets(Insets.of(0, insets.systemWindowInsetTop, 0, 0))
                .build()
        }
    }

    private fun makeContent(insets: WindowInsetsCompat): CharSequence {
        val displayDensity = resources.displayMetrics.density
        val displayWidth = resources.displayMetrics.widthPixels
        val displayHeight = resources.displayMetrics.heightPixels
        val systemWindowInsets = insets.systemWindowInsets
        val totalMemory = ActivityManager.MemoryInfo().apply {
            (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getMemoryInfo(this)
        }.totalMem

        return """
            Manufacturer: ${Build.MANUFACTURER}
            Model: ${Build.MODEL}
            Brand: ${Build.BRAND}
            Hardware: ${Build.HARDWARE}
            Board: ${Build.BOARD}
            Build ID: ${Build.DISPLAY}
            CPU ABI: ${Build.SUPPORTED_ABIS.joinToString(", ")}
            Memory: ${NumberFormat.getInstance().format(totalMemory)}B
            
            Fingerprint
            ${Build.FINGERPRINT}
            
            OS
            API Level: ${Build.VERSION.SDK_INT}
            Codename: ${Build.VERSION.CODENAME}
            Incremental: ${Build.VERSION.INCREMENTAL}
            Release: ${Build.VERSION.RELEASE}
            
            Display
            Density: $displayDensity
            Width: ${displayWidth.let { "${it}px (${it / displayDensity}dp)" }}
            Height: ${displayHeight.let { "${it}px, (${it / displayDensity}dp)" }}
            
            System window insets
            Top: ${systemWindowInsets.top.let { "${it}px (${it / displayDensity}dp)" }}
            Bottom: ${systemWindowInsets.bottom.let { "${it}px (${it / displayDensity}dp)" }}
            Left: ${systemWindowInsets.left.let { "${it}px (${it / displayDensity}dp)" }}
            Right: ${systemWindowInsets.right.let { "${it}px (${it / displayDensity}dp)" }}
        """.trimIndent()
    }
}
