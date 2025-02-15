package com.codetech.texttoimage.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide

object Extension {
    fun Activity.showMessage(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    fun Activity.newScreen(
        c: Class<*>,
        isFinish: Boolean = false,
        clearIntentStack: Boolean = false,
    ) {
        if (clearIntentStack) {
            val intent = Intent(this, c)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        } else {
            startActivity(Intent(this, c))
            if (isFinish) finish()
        }
    }

    fun Activity.loadImage(imageView: ImageView, imageUrl: Bitmap) {
        Glide.with(imageView.context)
            .load(imageUrl)
            .into(imageView)
    }

    fun View.visible(){
        this.visibility = View.VISIBLE
    }

    fun View.gone(){
        this.visibility = View.GONE
    }
}