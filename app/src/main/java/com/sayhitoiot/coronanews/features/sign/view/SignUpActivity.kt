package com.sayhitoiot.coronanews.features.sign.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.features.sign.contract.SignUpPresenterToView
import com.sayhitoiot.coronanews.features.sign.contract.SignUpViewToPresenter
import com.sayhitoiot.coronanews.features.sign.presenter.SignUpPresenter
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity(), SignUpViewToPresenter{

    companion object {
        const val TAG = "sign-activity"
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
        presenter.onCreate()
        initializeViews()
    }

    override fun initializeViews() {
        this.runOnUiThread {
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
        }
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

    override fun sendDataForActivityResult(data: ArrayList<String>) {
        activity?.runOnUiThread {
            val intent = Intent()
            intent.putExtra("MESSAGE", data)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun showMessageOnFail(messageError: String) {
        activity?.runOnUiThread {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Falha ao criar conta")
            builder.setMessage(messageError)
            builder.setPositiveButton(
                "Sair"
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
