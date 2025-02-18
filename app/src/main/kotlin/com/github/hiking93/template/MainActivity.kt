package com.github.hiking93.template

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.github.hiking93.template.base.ViewBindingActivity
import com.github.hiking93.template.databinding.ActivityMainBinding
import com.github.hiking93.template.extension.consumeContentInsets
import com.github.hiking93.template.extension.doOnWindowInsetsChanged
import com.github.hiking93.template.extension.dpToPxSize
import com.github.hiking93.template.extension.getContentInsets
import java.text.NumberFormat

class MainActivity : ViewBindingActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupWindow()
    }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ActivityMainBinding.inflate(inflater, container, false)

    private fun setupWindow() {
        enableEdgeToEdge()
        binding.root.doOnWindowInsetsChanged { v, insets ->
            val contentInsets = insets.getContentInsets()
            binding.root.updatePadding(
                left = contentInsets.left,
                right = contentInsets.right,
            )
            binding.appBarLayout.updatePadding(
                top = contentInsets.top,
            )
            binding.contentLayout.updatePadding(
                bottom = contentInsets.bottom + 16f.dpToPxSize(v.context)
            )
            binding.textView.text = makeContent(insets)
            insets.consumeContentInsets(left = true, top = true, right = true)
        }
    }

    private fun makeContent(windowInsets: WindowInsetsCompat): CharSequence {
        val displayDensity = resources.displayMetrics.density
        val displayWidth = resources.displayMetrics.widthPixels
        val displayHeight = resources.displayMetrics.heightPixels
        val totalMemory = ActivityManager.MemoryInfo().apply {
            (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getMemoryInfo(this)
        }.totalMem

        val systemBarInsets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
        val displayCutoutInsets = windowInsets.getInsets(WindowInsetsCompat.Type.displayCutout())

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
            
            System bar insets
            Top: ${systemBarInsets.top.let { "${it}px (${it / displayDensity}dp)" }}
            Bottom: ${systemBarInsets.bottom.let { "${it}px (${it / displayDensity}dp)" }}
            Left: ${systemBarInsets.left.let { "${it}px (${it / displayDensity}dp)" }}
            Right: ${systemBarInsets.right.let { "${it}px (${it / displayDensity}dp)" }}
            
            Display cutout insets
            Top: ${displayCutoutInsets.top.let { "${it}px (${it / displayDensity}dp)" }}
            Bottom: ${displayCutoutInsets.bottom.let { "${it}px (${it / displayDensity}dp)" }}
            Left: ${displayCutoutInsets.left.let { "${it}px (${it / displayDensity}dp)" }}
            Right: ${displayCutoutInsets.right.let { "${it}px (${it / displayDensity}dp)" }}
        """.trimIndent()
    }
}
