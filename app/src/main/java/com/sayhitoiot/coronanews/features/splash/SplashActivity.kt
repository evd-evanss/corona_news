package com.sayhitoiot.coronanews.features.splash

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.commom.realm.RealmDB
import com.sayhitoiot.coronanews.features.home.HomeActivity
import com.sayhitoiot.coronanews.features.login.view.LoginActivity
import com.sayhitoiot.coronanews.features.splash.contract.SplashPresenterToView
import com.sayhitoiot.coronanews.features.splash.contract.SplashViewToPresenter
import com.sayhitoiot.coronanews.features.splash.presenter.SplashPresenter

class SplashActivity : AppCompatActivity(), SplashViewToPresenter {

    private val presenter: SplashPresenterToView by lazy {
        SplashPresenter(this)
    }

    override val activity: Activity?
        get() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        presenter.onCreate()
    }

    override fun configureDataBase() {
        activity?.runOnUiThread {
            RealmDB.configureRealm(applicationContext)
        }
    }

    override fun loginWithUserActive() {
        activity?.runOnUiThread {
            val handle = Handler()
            handle.postDelayed(Runnable {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            },
                2000
            )
        }
    }

    override fun requestActivityLogin() {
        activity?.runOnUiThread {
            val handle = Handler()
            handle.postDelayed(Runnable {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            },
                2000
            )
        }
    }
}
