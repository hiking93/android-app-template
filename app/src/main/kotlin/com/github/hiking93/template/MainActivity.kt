package com.github.hiking93.template

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.core.graphics.Insets
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.github.hiking93.template.base.ViewBindingActivity
import com.github.hiking93.template.databinding.ActivityMainBinding
import com.github.hiking93.template.extension.doOnWindowInsetsChanged
import com.github.hiking93.template.extension.dpToPxSize
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
            val systemWindowInsets = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()
            )
            binding.root.updatePadding(
                left = systemWindowInsets.left,
                right = systemWindowInsets.right,
            )
            binding.appBarLayout.updatePadding(
                top = systemWindowInsets.top,
            )
            binding.contentLayout.updatePadding(
                bottom = systemWindowInsets.bottom + 16f.dpToPxSize(v.context)
            )
            val systemBarInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.textView.text = makeContent(systemBarInsets)
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            WindowInsetsCompat.Builder(insets)
                .setInsets(
                    WindowInsetsCompat.Type.systemBars(),
                    Insets.of(0, 0, 0, systemBarInsets.bottom)
                )
                .setInsets(
                    WindowInsetsCompat.Type.ime(),
                    Insets.of(0, 0, 0, imeInsets.bottom)
                )
                .build()
        }
    }

    private fun makeContent(systemBarInsets: Insets): CharSequence {
        val displayDensity = resources.displayMetrics.density
        val displayWidth = resources.displayMetrics.widthPixels
        val displayHeight = resources.displayMetrics.heightPixels
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
            
            System bar insets
            Top: ${systemBarInsets.top.let { "${it}px (${it / displayDensity}dp)" }}
            Bottom: ${systemBarInsets.bottom.let { "${it}px (${it / displayDensity}dp)" }}
            Left: ${systemBarInsets.left.let { "${it}px (${it / displayDensity}dp)" }}
            Right: ${systemBarInsets.right.let { "${it}px (${it / displayDensity}dp)" }}
        """.trimIndent()
    }
}
