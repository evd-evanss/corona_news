package com.sayhitoiot.coronanews.features.home.profile.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.features.home.profile.presenter.ProfilePresenter
import com.sayhitoiot.coronanews.features.home.profile.presenter.contract.ProfilePresenterToView
import com.sayhitoiot.coronanews.features.home.profile.presenter.contract.ProfileViewToPresenter
import com.sayhitoiot.coronanews.features.login.view.LoginActivity
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(), ProfileViewToPresenter {

    private val presenter: ProfilePresenterToView by lazy {
        ProfilePresenter(this)
    }

    override val activity: Activity? get() = requireActivity()

    private var textName: TextView? = null
    private var textEmail: TextView? = null
    private var textBirthdate: TextView? = null
    private var buttonEdit: MaterialButton? = null
    private var buttonLogout: ImageView? = null
    private var progress: DilatingDotsProgressBar? = null
    private var firstContainer: TableLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onViewCreated()
    }

    override fun initializeViews() {
        activity?.runOnUiThread {
            textName = profile_textView_name
            textEmail = profile_textView_email
            textBirthdate = profile_textView_birthdate
            buttonEdit = profile_materialButton_edit
            buttonLogout = profile_imageView_logout
            buttonLogout?.setOnClickListener { presenter.buttonLogoutTapped() }
            progress = profile_dilatingDots_progress
            progress?.show()
            firstContainer = tableLayout
            tableLayout?.visibility = INVISIBLE
        }
    }

    override fun logout() {
        activity?.runOnUiThread {
            activity?.startActivity(Intent(activity, LoginActivity::class.java))
            activity?.finish()
        }
    }

    override fun renderViewWithDataUser(name: String, email: String, birth: String) {
        activity?.runOnUiThread {
            progress?.hide()
            tableLayout?.visibility = VISIBLE
            textName?.text = name
            textEmail?.text = email
            textBirthdate?.text = birth
        }
    }
}
