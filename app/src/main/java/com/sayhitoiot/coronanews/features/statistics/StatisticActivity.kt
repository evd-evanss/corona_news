package com.sayhitoiot.coronanews.features.statistics

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hookedonplay.decoviewlib.DecoView
import com.hookedonplay.decoviewlib.events.DecoEvent
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.commom.util.ItemColors
import com.sayhitoiot.coronanews.features.statistics.presenter.StatisticsPresenter
import com.sayhitoiot.coronanews.features.statistics.presenter.contract.StatisticPresenterToPresenter
import com.sayhitoiot.coronanews.features.statistics.presenter.contract.StatisticPresenterToView
import kotlinx.android.synthetic.main.activity_statistics.*


class StatisticActivity : AppCompatActivity(), StatisticPresenterToView {

    private val presenter: StatisticPresenterToPresenter by lazy {
        StatisticsPresenter(this)
    }

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

    override var country: String?
        get() = intent.extras?.getString("country", "")
        set(value) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        supportActionBar?.hide()
        presenter.onCreate()
    }

    override fun initializeViews() {
        textCountry = activityStatistic_textView_country
        textTotal = activityStatistic_textView_total
        textRecoveries = activityStatistic_textView_recoveries
        textDeaths = activityStatistic_textView_deaths
        textRecoveriesRate = activityStatistic_textView_recoveriesRate
        textDeathsRate = activityStatistic_textView_mortalityRate
        imageBack = activityStatistic_imageView_back
        imageBack?.setOnClickListener { presenter.imageBackTapped() }
        graphTotal = activityStatistic_dynamicArcView_graph
        graphRecoveries = activityStatistic_dynamicArcView_graph2
        graphDeaths = activityStatistic_dynamicArcView_graph3
        graphRecoveriesRate = activityStatistic_dynamicArcView_graph4
        graphDeathsRate = activityStatistic_dynamicArcView_graph5
        presenter.didFinishInitializeViews()
    }

    override fun showStatistics(
        feed: FeedEntity,
        statistics: MutableList<Float>
    ) {
        val recoveriesRate = "%.2f".format(statistics[0]) + "%"
        val mortalityRate = "%.2f".format(statistics[1]) + "%"
        textCountry?.text = feed.country
        textTotal?.text = feed.cases.toString()
        textRecoveries?.text = feed.recovereds.toString()
        textDeaths?.text = feed.deaths.toString()
        textRecoveriesRate?.text = recoveriesRate
        textDeathsRate?.text = mortalityRate
    }

    override fun updateGraph(
        recoveries: Float,
        total: Float,
        deaths: Float,
        rateRecovery: Float,
        rateDeaths: Float) {
        setOffSetGraphs(total)
        graphTotal?.addEvent(DecoEvent.Builder(total).setIndex(1).build())
        graphRecoveries?.addEvent(DecoEvent.Builder(recoveries).setIndex(1).setDelay(1000).build())
        graphDeaths?.addEvent(DecoEvent.Builder(deaths).setIndex(1).setDelay(1000).build())
        graphRecoveriesRate?.addEvent(DecoEvent.Builder(rateRecovery).setIndex(1).setDelay(1000).build())
        graphDeathsRate?.addEvent(DecoEvent.Builder(rateDeaths).setIndex(1).setDelay(1000).build())
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

    override fun renderPreviousView() {
        onBackPressed()
    }
}
