package com.sayhitoiot.coronanews.features.feed.feed.view

import android.app.Activity
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.features.feed.feed.adapter.FeedAdapter
import com.sayhitoiot.coronanews.features.feed.feed.presenter.FeedPresenter
import com.sayhitoiot.coronanews.features.feed.feed.presenter.contract.FeedPresenterToView
import com.sayhitoiot.coronanews.features.feed.feed.presenter.contract.FeedViewToPresenter
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar
import kotlinx.android.synthetic.main.fragment_feed.*
import java.util.*

class FeedFragment : Fragment(), FeedViewToPresenter {

    companion object {
        var edtSearch: EditText? = null
        var FILTER = ""
    }

    private val presenter: FeedPresenterToView by lazy {
        FeedPresenter(this)
    }

    override var activity: Activity? get() = requireActivity()
        set(value) {}

    private val feedAdapter: FeedAdapter by lazy {
        FeedAdapter(
            requireContext(),
            mutableListOf()
        )
    }

    private var recyclerView: RecyclerView? = null

    private var buttonSearch: ImageView? = null
    private var buttonBack: ImageView? = null
    private var textTitle: TextView? = null
    private var firstContainer: LinearLayout? = null
    private var progress: DilatingDotsProgressBar? = null

    private var buttonMoreCases: MaterialButton? = null
    private var buttonFewerCases: MaterialButton? = null
    private var buttonContinentsCases: MaterialButton? = null
    private var buttonAllCases: MaterialButton? = null
    private var imageMenu: ImageView? = null
    private var containerMenu: LinearLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onViewCreated()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun initializeViews() {
        activity?.runOnUiThread {
            recyclerView = recyclerView_feed
            recyclerView?.layoutManager = LinearLayoutManager(context)
            recyclerView?.setItemViewCacheSize(227)
            recyclerView?.adapter = feedAdapter
            edtSearch = fragment_feed_editText_search
            buttonSearch = fragment_feed_imageView_search
            buttonSearch?.setOnClickListener { presenter.buttonSearchTapped() }
            buttonBack = fragment_feed_imageView_back
            buttonBack?.setOnClickListener { presenter.buttonBackTapped() }
            buttonMoreCases = fragment_feed_materialButton_moreCases
            buttonMoreCases?.setOnClickListener { presenter.moreCasesTapped() }
            buttonFewerCases = fragment_feed_materialButton_fewerCases
            buttonFewerCases?.setOnClickListener { presenter.fewerCasesTapped() }
            buttonContinentsCases = fragment_feed_mateerialButton_continentsCases
            buttonContinentsCases?.setOnClickListener { presenter.continentCasesTapped() }
            buttonAllCases = fragment_feed_materialButton_allCases
            buttonAllCases?.setOnClickListener { presenter.allCasesTapped() }
            imageMenu = fragment_feed_imageView_menu
            imageMenu?.setOnClickListener { presenter.imageMenuTapped() }
            containerMenu = fragment_feed_linearLayout_menu
            textTitle = fragment_feed_title
            firstContainer = fragment_feed_linearLayout
            progress = fragment_feed_dilatingDotsProgressBar
            progress?.show()
            presenter.didFinishInitializeViews()
        }
    }

    @ExperimentalStdlibApi
    override fun renderViewForSearch() {
        activity?.runOnUiThread {
            edtSearch?.visibility = VISIBLE
            buttonBack?.visibility = VISIBLE
            textTitle?.visibility = GONE
            imageMenu?.visibility = GONE
            edtSearch?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    FILTER = edtSearch?.text.toString().capitalize(Locale.ROOT)
                    presenter.filterData(FILTER)
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            })
        }
    }

    override fun renderViewDefault() {
        activity?.runOnUiThread {
            edtSearch?.visibility = GONE
            edtSearch?.setText("")
            buttonBack?.visibility = GONE
            textTitle?.visibility = VISIBLE
            imageMenu?.visibility = VISIBLE
        }
    }

    override fun openMenuFilters() {
        activity?.runOnUiThread {
            containerMenu?.visibility = VISIBLE
            edtSearch?.visibility = GONE
            buttonSearch?.visibility = GONE
            edtSearch?.setText("")
            buttonBack?.visibility = GONE
        }
    }

    override fun closeMenuFilters() {
        activity?.runOnUiThread {
            containerMenu?.visibility = GONE
            buttonSearch?.visibility = VISIBLE
        }
    }

    override fun renderViewWithFail(fail: String) {
        activity?.runOnUiThread {
            firstContainer?.visibility = GONE
            progress?.hide()
            Toast.makeText(activity, fail, Toast.LENGTH_LONG).show()
        }
    }

    override fun postValueInAdapter(feed: MutableList<FeedEntity>) {
        activity?.runOnUiThread {
            recyclerView?.setItemViewCacheSize(feed.size)
            firstContainer?.visibility = GONE
            progress?.hide()
            recyclerView?.visibility = VISIBLE
            feedAdapter.updateList(feed)
        }
    }

    override fun selectMoreFilterButton() {
        activity?.runOnUiThread {
            buttonMoreCases?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            buttonFewerCases?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.transparent))
            buttonContinentsCases?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.transparent))
            buttonAllCases?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.transparent))
        }
    }

    override fun selectFewerFilterButton() {
        activity?.runOnUiThread {
            buttonMoreCases?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.transparent))
            buttonFewerCases?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            buttonContinentsCases?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.transparent))
            buttonAllCases?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.transparent))
        }
    }

    override fun selectAllFilterButton() {
        activity?.runOnUiThread {
            buttonMoreCases?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.transparent))
            buttonFewerCases?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.transparent))
            buttonContinentsCases?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.transparent))
            buttonAllCases?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorAccent))
        }
    }

    override fun selectContinentFilterButton() {
        activity?.runOnUiThread {
            buttonMoreCases?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.transparent))
            buttonFewerCases?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.transparent))
            buttonContinentsCases?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            buttonAllCases?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.transparent))
        }
    }
}
