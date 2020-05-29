package com.sayhitoiot.coronanews.features.home.feed.view

import android.app.Activity
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.features.home.feed.adapter.FeedAdapter
import com.sayhitoiot.coronanews.features.home.feed.presenter.FeedPresenter
import com.sayhitoiot.coronanews.features.home.feed.presenter.contract.FeedPresenterToView
import com.sayhitoiot.coronanews.features.home.feed.presenter.contract.FeedViewToPresenter
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar
import kotlinx.android.synthetic.main.fragment_feed.*

class FeedFragment : Fragment(), FeedViewToPresenter {

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
    private var edtSearch: EditText? = null
    private var buttonSearch: ImageView? = null
    private var buttonBack: ImageView? = null
    private var textTitle: TextView? = null
    private var firstContainer: LinearLayout? = null
    private var progress: DilatingDotsProgressBar? = null

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
            textTitle = fragment_feed_title
            firstContainer = fragment_feed_linearLayout
            progress = fragment_feed_dilatingDotsProgressBar
            progress?.show()
            presenter.didFinishInitializeViews()
        }
    }

    override fun renderViewForSearch() {
        activity?.runOnUiThread {
            edtSearch?.visibility = VISIBLE
            buttonBack?.visibility = VISIBLE
            textTitle?.visibility = GONE
            edtSearch?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val text = edtSearch?.text.toString()
                    presenter.filterData(text)
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

}
