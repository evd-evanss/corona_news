package com.sayhitoiot.coronanews.features.feed.ui

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
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.button.MaterialButton
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.features.feed.repository.RepositoryFeeds
import com.sayhitoiot.coronanews.features.feed.viewmodel.FeedViewModel
import com.sayhitoiot.coronanews.features.feed.viewmodel.FeedViewModelFactory
import com.sayhitoiot.coronanews.features.feed.viewmodel.ViewModelToView
import com.sayhitoiot.coronanews.features.profile.viewmodel.ViewModelProfile
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.coroutines.Dispatchers.IO
import java.util.*

class FeedFragment : Fragment(), ViewModelToView {

    companion object {
        var edtSearch: EditText? = null
        const val MORE = 0
        const val FEWER = 1
        const val CONTINENTS = 2
        const val ALL = 3
    }

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
    private var textOnLoad: TextView? = null
    private var textUpdate: TextView? = null
    private var buttonMoreCases: MaterialButton? = null
    private var buttonFewerCases: MaterialButton? = null
    private var buttonContinentsCases: MaterialButton? = null
    private var buttonAllCases: MaterialButton? = null

    private val factory = FeedViewModelFactory(repositoryFeeds = RepositoryFeeds(IO))
    private val viewModel: FeedViewModel by lazy {
        ViewModelProvider(this, factory).get(FeedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    @ExperimentalStdlibApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        setupListeners()
        setupObserversForMessages()
        setupObserverForAdapter()
        setupObserverForFilters()
    }

    private fun initializeViews() {
        recyclerView = recyclerView_feed
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.setItemViewCacheSize(10)
        recyclerView?.adapter = feedAdapter
        edtSearch = fragment_feed_editText_search
        buttonSearch = fragment_feed_imageView_search
        buttonBack = fragment_feed_imageView_back
        buttonMoreCases = fragment_feed_materialButton_moreCases
        buttonFewerCases = fragment_feed_materialButton_fewerCases
        buttonContinentsCases = fragment_feed_mateerialButton_continentsCases
        buttonAllCases = fragment_feed_materialButton_allCases
        textTitle = fragment_feed_title
        firstContainer = fragment_feed_linearLayout
        progress = fragment_feed_dilatingDotsProgressBar
        progress?.show()
        textOnLoad = fragment_feed_textView_onLoad
        textUpdate = fragment_feed_textView_updateText
        textUpdate?.visibility = GONE
    }

    @ExperimentalStdlibApi
    private fun setupListeners() {
        buttonSearch?.setOnClickListener { renderViewForSearch() }
        buttonBack?.setOnClickListener {
            renderViewDefault()
            viewModel.fetchDataForFeed()
        }
        buttonMoreCases?.setOnClickListener { viewModel.filterData(MORE) }
        buttonFewerCases?.setOnClickListener { viewModel.filterData(FEWER) }
        buttonContinentsCases?.setOnClickListener { viewModel.filterData(CONTINENTS) }
        buttonAllCases?.setOnClickListener { viewModel.filterData(ALL) }
        edtSearch?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.filterData(edtSearch?.text.toString().capitalize(Locale.ROOT))
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int ) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
    }

    private fun setupObserversForMessages() {
        viewModel.messages.observe(this as LifecycleOwner, androidx.lifecycle.Observer { fail ->
            if(!fail.isNullOrEmpty()) {
                firstContainer?.visibility = GONE
                textOnLoad?.visibility = GONE
                progress?.hide()
                Toast.makeText(activity, fail, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setupObserverForAdapter() {
        viewModel.feed?.observe(this as LifecycleOwner, androidx.lifecycle.Observer { feed ->
            textUpdate?.visibility = VISIBLE
            textUpdate?.text = feed?.firstOrNull()?.day
            recyclerView?.setItemViewCacheSize(10)
            firstContainer?.visibility = GONE
            progress?.hide()
            textOnLoad?.visibility = GONE
            recyclerView?.visibility = VISIBLE
            feed?.let { feedAdapter.updateList(it, false, viewModel) }
        })
    }

    private fun setupObserverForFilters() {
        viewModel.filter.observe(this as LifecycleOwner, androidx.lifecycle.Observer { filter ->
                when(filter) {
                    MORE -> selectMoreFilterButton()
                    FEWER -> selectFewerFilterButton()
                    CONTINENTS -> selectContinentFilterButton()
                    ALL -> selectAllFilterButton()
            }
        })
    }

    @ExperimentalStdlibApi
    private fun renderViewForSearch() {
        edtSearch?.visibility = VISIBLE
        buttonBack?.visibility = VISIBLE
        textTitle?.visibility = GONE
    }

    private fun renderViewDefault() {
        edtSearch?.visibility = GONE
        edtSearch?.setText("")
        buttonBack?.visibility = GONE
        textTitle?.visibility = VISIBLE
        textOnLoad?.visibility = GONE
    }

    private fun selectMoreFilterButton() {
        activity?.runOnUiThread {
            buttonMoreCases?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorRed))
            buttonFewerCases?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.bg_gray))
            buttonContinentsCases?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.bg_gray))
            buttonAllCases?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.bg_gray))
        }
    }

    private fun selectFewerFilterButton() {
        activity?.runOnUiThread {
            buttonMoreCases?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.bg_gray))
            buttonFewerCases?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorRed))
            buttonContinentsCases?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.bg_gray))
            buttonAllCases?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.bg_gray))
        }
    }

    private fun selectAllFilterButton() {
        activity?.runOnUiThread {
            buttonMoreCases?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.bg_gray))
            buttonFewerCases?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.bg_gray))
            buttonContinentsCases?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.bg_gray))
            buttonAllCases?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorRed))
        }
    }

    private fun selectContinentFilterButton() {
        activity?.runOnUiThread {
            buttonMoreCases?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.bg_gray))
            buttonFewerCases?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.bg_gray))
            buttonContinentsCases?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorRed))
            buttonAllCases?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.bg_gray))
        }
    }

}
