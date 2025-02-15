package com.codetech.texttoimage.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codetech.texttoimage.databinding.ActivityMainBinding
import com.codetech.texttoimage.util.Extension.newScreen

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        clickListeners()
    }

    private fun clickListeners(){
        binding.apply {
            flux.setOnClickListener {
                newScreen(FluxActivity::class.java)
            }
            president.setOnClickListener {
                newScreen(PresidentActivity::class.java)
            }
            stable.setOnClickListener {
                newScreen(StableActivity::class.java)
            }
        }
    }
}