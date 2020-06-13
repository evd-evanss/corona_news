package com.sayhitoiot.coronanews.features.splash.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.features.HomeActivity
import com.sayhitoiot.coronanews.features.login.ui.LoginActivity
import com.sayhitoiot.coronanews.features.splash.cases.RepositorySplash
import com.sayhitoiot.coronanews.features.splash.viewmodel.ViewModelSplash
import com.sayhitoiot.coronanews.features.splash.viewmodel.ViewModelSplashFactory

class SplashActivity : AppCompatActivity() {

    val factory = ViewModelSplashFactory(RepositorySplash())
    private val viewModel: ViewModelSplash by lazy {
        ViewModelProvider(this, factory).get(ViewModelSplash::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
    }

    override fun onResume() {
        super.onResume()
        observeLoginOnComplete()
    }

    private fun observeLoginOnComplete() {
        viewModel.login.observe(this as LifecycleOwner, androidx.lifecycle.Observer { login ->
            when(login) {
                true -> startActivityHome()
                else -> startActivityLogin()
            }
        })
    }

    private fun startActivityHome() {
        val handle = Handler()
        handle.postDelayed(Runnable {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            },2000
        )
    }

    private fun startActivityLogin() {
        val handle = Handler()
        handle.postDelayed(Runnable {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
             },2000
        )
    }

}
