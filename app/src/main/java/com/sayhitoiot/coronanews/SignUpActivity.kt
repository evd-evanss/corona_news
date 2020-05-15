package com.sayhitoiot.coronanews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private var buttonBack: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        initializeViews()
    }

    private fun initializeViews() {
        buttonBack = activitySignUp_imageView_back
        buttonBack?.setOnClickListener { onBackPressed() }
    }
}
