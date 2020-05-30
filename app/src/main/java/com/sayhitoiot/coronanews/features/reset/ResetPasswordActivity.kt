package com.sayhitoiot.coronanews.features.reset

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.features.reset.presenteer.ResetPasswordPresenter
import com.sayhitoiot.coronanews.features.reset.presenteer.contract.ResetPasswordPresenterToView
import com.sayhitoiot.coronanews.features.reset.presenteer.contract.ResetPasswordViewToPresenter
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPasswordActivity : AppCompatActivity(), ResetPasswordViewToPresenter {

    private val presenter: ResetPasswordPresenterToView by lazy {
        ResetPasswordPresenter(this)
    }

    override var email: String? get() = editTextEmail?.text.toString()
        set(value) {}
    private var resetButton: MaterialButton? = null
    private var editTextEmail: EditText? = null
    private var buttonBack: ImageView? = null
    private var progress: DilatingDotsProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        supportActionBar?.hide()
        presenter.onCreate()
    }

    override fun initializeViews() {
        editTextEmail = activityReset_editText_email
        progress = activityReset_dilatingDotsProgressBar
        resetButton = activityReset_materialButton_reset
        resetButton?.setOnClickListener {
            progress?.show()
            resetButton?.visibility = INVISIBLE
            presenter.resetButtonTapped()
        }
        buttonBack = activityReset_imageView_back
        buttonBack?.setOnClickListener { presenter.buttonBackTapped() }
    }

    override fun renderMessageOnError(fail: String) {
        progress?.hide()
        resetButton?.visibility = VISIBLE
        editTextEmail?.error = fail
    }

    override fun renderMessageOnComplete(message: String) {
        progress?.hide()
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        resetButton?.visibility = VISIBLE
    }

    override fun renderPreviousView() {
        onBackPressed()
        finish()
    }
}
