package com.sayhitoiot.coronanews.features.sign.view

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
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.features.policy.ActivityReader
import com.sayhitoiot.coronanews.features.sign.presenter.SignUpPresenter
import com.sayhitoiot.coronanews.features.sign.presenter.contract.SignUpPresenterToView
import com.sayhitoiot.coronanews.features.sign.presenter.contract.SignUpViewToPresenter
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity(), SignUpViewToPresenter{

    companion object {
        const val TAG = "sign-activity"
        const val REQUEST_CODE = 100
        var ACCEPT: Boolean = false
    }

    private val presenter: SignUpPresenterToView by lazy {
        SignUpPresenter(this)
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

    override val activity: Activity?
        get() = this
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

    override var mAuth: FirebaseAuth? = null
        get() = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()
        presenter.onCreate()
    }

    override fun initializeViews() {
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
            buttonBack?.setOnClickListener { presenter.buttonBackTapped() }
            buttonSign = activitySignUp_materialButton_signUp
            buttonSign?.setOnClickListener { presenter.buttonSignTapped() }
            buttonTerms = activitySignUp_materialButton_terms
            buttonTerms?.setOnClickListener { presenter.buttonTermsTapped() }
        }
    }

    override fun showAlertInTermsAnConditions() {
        buttonTerms?.requestFocus()
        Toast.makeText(
            activity,
            "Para se cadastrar e usar o aplicativo você precisa aceitar os termos",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun startActivityTerms() {
        startActivityForResult(Intent(this, ActivityReader::class.java), REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            if(data != null) {
                ACCEPT = data.getBooleanExtra("accept", false)
                Toast.makeText(
                    activity,
                    "Usuario aceitou os termos $ACCEPT",
                    Toast.LENGTH_LONG
                ).show()
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

    override fun showErrorInName(messageError: String) {
        activity?.runOnUiThread {
            edtNome?.error = messageError
        }
    }

    override fun showErrorInDay(messageError: String) {
        activity?.runOnUiThread {
            edtDay?.error = messageError
        }
    }

    override fun showErrorInMonth(messageError: String) {
        activity?.runOnUiThread {
            edtMonth?.error = messageError
        }
    }

    override fun showErrorInYear(messageError: String) {
        activity?.runOnUiThread {
            edtYear?.error = messageError
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

    override fun showErrorInConfirmPassword(messageError: String) {
        activity?.runOnUiThread {
            edtConfirm?.error = messageError
        }
    }

    override fun showMessageOnSuccess(messageSuccess: String) {
        progress?.hide()
        buttonSign?.visibility = VISIBLE
        activity?.runOnUiThread {
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
    }

    override fun showMessageOnFail(messageError: String) {
        activity?.runOnUiThread {
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
    }

    override fun callPreviousActivity() {
        this.runOnUiThread {
            onBackPressed()
        }
    }
}
