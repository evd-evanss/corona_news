package com.sayhitoiot.coronanews.features.sign.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.features.policy.ActivityReader
import com.sayhitoiot.coronanews.features.sign.cases.SignUseCase
import com.sayhitoiot.coronanews.features.sign.viewmodel.DelegateToSignActivity
import com.sayhitoiot.coronanews.features.sign.viewmodel.ViewModelSign
import com.sayhitoiot.coronanews.features.sign.viewmodel.ViewModelSignFactory
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity(), DelegateToSignActivity{

    companion object {
        const val TAG = "sign-activity"
        const val REQUEST_CODE = 100
        var ACCEPT: Boolean = false
    }

    private val factory = ViewModelSignFactory(this, SignUseCase())

    private val viewModel: ViewModelSign by lazy {
        ViewModelProvider(this, factory).get(ViewModelSign::class.java)
    }

    private var edtNome: EditText? = null
    private var edtDay: EditText? = null
    private var edtMonth: EditText? = null
    private var edtYear: EditText? = null
    private var edtEmail: EditText? = null
    private var edtPassword: EditText? = null
    private var edtConfirm: EditText? = null
    private var buttonBack: ImageView? = null
    private var buttonSign: MaterialButton? = null
    private var buttonTerms: MaterialButton? = null
    private var progress: DilatingDotsProgressBar? = null

    override var name: String?
        get() = edtNome?.text?.toString()
        set(value) {}

    override var day: String?
        get() = edtDay?.text?.toString()
        set(value) {}

    override var month: String?
        get() = edtMonth?.text?.toString()
        set(value) {}

    override var year: String?
        get() = edtYear?.text?.toString()
        set(value) {}

    override var email: String?
        get() = edtEmail?.text?.toString()
        set(value) {}

    override var password: String?
        get() = edtPassword?.text?.toString()
        set(value) {}

    override var confirmPassword: String?
        get() = edtConfirm?.text?.toString()
        set(value) {}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()
        initUi()
    }

    private fun initUi() {
        this.runOnUiThread {
            progress = activitySignUp_dilatingDotsProgressBar
            edtNome = activitySignUp_editText_name
            edtDay = activitySignUp_editText_day
            edtMonth = activitySignUp_editText_month
            edtYear = activitySignUp_editText_year
            edtEmail = activitySignUp_editText_email
            edtPassword = activitySignUp_editText_password
            edtConfirm = activitySignUp_editText_confirm
            buttonBack = activitySignUp_imageView_back
            buttonBack?.setOnClickListener { onBackPressed() }
            buttonSign = activitySignUp_materialButton_signUp
            buttonSign?.setOnClickListener { viewModel.buttonSignTapped() }
            buttonTerms = activitySignUp_materialButton_terms
            buttonTerms?.setOnClickListener { viewModel.buttonTermsTapped() }
        }
    }

    override fun onResume() {
        super.onResume()
        observerOnSuccess()
        observerOnFail()
    }

    override fun startActivityTerms() {
        startActivityForResult(Intent(this, ActivityReader::class.java), REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            if(data != null) {
                viewModel.accept = data.getBooleanExtra("accept", false)
            }
        }
    }

    override fun renderViewsForProgress() {
        progress?.show()
        buttonSign?.visibility = INVISIBLE
    }

    override fun renderViewsForProgressDefault() {
        progress?.hide()
        buttonSign?.visibility = VISIBLE
    }

    override fun showErrorInName(error: String) {
        edtNome?.error = error
    }

    override fun showErrorInDay(error: String) {
        edtDay?.error = error
    }

    override fun showErrorInMonth(error: String) {
        edtMonth?.error = error
    }

    override fun showErrorInYear(error: String) {
        edtYear?.error = error
    }

    override fun showErrorInEmail(error: String) {
        edtEmail?.error = error
    }

    override fun showErrorInPassword(error: String) {
        edtPassword?.error = error
    }

    override fun showErrorInConfirmPassword(error: String) {
        edtConfirm?.error = error
    }

    private fun observerOnSuccess() {
        viewModel.messageSuccess?.observe(
            this as LifecycleOwner,
            androidx.lifecycle.Observer {messageSuccess ->
                progress?.hide()
                buttonSign?.visibility = VISIBLE
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Sua conta foi criada com sucesso")
                builder.setMessage(messageSuccess)
                builder.setPositiveButton(
                    "Voltar e fazer login"
                ) { _, _ ->
                    finish()
                }
                builder.show()
            }
        )
    }

    private fun observerOnFail() {
        viewModel.messageFail?.observe(
            this as LifecycleOwner,
            androidx.lifecycle.Observer {messageError ->
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Falha ao criar conta")
                builder.setMessage(messageError)
                builder.setPositiveButton(
                    "Voltar"
                ) { _, _ ->
                    finish()
                }
                builder.show()
            }
        )

    }

    override fun showAlertInTermsAnConditions() {
        buttonTerms?.requestFocus()
        Toast.makeText(
            this,
            "Para se cadastrar e usar o aplicativo você precisa aceitar os termos",
            Toast.LENGTH_LONG
        ).show()
    }

}
