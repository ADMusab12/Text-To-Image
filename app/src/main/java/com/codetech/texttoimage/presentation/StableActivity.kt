package com.codetech.texttoimage.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.codetech.texttoimage.R
import com.codetech.texttoimage.abstraction.HuggingFaceImageGenerator
import com.codetech.texttoimage.databinding.ActivityStableBinding
import com.codetech.texttoimage.util.Extension.gone
import com.codetech.texttoimage.util.Extension.loadImage
import com.codetech.texttoimage.util.Extension.showMessage
import com.codetech.texttoimage.util.Extension.visible

class StableActivity : AppCompatActivity() {
    private val binding by lazy { ActivityStableBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        binding.buttonGenerate.setOnClickListener {
            onButtonClickGenerate()
        }
    }

    private fun onButtonClickGenerate() {
        val prompt = binding.etPrompt.text.toString()
        if (prompt.isNotEmpty()) {
            binding.progress.visible()
            binding.buttonGenerate.gone()
            generateImage(prompt)
        } else {
            showMessage("Please enter prompt")
        }
    }

    private fun generateImage(prompt: String) {
        val imageGenerator = HuggingFaceImageGenerator()
        imageGenerator.generateImage(prompt,"stable") { bitmap ->
            if (bitmap != null) {
                binding.progress.gone()
                binding.buttonGenerate.visible()
                loadImage(binding.imageview, bitmap)
            } else {
                binding.progress.gone()
                binding.buttonGenerate.visible()
                Log.d("HuggingFaceImageGeneratorInfo", "Failed to generate image")
            }
        }
    }
}