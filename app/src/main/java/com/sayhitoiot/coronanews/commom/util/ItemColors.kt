package com.sayhitoiot.coronanews.commom.util

import android.content.Context
import androidx.core.content.ContextCompat
import com.hookedonplay.decoviewlib.charts.SeriesItem
import com.sayhitoiot.coronanews.R

class ItemColors {

    companion object {
        const val OFFSET = 0f
        const val HUNDRED_PERCENT = 100f
    }

    fun getSeriesItemBackground(context: Context) : SeriesItem {
        return SeriesItem.Builder(ContextCompat.getColor(context,
            R.color.bg_gray))
            .setRange(OFFSET,HUNDRED_PERCENT,HUNDRED_PERCENT)
            .setInitialVisibility(true)
            .setLineWidth(50f)
            .build()
    }

    fun getSeriesItemTotal(context: Context, total: Float) : SeriesItem {
        return SeriesItem.Builder(ContextCompat.getColor(context,
            R.color.bg_gray))
            .setRange(
                0f,
                total,
                total
            )
            .setInitialVisibility(true)
            .setLineWidth(50f)
            .build()
    }

    fun getSeriesItemYellow(context: Context, total: Float) : SeriesItem {
        return SeriesItem.Builder(
            ContextCompat.getColor(context, R.color.yellowSepia))
            .setRange(0f,total,0f)
            .setInitialVisibility(true)
            .setLineWidth(40f)
            .build()
    }

    fun getSeriesItemGreen(context: Context, total: Float) : SeriesItem {
        return SeriesItem.Builder(
            ContextCompat.getColor(context, R.color.colorGreen))
            .setRange(0f,total,0f)
            .setInitialVisibility(true)
            .setLineWidth(40f)
            .build()
    }

    fun getSeriesItemRed(context: Context, total: Float) : SeriesItem {
        return SeriesItem.Builder(
            ContextCompat.getColor(context, R.color.colorRed))
            .setRange(0f,total,0f)
            .setInitialVisibility(true)
            .setLineWidth(40f)
            .build()
    }

    fun getSeriesItemFineGreen(context: Context) : SeriesItem {
        return SeriesItem.Builder(
            ContextCompat.getColor(context, R.color.colorGreenRate))
            .setRange(0f, HUNDRED_PERCENT,0f)
            .setInitialVisibility(true)
            .setLineWidth(40f)
            .build()
    }

    fun getSeriesItemFineRed(context: Context) : SeriesItem {
        return SeriesItem.Builder(
            ContextCompat.getColor(context, R.color.colorRedRate))
            .setRange(0f, HUNDRED_PERCENT,0f)
            .setInitialVisibility(true)
            .setLineWidth(40f)
            .build()
    }

}