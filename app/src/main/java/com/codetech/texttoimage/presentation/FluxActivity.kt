package com.codetech.texttoimage.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.codetech.texttoimage.abstraction.HuggingFaceImageGenerator
import com.codetech.texttoimage.databinding.ActivityFluxBinding
import com.codetech.texttoimage.util.Extension.gone
import com.codetech.texttoimage.util.Extension.loadImage
import com.codetech.texttoimage.util.Extension.showMessage
import com.codetech.texttoimage.util.Extension.visible

class FluxActivity : AppCompatActivity() {
    private val binding by lazy { ActivityFluxBinding.inflate(layoutInflater) }
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
        imageGenerator.generateImage(prompt,"flux") { bitmap ->
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