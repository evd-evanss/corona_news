package com.sayhitoiot.coronanews.features.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.features.signup.view.SignUpActivity
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    companion object {
        const val TAG = "login-activity"
        const val REQUEST_CODE = 100
    }

    private var edtEmail: EditText? = null
    private var edtPassword: EditText? = null
    private var mAuth: FirebaseAuth? = null
    private var btnLogin: MaterialButton? = null
    private var btnSignUp: MaterialButton? = null
    private var progressBar: DilatingDotsProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        initializeViews()
    }

    private fun initializeViews() {
        edtEmail = activityLogin_editText_email
        edtPassword = activityLogin_editText_password
        btnLogin = loginActivity_materialButton_login
        btnSignUp = loginActivity_materialButton_signUp
        progressBar = loginActivity_dilatingDotsProgressBar
        initializeListeners()
    }

    private fun initializeListeners() {
        btnLogin?.setOnClickListener {
            progressBar?.show()
            btnLogin?.visibility = INVISIBLE
            signWithUserAndPassword(
                edtEmail?.text.toString(),
                edtPassword?.text.toString()
            )
        }
        btnSignUp?.setOnClickListener {
            progressBar?.show()
            val intent = Intent(this, SignUpActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            btnLogin?.visibility = INVISIBLE
            val message = data?.getStringArrayListExtra("MESSAGE")
            if (message != null) {
                signWithUserAndPassword(
                    email = message[0],
                    password = message[1]
                )
            }
        } else {
            progressBar?.hide()
            btnLogin?.visibility = VISIBLE
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth?.currentUser
        //updateUI(currentUser)
    }

    private fun signWithUserAndPassword(email: String, password: String) {
        mAuth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = mAuth?.currentUser
                    progressBar?.hide()
                    finish()
                    //updateUI(user)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    progressBar?.hide()
                    btnLogin?.visibility = VISIBLE
                    Toast.makeText(
                        this@LoginActivity, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    //updateUI(null)
                }
            }
    }

}