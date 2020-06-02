package com.sayhitoiot.coronanews.features.profile.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.button.MaterialButton
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.features.profile.presenter.ProfilePresenter
import com.sayhitoiot.coronanews.features.profile.presenter.contract.ProfilePresenterToView
import com.sayhitoiot.coronanews.features.profile.presenter.contract.ProfileViewToPresenter
import com.sayhitoiot.coronanews.features.login.view.LoginActivity
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar
import kotlinx.android.synthetic.main.edit_user.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(), ProfileViewToPresenter {

    private val presenter: ProfilePresenterToView by lazy {
        ProfilePresenter(this)
    }

    override val activity: Activity? get() = requireActivity()
    override val name: String? get() = textEditName?.text.toString()
    override val day: String? get() = textEditDay?.text.toString()
    override val month: String? get() = textEditMonth?.text.toString()
    override val year: String? get() = textEditYear?.text.toString()

    private var textName: TextView? = null
    private var textEmail: TextView? = null
    private var textBirthdate: TextView? = null
    private var buttonEdit: MaterialButton? = null
    private var buttonLogout: ImageView? = null
    private var progress: DilatingDotsProgressBar? = null
    private var firstContainer: TableLayout? = null
    private var secondContainer: LinearLayout? = null
    private var textEditName: EditText? = null
    private var textEditDay: EditText? = null
    private var textEditMonth: EditText? = null
    private var textEditYear: EditText? = null
    private var buttonChange: MaterialButton? = null
    private var buttonBack: ImageView? = null

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
            buttonEdit?.setOnClickListener { presenter.buttonEditTapped() }
            buttonLogout = profile_imageView_logout
            buttonLogout?.setOnClickListener { presenter.buttonLogoutTapped() }
            progress = profile_dilatingDots_progress
            progress?.show()
            firstContainer = tableLayout
            firstContainer?.visibility = INVISIBLE
            secondContainer = container_edit
            textEditName = custom_dialog_editText_name
            textEditDay = custom_dialog_editText_day
            textEditMonth = custom_dialog_editText_month
            textEditYear = custom_dialog_editText_year
            buttonChange = custom_dialog__materialButton_confirm
            buttonChange?.setOnClickListener { presenter.buttonChangeTapped() }
            buttonBack = custom_dialog_imageView_back
            buttonBack?.setOnClickListener { presenter.buttonBackTapped() }
        }
    }

    override fun renderSecondContainer() {
        activity?.runOnUiThread {
            firstContainer?.visibility = GONE
            secondContainer?.visibility = VISIBLE
        }
    }

    override fun renderFirstContainer() {
        activity?.runOnUiThread {
            firstContainer?.visibility = VISIBLE
            secondContainer?.visibility = GONE
        }
    }

    override fun showErrorInDay(messageError: String) {
        activity?.runOnUiThread {
            textEditDay?.error = messageError
        }
    }

    override fun showErrorInMonth(messageError: String) {
        activity?.runOnUiThread {
            textEditMonth?.error = messageError
        }
    }

    override fun showErrorInYear(messageError: String) {
        activity?.runOnUiThread {
            textEditYear?.error = messageError
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
