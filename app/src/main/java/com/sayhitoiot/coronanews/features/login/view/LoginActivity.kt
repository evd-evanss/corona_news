package com.sayhitoiot.coronanews.features.login.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.features.home.HomeActivity
import com.sayhitoiot.coronanews.features.login.contract.LoginPresenterToView
import com.sayhitoiot.coronanews.features.login.contract.LoginViewToPresenter
import com.sayhitoiot.coronanews.features.login.presenter.LoginPresenter
import com.sayhitoiot.coronanews.features.sign.view.SignUpActivity
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(), LoginViewToPresenter {

    companion object {
        const val TAG = "login-activity"
    }

    private val presenter: LoginPresenterToView by lazy {
        LoginPresenter(this)
    }

    override val activity: Activity? get() = this
    override val email: String? get() = edtEmail?.text.toString()
    override val password: String? get() = edtPassword?.text.toString()

    private var edtEmail: EditText? = null
    private var edtPassword: EditText? = null
    private var btnLogin: MaterialButton? = null
    private var btnSignUp: MaterialButton? = null
    private var progressBar: DilatingDotsProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        presenter.onCreate()
    }

    override fun initializeViews() {
        activity?.runOnUiThread {
            edtEmail = activityLogin_editText_email
            edtPassword = activityLogin_editText_password
            btnLogin = loginActivity_materialButton_login
            btnSignUp = loginActivity_materialButton_signUp
            progressBar = loginActivity_dilatingDotsProgressBar
            btnLogin?.setOnClickListener { presenter.btnLoginTapped() }
            btnSignUp?.setOnClickListener { presenter.btnSignUpTapped() }
        }
    }

    override fun renderViewForLogin() {
        activity?.runOnUiThread {
            progressBar?.show()
            btnLogin?.visibility = INVISIBLE
        }
    }

    override fun startSignUpActivity() {
        activity?.runOnUiThread {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    override fun showErrorInEmail(messageError: String) {
        activity?.runOnUiThread {
            edtEmail?.error = messageError
        }
    }

    override fun showErrorInPassword(messageError: String) {
        activity?.runOnUiThread {
            edtPassword?.error = messageError
        }
    }

    override fun loginSuccess() {
        activity?.runOnUiThread {
            progressBar?.hide()
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    override fun loginFail(messageError: String) {
        activity?.runOnUiThread {
            progressBar?.hide()
            btnLogin?.visibility = VISIBLE
            Toast.makeText(
                this@LoginActivity, messageError,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}
