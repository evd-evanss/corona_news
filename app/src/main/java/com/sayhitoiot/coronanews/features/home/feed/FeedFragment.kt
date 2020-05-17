package com.sayhitoiot.coronanews.features.home.feed

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.features.home.feed.presenter.FeedPresenter
import com.sayhitoiot.coronanews.features.home.feed.presenter.contract.FeedPresenterToView
import com.sayhitoiot.coronanews.features.home.feed.presenter.contract.FeedViewToPresenter
import kotlinx.android.synthetic.main.fragment_feed.*

class FeedFragment : Fragment(), FeedViewToPresenter {

    private val presenter: FeedPresenterToView by lazy {
        FeedPresenter(this)
    }

    override var activity: Activity? get() = requireActivity()
        set(value) {}

    private val feedAdapter: FeedAdapter by lazy {
        FeedAdapter(
            mutableListOf()
        )
    }
    private var recyclerView: RecyclerView? = null
    private var edtSearch: EditText? = null
    private var buttonSearch: ImageView? = null

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

    override fun initializeViews() {
        activity?.runOnUiThread {
            recyclerView = recyclerView_feed
            recyclerView?.layoutManager = LinearLayoutManager(context)
            recyclerView?.adapter = feedAdapter
            edtSearch = fragment_feed_editText_search
            buttonSearch = fragment_feed_imageView_search
            buttonSearch?.setOnClickListener {  }
        }
    }

    override fun postValueInAdapter(feed: MutableList<FeedEntity>) {
        activity?.runOnUiThread {
            feedAdapter.updateList(feed)
        }
    }
}
