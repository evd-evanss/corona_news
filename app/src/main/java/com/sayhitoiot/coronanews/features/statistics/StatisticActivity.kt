package com.sayhitoiot.coronanews.features.statistics

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.features.statistics.presenter.StatisticsPresenterToInteract
import com.sayhitoiot.coronanews.features.statistics.presenter.contract.StatisticPresenterToPresenter
import com.sayhitoiot.coronanews.features.statistics.presenter.contract.StatisticPresenterToView
import kotlinx.android.synthetic.main.activity_statistics.*
import kotlin.math.roundToInt


class StatisticActivity : AppCompatActivity(), StatisticPresenterToView {

    private val presenter: StatisticPresenterToPresenter by lazy {
        StatisticsPresenterToInteract(this)
    }

    private var textCountry: TextView? = null
    private var textTotal: TextView? = null
    private var textRecoveries: TextView? = null
    private var textDeaths: TextView? = null
    private var textRecoveriesRate: TextView? = null
    private var textDeathsRate: TextView? = null

    private var viewTotal: View? = null
    private var viewRecoveries: View? = null
    private var viewRecoveriesRate: View? = null
    private var viewDeath: View? = null
    private var viewDeathRate: View? = null
    private var imageBack: View? = null

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
        viewTotal = itemStatistic_view_total
        viewRecoveries = itemStatistic_view_recovered
        viewDeath = itemStatistic_view_deaths
        viewRecoveriesRate = itemStatistic_view_recoveriesRate
        viewDeathRate = itemStatistic_view_deathsRate
        imageBack = activityStatistic_imageView_back
        imageBack?.setOnClickListener { presenter.imageBackTapped() }
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

        viewTotal?.layoutParams =
            viewTotal?.layoutParams?.width?.let { width ->
                LinearLayout.LayoutParams(
                    width,
                    total.roundToInt(),
                    0f
                )
            }

        viewRecoveries?.layoutParams =
            viewRecoveries?.layoutParams?.width?.let { width ->
            LinearLayout.LayoutParams(
                width,
                recoveries.roundToInt(),
                0f
            )
        }

        viewDeath?.layoutParams =
            viewDeath?.layoutParams?.width?.let { width ->
                LinearLayout.LayoutParams(
                    width,
                    deaths.roundToInt(),
                    0f
                )
            }

        viewRecoveriesRate?.layoutParams =
            viewRecoveriesRate?.layoutParams?.width?.let { width ->
                LinearLayout.LayoutParams(
                    width,
                    rateRecovery.roundToInt(),
                    0f
                )
            }

        viewDeathRate?.layoutParams =
            viewDeathRate?.layoutParams?.width?.let { width ->
                LinearLayout.LayoutParams(
                    width,
                    rateDeaths.roundToInt(),
                    0f
                )
            }


        (viewRecoveries?.layoutParams as LinearLayout.LayoutParams?)?.setMargins(10,0,10,0)
        (viewTotal?.layoutParams as LinearLayout.LayoutParams?)?.setMargins(10,0,10,0)
        (viewDeath?.layoutParams as LinearLayout.LayoutParams?)?.setMargins(10,0,10,0)
        (viewRecoveriesRate?.layoutParams as LinearLayout.LayoutParams?)?.setMargins(10,0,10,0)
        (viewDeathRate?.layoutParams as LinearLayout.LayoutParams?)?.setMargins(10,0,10,0)


    }

    override fun renderPreviousView() {
        onBackPressed()
    }
}
