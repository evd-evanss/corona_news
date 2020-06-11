package com.sayhitoiot.coronanews.features.favorites.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.features.favorites.repository.RepositoryFavorites
import com.sayhitoiot.coronanews.features.favorites.viewmodel.ViewModelFavorites
import com.sayhitoiot.coronanews.features.favorites.viewmodel.ViewModelFavoritesFactory
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.coroutines.Dispatchers.IO

class FavoritesFragment : Fragment() {

    lateinit var viewModel: ViewModelFavorites

    private val favoriteAdapter: FavoriteAdapter by lazy {
        FavoriteAdapter(
            requireContext(),
            mutableListOf()
        )
    }
    private var recyclerView: RecyclerView? = null
    private var firstContainer: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
    }

    private fun setupViewModel() {
        val factory = ViewModelFavoritesFactory(repositoryFavorites = RepositoryFavorites())
        viewModel = ViewModelProvider(this, factory).get(ViewModelFavorites::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
    }

    fun initializeViews() {
        activity?.runOnUiThread {
            firstContainer?.visibility = View.VISIBLE
            recyclerView?.visibility = View.GONE
            firstContainer = fragment_favorites_linearLayout
            recyclerView = recyclerView_favorites
            recyclerView?.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            recyclerView?.adapter = favoriteAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.favoritesFeed?.observe(this as LifecycleOwner, androidx.lifecycle.Observer { feedFavorites ->
            feedFavorites?.size?.let { recyclerView?.setItemViewCacheSize(it) }
            firstContainer?.visibility = View.GONE
            recyclerView?.visibility = View.VISIBLE
            feedFavorites?.let { favoriteAdapter.updateList(it) }
        })
    }

}
