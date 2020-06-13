package com.sayhitoiot.coronanews.features.profile.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.features.login.ui.LoginActivity
import com.sayhitoiot.coronanews.features.profile.cases.ProfileUseCase
import com.sayhitoiot.coronanews.features.profile.viewmodel.ViewModelProfile
import com.sayhitoiot.coronanews.features.profile.viewmodel.ViewModelProfileFactory
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.Dispatchers.IO

class ProfileFragment : Fragment() {

    private var textName: TextView? = null
    private var textEmail: TextView? = null
    private var textBirthdate: TextView? = null
    private var textDelete: TextView? = null
    private var buttonLogout: ImageView? = null
    private var progress: DilatingDotsProgressBar? = null
    private var firstContainer: TableLayout? = null

    private val factory = ViewModelProfileFactory(profileUseCase = ProfileUseCase(IO))

    private val viewModel: ViewModelProfile by lazy {
        ViewModelProvider(this, factory).get(ViewModelProfile::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initializeListeners()
    }

    private fun initUi() {
        activity?.runOnUiThread {
            textName = profile_textView_name
            textEmail = profile_textView_email
            textBirthdate = profile_textView_birthdate
            textDelete = profile_textView_exclude
            buttonLogout = profile_imageView_logout
            progress = profile_dilatingDots_progress
            progress?.show()
            firstContainer = tableLayout
            firstContainer?.visibility = INVISIBLE
        }
    }

    private fun initializeListeners() {
        buttonLogout?.setOnClickListener { viewModel.buttonLogoutTapped() }
        textDelete?.setOnClickListener { viewModel.buttonDeleteTapped() }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
        observerUserInformation()
        observerLogout()
        observeDelete()
        observeFailure()
    }

    private fun observerUserInformation() {
        viewModel.user?.observe(
            this as LifecycleOwner,
            androidx.lifecycle.Observer {userEntity ->
                firstContainer?.visibility = VISIBLE
                progress?.hide()
                tableLayout?.visibility = VISIBLE
                textName?.text = userEntity?.name
                textEmail?.text = userEntity?.email
                textBirthdate?.text = userEntity?.birth
            }
        )
    }

    private fun observerLogout() {
        viewModel.logout.observe(
            this as LifecycleOwner,
            androidx.lifecycle.Observer {logout ->
                if(logout) {
                    requireActivity().viewModelStore.clear()
                    activity?.startActivity(Intent(activity, LoginActivity::class.java))
                    activity?.finish()
                }
            }
        )
    }

    private fun observeDelete() {
        viewModel.delete.observe(
            this as LifecycleOwner,
            androidx.lifecycle.Observer {
                requireActivity().viewModelStore.clear()
                activity?.startActivity(Intent(activity, LoginActivity::class.java))
                activity?.finish()
            }
        )
    }

    private fun observeFailure() {
        viewModel.failure.observe(
            this as LifecycleOwner,
            androidx.lifecycle.Observer {failure ->
                if(failure) {
                    Toast.makeText(
                        requireContext(),
                        "Efetue logout e tente excluir novamente.",
                        Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}
