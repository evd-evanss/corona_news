package com.sayhitoiot.coronanews

import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    companion object {
        const val TAG = "login-activity"
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
