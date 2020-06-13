package com.sayhitoiot.coronanews.features.login.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.features.HomeActivity
import com.sayhitoiot.coronanews.features.login.cases.LoginUseCase
import com.sayhitoiot.coronanews.features.login.viewmodel.DelegateToLoginActivity
import com.sayhitoiot.coronanews.features.login.viewmodel.ViewModelLogin
import com.sayhitoiot.coronanews.features.login.viewmodel.ViewModelLoginFactory
import com.sayhitoiot.coronanews.features.reset.ui.ResetPasswordActivity
import com.sayhitoiot.coronanews.features.sign.ui.SignUpActivity
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), DelegateToLoginActivity {

    companion object {
        const val TAG = "login-activity"
    }

    private val factory = ViewModelLoginFactory(this, LoginUseCase())
    private val viewModel: ViewModelLogin by lazy {
        ViewModelProvider(this, factory).get(ViewModelLogin::class.java)
    }

    private val activity: Activity? get() = this
    override val email: String? get() = edtEmail?.text.toString()
    override val password: String? get() = edtPassword?.text.toString()

    private var edtEmail: EditText? = null
    private var edtPassword: EditText? = null
    private var btnLogin: MaterialButton? = null
    private var btnSignUp: MaterialButton? = null
    private var progressBar: DilatingDotsProgressBar? = null
    private var textForgetPassword: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        initUi()
    }

    override fun onResume() {
        super.onResume()
        observerLogin()
    }

    private fun initUi() {
        activity?.runOnUiThread {
            edtEmail = activityLogin_editText_email
            edtPassword = activityLogin_editText_password
            btnLogin = loginActivity_materialButton_login
            btnSignUp = loginActivity_materialButton_signUp
            progressBar = loginActivity_dilatingDotsProgressBar
            textForgetPassword = loginActivity_textView_resetPassword
            btnLogin?.setOnClickListener {
                viewModel.btnLoginTapped()
            }
            btnSignUp?.setOnClickListener { viewModel.btnSignUpTapped() }
            textForgetPassword?.setOnClickListener { viewModel.forgetPasswordTapped() }
        }
    }

    private fun observerLogin() {
        viewModel.login.observe(this as LifecycleOwner, androidx.lifecycle.Observer {
            if(it) {
                loginSuccess()
            }
        })
        viewModel.message?.observe(this as LifecycleOwner, androidx.lifecycle.Observer {
            it?.let { it1 -> loginFail(it1) }
        })
    }

    override fun renderViewForLogin() {
        activity?.runOnUiThread {
            progressBar?.show()
            btnLogin?.visibility = INVISIBLE
        }
    }

    override fun renderViewForResetPassword() {
        startActivity(Intent(this, ResetPasswordActivity::class.java))
    }

    override fun startSignUpActivity() {
        activity?.runOnUiThread {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    override fun showErrorInEmail(error: String) {
        activity?.runOnUiThread {
            edtEmail?.error = error
        }
    }

    override fun showErrorInPassword(error: String) {
        activity?.runOnUiThread {
            edtPassword?.error = error
        }
    }

    private fun loginSuccess() {
        activity?.runOnUiThread {
            progressBar?.hide()
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    private fun loginFail(fail: String) {
        activity?.runOnUiThread {
            progressBar?.hide()
            btnLogin?.visibility = VISIBLE
            Toast.makeText(
                this@LoginActivity, fail,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}
