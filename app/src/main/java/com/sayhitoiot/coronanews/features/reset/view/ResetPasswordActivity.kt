package com.sayhitoiot.coronanews.features.reset.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.features.reset.repository.RepositoryReset
import com.sayhitoiot.coronanews.features.reset.viewmodel.DelegateToResetActivity
import com.sayhitoiot.coronanews.features.reset.viewmodel.ViewModelReset
import com.sayhitoiot.coronanews.features.reset.viewmodel.ViewModelResetFactory
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPasswordActivity : AppCompatActivity(), DelegateToResetActivity {


    private val factory = ViewModelResetFactory(this, RepositoryReset())
    private val viewModel: ViewModelReset by lazy {
        ViewModelProvider(this, factory).get(ViewModelReset::class.java)
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
        initializeViews()
    }

    override fun onResume() {
        super.onResume()
        observerOnComplete()
    }

    private fun initializeViews() {
        editTextEmail = activityReset_editText_email
        progress = activityReset_dilatingDotsProgressBar
        resetButton = activityReset_materialButton_reset
        resetButton?.setOnClickListener {
            progress?.show()
            resetButton?.visibility = INVISIBLE
            viewModel.resetButtonTapped()
        }
        buttonBack = activityReset_imageView_back
        buttonBack?.setOnClickListener { onBackPressed() }
    }

    override fun renderMessageOnError(fail: String) {
        progress?.hide()
        resetButton?.visibility = VISIBLE
        editTextEmail?.error = fail
    }

    private fun observerOnComplete() {
        viewModel.messageSuccess?.observe(this as LifecycleOwner, androidx.lifecycle.Observer { message ->
            progress?.hide()
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            resetButton?.visibility = VISIBLE
            }
        )
    }

}
