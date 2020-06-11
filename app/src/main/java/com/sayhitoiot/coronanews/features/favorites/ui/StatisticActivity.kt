package com.sayhitoiot.coronanews.features.favorites.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.hookedonplay.decoviewlib.DecoView
import com.hookedonplay.decoviewlib.events.DecoEvent
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.commom.util.ItemColors
import com.sayhitoiot.coronanews.features.favorites.repository.RepositoryFavorites
import com.sayhitoiot.coronanews.features.favorites.viewmodel.ViewModelFavorites
import com.sayhitoiot.coronanews.features.favorites.viewmodel.ViewModelFavoritesFactory
import kotlinx.android.synthetic.main.activity_statistics.*


class StatisticActivity : AppCompatActivity() {


    lateinit var viewModel: ViewModelFavorites

    private var textCountry: TextView? = null
    private var textTotal: TextView? = null
    private var textRecoveries: TextView? = null
    private var textDeaths: TextView? = null
    private var textRecoveriesRate: TextView? = null
    private var textDeathsRate: TextView? = null

    private var imageBack: View? = null
    private var graphTotal: DecoView? = null
    private var graphRecoveries: DecoView? = null
    private var graphDeaths: DecoView? = null
    private var graphRecoveriesRate: DecoView? = null
    private var graphDeathsRate: DecoView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        supportActionBar?.hide()
        initializeViews()
        initializeListeners()
        setupViewModel()
    }

    private fun initializeViews() {
        textCountry = activityStatistic_textView_country
        textTotal = activityStatistic_textView_total
        textRecoveries = activityStatistic_textView_recoveries
        textDeaths = activityStatistic_textView_deaths
        textRecoveriesRate = activityStatistic_textView_recoveriesRate
        textDeathsRate = activityStatistic_textView_mortalityRate
        imageBack = activityStatistic_imageView_back
        graphTotal = activityStatistic_dynamicArcView_graph
        graphRecoveries = activityStatistic_dynamicArcView_graph2
        graphDeaths = activityStatistic_dynamicArcView_graph3
        graphRecoveriesRate = activityStatistic_dynamicArcView_graph4
        graphDeathsRate = activityStatistic_dynamicArcView_graph5
    }

    private fun initializeListeners() {
        imageBack?.setOnClickListener { onBackPressed() }
    }

    private fun setupViewModel() {
        val factory = ViewModelFavoritesFactory(repositoryFavorites = RepositoryFavorites())
        viewModel = ViewModelProvider(this, factory).get(ViewModelFavorites::class.java)
    }

    override fun onResume() {
        super.onResume()
        val country = intent.extras?.getString("country", "")
        viewModel.onResumeStatistics(country)
        observerFeed()
        observerRates()
        observerGraphStatistics()
    }

    private fun observerFeed() {
        viewModel.feed?.observe(this as LifecycleOwner, androidx.lifecycle.Observer {feed->
            textCountry?.text = feed?.country
            textTotal?.text = feed?.cases.toString()
            textRecoveries?.text = feed?.recovereds.toString()
            textDeaths?.text = feed?.deaths.toString()
        })
    }

    private fun observerRates() {
        viewModel.rates?.observe(this as LifecycleOwner, androidx.lifecycle.Observer {statistics ->
            val recoveriesRate = statistics?.get(0)
            val mortalityRate = statistics?.get(1)
            textRecoveriesRate?.text = recoveriesRate
            textDeathsRate?.text = mortalityRate
        })
    }

    private fun observerGraphStatistics() {
        viewModel.statistics.observe(
            this as LifecycleOwner,
            androidx.lifecycle.Observer {statistics ->
                setOffSetGraphs(statistics[2])
                graphTotal?.addEvent(DecoEvent.Builder(statistics[2]).setIndex(1).build())
                graphRecoveries?.addEvent(DecoEvent.Builder(statistics[0]).setIndex(1).setDelay(1000).build())
                graphDeaths?.addEvent(DecoEvent.Builder(statistics[1]).setIndex(1).setDelay(1000).build())
                graphRecoveriesRate?.addEvent(DecoEvent.Builder(statistics[3]).setIndex(1).setDelay(1000).build())
                graphDeathsRate?.addEvent(DecoEvent.Builder(statistics[4]).setIndex(1).setDelay(1000).build())
            }
        )
    }

    private fun setOffSetGraphs(total: Float) {

        val seriesItemBackground = ItemColors().getSeriesItemBackground(this)
        val seriesItemTotal = ItemColors().getSeriesItemTotal(this, total)
        val seriesItemYellow = ItemColors().getSeriesItemYellow(this, total)
        val seriesItemGreen = ItemColors().getSeriesItemGreen(this, total)
        val seriesItemRed = ItemColors().getSeriesItemRed(this, total)
        val seriesItemFineGreen = ItemColors().getSeriesItemFineGreen(this)
        val seriesItemFineRed = ItemColors().getSeriesItemFineRed(this)

        graphTotal?.addSeries(seriesItemTotal)
        graphTotal?.addSeries(seriesItemYellow)
        graphRecoveries?.addSeries(seriesItemTotal)
        graphRecoveries?.addSeries(seriesItemGreen)
        graphDeaths?.addSeries(seriesItemTotal)
        graphDeaths?.addSeries(seriesItemRed)
        graphRecoveriesRate?.addSeries(seriesItemBackground)
        graphRecoveriesRate?.addSeries(seriesItemFineGreen)
        graphDeathsRate?.addSeries(seriesItemBackground)
        graphDeathsRate?.addSeries(seriesItemFineRed)
    }

}
