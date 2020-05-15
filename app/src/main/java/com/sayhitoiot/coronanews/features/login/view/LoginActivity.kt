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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.features.login.contract.LoginPresenterToView
import com.sayhitoiot.coronanews.features.login.contract.LoginViewToPresenter
import com.sayhitoiot.coronanews.features.login.presenter.LoginPresenter
import com.sayhitoiot.coronanews.features.sign.view.SignUpActivity
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(), LoginViewToPresenter {

    companion object {
        const val TAG = "login-activity"
        const val REQUEST_CODE = 100
    }

    private val presenter: LoginPresenterToView by lazy {
        LoginPresenter(this)
    }

    override val activity: Activity?
        get() = this

    override var mAuth: FirebaseAuth?
        get() = FirebaseAuth.getInstance()
        set(value) {}

    override val currentUser: FirebaseUser?
        get() = mAuth?.currentUser

    override val email: String?
        get() = edtEmail?.text.toString()

    override val password: String?
        get() = edtPassword?.text.toString()

    private var edtEmail: EditText? = null
    private var edtPassword: EditText? = null
    private var btnLogin: MaterialButton? = null
    private var btnSignUp: MaterialButton? = null
    private var progressBar: DilatingDotsProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        presenter.onCreate()
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
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

    override fun startActivityForResult() {
        activity?.runOnUiThread {
            progressBar?.show()
            val intent = Intent(this, SignUpActivity::class.java)
            startActivityForResult(intent,
                REQUEST_CODE
            )
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
            Toast.makeText(
                this@LoginActivity, "Login realizado com sucesso",
                Toast.LENGTH_SHORT
            ).show()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            btnLogin?.visibility = INVISIBLE
            val message = data?.getStringArrayListExtra("MESSAGE")
            if (message != null) {
                presenter.loginByActivityResult(
                    emailByActivityResult = message[0],
                    passwordByActivityResult = message[1]
                )
            }
        } else {
            progressBar?.hide()
            btnLogin?.visibility = VISIBLE
        }
    }

    override fun loginWithCurrentUser() {
        activity?.runOnUiThread {
            Toast.makeText(
                this@LoginActivity, "Olá ${currentUser?.email}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}
