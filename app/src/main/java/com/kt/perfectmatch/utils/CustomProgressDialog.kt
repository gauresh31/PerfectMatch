package com.kt.perfectmatch.utils

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.kt.perfectmatch.R

class CustomProgressDialog {

    private var dialog: ProgressDialog? = null

    fun CustomProgressDialog() {
    }

    fun showDialog(c: Activity?) {
        dialog = object : ProgressDialog(c, R.style.full_screen_dialog) {
            override fun onCreate(savedInstanceState: Bundle) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.custom_progress_bar)
                window!!.setLayout(
                    ActionBar.LayoutParams.FILL_PARENT,
                    ActionBar.LayoutParams.FILL_PARENT
                )
                val imageView = findViewById<ImageView>(R.id.progressBar)
                val StoryAnimation = AnimationUtils.loadAnimation(c, R.anim.fade_anim)
                imageView.startAnimation(StoryAnimation)
                val tvMessage = findViewById<TextView>(R.id.progress_message)
                //tvMessage.setText(message);
            }
        }
        (dialog as ProgressDialog).setCancelable(false)
        if (null != dialog) dialog!!.show()
    }

    fun destroy() {
        if (null != dialog) dialog!!.dismiss()
    }
}