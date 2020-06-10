package com.sayhitoiot.coronanews.features.favorites

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.features.favorites.presenter.FavoritesPresenter
import com.sayhitoiot.coronanews.features.favorites.presenter.contract.FavoritesToPresenter
import com.sayhitoiot.coronanews.features.favorites.presenter.contract.FavoritesToView
import kotlinx.android.synthetic.main.fragment_favorites.*

class FavoritesFragment : Fragment() , FavoritesToView {

    private val presenter: FavoritesToPresenter by lazy {
        FavoritesPresenter(this)
    }

    private val favoriteAdapter: FavoriteAdapter by lazy {
        FavoriteAdapter(
            requireContext(),
            mutableListOf()
        )
    }

    private var recyclerView: RecyclerView? = null

    override val activity: Activity?
        get() = requireActivity()

    private var firstContainer: LinearLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onCreate()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun initializeViews() {
        activity?.runOnUiThread {
            firstContainer?.visibility = View.VISIBLE
            recyclerView?.visibility = View.GONE
            firstContainer = fragment_favorites_linearLayout
            recyclerView = recyclerView_favorites
            recyclerView?.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            recyclerView?.setItemViewCacheSize(227)
            recyclerView?.adapter = favoriteAdapter
            presenter.didFinishInitializeViews()
        }
    }

    override fun postFeedInAdapter(feedFavorites: MutableList<FeedEntity>) {
        activity?.runOnUiThread {
            recyclerView?.setItemViewCacheSize(feedFavorites.size)
            firstContainer?.visibility = View.GONE
            recyclerView?.visibility = View.VISIBLE
            favoriteAdapter.updateList(feedFavorites)
        }
    }
}
